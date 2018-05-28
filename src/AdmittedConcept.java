import edu.stanford.nlp.pipeline.Annotation;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdmittedConcept {

    private GrammarSeparator grammarSeparator;
    private Set<Concept> passConcepts = new TreeSet<>();


    public void testConcepts(Annotation annotation,ConceptClass conceptClass){
        grammarSeparator = new GrammarSeparator();
        grammarSeparator.createList(annotation);
        Set<Concept> validConcepts = new TreeSet<>();

        for (String possibelConcept:grammarSeparator.getPassingWords())
        {
            htmlText page = new htmlText("http://en.wikipedia.org/wiki/"+possibelConcept);
            Boolean found = false;
            if (page.getValidpage() == htmlText.Found.yes) {
                found =true;
                for (String keyword : conceptClass.getKeywords()) {

                    if (!page.getText().contains(keyword)) {
                        found = false;
                        break;
                    }
                }
            }
            if(found == true)
            {
                Concept concept = new Concept();
                concept.setName(possibelConcept);
                concept.setClassName(conceptClass.getNameClass());
                concept.setUrl("http://en.wikipedia.org/wiki/" + possibelConcept);
                concept.setFoundInDB(true);
                if(page.getText().contains(conceptClass.getSubclass()))
                {
                    Pattern pattern = Pattern.compile(conceptClass.getSubclass()+" "+"[a-zA-Z]+");

                    Matcher matcher = pattern.matcher(page.getText());
                    if(matcher.find())
                        concept.setConceptSubclass(matcher.group().replace(conceptClass.getSubclass(),"").substring(1));
                }
                validConcepts.add(concept);
            }

        }
        for (Concept concept1:validConcepts)
        {
            Boolean rootConcept = true;
            for(Concept concept2:validConcepts)
            {
                if(concept2.getName().contains("_"+concept1.getName()))
                {
                    rootConcept = false;
                    passConcepts.add(concept2);
                    break;
                }
            }
            if(rootConcept==true)
            {
                passConcepts.add(concept1);
            }
        }
    }

    public Set<Concept> getPassConcepts() {
        return passConcepts;
    }

}
