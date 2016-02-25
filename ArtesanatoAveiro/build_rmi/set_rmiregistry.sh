rmiregistry -J-Djava.rmi.server.codebase="http://$REGISTER_HOST/$GROUP/classes/"\
	    -J-Djava.rmi.server.useCodebaseOnly=true $1
