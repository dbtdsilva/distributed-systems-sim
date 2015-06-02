find . -name "*.class" -exec rm -rf {} \;
find . -type d -empty -delete
find .. -name "*.java" > sources.txt
javac @sources.txt -d .
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
cp --parents -r Static/ dir_registry/

cp --parents -r ServerSide/Logger/ dir_serverSide/logging/
cp --parents -r Interfaces/ dir_serverSide/logging/
cp --parents -r Static/ dir_serverSide/logging/
cp --parents -r VectorClock/ dir_serverSide/logging/

cp --parents -r ServerSide/Shop/ dir_serverSide/shop/
cp --parents -r Interfaces/ dir_serverSide/shop/
cp --parents -r Static/ dir_serverSide/shop/

cp --parents -r ServerSide/Warehouse/ dir_serverSide/warehouse/
cp --parents -r Interfaces/ dir_serverSide/warehouse/
cp --parents -r Static/ dir_serverSide/warehouse/

cp --parents -r ServerSide/Workshop/ dir_serverSide/workshop/
cp --parents -r Interfaces/ dir_serverSide/workshop/
cp --parents -r Static/ dir_serverSide/workshop/

cp --parents -r ClientSide/Entrepreneur/ dir_clientSide/entrepreneur/
cp --parents -r Interfaces/ dir_clientSide/entrepreneur/
cp --parents -r Static/ dir_clientSide/entrepreneur/

cp --parents -r ClientSide/Customer/ dir_clientSide/customer/
cp --parents -r Interfaces/ dir_clientSide/customer/
cp --parents -r Static/ dir_clientSide/customer/

cp --parents -r ClientSide/Craftsman/ dir_clientSide/craftsman/
cp --parents -r Interfaces/ dir_clientSide/craftsman/
cp --parents -r Static/ dir_clientSide/craftsman/