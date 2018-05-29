#!/bin/bash
cd src/
javac registry/*.java
javac Interfaces/*.java
javac Enum/*.java
cd ..
cp src/Interfaces/*.class dir_registry/Interfaces
cp src/registry/*.class dir_registry/registry
cp -R src/resources dir_registry
cp -R src/Enum dir_registry
cd dir_registry

#rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 5000

#java registry.ServerRegisterRemoteObject
java -Djava.rmi.server.codebase="file:///home/rute/Documents/cadeiras/SD/projtobeans/proj/Registry/dir_registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     registry.ServerRegisterRemoteObject
