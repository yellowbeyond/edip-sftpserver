package com.cib.edip.edipsftpserver.clops;

import ie.ucd.clops.runtime.options.CLOPSErrorOption;
import ie.ucd.clops.runtime.options.OptionGroup;
import ie.ucd.clops.runtime.options.OptionStore;
import ie.ucd.clops.runtime.options.exception.InvalidOptionPropertyValueException;
import java.io.File;
import ie.ucd.clops.runtime.options.FileOption;
import ie.ucd.clops.runtime.options.IntegerOption;
import ie.ucd.clops.runtime.options.StringOption;
import ie.ucd.clops.runtime.options.BooleanOption;

public class SftpServerOptionStore extends OptionStore implements SftpServerOptionsInterface {

  private final IntegerOption ogPort;
  private final FileOption ogRootDir;
  private final BooleanOption ogHelp;
  private final StringOption ogServerName;
  private final StringOption ogSecurityKey;
  private final StringOption ogRegisterServerPath;
  private final BooleanOption ogVer;
  private final CLOPSErrorOption CLOPSERROROPTION;

  public SftpServerOptionStore() throws InvalidOptionPropertyValueException {

    //Options
    ogPort = new IntegerOption("Port", "(?:-p)|(?:--Port)");
    addOption(ogPort);
    ogPort.setProperty("minvalue", "1025");
    ogPort.setProperty("maxvalue", "65535");
    ogPort.setProperty("default", "2222");
    ogPort.setProperty("aliases", "-p,--Port");
    ogPort.setProperty("description", "set server port, default port is 2222.");
    ogRootDir = new FileOption("RootDir", "(?:-d)|(?:--RootDir)");
    addOption(ogRootDir);
    ogRootDir.setProperty("default", "./");
    ogRootDir.setProperty("aliases", "-d,--RootDir");
    ogRootDir.setProperty("description", "the root directory set default root directory is ./");
    ogHelp = new BooleanOption("Help", "(?:-h)|(?:--help)");
    addOption(ogHelp);
    ogHelp.setProperty("allowArg", "true");
    ogHelp.setProperty("default", "false");
    ogHelp.setProperty("aliases", "-h,--help");
    ogHelp.setProperty("description", "Displays this message.");
    ogServerName = new StringOption("ServerName", "(?:-n)|(?:--ServerName)");
    addOption(ogServerName);
    ogServerName.setProperty("default", "SftpSever-default");
    ogServerName.setProperty("aliases", "-n,--ServerName");
    ogServerName.setProperty("description", "The instance server name.");
    ogSecurityKey = new StringOption("SecurityKey", "(?:-k)|(?:--SecurityKey)");
    addOption(ogSecurityKey);
    ogSecurityKey.setProperty("aliases", "-k,--SecurityKey");
    ogSecurityKey.setProperty("description", "The security key for check");
    ogRegisterServerPath = new StringOption("RegisterServerPath", "(?:-r)|(?:--RegisterServer)");
    addOption(ogRegisterServerPath);
    ogRegisterServerPath.setProperty("aliases", "-r,--RegisterServer");
    ogRegisterServerPath.setProperty("description", "Register Server with rest request by the path");
    ogVer = new BooleanOption("Ver", "(?:--version)");
    addOption(ogVer);
    ogVer.setProperty("aliases", "--version");
  
    CLOPSERROROPTION = new ie.ucd.clops.runtime.options.CLOPSErrorOption();
    addOption(CLOPSERROROPTION);
  
    //Option groups
    final OptionGroup ogoption = new OptionGroup("option");
    addOptionGroup(ogoption);
    final OptionGroup ogAllOptions = new OptionGroup("AllOptions");
    addOptionGroup(ogAllOptions);
    
    //Setup groupings
    ogoption.addOptionOrGroup(ogSecurityKey);
    ogoption.addOptionOrGroup(ogRootDir);
    ogoption.addOptionOrGroup(ogVer);
    ogoption.addOptionOrGroup(ogHelp);
    ogoption.addOptionOrGroup(ogPort);
    ogoption.addOptionOrGroup(ogServerName);
    ogoption.addOptionOrGroup(ogRegisterServerPath);
    //AllOptions group
    ogAllOptions.addOptionOrGroup(ogPort);
    ogAllOptions.addOptionOrGroup(ogRootDir);
    ogAllOptions.addOptionOrGroup(ogHelp);
    ogAllOptions.addOptionOrGroup(ogServerName);
    ogAllOptions.addOptionOrGroup(ogSecurityKey);
    ogAllOptions.addOptionOrGroup(ogRegisterServerPath);
    ogAllOptions.addOptionOrGroup(ogVer);
  }
  
// Option Port.
// Aliases: [-p, --Port]
  
