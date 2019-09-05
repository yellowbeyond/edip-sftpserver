package com.cib.edip.edipsftpserver.fork;

import java.util.HashMap;
import java.util.UUID;

public class ForkSftpServerTest {

    @org.testng.annotations.Test
    public void testStartProcess() {

        ForkSftpServer fss=new ForkSftpServer();
        HashMap<String,String> args=new HashMap<String,String>();
        args.put("-p","2009");
        args.put("-d","/tmp");
        args.put("-n","server-1");
        args.put("-k", UUID.randomUUID().toString());

        HashMap<String,String> env=new HashMap<String,String>();

        try{
            ProcessInfo pi=fss.startProcess(Class.forName("com.cib.edip.edipsftpserver.sftpd.SftpServer"),args,env,"./target/*:./target/lib/*","server_test");
            Process p=pi.getP();

            if(p!=null && p.isAlive()){
                p.destroy();
            }



        }catch (ClassNotFoundException e){
            e.printStackTrace();

        }

    }

    @org.testng.annotations.Test
    public void testKillProcess(Process p) {

        if(p!=null && p.isAlive()){
            p.destroy();
        }
    }



}