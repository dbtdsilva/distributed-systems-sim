java -Djava.rmi.server.codebase="file://$(pwd)/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=../java.policy\
     ServerSide.Shop.ShopServer