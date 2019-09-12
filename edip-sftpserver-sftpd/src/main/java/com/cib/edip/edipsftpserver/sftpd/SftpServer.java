package com.cib.edip.edipsftpserver.sftpd;

import com.alibaba.fastjson.JSONObject;
import com.cib.edip.edipsftpserver.clops.SftpServerOptionsInterface;
import com.cib.edip.edipsftpserver.clops.SftpServerParseResult;
import com.cib.edip.edipsftpserver.clops.SftpServerParser;
import com.cib.edip.edipsftpserver.configs.ConfigParseYAML;
import com.cib.edip.edipsftpserver.rest.RestResponseHandler;
import com.cib.edip.edipsftpserver.sftpd.config.Sftpd;
import com.cib.edip.edipsftpserver.utils.Helpers;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.compression.BuiltinCompressions;
import org.apache.sshd.common.compression.Compression;
import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.mac.BuiltinMacs;
import org.apache.sshd.common.mac.Mac;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.channel.ChannelSessionFactory;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.AbstractGeneratorHostKeyProvider;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
import org.apache.sshd.server.shell.ShellFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sshd.server.SshServer;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.cib.edip.client.RestClient;


public class SftpServer implements PasswordAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(SftpServer.class);

    private SshServer sshd;

    private volatile boolean running = true;

    public static final String HOSTKEY_FILE_PEM = "keys/edip-sftpserver-host-rsa-key";
    public static final String HOSTKEY_FILE_SER = "keys/hostkey.ser";

    private int port;
    private Sftpd sftpdConfig;
    private String rootDir;
    private String RegisterServerPath;
    private Map<String,Object> returnM =new HashMap<String,Object>();
    private int RegisterStatus;
    private String ServerToken;
    private String ServerName;


    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public Map<String, Object> getReturnM() {
        return returnM;
    }

    public void setReturnM(Map<String, Object> returnM) {
        this.returnM = returnM;
    }

    public int getRegisterStatus() {
        return RegisterStatus;
    }

    public void setRegisterStatus(int registerStatus) {
        RegisterStatus = registerStatus;
    }

    public String getServerToken() {
        return ServerToken;
    }

    public void setServerToken(String serverToken) {
        ServerToken = serverToken;
    }

    public String getRegisterServerPath() {
        return RegisterServerPath;
    }

    public void setRegisterServerPath(String registerServerPath) {
        RegisterServerPath = registerServerPath;
    }

    public Sftpd getSftpdConfig() {
        return sftpdConfig;
    }

    public void setSftpdConfig(Sftpd sftpdConfig) {
        this.sftpdConfig = sftpdConfig;
    }

    public static void main(String[] args) {

        //SftpServerParseResult sr = SftpServerParser.parse(args,"SftpServer");


        SftpServerParser parser = new SftpServerParser();
        //ParseResult parseResult = parser.parseInternal(args, "SftpServer");
        SftpServerParseResult sr = new SftpServerParseResult(parser.parseInternal(args, "SftpServer"), parser.getOptionStore());
        if (!sr.successfulParse()) {

            sr.getErrors().forEach(e->System.out.println(e.getMessage()));
            //System.out.println();
            parser.printUsage(System.err);

        }
        SftpServerOptionsInterface opt =
                sr.getOptionStore();
        if (opt.getHelp())
            parser.printUsage(System.err);

        if (opt.isConfigFileSet() && opt.getConfigFile()!=null){

            try{

            SftpServer.getServer().setSftpdConfig(ConfigParseYAML.<Sftpd>loadConfig(opt.getConfigFile(), Sftpd.class).getOrElse(null));
                SftpServer.getServer().initConfig();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
        if (opt.isPortSet() && opt.getPort() > 1024) {
            SftpServer.getServer().setPort(opt.getPort());
            SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_PORT,SftpServer.getServer().getPort());

        }
        if (opt.isServerNameSet()&&opt.getServerName()!=null) {
            SftpServer.getServer().setServerName(opt.getServerName());
            SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_NANME,SftpServer.getServer().getServerName());

        }
        if (opt.isSecurityKeySet() && opt.getSecurityKey()!=null) {

            SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY,opt.getSecurityKey());

        }

        int pid=getProcessID();
        if(pid>0){
            SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_PID,String.valueOf(pid));
        }

        if (opt.isRootDirSet() && opt.getRootDir()!=null ){
            //System.out.println(opt.getRootDir().toString());
            if(opt.getRootDir().isDirectory()) {
                SftpServer.getServer().setRootDir(opt.getRootDir().toString());
                SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_ROOT_DIR, SftpServer.getServer().getRootDir());
            }else{
                LOG.error("RooDir option isn't set or not a directory ");
                //System.err.println("RooDir option isn't set or not a directory ");
            }
        }

        if(opt.isRegisterServerURLSet() && opt.getRegisterServerURL()!=null && opt.isRegisterServerSet() && opt.getRegisterServer() ){
            LOG.debug("RegisterServerPath:"+opt.getRegisterServerURL());

            SftpServer.getServer().setRegisterServerPath(opt.getRegisterServerURL());

        }

        SftpServer.getServer().setRegisterStatus(ReturnInfoConstant.SFTP_REGISTER_STATUS_UNREGISTERED);

        SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTP_REGISTER_STATUS,ReturnInfoConstant.SFTP_REGISTER_STATUS_UNREGISTERED);


        SftpServer.getServer().start();



    }

    private static SftpServer serverInstance;

    private static SftpServer getServer(){

        if(serverInstance!=null){
            return serverInstance;
        }else{
            serverInstance=new SftpServer();
            return serverInstance;
        }

    }

    private void initConfig(){
        Sftpd sftpdConfig=this.getSftpdConfig();
        this.setPort(sftpdConfig.getPort());
        this.setRootDir(sftpdConfig.getRootDir());

        if(sftpdConfig.isRegisterServer()) {
            this.setRegisterServerPath(sftpdConfig.getRegisterServerUrl());
        }
        this.setServerName(sftpdConfig.getServerName());

    }

    public void start() {
        LOG.info("Starting");
        //db = loadConfig();
       // LOG.info("BouncyCastle enabled=" + SecurityUtils.isBouncyCastleRegistered());
        sshd = SshServer.setUpDefaultServer();
        LOG.info("SSHD " + sshd.getVersion());
        setupFactories();
        setupAuth();
       /* hackVersion();
        setupMaxPacketLength();
        setupFactories();
        setupKeyPair();
        setupScp();
        setupAuth();
        setupSysprops();*/
        setupKeyPair();

        try {
            //final int port = 2222;
            final boolean enableCompress = true;
            final boolean enableDummyShell = true;
            setupCompress(enableCompress);
            setupDummyShell(enableDummyShell);
            //loadHtPasswd();
            sshd.setPort(this.getPort());

            LOG.info("Listen on port=" + port);

            //final Server thisServer = this;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    SftpServer.getServer().stop();
                }
            });

            sshd.start();

            System.out.println("[SftpServer Return Info]:"+ JSONObject.toJSONString(returnM));

            Thread.sleep(5);

            registerServer();

        } catch (Exception e) {
            LOG.error("Exception " + e.toString(), e);
            e.printStackTrace();
        }
        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        LOG.info("Stopping");
        running = false;
        try {
            sshd.stop();
        } catch (IOException e) {
            try {
                sshd.stop(true);
            } catch (IOException ee) {
                LOG.error("Failed to stop", ee);
            }
        }
    }

    protected void setupCompress(final boolean enable) {
        // Compression is not enabled by default
        // You need download and compile:
        // http://www.jcraft.com/jzlib/
        if (enable) {
            sshd.setCompressionFactories(Arrays.<NamedFactory<Compression>>asList( //
                    BuiltinCompressions.none, //
                    BuiltinCompressions.zlib, //
                    BuiltinCompressions.delayedZlib));
        } else {
            sshd.setCompressionFactories(Arrays.<NamedFactory<Compression>>asList( //
                    BuiltinCompressions.none));
        }
    }

    protected void setupDummyShell(final boolean enable) {
        sshd.setShellFactory(enable ? new SecureShellFactory() : InteractiveProcessShellFactory.INSTANCE);
    }


    static class SecureShellFactory implements ShellFactory {
        @Override
        public Command createShell(ChannelSession var1) {
            return new SecureShellCommand();
        }
    }

    static class SecureShellCommand implements Command {
        private OutputStream err = null;
        private ExitCallback callback = null;

        @Override
        public void setInputStream(final InputStream in) {
        }

        @Override
        public void setOutputStream(final OutputStream out) {
        }

        @Override
        public void setErrorStream(final OutputStream err) {
            this.err = err;
        }

        @Override
        public void setExitCallback(final ExitCallback callback) {
            this.callback = callback;
        }

        @Override
        public void start(final ChannelSession cs,final Environment env) throws IOException {
            if (err != null) {
                err.write("shell not allowed\r\n".getBytes("ISO-8859-1"));
                err.flush();
            }
            if (callback != null)
                callback.onExit(-1, "shell not allowed");
        }

        @Override
        public void destroy(final ChannelSession cs) {
        }
    }

    protected void setupKeyPair() {
        final AbstractGeneratorHostKeyProvider provider;
        if (SecurityUtils.isBouncyCastleRegistered()) {
            provider = SecurityUtils.createGeneratorHostKeyProvider(new File(HOSTKEY_FILE_PEM).toPath());
        } else {
            provider = new SimpleGeneratorHostKeyProvider(new File(HOSTKEY_FILE_SER).toPath());
        }
        provider.setAlgorithm(KeyUtils.RSA_ALGORITHM);
        sshd.setKeyPairProvider(provider);
    }

    protected void setupFactories() {
       // final CustomSftpSubsystemFactory sftpSubsys = new CustomSftpSubsystemFactory();
        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new CustomSftpSubsystemFactory()));
        sshd.setMacFactories(Arrays.<NamedFactory<Mac>>asList( //
                BuiltinMacs.hmacsha512, //
                BuiltinMacs.hmacsha256, //
                BuiltinMacs.hmacsha1));
        sshd.setChannelFactories(Arrays.<NamedFactory<Channel>>asList(ChannelSessionFactory.INSTANCE));
       // sshd.setShellFactory(InteractiveProcessShellFactory.INSTANCE);

        sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(this.getRootDir())) {
            @Override
            protected Path computeRootDir(Session session) throws IOException  {
                //this.setUserHomeDir(session.getUsername(),Paths.get("/tmp/sftptest/0112345"));

                return this.getUserHomeDir(session.getUsername());
            }

           @Override
            public Path getUserHomeDir(String userName) {

                Path userDir=Paths.get(this.getDefaultHomeDir().toString()+"/"+userName);
                if(userDir.toFile().exists()){
                    if(!userDir.toFile().isDirectory()){
                        LOG.error("file userdir point isn't a directory ");
                    }
                }else{
                    if(!userDir.toFile().mkdir()){
                        LOG.error("mkdir userdir failure");
                    }else{
                        LOG.debug("mkdir userdir sucessfully");
                    };
                }

                return userDir;
            }
        });



    }

    protected void setupAuth() {
        sshd.setPasswordAuthenticator(null);
        sshd.setPublickeyAuthenticator(new CustomerPublickeyAuthenticator(this.getRootDir()));
        sshd.setGSSAuthenticator(null);
    }




    @Override
    public boolean authenticate(final String username, final String password, final ServerSession session) {
        LOG.info("Request auth (Password) for username=" + username);
        if ((username != null) && (password != null)) {
           // return db.checkUserPassword(username, password);
        }
        return true;
    }

    public void addReturnInfo(String key,Object value){
        this.returnM.put(key,value);
    }

    public void registerServer(){


        if(this.getRegisterServerPath()!=null ){

           // String serverPaht="http://"+

                Map<String,Object> body=new HashMap(SftpServer.getServer().getReturnM());

              /*
               * body.put() //use for add new post arg
               */

            LOG.debug("-------------------------------------------------------------------1"
                    + SftpServer.getServer().getRegisterServerPath() + "=========" + body.toString());

                RestClient.doPost(SftpServer.getServer().getRegisterServerPath(),body, new RestResponseHandler<Map>(Map.class){

                    @Override
                    public <A> void handleByMap(Map<A, ?> obj) {

                        LOG.debug("SftpSever ReturnM1:"+obj);



                        if(Helpers.checkNotNullAndEmpty(obj)
                                & ((Integer)obj.get((A)ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS)).equals(Integer.valueOf(ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS_SUCCESS))
                                & obj.get((A)ReturnInfoConstant.SFTPD_SERVER_NANME).toString().equals(SftpServer.getServer().getReturnM().get(ReturnInfoConstant.SFTPD_SERVER_NANME).toString())
                                & obj.get((A)ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY).toString().equals(SftpServer.getServer().getReturnM().get(ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY).toString())){
                            if(obj.containsKey((A)ReturnInfoConstant.SFTPD_SERVER_TOKEN)){
                                //ReturnInfoConstant.SFTPD_SERVER_TOKEN=obj.get(ReturnInfoConstant.SFTPD_SERVER_TOKEN).toString();
                                SftpServer.getServer().getReturnM().put(ReturnInfoConstant.SFTPD_SERVER_TOKEN,obj.get((A)ReturnInfoConstant.SFTPD_SERVER_TOKEN).toString());

                                SftpServer.getServer().setRegisterStatus(((Integer) obj.get((A)ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS)).intValue());

                                LOG.debug("SftpSever ReturnM:"+SftpServer.getServer().getReturnM().toString());

                            }
                        }
                    }
                }, null);
        }

    }


    public static class ReturnInfoConstant{
        public static String SFTPD_SERVER_NANME="server-name";
        public static String SFTPD_SERVER_SECURITY_KEY="security-key";
        public static String SFTPD_SERVER_PID="pid";
        public static String SFTPD_SERVER_ROOT_DIR="root-dir";
        public static String SFTPD_SERVER_PORT="server-port";
        public static String SFTPD_SERVER_TOKEN="sftp-server-token";

        public static String SFTP_REGISTER_STATUS="register-status";
        public static int SFTP_REGISTER_STATUS_REGISTERED=0X0001;
        public static int SFTP_REGISTER_STATUS_UNREGISTERED=0X0002;

        public static String SERVER_REGISTER_RESPONSE_STATUS="response-status";

        public static int SERVER_REGISTER_RESPONSE_STATUS_SUCCESS=0x0001;
        public static int SERVER_REGISTER_RESPONSE_STATUS_FAILURE=0x0101;

    }

    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //System.out.println(runtimeMXBean.getName());
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
    }



}
