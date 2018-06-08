#!/bin/bash
gnome-terminal -x bash -c "cd Registry/src/ ;rm Enum/*.class;rm Interfaces/*.class;rm registry/*.class;rm *.class;echo Cleaned; javac registry/*.java;javac Interfaces/*.java"
gnome-terminal -x bash -c "cd Registry/src/ ;rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 5000"
gnome-terminal -x bash -c "cd Registry/src/ ;javac registry/ServerRegisterRemoteObject.java; java registry.ServerRegisterRemoteObject"

gnome-terminal -x bash -c "cd Repository/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class;echo Cleaned ; javac RunRepository.java; java RunRepository; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorBettingCenter/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorBettingCenter.java; java RunMonitorBettingCenter; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorControlCenter/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunControlCenter.java; java RunControlCenter ; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorStable/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorStable.java; java RunMonitorStable; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorPaddock/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunPaddock.java; java RunPaddock; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorRacingTrack/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorRacingTrack.java; java RunMonitorRacingTrack; exec $SHELL"

gnome-terminal -x bash -c "cd Broker/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; rm communication/*.class; rm stakeholders/.class; javac RunBroker.java; java RunBroker; exec $SHELL"
gnome-terminal -x bash -c "cd Horses/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; rm communication/*.class; rm stakeholders/.class; javac RunHorses.java; java RunHorses; exec $SHELL"
gnome-terminal -x bash -c "cd Spectators/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunSpectators.java; java RunSpectators; exec $SHELL"