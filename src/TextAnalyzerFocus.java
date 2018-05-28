import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzerFocus {

    private StanfordCoreNLP pipeline;
    private CoreDocument document;
    private SubjectExtractor subjectExtractor;
    private Annotation annotation;
    private AdmittedConcept admittedConcept;
    private LongMemory memory;
    //private HashMap<String, Characteristics> subjects = new HashMap<>();
   // ArrayList<ArrayList<String>> listOfOthers = new ArrayList<ArrayList<String>>();
    private Set<ConceptClass> listOfConceptsDefault = new TreeSet<>();
	private List<Concept> listOfConceptsFound = new ArrayList<>();
	
    public void textAnalyzer(String text, String classconcepts)
    {
        memory = new LongMemory("brain","root","");
        setconcepts(classconcepts,memory);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        document = new CoreDocument(text.toLowerCase());
        annotation= new Annotation(text.toLowerCase());
        pipeline.annotate(document);
        pipeline.annotate(annotation);
        subjectExtractor =new SubjectExtractor();
        subjectExtractor.createList(document);
        System.out.println(subjectExtractor.getPassingSubjects());
       admittedConcept = new AdmittedConcept();
       for(ConceptClass conceptClass:listOfConceptsDefault)
       {

           admittedConcept.testConcepts(annotation,conceptClass);
       }

       Boolean subjectExists=false;
        pit:  for (String subject:subjectExtractor.getPassingSubjects())
        {
            for(Concept validConcept:admittedConcept.getPassConcepts())
            {
                if(validConcept.getName().contains(subject))
                {
                    subjectExists=true;
                    for (CoreSentence sentence : document.sentences())
                    {
                        SemanticGraph dependencyParse = sentence.dependencyParse();
                        Pattern pattern = Pattern.compile("[ ]*"+"->"+"[ ]*"+"[A-Za-z/]*"+"[ ]*"+"\\(nsubj\\)");
                        Matcher matcher = pattern.matcher(dependencyParse.toString());
                        Boolean isTheSubject =false;
                        while (matcher.find())
                        {
                            if(matcher.group().contains(subject))
                            {
                                isTheSubject=true;
                                break;
                            }
                        }
                        if(isTheSubject)
                        {
                            for (IndexedWord action : dependencyParse.getAllNodesByPartOfSpeechPattern("VB[A-Z]*"))
                            {
                                Pattern pattern2 = Pattern.compile("[A-Za-z]*+" + "/" + "[A-Z]*");
                                Matcher matcher2 = pattern2.matcher(action.toString());
                                while (matcher2.find())
                                {
                                    String[] pair = matcher2.group().split(Pattern.quote("/"));
                                    String word = new String(pair[0]);
                                    if (!validConcept.getCharacteristics().contains(word))
                                        validConcept.addCharacteristics(word);
                                }
                            }
                        }
                        for (IndexedWord characteristic : dependencyParse.getAllNodesByPartOfSpeechPattern("JJ[A-Z]*")) {
                            String adjs = null;
                            String[] split2 =characteristic.toString().split("/");
                            String word = split2[0];
                            String[] split = adj(sentence.toString(), subjectExtractor.getPassingSubjects(), subject).split("\\.1\\.");
                            adjs = split[0];
                            if (adjs.contains(".0.")){
                                String[] listOfadj = adjs.split("\\.0\\.");
                                adjs = listOfadj[listOfadj.length-1];
                            }
                                if(adjs.contains(word)&&!validConcept.getCharacteristics().contains(word))
                                {
                                    validConcept.addCharacteristics(word);
                                }
                            

                        }
                    }
                    this.listOfConceptsFound.add(validConcept);
					
                 //   break pit;
                }
            }
        }


    }
    private String adj(String text,Set<String>subjects,String subject) {

        for(String sub:subjects)
        {
            if(text.contains(sub))
            {
                String[] split = text.split(sub);
                String firstSubString = split[0];
                String secondSubString = split[1];
                if (sub != subject)
                    text = split[0] + ".0." + split[1];
                else
                    text = split[0] + ".1." + split[1];
            }
        }
        return text;

    }


    private void setconcepts(String classconcepts, LongMemory memory)
    {

        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(classconcepts);
        while (matcher.find())
        {

            ConceptClass possibleClass = (ConceptClass)memory.search("concepts_class",matcher.group());
            if(possibleClass.getNameClass()!=null && possibleClass.getNameClass()!="")
            {
                this.listOfConceptsDefault.add(possibleClass);
            }
        }
    }
	
	  public ArrayList<Concept> getListOfConceptsFound() { return listOfConceptsFound; }

}
