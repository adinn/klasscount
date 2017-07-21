# klasscount

Small java agent to list all currently loaded classes and count hwo
many of them belong to JDK packages

Usage

Build the klasscount agent
```
$ mvn install
```
Find the pid of the JVM you want to check
```
$ jps -l
...
28317 HelloWait
```
Install the agent into the JVM
```
$ java -cp $PWD/target/klasscount-1.0-SNAPSHOT.jar:$JAVA_HOME/lib/tools.jar \
    org.jboss.klasscount.AgentInstall pid target/klasscount-1.0-SNAPSHOT.jar
```
where pid is the process id of the JVM listed by jps that you are interested in (e.g. 28317)

The target JVM will dump stats to its System.out looking like this
```
       1: org.jboss.klasscount.AgentMain                             - 0       
       2: HelloWait                                                  - 0       
       3: sun.instrument.InstrumentationImpl$1                       + 1       
       4: java.util.concurrent.ConcurrentHashMap$ForwardingNode      + 2       
       5: sun.security.util.ManifestEntryVerifier                    + 3       
       . . .
     538: [C                                                         + 536
     539: [Z                                                         + 537     
```
The indices on the left count successive loaded classes. The runing
total on the right counts whkch ones belong to JDK runtime packages.
