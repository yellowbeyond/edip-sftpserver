package com.cib.edip.edipsftpserver.clops;

import java.io.PrintStream;
import java.util.List;

import ie.ucd.clops.runtime.errors.ParseResult;
import ie.ucd.clops.runtime.options.Option;
import ie.ucd.clops.runtime.options.exception.InvalidOptionPropertyValueException;
import ie.ucd.clops.runtime.parser.AbstractSpecificCLParser;
import ie.ucd.clops.runtime.rules.RuleStore;
import ie.ucd.clops.util.OptionUtil;

/**
 * The arguments parser.
 * This is the main entry point for the option's handling.
 * @author The CLOPS team
 */
public class SftpServerParser extends AbstractSpecificCLParser {

  /** The option store used to hold the option's status. */
  private final SftpServerOptionStore optionStore;
  /** The store which contains the constraints associated with the options. */
  private final ie.ucd.clops.runtime.rules.RuleStore ruleStore;

  /**
   * Creates a parser to handle the options.
   * @throws InvalidOptionPropertyValueException if one of the options had
   * an invalid property attached to it in the CLOPS description file.
   */
  public SftpServerParser() throws InvalidOptionPropertyValueException {
    optionStore = new SftpServerOptionStore();
    ruleStore = new SftpServerRuleStore();
  }

  /**
   * Get the {@link ie.ucd.clops.runtime.options.OptionStore} containing the option instances for this parser.
   * @return the option store.
   */
  public SftpServerOptionStore getOptionStore() {
    return optionStore;  
  }
  
  /**
   * Get the {@link RuleStore} containing the rules for this parser.
   * @return the option store.
   */
  public RuleStore getRuleStore() {
    return ruleStore;
  }
  
  /**
   * Get the format string for this parser.
   * @return the format string.
   */
  public String getFormatString() {
    return "(option)*"; 
  }

  public void printUsage(PrintStream os) {
    printUsage(os, 80, 0);
  }

  public void printUsage(PrintStream out, int width, int indent) {
    List<Option<?>> all = optionStore.getOptionsWithoutErrorOption();
    OptionUtil.printOptions(out, all, width, indent);
    out.flush();
  }
  
  /**
   * Parse the given command line arguments using a new SftpServerParser,
   * with normal lookahead. 
   */
  public static SftpServerParseResult parse(String[] args, String progName) {
    SftpServerParser parser = new SftpServerParser();
    ParseResult parseResult = parser.parseInternal(args, progName);
    return new SftpServerParseResult(parseResult, parser.getOptionStore());
  }
  
  /**
   * Parse the given command line arguments using a new SftpServerParser,
   * with infinite lookahead.
   */
  public static SftpServerParseResult parseAlternate(String[] args, String progName) {
    SftpServerParser parser = new SftpServerParser();
    ParseResult parseResult = parser.parseAlternateInternal(args, progName);
    return new SftpServerParseResult(parseResult, parser.getOptionStore());
  }
}
