package com.cib.edip.edipsftpserver.clops;

import ie.ucd.clops.runtime.errors.ParseResult;

/**
 * A parse result for the SftpServerParser.
 * @author The CLOPS team
 */
public class SftpServerParseResult extends ParseResult {

  private SftpServerOptionStore optionStore;

  public SftpServerParseResult(ParseResult parseResult, SftpServerOptionStore optionStore) {
    super(parseResult);
    this.optionStore = optionStore;
  }

  public SftpServerOptionStore getOptionStore() {
    return optionStore;
  }

} 