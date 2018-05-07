#!/bin/bash
Password=depoisdecidimos

sshpass -p Password ssh sd0309@l040101-ws01.ua.pt "cd SD_Proj/Race_2/proj; ./Repository.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws02.ua.pt "cd SD_Proj/Race_2/proj; ./ControlC.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws03.ua.pt "cd SD_Proj/Race_2/proj; ./Paddock.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws04.ua.pt "cd SD_Proj/Race_2/proj; ./RacingT.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws05.ua.pt "cd SD_Proj/Race_2/proj; ./BettingC.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws06.ua.pt "cd SD_Proj/Race_2/proj; ./Stable.sh" & sleep 2

sshpass -p Password ssh sd0309@l040101-ws10.ua.pt "cd SD_Proj/Race_2/proj; ./Broker.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws08.ua.pt "cd SD_Proj/Race_2/proj; ./Spec.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws09.ua.pt "cd SD_Proj/Race_2/proj; ./Horses.sh" & sleep 2