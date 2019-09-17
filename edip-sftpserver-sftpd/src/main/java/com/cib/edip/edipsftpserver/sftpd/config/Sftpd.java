package com.cib.edip.edipsftpserver.sftpd.config;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;



public class Sftpd{

    private Integer port;
    private String rootDir;
    private String serverName;
    private String registerServerUrl;
    private boolean registerServer;
    private String hostKey;




    @Getter
    public String getHostKey() {
        return hostKey;
    }
    @Setter
    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    @Getter
    public Integer getPort() {
        return port;
    }

    @Setter
    public void setPort(Integer port) {
        this.port = port;
    }


    @Getter
    public String getRootDir() {
        return rootDir;
    }

    @Setter
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }


    @Getter
    public String getServerName() {
        return serverName;
    }

    @Setter
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    @Getter
    public String getRegisterServerUrl() {
        return registerServerUrl;
    }

    @Setter
    public void setRegisterServerUrl(String registerServerUrl) {
        this.registerServerUrl = registerServerUrl;
    }

    public boolean isRegisterServer() {
        return registerServer;
    }
    @Setter
    public void setRegisterServer(boolean registerServer) {
        this.registerServer = registerServer;
    }
}
