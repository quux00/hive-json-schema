# Overview

The best tool for using JSON docs with Hive is [rcongui's openx Hive-JSON-Serde](https://github.com/rcongiu/Hive-JSON-Serde).  When using that JSON Serde, you define your Hive schema based on the contents of the JSON.

Hive schemas understand arrays, maps and structs.  You can map a JSON array to a Hive array and a JSON "object" to either a Hive map or struct.  I prefer to map JSON objects to structs.

This tool will take a representative JSON document and generate the Hive schema (CREATE TABLE statement) for use with the openx Hive-JSON-Serde.

For more information on using the openx Hive-JSON-SerDe, see my [blog post entry](http://thornydev.blogspot.com/2013/07/querying-json-records-via-hive.html).


# Build

    mvn package

Creates `json-hive-schema-1.0.jar` and `json-hive-schema-1.0-jar-with-dependencies.jar` in the `target` directory.



# Usage

#### with the non-executable jar

    java -cp target/json-hive-schema-1.0.jar net.thorndev.JsonHiveSchema file.json

    # optionally specify the name of the table
    java -cp target/json-hive-schema-1.0.jar net.thorndev.JsonHiveSchema file.json my_table_name


#### with the executable jar

    java -jar target/json-hive-schema-1.0-jar-with-dependencies.jar file.json

    java -jar target/json-hive-schema-1.0-jar-with-dependencies.jar file.json my_table_name


Both print the Hive schema to stdout.


#### Example:

For the JSON document:


    {
      "description": "my doc",
      "foo": {
        "bar": "baz",
        "quux": "revlos",
        "level1" : {
          "l2string": "l2val",
          "l2struct": {
            "level3": "l3val"
          }
        }
      },
      "wibble": "123",
      "wobble": [
        {
          "entry": 1,
          "EntryDetails": {
            "details1": "lazybones",
            "details2": 414
          }
        },
        {
          "entry": 2,
          "EntryDetails": {
            "details1": "entry 123"
          }
        }
      ]
    }


Here's the output:

    $ java -jar target/json-hive-schema-1.0-jar-with-dependencies.jar in.json TopQuark
    CREATE TABLE TopQuark (
      description string,
      foo struct<bar:string, level1:struct<l2string:string, l2struct:struct<level3:string>>, quux:string>,
      wibble string,
      wobble array<struct<entry:int, entrydetails:struct<details1:string, details2:int>>>)
    ROW FORMAT SERDE 'org.openx.data.jsonserde.JsonSerDe';



You can then load your data into Hive and run queries like this:

    hive > select wobble.entry, wobble.EntryDetails.details1, wobble.EntryDetails[0].details2 from TopQuark;
    entry   details1                    details2
    [1,2]   ["lazybones","entry 123"]   414
    Time taken: 15.665 seconds
