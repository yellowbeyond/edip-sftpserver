package com.cib.edip.edipsftpserver.fork;

import java.util.Map;

public class ProcessInfo {

    public static final int SFTPD_PROCESS_STATUS_STARTING=1;
    public static final int SFTPD_PROCESS_STATUS_STARTED=2;
    public static final int SFTPD_PROCESS_STATUS_KILLED=3;

    public ProcessInfo(Process p) {
        this.p = p;
    }

    private Process p;

    private int serverStatus;

    public Process getP() {
        return p;
    }

    public void setP(Process p) {
        this.p = p;
    }

    public Map<String, Object> getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(Map<String, Object> returnInfo) {
        this.returnInfo = returnInfo;
    }

    private Map<String,Object> returnInfo;



}
