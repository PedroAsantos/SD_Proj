#!/bin/bash
gnome-terminal -x bash -c "cd Repository/src/; javac *.java; java RunRepository ; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorBettingCenter/src/; javac *.java; java RunMonitorBettingCenter ; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorControlCenter/src/; javac *.java; java RunControlCenter ; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorStable/src/; javac *.java; java RunMonitorStable ; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorPaddock/src/; javac *.java; java RunPaddock ; exec $SHELL"
gnome-terminal -x bash -c "cd MonitorRacingTrack/src/; javac *.java; java RunMonitorRacingTrack ; exec $SHELL"

gnome-terminal -x bash -c "cd Broker/src/; javac *.java; java RunBroker ; exec $SHELL"
gnome-terminal -x bash -c "cd Horses/src/; javac *.java; java RunHorses ; exec $SHELL"
gnome-terminal -x bash -c "cd Spectators/src/; javac *.java; java RunSpectators ; exec $SHELL"
