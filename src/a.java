import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;

import java.util.HashMap;
import java.util.Properties;

public class a {
    private HashMap<String, Characteristics> subjects = new HashMap<>();
    public void f(String text)
    {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text.toLowerCase());
        pipeline.annotate(document);
        for (CoreSentence sentence : document.sentences())
        {

            System.out.println(sentence.toString());
        }
    }

    public a() {

    }
}
