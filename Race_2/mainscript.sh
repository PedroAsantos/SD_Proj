#!/bin/bash
Password=depoisdecidimos

sshpass -p Password ssh sd0309@l040101-ws01.ua.pt "./Repository.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws02.ua.pt "./ControlC.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws03.ua.pt "./Paddock.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws04.ua.pt "./RacingT.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws05.ua.pt "./BettingC.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws06.ua.pt "./Stable.sh" & sleep 2

sshpass -p Password ssh sd0309@l040101-ws10.ua.pt "./Broker.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws08.ua.pt "./Spec.sh" & sleep 2
sshpass -p Password ssh sd0309@l040101-ws09.ua.pt "./Horses.sh" & sleep 2