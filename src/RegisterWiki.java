import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: install jsoup first
public class RegisterWiki implements Register {
    private LongMemory _memory;

    RegisterWiki(LongMemory memory) {
        this._memory = memory;
    }

    public Concept regist(String searchedWord, ConceptClass cc) {
        try{

        htmlText doc = new htmlText("http://en.wikipedia.org/wiki/" + searchedWord);
        boolean found = true;
        String doctxt = doc.getText();
        if (doc.getValidpage() == htmlText.Found.yes) {
            for (String keyword : cc.getKeywords()) {

                if (!doctxt.contains(keyword)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                Concept c = new Concept();
                c.setConceptClass(cc);
                c.setName(searchedWord);
                c.setUrl("http://en.wikipedia.org/wiki/" + searchedWord);
                c.setFoundInDB(true);
                //System.out.println(cc.getSubclass());
                if(doctxt.contains(cc.getSubclass()))
                {
                    Pattern pattern = Pattern.compile(cc.getSubclass()+" "+"[a-zA-Z]+");

                    Matcher matcher = pattern.matcher(doctxt);
                    if(matcher.find())
                    c.setConceptSubclass(matcher.group().replace(cc.getSubclass(),"").substring(1));
                }
                for (String characteristic :cc.getCharacteristics()
                     ) {

                        if(doctxt.contains(characteristic))
                            c.addCharacteristics(characteristic);
                }
                c.setClassName(cc.getNameClass());

                _memory.add(c,"concepts");
                return c;
            } else {

                return new Concept();
            }

        }
} catch (Exception ex) {
    System.out.println(ex);
}
        return new Concept();
    }
}
