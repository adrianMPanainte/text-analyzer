import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ConceptClassHandler {
    static class addConcept implements HttpHandler {
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

                String name = jsonObj.getString("name");
                String validText = jsonObj.getString("validText");
                String nonValidText = jsonObj.getString("nonValidText");
                String subclass = jsonObj.getString("subclass");
                String characteristics = jsonObj.getString("characteristics");

                TextAnalyzer textAnalyzer = new TextAnalyzer();
                String response = "ok";

                textAnalyzer.addConceptClass(name, validText, nonValidText, subclass, characteristics);
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
