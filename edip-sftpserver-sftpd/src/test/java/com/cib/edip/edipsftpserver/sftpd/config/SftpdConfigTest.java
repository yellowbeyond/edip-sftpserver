package com.cib.edip.edipsftpserver.sftpd.config;

import com.cib.edip.edipsftpserver.configs.ConfigParseYAML;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class SftpdConfigTest {

    @Test
    public void testGetPort() {

            try{
                Sftpd config= ConfigParseYAML.<Sftpd>loadConfig("sftpd.yml", Sftpd.class).getOrElse(null);
                System.out.println(config);
            }catch (FileNotFoundException e){

            }





    }

    @Test
    public void testSetPort() {
    }

    @Test
    public void testGetRootDir() {
    }

    @Test
    public void testSetRootDir() {
    }

    @Test
    public void testGetServerName() {
    }

    @Test
    public void testSetServerName() {
    }

    @Test
    public void testGetRegisterServerPath() {
    }

    @Test
    public void testSetRegisterServerPath() {
    }
}