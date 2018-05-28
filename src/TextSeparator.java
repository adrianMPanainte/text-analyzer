import edu.stanford.nlp.pipeline.Annotation;

import java.util.Set;

public interface TextSeparator {

    void createList(Annotation document);

  //  Set<String> getPassingWords();
}