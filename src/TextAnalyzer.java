import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer {
    private GrammarSeparator textSeparator;
    private Register register;
    private LongMemory memory;
    private Set<String> listOfWords = new TreeSet<>();
    private Set<Concept> listOfConceptsFound = new TreeSet<>();
    private Set<ConceptClass> listOfConceptsDefault = new TreeSet<>();
    private JSONObject theJSON;
    private final String filename = "./assessments/our_concepts_class.txt";

    TextAnalyzer() { init(); }

    private void init() {
        this.memory = new LongMemory("brain","root","");
        this.register = new RegisterWiki(this.memory);
        this.textSeparator = new GrammarSeparator();

        // we add the concept classes (manually) from the our_concepts_class.txt
        /*Map<String, Set<String>> listFromTxt = getConceptsFromTxt(this.filename);

        for (Map.Entry<String, Set<String>> entry : listFromTxt.entrySet()) {
            ConceptClass cc = new ConceptClass(entry.getKey());
            cc.setKeywords(entry.getValue());
            this.listOfConceptsDefault.add(cc);
        }*/
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


    public Set<String>  addkeywords(String text,String text2){
        Annotation document1= new Annotation(text.toLowerCase());
        Annotation document2= new Annotation(text2.toLowerCase());
        GrammarSeparator textSeparator = new GrammarSeparator();
        textSeparator.createList(document1);
        Set<String> listOfurl = new TreeSet<>();
        listOfurl = textSeparator.getPassingWords();
        htmlText ht;
        List<String> listOfpages = new ArrayList<String>();
        if(listOfurl.size()>=2)
            for (String url: listOfurl) {
                ht = new htmlText("http://en.wikipedia.org/wiki/" + url);
                if(ht.getValidpage()== htmlText.Found.yes)
                    listOfpages.add(ht.getText());

            }
        textSeparator.createList(document2);

        listOfurl = textSeparator.getPassingWords();
        List<String> listOfpages2 = new ArrayList<String>();

        for (String url: listOfurl) {
            ht = new htmlText("http://en.wikipedia.org/wiki/" + url);

            if(ht.getValidpage()== htmlText.Found.yes)
                listOfpages2.add(ht.getText());

        }


        Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z]+");

        Matcher m1 = p.matcher(listOfpages.get(0));
        List<String> listOfwords = new ArrayList<String>(); //no fixed size mentioned
        while (m1.find()) {
            listOfwords.add(m1.group());

        }
        List<String> listOfwords2 = new ArrayList<String>(); //no fixed size mentioned
        m1 = p.matcher(listOfpages.get(1));
        while (m1.find()) {
            listOfwords2.add(m1.group());
        }

        Set<String> listOfkay = new TreeSet<>(); //no fixed size mentioned
        for (int i=0;i<listOfwords.size();i++)
        {
            for (int j=0;j<listOfwords2.size();j++)
            {
                if(listOfwords.get(i).equals(listOfwords2.get(j)))
                {
                    listOfkay.add(listOfwords.get(i));
                }
            }
        }



        Set<String> listOfkayy = new TreeSet<>();
        for(String k:listOfpages)
        {

            for (String i:listOfkay
                    ) {
                if(k.contains(i))
                    listOfkayy.add(i);
            }
            listOfkay.removeAll(listOfkay);
            listOfkay.addAll(listOfkayy);
            listOfkayy.removeAll(listOfkayy);
        }

        for(String k:listOfpages2)
        {

            for (String i:listOfkay
                    ) {
                if(k.contains(i))
                {;
                }
                else
                {
                    listOfkayy.add(i);
                }
            }
            listOfkay.removeAll(listOfkay);
            listOfkay.addAll(listOfkayy);
            listOfkayy.removeAll(listOfkayy);
        }

        return listOfkay;
    }


    public void addConceptClass(String name,String ValidTxt,String NotValidTxt,String subclass,String characteristics){
        ConceptClass cc = new ConceptClass(name);
        cc.setKeywords(addkeywords(ValidTxt,NotValidTxt));
        if(subclass != null && subclass != "")
        {
            cc.setSubclass(subclass);
        }
        if(characteristics !=null && characteristics != "")
        {
            Pattern pattern = Pattern.compile("[a-zA-Z]+");
            Matcher matcher = pattern.matcher(characteristics);
            while (matcher.find())
            {
                cc.addCharacteristics(matcher.group());
            }
        }

        memory.add(cc,"concepts_class");
    }
    private StanfordCoreNLP pipeline;
    private Annotation  document;
    public void textAnalyzer(String text, String classconcepts) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,parse");
        pipeline = new StanfordCoreNLP(props);
        document= new Annotation(text.toLowerCase());
        pipeline.annotate(document);
        setconcepts(classconcepts,memory);

        this.textSeparator.createList(document);
        this.listOfWords = this.textSeparator.getPassingWords();
        if (listOfWords != null)
            for (String word: listOfWords) {
                Concept item =new Concept();
                item = (Concept)memory.search("concepts",word);
                Boolean valid = false;
                for (ConceptClass cc:this.listOfConceptsDefault) {
                    if(cc.getNameClass().equals(item.getClassName()))
                    {
                        valid =true;
                        break;
                    }
                }

                    if (item.getFoundInDB())
                    {
                        if(valid==true) {
                            this.listOfConceptsFound.add(item);
                        }
                    }
                    else {

                        for (ConceptClass cc:this.listOfConceptsDefault) {

                            item = this.register.regist(word, cc);

                            if (item.getFoundInDB()) {
                                this.listOfConceptsFound.add(item);
                                break;
                            }

                         }
                         }

            }

      //  this.theJSON = toJSON(listOfConceptsFound);
    }
