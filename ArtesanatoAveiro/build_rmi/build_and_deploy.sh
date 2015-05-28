find .. -name "*.java" > sources.txt
javac @sources.txt -d .
mkdir -p dir_serverSide/logging/
mkdir -p dir_serverSide/shop/
mkdir -p dir_serverSide/workshop/
mkdir -p dir_serverSide/warehouse/

cp --parents -r ServerSide/Logger/ dir_serverSide/logging/
cp --parents -r Interfaces/ dir_serverSide/logging/
cp --parents -r Static/ dir_serverSide/logging/