import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;


public class GrammarSeparator implements TextSeparator {
    private StanfordCoreNLP pipeline;
    private Set<String> passingWords = new TreeSet<>();
    private Set<String> partsOfSpeech = new TreeSet<>();
    private Set<String> pair = new TreeSet<>();
    private Set<String> validTags = new TreeSet<>();
    private String adj;

    // https://cs.nyu.edu/grishman/jet/guide/PennPOS.html <- valid tags
    private final String filename = "./assessments/valid_tags.txt";

    public void resetPartsOfSpeech(String... words) {
        Set<String> aux = new TreeSet<>();

        for (String word: words)
            if (word != null && !word.equals("") && this.validTags.contains(word))
                aux.add(word);

        this.partsOfSpeech = aux;
    }

    public void resetPair(String... words) {
        Set<String> aux = new TreeSet<>();

        for (String word: words)
            if (word != null && !word.equals("") && this.validTags.contains(word))
                aux.add(word);

        this.pair = aux;
    }


    public GrammarSeparator() { setup(); }

    public void createList(Annotation document){

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        passingWords.removeAll(passingWords);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                addPConcept(pos, word);
            }
        }
    }

    protected void addPConcept(String pos, String word) {

        // if part of speech is plural, make is singular
        if (pos.equals("NNS")) {
            word = PutSingularFormForNouns(word);
            this.passingWords.add(word.toLowerCase());


            // word has an adj?
            if (!adj.equals("")) {
                this.passingWords.add((adj + "_" + word.toLowerCase()));


                // clear the adj for the next words/tokens
                this.adj = "";
            }
        }
        // we have other parts of speech?
        else if (partsOfSpeech.contains(pos)) {
            this.passingWords.add(word.toLowerCase());

            if (!adj.equals("")) {
                this.passingWords.add((adj + "_" + word.toLowerCase()));


                // clear the adj for the next words/tokens
                adj = "";
            }
        }
        // we have an adj?
        else if (pair.contains(pos))
            adj = word;
    }

    private void setup()
    {
        // read the valid tags from .txt and put it in a set
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                this.validTags.add(line);
            }
        } catch (FileNotFoundException error) {
            System.out.println(error);
        } catch (IOException error) {
            System.out.println(error);
        }

        // basic initialize
        this.partsOfSpeech.add("NN");
        this.partsOfSpeech.add("NNS");
        this.pair.add("JJ");
        this.pair.add("NNP");
        this.adj = "";

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
    }

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

    public Set<String> getPassingWords() {
        return passingWords;
    }
}
