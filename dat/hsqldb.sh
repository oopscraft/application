#!/bin/bash

function start() {
	java -cp hsqldb.jar org.hsqldb.server.Server -database.0 file:application -dbname.0 application
}

function stop() {
	java -jar sqltool.jar --sql "SHUTDOWN;" application
}

case ${1} in 
	start)
		start
		;;
	stop)
		stop
		;;
	*)
		echo "Usage: ${0} [start|stop]"
		;;
esac

exit 0
