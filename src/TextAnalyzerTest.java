import junit.framework.TestCase;

public class TextAnalyzerTest extends TestCase {

    private TextAnalyzer x = new TextAnalyzer();


    public void testTextAnalyzer() {
        String mocktext = "there is a nice cat a nicer dog and a polar bear on the street ";
        String y ="cat dog bear polar_bear";
        x.textAnalyzer(mocktext,"animals");
        //assertTrue(x.getListOfConceptsFound().contains(y));
        for(Concept animal : x.getListOfConceptsFound() )
            assertTrue(y.contains(animal.getName()));
    }
    public void testTextAnalyzerNull() {
        x.textAnalyzer("","");
        assertTrue(x.getListOfConceptsFound().isEmpty());
    }

}