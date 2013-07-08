# Overview

The best tool for using JSON docs with Hive is [rcongui's openx Hive-JSON-Serde](https://github.com/rcongiu/Hive-JSON-Serde).  When using that JSON Serde, you define your Hive schema based on the contents of the JSON.

Hive schemas understand arrays, maps and structs.  You can map a JSON array to a Hive array and a JSON "object" to either a Hive map or struct.  I prefer to map JSON objects to structs.

This tool will take a representative JSON document and generate the Hive schema (CREATE TABLE statement) for use with the openx Hive-JSON-Serde.

For more information on using the openx Hive-JSON-SerDe, see my [blog post entry](http://thornydev.blogspot.com/2013/07/querying-json-records-via-hive.html).


# Build

    mvn package

Creates json-hive-schema-1.0.jar in the `target` directory.



# Usage

    java -cp target/json-hive-schema-1.0.jar net.thorndev.JsonHiveSchema file.json

    # optionally specify the name of the table
    java -cp target/json-hive-schema-1.0.jar net.thorndev.JsonHiveSchema file.json my_table_name
    
Prints the Hive schema to stdout.