/*
    private Map<String, Set<String>> getConceptsFromTxt(String filename) {
        // function gets the concepts from a txt using regex

        Map<String, Set<String>> result = new HashMap<>();

        try {
            // open the file and read the whole content
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            // split the content with regex
            String str = new String(data, "UTF-8");
            List<String> allMatches = new ArrayList<>();
            String patternBlock = "\\{([^\\}]+)\\}";
            String patternName = "\\[(\\w+)\\]";
            String patternKeywords = "\"([^\"]+)\"";
            Pattern r = Pattern.compile(patternBlock);
            Pattern r1 = Pattern.compile(patternName);
            Pattern r2 = Pattern.compile(patternKeywords);

            // split after blocks -> {...}
            Matcher m = r.matcher(str);
            while (m.find()) {
                allMatches.add(m.group());
            }
           // System.out.println(allMatches);

            // split after name and keywords
            for (String match: allMatches) {
                Matcher m1 = r1.matcher(match);
                Matcher m2 = r2.matcher(match);
                String name = null;
                Set<String> keywords = new TreeSet<>();
                while (m1.find()) {
                    name = m1.group(1);
                }

                while (m2.find()) {
                    keywords.add(m2.group(1));
                }
                result.put(name, keywords);
            }
            return result;

        } catch (FileNotFoundException error) {
            System.out.println(error);
        } catch (IOException error) {
            System.out.println(error);
        }
        return null;
    }
*/
    private JSONObject toJSON(Set<Concept> list) {
        // a function to take data from a list and make a json

        JSONObject jsonData = new JSONObject();

        for (Concept concept: list) {
            Set<Object> value = new TreeSet<>();
            value.add(concept.getConceptClass().getNameClass());
            value.add(concept.getUrl());
            value.add(Integer.toString(concept.getAccuracy()));
            try {
                jsonData.put(concept.getName(), value);
            } catch (JSONException error) {
                System.out.println(error);
                error.printStackTrace();
                return null;
            }
        }

      //  System.out.println(jsonData.toString());
        return  jsonData;
    }

    public Set<Concept> getListOfConceptsFound() { return listOfConceptsFound; }

    public JSONObject getTheJSON() { return theJSON; }
}
