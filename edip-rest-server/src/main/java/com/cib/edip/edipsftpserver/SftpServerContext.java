package com.cib.edip.edipsftpserver;

import com.cib.edip.edipsftpserver.fork.ProcessInfo;

import java.util.HashMap;
import java.util.Map;

public class SftpServerContext {

    private final static SftpServerContext sftpServerContext=new SftpServerContext();

    public static SftpServerContext getInstance(){
        return SftpServerContext.sftpServerContext;

    }

    private Map<String,Object> registerServers=new HashMap<String,Object>();

    public Map<String, Object> getRegisterServers() {
        return registerServers;
    }

    public void setRegisterServers(Map<String, Object> registerServers) {
        this.registerServers = registerServers;
    }

    public void registerServer(String uuid, ProcessInfo pi){
        if(!this.registerServers.containsKey(uuid)){
            this.registerServers.put(uuid,pi);
        };

    }
}
