#!/bin/bash
gnome-terminal -x bash -c "cd Registry/src/ ;rm Enum/*.class;rm Interfaces/*.class;rm registry/*.class;rm *.class;echo Cleaned; javac registry/*.java;javac Interfaces/*.java"
gnome-terminal -x bash -c "cd Registry/src/ ;rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 5000"
gnome-terminal -x bash -c "cd Registry/src/ ;javac registry/ServerRegisterRemoteObject.java; java registry.ServerRegisterRemoteObject"

gnome-terminal -x bash -c "cd Repository/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class;echo Cleaned ; javac RunRepository.java; java RunRepository"
gnome-terminal -x bash -c "cd MonitorBettingCenter/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorBettingCenter.java; java RunMonitorBettingCenter"
gnome-terminal -x bash -c "cd MonitorControlCenter/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunControlCenter.java; java RunControlCenter"
gnome-terminal -x bash -c "cd MonitorStable/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorStable.java; java RunMonitorStable"
gnome-terminal -x bash -c "cd MonitorPaddock/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunPaddock.java; java RunPaddock"
gnome-terminal -x bash -c "cd MonitorRacingTrack/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorRacingTrack.java; java RunMonitorRacingTrack"

gnome-terminal -x bash -c "cd Broker/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; rm communication/*.class; rm stakeholders/.class; javac RunBroker.java; java RunBroker;exec $shell"
gnome-terminal -x bash -c "cd Horses/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; rm communication/*.class; rm stakeholders/.class; javac RunHorses.java; java RunHorses;exec $shell"
gnome-terminal -x bash -c "cd Spectators/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunSpectators.java; java RunSpectators;exec $shell"
