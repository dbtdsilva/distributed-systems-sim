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

source config.ini

if [[ "$1" = "remote" ]]; then
	echo "  > Cleaning up remote server"
	sshpass -p sistema2015 ssh $GROUP@$REGISTER_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$LOGGING_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$SHOP_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$WORKSHOP_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$WAREHOUSE_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$ENTREPRENEUR_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$CRAFTSMEN_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	sshpass -p sistema2015 ssh $GROUP@$CUSTOMERS_HOST -o StrictHostKeyChecking=no 'rm -rf Public/*'
	echo "	> Creating folders on the remote server"
	sshpass -p sistema2015 ssh $GROUP@$REGISTER_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_registry/'
	sshpass -p sistema2015 ssh $GROUP@$LOGGING_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_serverSide/logging/'
	sshpass -p sistema2015 ssh $GROUP@$SHOP_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_serverSide/shop/'
	sshpass -p sistema2015 ssh $GROUP@$WORKSHOP_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_serverSide/workshop/'
	sshpass -p sistema2015 ssh $GROUP@$WAREHOUSE_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_serverSide/warehouse/'
	sshpass -p sistema2015 ssh $GROUP@$ENTREPRENEUR_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_clientSide/entrepreneur/'
	sshpass -p sistema2015 ssh $GROUP@$CRAFTSMEN_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_clientSide/craftsman/'
	sshpass -p sistema2015 ssh $GROUP@$CUSTOMERS_HOST -o StrictHostKeyChecking=no 'mkdir -p Public/classes/dir_clientSide/customer/'
	echo "	> Sending the proper files to the correct workstation"
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no * $GROUP@$REGISTER_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_registry/* $GROUP@$REGISTER_HOST:~/Public/classes/dir_registry/
	
	sshpass -p sistema2015 scp -o StrictHostKeyChecking=no dir_serverSide/java.policy $GROUP@$LOGGING_HOST:~/Public/classes/dir_serverSide/
	sshpass -p sistema2015 scp -o StrictHostKeyChecking=no dir_serverSide/java.policy $GROUP@$SHOP_HOST:~/Public/classes/dir_serverSide/
	sshpass -p sistema2015 scp -o StrictHostKeyChecking=no dir_serverSide/java.policy $GROUP@$WORKSHOP_HOST:~/Public/classes/dir_serverSide/
	sshpass -p sistema2015 scp -o StrictHostKeyChecking=no dir_serverSide/java.policy $GROUP@$WAREHOUSE_HOST:~/Public/classes/dir_serverSide/

	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$LOGGING_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$SHOP_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$WORKSHOP_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$WAREHOUSE_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$ENTREPRENEUR_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$CRAFTSMEN_HOST:~/Public/classes/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no config.ini $GROUP@$CUSTOMERS_HOST:~/Public/classes/

	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/logging/* $GROUP@$LOGGING_HOST:~/Public/classes/dir_serverSide/logging/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/shop/* $GROUP@$SHOP_HOST:~/Public/classes/dir_serverSide/shop/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/workshop/* $GROUP@$WORKSHOP_HOST:~/Public/classes/dir_serverSide/workshop/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_serverSide/warehouse/* $GROUP@$WAREHOUSE_HOST:~/Public/classes/dir_serverSide/warehouse/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_clientSide/entrepreneur/* $GROUP@$ENTREPRENEUR_HOST:~/Public/classes/dir_clientSide/entrepreneur/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_clientSide/craftsman/* $GROUP@$CRAFTSMEN_HOST:~/Public/classes/dir_clientSide/craftsman/
	sshpass -p sistema2015 scp -r -o StrictHostKeyChecking=no dir_clientSide/customer/* $GROUP@$CUSTOMERS_HOST:~/Public/classes/dir_clientSide/customer/
	
	echo "  > Cleaning logs from the server where Logging is going to run"
	sshpass -p sistema2015 ssh $GROUP@$LOGGING_HOST -o StrictHostKeyChecking=no 'rm -rf Public/classes/dir_serverSide/logging/*.log'

	echo "	> Executing each jar file on the proper workstation"
	sshpass -p sistema2015 ssh $GROUP@$REGISTER_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; ./set_rmiregistry.sh $REGISTER_PORT' &
	sleep 2
	sshpass -p sistema2015 ssh $GROUP@$REGISTER_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_registry; ./registry_com.sh' &
	sleep 4
	sshpass -p sistema2015 ssh $GROUP@$LOGGING_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_serverSide/logging; ./serverSide_com.sh' &
	PID_Log=$!
	sleep 4
	sshpass -p sistema2015 ssh $GROUP@$SHOP_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_serverSide/shop; ./serverSide_com.sh' &
	sshpass -p sistema2015 ssh $GROUP@$WAREHOUSE_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_serverSide/warehouse; ./serverSide_com.sh' &
	sleep 4
	sshpass -p sistema2015 ssh $GROUP@$WORKSHOP_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_serverSide/workshop; ./serverSide_com.sh' &
	sleep 4
	sshpass -p sistema2015 ssh $GROUP@$ENTREPRENEUR_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_clientSide/entrepreneur; ./clientSide_com.sh' &
	sshpass -p sistema2015 ssh $GROUP@$CUSTOMERS_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_clientSide/customer; ./clientSide_com.sh' &
	sshpass -p sistema2015 ssh $GROUP@$CRAFTSMEN_HOST -o StrictHostKeyChecking=no 'cd Public/classes/; source config.ini; cd dir_clientSide/craftsman; ./clientSide_com.sh' &
	
	echo "	> Waiting for simulation to end (generate a logging file).."
	wait $PID_Log
	sshpass -p sistema2015 scp -o StrictHostKeyChecking=no $GROUP@$LOGGING_HOST:~/Public/classes/dir_serverSide/logging/Artesanato* final_logs/
	sshpass -p sistema2015 ssh $GROUP@$REGISTER_HOST -o StrictHostKeyChecking=no 'killall rmiregistry; killall java'
fi

if [[ "$1" = "local" ]]; then
	rm -rf dir_serverSide/logging/*.log
	echo "	> Running at local machine"
	./set_rmiregistry_alt.sh $REGISTER_PORT &
	PID0=$!
	sleep 2
	cd dir_registry; ./registry_com_alt.sh &
	cd ..
	PID1=$!
	sleep 3
	cd dir_serverSide/logging; ./serverSide_com_alt.sh &
	cd ../..; PID_Log=$!
	sleep 3
	(cd dir_serverSide/shop; ./serverSide_com_alt.sh &)
	(cd dir_serverSide/warehouse; ./serverSide_com_alt.sh &)
	sleep 3
	(cd dir_serverSide/workshop; ./serverSide_com_alt.sh &)
	sleep 3
	(cd dir_clientSide/entrepreneur; ./clientSide_com_alt.sh &)
	(cd dir_clientSide/customer; ./clientSide_com_alt.sh &)
	(cd dir_clientSide/craftsman; ./clientSide_com_alt.sh &)
	wait $PID_Log
	pkill -TERM -P $PID0 
	pkill -TERM -P $PID1
	mv dir_serverSide/logging/Artesanato* final_logs/
fi
