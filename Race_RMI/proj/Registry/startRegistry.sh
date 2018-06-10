function startRegistry {
        cd src/;
        netstat -ln | grep 22391 2>&1 > /dev/null;
        if [ $? -eq 1 ]
        then
                echo rmiregistry
                rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 22391
        else
                echo error_port_22391_being_used
        fi

        netstat -ln | grep 22390 2>&1 > /dev/null;
        if [ $? -eq 1 ]
        then
                echo registryServer_javac_javac
                javac registry/ServerRegisterRemoteObject.java;
                java registry.ServerRegisterRemoteObject
        else
                echo error_port_22390_being_used
        fi
}

startRegistry
