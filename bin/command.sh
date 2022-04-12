#!/bin/bash

node=$1
command=$2
port=8080

if [ $# -le 1 ]; then
	echo "node and command params must not blank, try below: "
	echo "1) sh command.sh service_1 start"
	echo "2) sh command.sh service_1 start 8080"
	exit 1
fi


if [ $3 ];then
    port=$3
fi



start(){
	stop
	nohup java -jar ../src/neptune-binlog-thief-bootstrap-0.0.1-exec.jar ${node} \
	--server.port=$port --spring.config.location=../config/application.properties > ../log/app.log 2>&1 &
	echo $! > ./${node}_pid.file
}

stop(){
	kill $(cat ./${node}_pid.file)
}



if [ $command = start ]
then
	start
	echo "start ${node} on ${port} success"
elif [ $command = stop ]
then
	stop
	echo "stop ${node} success"
else
	echo 'unsupported command:' $command
fi 

