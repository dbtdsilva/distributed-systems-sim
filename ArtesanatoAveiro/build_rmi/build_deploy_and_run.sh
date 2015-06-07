set -x
echo "	> Compiling all source code "
find . -name "*.class" -exec rm -rf {} \;
find . -type d -empty -delete
find .. -name "*.java" > sources.txt
javac @sources.txt -d .
echo " > Moving to the proper folders"
mkdir -p dir_clientSide/entrepreneur/
mkdir -p dir_clientSide/customer/
mkdir -p dir_clientSide/craftsman/
mkdir -p dir_serverSide/logging/
mkdir -p dir_serverSide/shop/
mkdir -p dir_serverSide/workshop/
mkdir -p dir_serverSide/warehouse/
mkdir -p dir_registry

cp --parents -r Registry/ dir_registry/
cp --parents -r Interfaces/ dir_registry/
cp --parents -r Structures/ dir_registry/
cp --parents -r ServerSide/Logger/ dir_serverSide/logging/
cp --parents -r Interfaces/ dir_serverSide/logging/
cp --parents -r Structures/ dir_serverSide/logging/
cp --parents -r ServerSide/Shop/ dir_serverSide/shop/
cp --parents -r Interfaces/ dir_serverSide/shop/
cp --parents -r Structures/ dir_serverSide/shop/
cp --parents -r ServerSide/Warehouse/ dir_serverSide/warehouse/
cp --parents -r Interfaces/ dir_serverSide/warehouse/
cp --parents -r Structures/ dir_serverSide/warehouse/
cp --parents -r ServerSide/Workshop/ dir_serverSide/workshop/
cp --parents -r Interfaces/ dir_serverSide/workshop/
cp --parents -r Structures/ dir_serverSide/workshop/
cp --parents -r ClientSide/Entrepreneur/ dir_clientSide/entrepreneur/
cp --parents -r Interfaces/ dir_clientSide/entrepreneur/
cp --parents -r Structures/ dir_clientSide/entrepreneur/
cp --parents -r ClientSide/Customer/ dir_clientSide/customer/
cp --parents -r Interfaces/ dir_clientSide/customer/
cp --parents -r Structures/ dir_clientSide/customer/
cp --parents -r ClientSide/Craftsman/ dir_clientSide/craftsman/
cp --parents -r Interfaces/ dir_clientSide/craftsman/
cp --parents -r Structures/ dir_clientSide/craftsman/

if [[ ! -e final_logs ]]; then
    mkdir final_logs;
fi

if [[ "$1" = "remote" ]]; then
	echo "	> Sending the proper files to the correct workstation"
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_registry/ sd0405@l040101-ws10.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/logging/ sd0405@l040101-ws01.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/shop/ sd0405@l040101-ws05.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/workshop/ sd0405@l040101-ws04.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/warehouse/ sd0405@l040101-ws03.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_clientSide/entrepreneur/ sd0405@l040101-ws11.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_clientSide/craftsman/ sd0405@l040101-ws08.ua.pt:~
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_clientSide/customer/ sd0405@l040101-ws09.ua.pt:~

	echo "  > Cleaning logs from the server where Logging is going to run"
	sshpass -p sistema2015 ssh sd0405@l040101-ws01.ua.pt -o StrictHostKeyChecking 'rm *.log'

	echo "	> Executing each jar file on the proper workstation"
	sshpass -p sistema2015 ssh sd0405@l040101-ws10.ua.pt -o StrictHostKeyChecking=no './set_rmiregistry.sh 22440'
	sshpass -p sistema2015 ssh sd0405@l040101-ws10.ua.pt -o StrictHostKeyChecking=no './dir_registry/registry_com.sh'
	sshpass -p sistema2015 ssh sd0405@l040101-ws01.ua.pt -o StrictHostKeyChecking=no './' &
	PID_Logging=$!
	sshpass -p sistema2015 ssh sd0405@l040101-ws05.ua.pt -o StrictHostKeyChecking=no 'java -jar Shop.jar' &
	sshpass -p sistema2015 ssh sd0405@l040101-ws04.ua.pt -o StrictHostKeyChecking=no 'java -jar Workshop.jar' &
	sshpass -p sistema2015 ssh sd0405@l040101-ws03.ua.pt -o StrictHostKeyChecking=no 'java -jar Warehouse.jar' &
	sshpass -p sistema2015 ssh sd0405@l040101-ws11.ua.pt -o StrictHostKeyChecking=no 'java -jar Entrepreneur.jar' &
	sshpass -p sistema2015 ssh sd0405@l040101-ws08.ua.pt -o StrictHostKeyChecking=no 'java -jar Craftsman.jar' &
	sshpass -p sistema2015 ssh sd0405@l040101-ws09.ua.pt -o StrictHostKeyChecking=no 'java -jar Customer.jar' &
	echo "	> Waiting for simulation to end (generate a logging file).."
	wait $PID_Logging
	echo "  > Simulation ended, copying log file to the local machine"
	
	sshpass -p sistema2015 scp -o StrictHostKeyChecking=no sd0405@l040101-ws01.ua.pt:~/Artesanato* final_logs/
fi
if [[ "$1" = "local" ]]; then
	rm -rf dir_serverSide/logging/*.log
	echo "	> Running at local machine"
	./set_rmiregistry.sh 22440 &
	PID0=$!
	sleep 3
	cd dir_registry; ./registry_com_alt.sh &
	cd ..
	PID1=$!
	sleep 3
	cd dir_serverSide/logging; ./serverSide_com_alt.sh &
	cd ../..
	PID2=$!
	sleep 3
	cd dir_serverSide/shop; ./serverSide_com_alt.sh &
	cd ../..
	PID3=$!
	sleep 3
	cd dir_serverSide/workshop; ./serverSide_com_alt.sh &
	cd ../..
	PID4=$!
	sleep 3
	cd dir_serverSide/warehouse; ./serverSide_com_alt.sh &
	cd ../..
	PID5=$!
	sleep 3
	cd dir_clientSide/entrepreneur; ./clientSide_com_alt.sh &
	cd ../..
	PIDEntr=$!
	cd dir_clientSide/customer; ./clientSide_com_alt.sh &
	cd ../..
	cd dir_clientSide/craftsman; ./clientSide_com_alt.sh &
	cd ../..
	wait $PIDEntr
	sleep 1
	pkill -TERM -P $PID0 
	pkill -TERM -P $PID1
	pkill -TERM -P $PID2 
	pkill -TERM -P $PID3 
	pkill -TERM -P $PID4 
	pkill -TERM -P $PID5 
	mv dir_serverSide/logging/Artesanato* final_logs/
fi