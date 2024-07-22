#!/bin/bash

# run the default server without any args
java -jar libs/reference-server-0.2.3.jar

# run server on a different port
#java -jar libs/reference-server-0.2.3.jar -p 5001

# run server with size of 20x20
#java -jar libs/reference-server-0.2.3.jar -s 20 -o 1,2

# run server with obstacle at [1,2]
#java -jar libs/reference-server-0.2.3.jar -o 1,2

# run server on a different port with size of 20x20 and obstacle at [1,2]
#java -jar libs/reference-server-0.2.3.jar -p 5001 -s 20 -o 1,2