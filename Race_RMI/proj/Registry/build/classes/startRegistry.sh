#!/bin/bash
javac registry/*.java
javac Interfaces/*.java
cp Interfaces/*.class classesDir
cp registry/*.class classesDir

rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 2007

java classesDir/ServerRegisterRemoteObject.class
