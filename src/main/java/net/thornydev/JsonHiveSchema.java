package net.thornydev;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class JsonHiveSchema  {
  public static void main( String[] args ) throws Exception {
    String json = "{ " + 
        "\"description\": \"Pepper Flash Player\"," + 
        "\"foo\": {" + 
            "\"bar\": \"baz\"," +
            "\"quux\": \"revlos\"," +
            "\"level1\" : {" +
                "\"l2string\": \"l2val\"," +
                "\"l2struct\": {" + 
                  "\"level3\": \"l3val\"" +
                "}" +
            "}" +
         "}," + 
         "\"wibble\": \"123\"," + 
         "\"ari\": [" +
                 "{" +
                 "\"beetle\": \"bailey\"," + 
                 "\"chupa\": {" + 
                     "\"cabra\": \"hissss\"" +
                   "}" + 
                 "}," +
                 "{" +
                   "\"beetle\": \"dung\"" + 
                 "}" +
               "]"+
       "}";
//    System.out.println(json);
//    System.out.println("--------------------------");
    
    JSONObject jo = new JSONObject(json);
    System.out.println(jo);
    System.out.println("--------------------------");

    JsonHiveSchema schemaWriter = new JsonHiveSchema();
    System.out.println(schemaWriter.createHiveSchema(json));
  }
  
  static class OrderedIterator implements Iterator<String> {

    Iterator<String> it;
    
    public OrderedIterator(Iterator<String> iter) {
      SortedSet<String> keys = new TreeSet<String>();
      while (iter.hasNext()) {
        keys.add(iter.next());
      }
      it = keys.iterator();
    }
    
    public boolean hasNext() {
      return it.hasNext();
    }

    public String next() {
      return it.next();
    }

    public void remove() {
      it.remove();
    }
  }
  
  public String createHiveSchema(String json) throws JSONException {
    JSONObject jo = new JSONObject(json);
    
    try {
      // TODO: need to write wrapper Iterator that returns them in alphabetical order
      @SuppressWarnings("unchecked")
      Iterator<String> keys = jo.keys();
      keys = new OrderedIterator(keys);
      StringBuilder sb = new StringBuilder("CREATE TABLE x (\n");

      while (keys.hasNext()) {
        String k = keys.next();
        sb.append("  ");
        sb.append(k.toString());
        sb.append(' ');
        sb.append(valueToHiveSchema(jo.opt(k)));
        sb.append(',').append("\n");
      }
    
      sb.replace(sb.length() - 2, sb.length(), ")\n"); // remove last comma
      return sb.append("ROW FORMAT SERDE 'org.openx.data.jsonserde.JsonSerDe';").toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private String toHiveSchema(JSONObject o) { 
    @SuppressWarnings("unchecked")
    Iterator<String> keys = o.keys();
    keys = new OrderedIterator(keys);
    StringBuilder sb = new StringBuilder("struct<");
    
    while (keys.hasNext()) {
      String k = keys.next();
      sb.append(k.toString());
      sb.append(':');
      sb.append(valueToHiveSchema(o.opt(k)));
      sb.append(", ");
    }
    sb.replace(sb.length() - 2, sb.length(), ">"); // remove last comma
    return sb.toString();
  }

  private String toHiveSchema(JSONArray a) {
    try {
      return "array<" + aryjoin(a, ",") + '>';
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String aryjoin(JSONArray a, String separator) throws JSONException {
    int len = a.length();
    StringBuilder sb = new StringBuilder();

    if (len == 0) {
      throw new IllegalStateException("Array is empty: " + a.toString());
    }
    
    Object entry0 = a.get(0);
    if (entry0 instanceof String) {
      sb.append("string");
    } else if (entry0 instanceof JSONObject) {
      sb.append( toHiveSchema((JSONObject)entry0) );
    } else if (entry0 instanceof JSONArray) {    
      sb.append( toHiveSchema((JSONArray)entry0) );
    }
    return sb.toString();
  }
  
  private String valueToHiveSchema(Object o) {
    if (o instanceof String) {
      return "string";
    } else if (o instanceof JSONObject) {
      return toHiveSchema((JSONObject)o);
    } else if (o instanceof JSONArray) {
      return toHiveSchema((JSONArray)o);
    } else {
      throw new IllegalArgumentException("unknown type: " + o.getClass());
    }
  }
}
