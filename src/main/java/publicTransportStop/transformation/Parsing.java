package publicTransportStop.transformation;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parsing {
    public static String parseHttpResponse(Document document) {
        Elements headers = document.select("thead").getFirst().select("td");
        Elements table = document.select("tbody").select("tr");
        StringBuilder builder = new StringBuilder("");
        String s1 = String.format("%-14s", headers.get(0).text());
        String s2 = String.format("%-59s", headers.get(1).text());
        String s3 = String.format("%s\n", headers.get(2).text());
        builder.append(s1).append(s2).append(s3);

        for (int i = 0; i < table.size(); i++) {
            Elements el = table.get(i).select("td.trans-num, a, div.trans-model, td.trans-position");
            for (int j = 0; j < el.size() / 2; j++) {
                String s = String.format("%-14s", el.get(j).ownText());
                builder.append(s);
            }
            builder.append("\n");
            for (int j = el.size() / 2; j < el.size(); j++) {
                String s4 = String.format("%-14s", "");
                String s5 = String.format("%-45s", el.get(j).ownText());
                builder.append(s4).append(s5);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}



















