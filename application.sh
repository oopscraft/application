#!/bin/bash
APP_FILE=$(basename ${0})
APP_NAME=${APP_FILE%.*}
PID_FILE=${APP_NAME}.pid
touch ${PID_FILE}
PID=$(cat ${PID_FILE})
MAIN_CLASS="net.oopscraft.application.Application"

# start
function start() {
	echo "+ Start Application"
	if [ -f /proc/${PID}/status ]; then
		echo "Application is already running." 
		status
		exit -1
	fi
	CLASSPATH="./*:./lib/*"
	JAVA_OPTS=${JAVA_OPTS}" -server -Djava.net.preferIPv4Stack=true -Dlog4j.configuration=file:conf/log4j.xml"
	java ${JAVA_OPTS} -classpath ${CLASSPATH} ${MAIN_CLASS} 2>&1 > /dev/null & 
	echo $! > ${PID_FILE}
}

# status
function status() {
    echo "Application Status";
	ps -f ${PID} | grep java | grep -F ${MAIN_CLASS}
}

# stop
function stop() {
    echo "Stop Application";
	if [ ! -f /proc/${PID}/status ]; then
		echo "Application is not running."
		exit -1
	fi
	kill ${PID}
}

# log
function log() {
	tail -F ./log/application.log
}	

# crypto
function crypto() {
	java -cp ./*:./lib/* net.oopscraft.application.core.PasswordBasedEncryptor
}

# main
case ${1} in
    start) 
        start
        ;;
	status)
		status
		;;
	stop)
		stop
		;;
	log)
		log
		;;
	crypto)
		crypto
		;;
    *)
        echo "Usage: ${0} [start|status|stop|log|crypto]"
        ;;
esac

exit 0


