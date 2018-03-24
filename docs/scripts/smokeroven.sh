#!/usr/bin/env bash
#
#
#
#
#
#
export HOME=/opt/smokeroven
export PID=${HOME}/bin/.pid

case $1 in
    start)
        start-stop-deamon -b -exec /bin/java -- -Xmx1G -Xms1G -Dspring.configuration.location=${HOME}/config -jar ${HOME}/SmokerOvenApplication.jar
    ;;
    stop)
        start-stop-deamon --stop
    ;;
    status)
        start-stop-deamon --test
    ;;
    restart)
        $0 stop
        $0 start
    ;;
    *)

esac
