java -Djava.rmi.server.codebase="http://$REGISTER_HOST/$GROUP/classes/"\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=../java.policy\
     ServerSide.Shop.ShopServer
