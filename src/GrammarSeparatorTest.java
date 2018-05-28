import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class GrammarSeparatorTest extends TestCase {

    private GrammarSeparator x = new GrammarSeparator();

    public void testAddPConcept() {
        Set<String> mockNouns = new TreeSet<>(Arrays.asList("nice cat", "star", "dog", "frog"));
        for (String noun : mockNouns) {
            x.addPConcept("NN", noun);
        }
        assertEquals(mockNouns, x.getPassingWords());
    }
    public void testAddPConceptNull() {
        x.addPConcept("", "");
        assertTrue(x.getPassingWords().isEmpty());
    }

    public void testPutSingularFormForNouns() {
        String mockPlural = "cats";
        String mockSingular = x.PutSingularFormForNouns(mockPlural);
        assertEquals(mockSingular, "cat");
    }


}