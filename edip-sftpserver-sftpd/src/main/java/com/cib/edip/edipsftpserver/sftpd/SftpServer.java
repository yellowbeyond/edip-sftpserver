package com.cib.edip.edipsftpserver.sftpd;

import com.alibaba.fastjson.JSONObject;
import com.cib.edip.edipsftpserver.clops.SftpServerOptionsInterface;
import com.cib.edip.edipsftpserver.clops.SftpServerParseResult;
import com.cib.edip.edipsftpserver.clops.SftpServerParser;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.compression.BuiltinCompressions;
import org.apache.sshd.common.compression.Compression;
import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.mac.BuiltinMacs;
import org.apache.sshd.common.mac.Mac;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.common.util.ValidateUtils;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.channel.ChannelSessionFactory;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.keyprovider.AbstractGeneratorHostKeyProvider;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.shell.ShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpEventListener;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystem;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sshd.server.SshServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SftpServer implements PasswordAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(SftpServer.class);

    private SshServer sshd;

    private volatile boolean running = true;

    public static final String HOSTKEY_FILE_PEM = "keys/edip-sftpserver-host-rsa-key";
    public static final String HOSTKEY_FILE_SER = "keys/hostkey.ser";

    private int port;

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

    private String rootDir;

    private Map<String,Object> returnM =new HashMap<String,Object>();

    public static void main(String[] args) {

        //SftpServerParseResult sr = SftpServerParser.parse(args,"SftpServer");
        SftpServerParser parser = new SftpServerParser();
        //ParseResult parseResult = parser.parseInternal(args, "SftpServer");
        SftpServerParseResult sr = new SftpServerParseResult(parser.parseInternal(args, "SftpServer"), parser.getOptionStore());
        if (!sr.successfulParse()) {
            parser.printUsage(System.err);

        }
        SftpServerOptionsInterface opt =
                sr.getOptionStore();
        if (opt.getHelp())
            parser.printUsage(System.err);
        if (opt.getPort() > 1024) {
            SftpServer.getServer().setPort(opt.getPort());

        }
        if (opt.getServerName()!=null) {

            SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_NANME,opt.getServerName());

        }
        if (opt.getSecurityKey()!=null) {

            SftpServer.getServer().addReturnInfo(ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY,opt.getSecurityKey());

        }
        if (opt.getRootDir()!=null & opt.getRootDir().isDirectory()){
            //System.out.println(opt.getRootDir().toString());
            SftpServer.getServer().setRootDir(opt.getRootDir().toString());
        }else{
            LOG.error("RooDir option isn't set or not a directory ");
            //System.err.println("RooDir option isn't set or not a directory ");
        }


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




            System.out.println("[SftpServer Return Info]:"+ JSONObject.toJSONString(returnM));
            sshd.start();



        } catch (Exception e) {
            LOG.error("Exception " + e.toString(), e);
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


    static class ReturnInfoConstant{
        public static String SFTPD_SERVER_NANME="server-name";
        public static String SFTPD_SERVER_SECURITY_KEY="security-key";

    }
}