  /**
   * {@inheritDoc}
   */
  public boolean isPortSet() {
    return ogPort.hasValue();
  }
  
  /** {@inheritDoc} */
  public int getPort() {
    return ogPort.getValue();
  }

  /** Gets the value of option Port without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public int getRawPort() {
    return ogPort.getRawValue();
  }
  
  public IntegerOption getPortOption() {
    return ogPort;
  }
  
// Option RootDir.
// Aliases: [-d, --RootDir]
  
  /**
   * {@inheritDoc}
   */
  public boolean isRootDirSet() {
    return ogRootDir.hasValue();
  }
  
  /** {@inheritDoc} */
  public File getRootDir() {
    return ogRootDir.getValue();
  }

  /** Gets the value of option RootDir without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public File getRawRootDir() {
    return ogRootDir.getRawValue();
  }
  
  public FileOption getRootDirOption() {
    return ogRootDir;
  }
  
// Option Help.
// Aliases: [-h, --help]
  
  /**
   * {@inheritDoc}
   */
  public boolean isHelpSet() {
    return ogHelp.hasValue();
  }
  
  /** {@inheritDoc} */
  public boolean getHelp() {
    return ogHelp.getValue();
  }

  /** Gets the value of option Help without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public boolean getRawHelp() {
    return ogHelp.getRawValue();
  }
  
  public BooleanOption getHelpOption() {
    return ogHelp;
  }
  
// Option ServerName.
// Aliases: [-n, --ServerName]
  
  /**
   * {@inheritDoc}
   */
  public boolean isServerNameSet() {
    return ogServerName.hasValue();
  }
  
  /** {@inheritDoc} */
  public String getServerName() {
    return ogServerName.getValue();
  }

  /** Gets the value of option ServerName without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public String getRawServerName() {
    return ogServerName.getRawValue();
  }
  
  public StringOption getServerNameOption() {
    return ogServerName;
  }
  
// Option SecurityKey.
// Aliases: [-k, --SecurityKey]
  
  /**
   * {@inheritDoc}
   */
  public boolean isSecurityKeySet() {
    return ogSecurityKey.hasValue();
  }
  
  /** {@inheritDoc} */
  public String getSecurityKey() {
    return ogSecurityKey.getValue();
  }

  /** Gets the value of option SecurityKey without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public String getRawSecurityKey() {
    return ogSecurityKey.getRawValue();
  }
  
  public StringOption getSecurityKeyOption() {
    return ogSecurityKey;
  }
  
// Option RegisterServerPath.
// Aliases: [-r, --RegisterServer]
  
  /**
   * {@inheritDoc}
   */
  public boolean isRegisterServerPathSet() {
    return ogRegisterServerPath.hasValue();
  }
  
  /** {@inheritDoc} */
  public String getRegisterServerPath() {
    return ogRegisterServerPath.getValue();
  }

  /** Gets the value of option RegisterServerPath without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public String getRawRegisterServerPath() {
    return ogRegisterServerPath.getRawValue();
  }
  
  public StringOption getRegisterServerPathOption() {
    return ogRegisterServerPath;
  }
  
// Option Ver.
// Aliases: [--version]
  
  /**
   * {@inheritDoc}
   */
  public boolean isVerSet() {
    return ogVer.hasValue();
  }
  
  /** {@inheritDoc} */
  public boolean getVer() {
    return ogVer.getValue();
  }

  /** Gets the value of option Ver without checking if it is set.
   *  This method will not throw an exception, but may return null. 
   */
  public boolean getRawVer() {
    return ogVer.getRawValue();
  }
  
  public BooleanOption getVerOption() {
    return ogVer;
  }
  
}
