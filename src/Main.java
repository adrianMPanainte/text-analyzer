import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {


        //LongMemory mem = new LongMemory("brain","root","");
        //ConceptClass an =new ConceptClass("animals");
        //an.setSubclass("Class:");
        //an.setKeywords("Kingdom: Animalia");
        //an.addCharacteristics("poisonous");
       // an.addCharacteristics("venomous");
       // an.addCharacteristics("carnivorous");
      //  mem.add(an,"concepts_class");
    //   TextAnalyzer a = new TextAnalyzer();
      //a.addConceptClass("animlas","dog","Barcelona","Class:","");
//        a.textAnalyzer("Dogs and lions eat nice cats. whale" ,"animals");
  //      System.out.println(a.getListOfConceptsFound());
       // TextAnalyzerFocus a = new TextAnalyzerFocus();
       // a.textAnalyzer("Big dogs, cats and cute polar bears are cool, smart and nice. A dog eat a cat yesterday. Dogs and bears are pooping more than cats. Dogs and bears are fat, but cats are big and cute. Dogs eat meat.","animals");
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            System.out.println(server.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/analyze", new AnalyzeHandler.analyzeText());
        server.createContext("/add-concept-class", new ConceptClassHandler.addConcept());
        server.setExecutor(null); // creates a default executor
        System.out.println("AnalyzeHandler is listening on port 8000...");
        server.start();
    }
}
