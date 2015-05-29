find . -name "*.class" -exec rm -rf {} \;
find .. -name "*.java" > sources.txt
javac @sources.txt -d .
mkdir -p dir_serverSide/logging/
mkdir -p dir_serverSide/shop/
mkdir -p dir_serverSide/workshop/
mkdir -p dir_serverSide/warehouse/

cp --parents -r ServerSide/Logger/ dir_serverSide/logging/
cp --parents -r Interfaces/ dir_serverSide/logging/
cp --parents -r Static/ dir_serverSide/logging/

cp --parents -r ServerSide/Shop/ dir_serverSide/shop/
cp --parents -r Interfaces/ dir_serverSide/shop/
cp --parents -r Static/ dir_serverSide/shop/

cp --parents -r ServerSide/Warehouse/ dir_serverSide/warehouse/
cp --parents -r Interfaces/ dir_serverSide/warehouse/
cp --parents -r Static/ dir_serverSide/warehouse/

cp --parents -r ServerSide/Workshop/ dir_serverSide/workshop/
cp --parents -r Interfaces/ dir_serverSide/workshop/
cp --parents -r Static/ dir_serverSide/workshop/

cp --parents -r ClientSide/ dir_clientSide/
cp --parents -r Interfaces/ dir_clientSide/
cp --parents -r Static/ dir_clientSide/