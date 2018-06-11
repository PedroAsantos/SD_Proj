#!/bin/bash
(
ssh sd0309@l040101-ws10.ua.pt "cd SD_Proj/Race_RMI/proj/Registry/; ./startRegistry.sh;" &
sleep 3
ssh sd0309@l040101-ws01.ua.pt "cd SD_Proj/Race_RMI/proj/Repository/src/; rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class;echo Cleaned ; javac RunRepository.java; java RunRepository " &
sleep 3
ssh sd0309@l040101-ws02.ua.pt "cd SD_Proj/Race_RMI/proj/MonitorControlCenter/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunControlCenter.java; java RunControlCenter " &
sleep 3
ssh sd0309@l040101-ws03.ua.pt "cd SD_Proj/Race_RMI/proj/MonitorPaddock/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunPaddock.java; java RunPaddock " &
sleep 3
ssh sd0309@l040101-ws04.ua.pt "cd SD_Proj/Race_RMI/proj/MonitorRacingTrack/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorRacingTrack.java; java RunMonitorRacingTrack " &
sleep 3
ssh sd0309@l040101-ws05.ua.pt "cd SD_Proj/Race_RMI/proj/MonitorBettingCenter/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorBettingCenter.java; java RunMonitorBettingCenter " &
sleep 3
ssh sd0309@l040101-ws06.ua.pt "cd SD_Proj/Race_RMI/proj/MonitorStable/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunMonitorStable.java; java RunMonitorStable " &
sleep 3
ssh sd0309@l040101-ws07.ua.pt "cd SD_Proj/Race_RMI/proj/Broker/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; rm communication/*.class; javac RunBroker.java; java RunBroker " &
sleep 3
ssh sd0309@l040101-ws08.ua.pt "cd SD_Proj/Race_RMI/proj/Spectators/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; javac RunSpectators.java; java RunSpectators " &
sleep 3
ssh sd0309@l040101-ws09.ua.pt "cd SD_Proj/Race_RMI/proj/Horses/src/;rm Enum/*.class;rm Interfaces/*.class;rm sharingRegions/*.class;rm *.class; rm communication/*.class; javac RunHorses.java; java RunHorses "
)
