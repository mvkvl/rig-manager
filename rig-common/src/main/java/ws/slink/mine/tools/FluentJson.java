package ws.slink.mine.tools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 *   implementation of Fluent Interface for org.json.simple
 */
public class FluentJson implements Iterable<Object> {
    private Object value;

    public FluentJson(Object value) {
        this.value = value;
    }

    public static FluentJson copy(FluentJson jsonObj) {
        return new FluentJson(jsonObj.value);
    }

    @Override
    public Iterator<Object> iterator() {
        JSONArray a = (JSONArray) value;
        return a.iterator();
    }
    public Stream<FluentJson> stream() {
        JSONArray a = (JSONArray) value;
        return a.stream().map(FluentJson::new);
    }

    public FluentJson get(int index) {
        JSONArray a = (JSONArray) value;
        return new FluentJson(a.get(index));
    }
    public FluentJson get(String key) {
        JSONObject o = (JSONObject) value;
        return new FluentJson(o.get(key));
    }


    public int intValue() {
        return Integer.parseInt(value.toString());
    }
    public int getInt(String key) {
        JSONObject o = (JSONObject) value;
        return new FluentJson(o.get(key)).intValue();
    }
    public double doubleValue() {
        return Double.parseDouble(value.toString());
    }
    public double getDouble(String key) {
        JSONObject o = (JSONObject) value;
        return new FluentJson(o.get(key)).doubleValue();
    }
    public String toString() {
        return value == null ? null : value.toString();
    }
    public String getString(String key) {
        JSONObject o = (JSONObject) value;
        return new FluentJson(o.get(key)).toString();
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
