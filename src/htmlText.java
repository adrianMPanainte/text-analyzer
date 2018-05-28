import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class htmlText {
    private String text;

    enum Found {
        yes, no;
    }

    public Found getValidpage() {
        return validpage;
    }

    private Found validpage;

    public htmlText(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            text = Jsoup.parse(String.valueOf(doc)).text();

            validpage = Found.yes;
        } catch (Exception ex) {
            validpage = Found.no;
            System.out.println(ex);

        }

    }
    public String getText() {
        return text;
    }
}