# UDP Coordinator

A small Java networking project in which several local processes share integer values. The first process becomes the coordinator and the others send their values to it over UDP.

## Run

Requires JDK 21. Compile with `javac -encoding UTF-8 -d out src/*.java`, then start `java -cp out DAS 5000 10`. Start another terminal with the same port and a different integer to add a worker.
