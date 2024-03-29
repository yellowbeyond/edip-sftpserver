package com.cib.edip.edipsftpserver.clops;

import java.io.File;

/**
 * The interface used to handle the options on the user side.
 * @author The CLOPS team
 */
public interface SftpServerOptionsInterface {


// Option Port. 
// Aliases: [-p, --Port]

  /**
   * @return true if the option Port has been used
   * in the command line.
   */
  boolean isPortSet();

  /**
   * Get the value of {@code Option} Port.
   * @return the value of the option Port if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  int getPort();
  

// Option RootDir. 
// Aliases: [-d, --RootDir]

  /**
   * @return true if the option RootDir has been used
   * in the command line.
   */
  boolean isRootDirSet();

  /**
   * Get the value of {@code Option} RootDir.
   * @return the value of the option RootDir if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  File getRootDir();
  

// Option Help. 
// Aliases: [-h, --help]

  /**
   * @return true if the option Help has been used
   * in the command line.
   */
  boolean isHelpSet();

  /**
   * Get the value of {@code Option} Help.
   * @return the value of the option Help if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  boolean getHelp();
  

// Option ServerName. 
// Aliases: [-n, --ServerName]

  /**
   * @return true if the option ServerName has been used
   * in the command line.
   */
  boolean isServerNameSet();

  /**
   * Get the value of {@code Option} ServerName.
   * @return the value of the option ServerName if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  String getServerName();
  

// Option SecurityKey. 
// Aliases: [-k, --SecurityKey]

  /**
   * @return true if the option SecurityKey has been used
   * in the command line.
   */
  boolean isSecurityKeySet();

  /**
   * Get the value of {@code Option} SecurityKey.
   * @return the value of the option SecurityKey if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  String getSecurityKey();
  

// Option RegisterServerURL. 
// Aliases: [-url, --RegisterServerURL]

  /**
   * @return true if the option RegisterServerURL has been used
   * in the command line.
   */
  boolean isRegisterServerURLSet();

  /**
   * Get the value of {@code Option} RegisterServerURL.
   * @return the value of the option RegisterServerURL if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  String getRegisterServerURL();
  

// Option ConfigFile. 
// Aliases: [-f, --ConfigFile]

  /**
   * @return true if the option ConfigFile has been used
   * in the command line.
   */
  boolean isConfigFileSet();

  /**
   * Get the value of {@code Option} ConfigFile.
   * @return the value of the option ConfigFile if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  String getConfigFile();
  

// Option RegisterServer. 
// Aliases: [-r, --RegisterServer]

  /**
   * @return true if the option RegisterServer has been used
   * in the command line.
   */
  boolean isRegisterServerSet();

  /**
   * Get the value of {@code Option} RegisterServer.
   * @return the value of the option RegisterServer if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  boolean getRegisterServer();
  

// Option HostKey. 
// Aliases: [--HostKey]

  /**
   * @return true if the option HostKey has been used
   * in the command line.
   */
  boolean isHostKeySet();

  /**
   * Get the value of {@code Option} HostKey.
   * @return the value of the option HostKey if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  String getHostKey();
  

// Option Ver. 
// Aliases: [--version]

  /**
   * @return true if the option Ver has been used
   * in the command line.
   */
  boolean isVerSet();

  /**
   * Get the value of {@code Option} Ver.
   * @return the value of the option Ver if it has been set
   * using the arguments. Throws an {@code IllegalStateException} otherwise.
   */ 
  boolean getVer();
  
}
