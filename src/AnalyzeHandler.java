import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeHandler {
    static class analyzeText implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            Map<String, Object> parameters = new HashMap<String, Object>();
            InputStreamReader isr =  new InputStreamReader(he.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            String result = buf.toString();
            JSONObject jsonObj = null;
            try {

                br.close();
                isr.close();

                jsonObj = new JSONObject(result);

                String text = jsonObj.getString("text");
                String conceptClass = jsonObj.getString("conceptClass");


                System.out.println(text + " " + conceptClass);
                JSONObject res = new JSONObject();

                TextAnalyzer textAnalyzer = new TextAnalyzer();
                textAnalyzer.textAnalyzer(text, conceptClass);
                List<String> concepts = new ArrayList<>();

                System.out.println(textAnalyzer.getListOfConceptsFound());

                Concept[] c = new Concept[textAnalyzer.getListOfConceptsFound().size()];
                int k=0;
                for(Concept x : textAnalyzer.getListOfConceptsFound())
                {
                    c[k]=x;
                    k++;

                }
                res.put("Name", c[0].getName());
                res.put("Attachement path", "");
                res.put("Class", c[0].getConceptSubclass());
                res.put("Metadata", "");
                res.put("Class matching", c[0].getAccuracy());
                res.put("Concept matching", 1.0f/c.length);

                List<JSONObject> othersList = new ArrayList<>();

                for(int i = 1; i < c.length; i ++) {
                    JSONObject o = new JSONObject();
                    o.put("name", c[i].getName());
                    o.put("matching", 1.0f/c.length);
                    othersList.add(o);
                }

                res.put("others", othersList.toArray());
                res.put("Description", "");

                // send response
                String response = res.toString();

                System.out.println(response);
                he.sendResponseHeaders(200, response.length());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
