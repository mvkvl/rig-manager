package ws.slink.mine.tools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *   implementation of Fluent Interface for org.json.simple
 */
public class FluentJson {
    private Object value;

    public FluentJson(Object value) {
        this.value = value;
    }

    public FluentJson get(int index) {
        JSONArray a = (JSONArray) value;
        return new FluentJson(a.get(index));
    }

    public FluentJson get(String key) {
        JSONObject o = (JSONObject) value;
        return new FluentJson(o.get(key));
    }

    public String toString() {
        return value == null ? null : value.toString();
    }

    public Number toNumber() {
        return (Number) value;
    }

    public static final String JSON_STR = "{\"entries\": [{\"runs\": [{\"id\": 11}, {\"id\": 12}, {\"id\": 13}]},{\"runs\": [{\"id\": 21}, {\"id\": 22}, {\"id\": 23}]}]}";
    public static void main(String [] args) throws ParseException {
        FluentJson fj = new FluentJson(new JSONParser().parse(JSON_STR));
        String run_id = fj.get("entries")
                          .get(0)
                          .get("runs")
                          .get(0)
                          .get("id")
                          .toString();
        System.out.println(run_id);
    }
}
