package com.cib.edip.edipsftpserver.sftpd.config;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import scala.Function0;



public class Sftpd{

    private Integer port;
    private String rootDir;
    private String serverName;
    private String registerServerUrl;
    private boolean registerServer;


    @Getter
    public Integer getPort() {
        return port;
    }

    @Setter
    public void setPort(Integer port) {
        this.port = port;
    }

    @Setter
    @Getter
    public String getRootDir() {
        return rootDir;
    }

    @Setter
    @Getter
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    @Setter
    @Getter
    public String getServerName() {
        return serverName;
    }

    @Setter
    @Getter
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

    public void setRegisterServer(boolean registerServer) {
        this.registerServer = registerServer;
    }
}
