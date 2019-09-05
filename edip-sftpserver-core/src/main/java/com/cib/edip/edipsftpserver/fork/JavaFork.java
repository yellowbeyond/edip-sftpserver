package com.cib.edip.edipsftpserver.fork;


import java.util.List;
import java.util.Map;

public interface JavaFork {

    public ProcessInfo startProcess(Class mainClass, Map<String,String> args, Map<String,String> env,String cp,String serverName);

    public void killProcess(Process p);




}
