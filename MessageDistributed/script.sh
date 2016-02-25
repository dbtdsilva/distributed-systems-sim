set -x
echo "	> Compiling all source code to a directory __bin (using a Makefile)"
make;

echo "	> Creating directory jars if it doesn't exist"
if [[ ! -e Jars ]]; then
    mkdir Jars;
fi

echo "	> Generating all different jars (7 at total)"
(cd __bin;
jar cfe Entrepreneur.jar ClientSide.Entrepreneur.EntrepreneurExec ./
jar cfe Craftsman.jar ClientSide.Craftsman.CraftsmanExec ./
jar cfe Customer.jar ClientSide.Customer.CustomerExec ./
jar cfe Workshop.jar ServerSide.Workshop.WorkshopExec ./
jar cfe Shop.jar ServerSide.Shop.ShopExec ./
jar cfe Warehouse.jar ServerSide.Warehouse.WarehouseExec ./
jar cfe Logging.jar ServerSide.Logger.LoggingExec ./
mv *.jar ../Jars/)

echo "	> Sending the proper jar to the correct workstation"
(cd Jars;
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Logging.jar sd0405@l040101-ws01.ua.pt:~
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Shop.jar sd0405@l040101-ws05.ua.pt:~
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Workshop.jar sd0405@l040101-ws04.ua.pt:~
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Warehouse.jar sd0405@l040101-ws03.ua.pt:~
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Entrepreneur.jar sd0405@l040101-ws11.ua.pt:~
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Craftsman.jar sd0405@l040101-ws08.ua.pt:~
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no Customer.jar sd0405@l040101-ws09.ua.pt:~
)

echo "  > Cleaning logs from the server where Logging is going to run"
sshpass -p sistema2015 ssh sd0405@l040101-ws01.ua.pt -o StrictHostKeyChecking 'rm *.log'

echo "	> Executing each jar file on the proper workstation"
sshpass -p sistema2015 ssh sd0405@l040101-ws01.ua.pt -o StrictHostKeyChecking=no 'java -jar Logging.jar' &
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
if [[ ! -e FinalLogs ]]; then
    mkdir FinalLogs;
fi
sshpass -p sistema2015 scp -o StrictHostKeyChecking=no sd0405@l040101-ws01.ua.pt:~/Artesanato* FinalLogs/