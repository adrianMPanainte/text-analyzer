import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.junit.Test;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class AdmittedConceptTest {
    AdmittedConcept admittedConcept = new AdmittedConcept();
    StanfordCoreNLP pipeline;
    Annotation annotation;
    Properties props = new Properties();
    ConceptClass conceptClass = new ConceptClass("animals");
    Set<String> passConcepts = new TreeSet<>();

    @Test
    public void testConceptsTest() {

        String text = "A dog eat a cat yesterday. Polar bears are cute.";
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        annotation = new Annotation(text.toLowerCase());
        pipeline.annotate(annotation);
        conceptClass.setSubclass("Class:");
        conceptClass.setKeywords("Kingdom: Animalia");
        admittedConcept.testConcepts(annotation, conceptClass);
        passConcepts.add("dog");
        passConcepts.add("cat");
        passConcepts.add("polar_bear");
        assertEquals(admittedConcept.getPassConcepts(),passConcepts);
    }

    @Test
    public void testConceptsEmptyInput() {
        String text = "";
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        annotation = new Annotation(text.toLowerCase());
        pipeline.annotate(annotation);
        conceptClass.setSubclass("Class:");
        conceptClass.setKeywords("Kingdom: Animalia");
        admittedConcept.testConcepts(annotation, conceptClass);
        assertEquals(admittedConcept.getPassConcepts(), passConcepts);
    }

    @Test
    public void testConceptsNullInput() {
        String text=new String();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        annotation = new Annotation(text.toLowerCase());
        pipeline.annotate(annotation);
        conceptClass.setSubclass("Class:");
        conceptClass.setKeywords("Kingdom: Animalia");
        admittedConcept.testConcepts(annotation, conceptClass);
        assertEquals(admittedConcept.getPassConcepts(), passConcepts);
    }
}