import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubjectExtractor {

    private HashMap<String, Integer> subjects = new HashMap<>();
    private ValueComparator bvc = new ValueComparator(subjects);
    private TreeMap<String, Integer>  subjectsSort = new TreeMap<>(bvc);
    private Set<String> passingSubjects = new TreeSet<>();
    private int weight;

    public void createList(CoreDocument document){

        weight = document.sentences().size();
        for (CoreSentence sentence:document.sentences()) {
            SemanticGraph dependencyParse = sentence.dependencyParse();
            Pattern pattern = Pattern.compile("[ ]*"+"->"+"[ ]*"+"[A-Za-z/]*"+"[ ]*"+"\\(nsubj\\)");
            Matcher matcher = pattern.matcher(dependencyParse.toString());
            while (matcher.find())
            {


               Pattern pattern2 = Pattern.compile("[A-Za-z]*+"+"/NN"+"[S]*");
               Matcher matcher2 = pattern2.matcher(matcher.group());
               while (matcher2.find())
               {
                   String[] pair = matcher2.group().split(Pattern.quote("/"));
                   String word = new String(pair[0]);

                   String pos = new String(pair[1]);
                   word = word.toLowerCase();
                   if(pos.equals("NNS"))
                       word = PutSingularFormForNouns(word);
                   if(subjects.containsKey(word))
                       subjects.put(word,subjects.get(word).intValue()+weight);
                       else
                            subjects.put(word,weight);
               }

            }
            weight--;
        }

        subjectsSort.putAll(subjects);
       setPassingSubjects(subjectsSort.keySet());
    };

    protected String PutSingularFormForNouns(String word) {
        if (word.charAt(word.length()-3) == 'i' && word.charAt(word.length()-2) == 'e' && word.charAt(word.length()-1) == 's') {
            word = word.substring(0, word.length() - 3);
            word += 'y';
        }
        else
            //s, x, z, ch, sh
            if ((word.charAt(word.length()-3) == 's' || word.charAt(word.length()-3) == 'x' || word.charAt(word.length()-3) == 'z' ||
                    (word.charAt(word.length()-3) == 'h'&&(word.charAt(word.length()-4) == 'c'|| word.charAt(word.length()-4) == 's')))
                    && word.charAt(word.length()-2) == 'e' && word.charAt(word.length()-1) == 's')
                word = word.substring(0, word.length() - 2);
            else
                word = word.substring(0, word.length() - 1);

        return word;
    }

    private void setPassingSubjects(Set<String> passingSubjects) {
        this.passingSubjects = passingSubjects;
    }

    public Set<String> getPassingSubjects() {
        return passingSubjects;
    }
}
