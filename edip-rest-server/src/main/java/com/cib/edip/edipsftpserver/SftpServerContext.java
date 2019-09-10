package com.cib.edip.edipsftpserver;

import com.cib.edip.edipsftpserver.fork.ProcessInfo;

import java.util.HashMap;
import java.util.Map;

public class SftpServerContext {

    private final static SftpServerContext sftpServerContext=new SftpServerContext();

    public static SftpServerContext getInstance(){
        return SftpServerContext.sftpServerContext;

    }

    private Map<String,ProcessInfo> registerServers=new HashMap<String,ProcessInfo>();

    public Map<String, ProcessInfo> getRegisterServers() {
        return registerServers;
    }

    public void setRegisterServers(Map<String, ProcessInfo> registerServers) {
        this.registerServers = registerServers;
    }

    public void registerServer(String uuid, ProcessInfo pi){
        if(!isServerRegisted(uuid)){
            this.registerServers.put(uuid,pi);
        };

    }

    public ProcessInfo getRegisterServer(String uuid){
        if(this.isServerRegisted(uuid)){
            return (ProcessInfo) this.registerServers.get(uuid);
        }

        return null;

    }

    public boolean isServerRegisted(String uuid){
        if(this.registerServers.containsKey(uuid)){
            return true;
        }

        return false;

    }



}
