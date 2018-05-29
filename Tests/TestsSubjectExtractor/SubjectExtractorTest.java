import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.junit.Test;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class SubjectExtractorTest {
    @Test
    public void first() {
         StanfordCoreNLP pipeline;
        CoreDocument document;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        String text ="Big dogs, cats and cute polar bears are cool, smart and nice. A dog eat a cat yesterday.At something bear. Nothing goes chair goat";
        document = new CoreDocument(text);
        pipeline.annotate(document);
        SubjectExtractor detectiv= new SubjectExtractor();
        detectiv.createList(document);
        Set<String> subiecti_trecuti = detectiv.getPassingSubjects();
        System.out.println(subiecti_trecuti);
        Set<String> verificare = new TreeSet<>();
        verificare.add("dog");
        verificare.add("bear");
        verificare.add("cat");
        verificare.add("nothing");
        assertEquals(verificare,subiecti_trecuti);
    }
    @Test
    public void dog0() {
        StanfordCoreNLP pipeline;
        CoreDocument document;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        String text ="dog are awesome.";
        document = new CoreDocument(text);
        pipeline.annotate(document);
        SubjectExtractor detectiv= new SubjectExtractor();
        detectiv.createList(document);
        Set<String> subiecti_trecuti = detectiv.getPassingSubjects();
        System.out.println(subiecti_trecuti);
        Set<String> verificare = new TreeSet<>();
        verificare.add("dog");
        assertEquals(verificare,subiecti_trecuti);
    }
    @Test
    public void gorillaguacamole1() {
        StanfordCoreNLP pipeline;
        CoreDocument document;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        String text ="gorilla hits .";
        document = new CoreDocument(text);
        pipeline.annotate(document);
        SubjectExtractor detectiv= new SubjectExtractor();
        detectiv.createList(document);
        Set<String> subiecti_trecuti = detectiv.getPassingSubjects();
        System.out.println(subiecti_trecuti);
        Set<String> verificare = new TreeSet<>();
        verificare.add("gorilla");
        assertEquals(verificare,subiecti_trecuti);
    }

}