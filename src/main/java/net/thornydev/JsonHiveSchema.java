package net.thornydev;

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
    System.out.println(json);
    System.out.println("--------------------------");
    
    JSONObject jo = new JSONObject(json);
    System.out.println(jo);
    System.out.println("--------------------------");

    System.out.println(jo.toHiveSchema());
  }
}
