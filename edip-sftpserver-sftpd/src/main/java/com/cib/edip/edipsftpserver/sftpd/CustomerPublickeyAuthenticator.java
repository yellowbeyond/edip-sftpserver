package com.cib.edip.edipsftpserver.sftpd;


import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;


public class CustomerPublickeyAuthenticator implements PublickeyAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(SftpServer.class);

   // public static final String LOCAL_PK_FILE_NAME=""

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    private  String rootDir=null;

    public CustomerPublickeyAuthenticator(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public boolean authenticate(final String username, final PublicKey key, final ServerSession session) {
        LOG.info("Request auth (PublicKey) for username=" + username);
        // File f = nevagw File("/home/" + username + "/.ssh/authorized_keys");
        if ((username != null) && (key != null)) {
            String encodedKey = PublicKeyHelper.getEncodedPublicKey(key);
            if(encodedKey==null)return false;

            if(checkPublicKey(new StringBuilder(this.getRootDir()).append(File.separator).append(username).append(File.separator).append(username).append(".pub").toString(),encodedKey))return true;



            // return db.checkUserPublicKey(username, key);
        }
        return false;
    }

    private boolean checkPublicKey(final String localPublicKeyFile, final String clientPublickeyStr){
        Path p= Paths.get(localPublicKeyFile);

        if(p.toFile().exists()&p.toFile().canRead()){
           try{
               byte[] buf=Files.readAllBytes(p);
               if(buf != null&buf.length>0){
                   String localStr=new String(buf,PublicKeyHelper.US_ASCII);
                   if(localStr.contains(clientPublickeyStr)){
                       return true;
                   }
               }
           }catch(IOException e ) {
               e.printStackTrace();
               LOG.error("read PublicKey file fail");
           }


        }

        return false;
    }

}
