package com.cib.edip.edipsftpserver.clops;

import ie.ucd.clops.runtime.options.OptionStore;
import ie.ucd.clops.runtime.rules.Action;
import ie.ucd.clops.runtime.rules.Expression;
import ie.ucd.clops.runtime.rules.FlyRule;
import ie.ucd.clops.runtime.rules.OverrideRule;
import ie.ucd.clops.runtime.rules.RuleStore;
import ie.ucd.clops.runtime.rules.ValidityRule;


import java.util.Arrays;
import java.util.List;

/**
 * The rule store is used to handle the constraints and the validity
 * checks associated with the options.
 * @author The CLOPS team (kind@ucd.ie)
 */
public class SftpServerRuleStore extends RuleStore {
  
  public SftpServerRuleStore() {
    Expression<Boolean> rule1Condition = new Rule1Condition();
    ValidityRule rule1 = new ValidityRule(rule1Condition);
    rule1.addAction(new Action<List<String>>("CLOPSERROROPTION", new Rule1Expression()));
    addValidityRule(rule1);
    Expression<Boolean> rule2Condition = new Rule2Condition();
    ValidityRule rule2 = new ValidityRule(rule2Condition);
    rule2.addAction(new Action<List<String>>("CLOPSERROROPTION", new Rule2Expression()));
    addValidityRule(rule2);
  }

  public static class Rule1Condition implements Expression<Boolean> {
    /**
     * {@inheritDoc}
     */
    public Boolean evaluate(final OptionStore optionStore) {
      return ((ie.ucd.clops.runtime.options.BooleanOption)optionStore.getOptionByIdentifier("RegisterServer")).getRawValue() & !((ie.ucd.clops.runtime.options.StringOption)optionStore.getOptionByIdentifier("RegisterServerURL")).hasValue();
    }
  }
    
  public static class Rule1Expression implements Expression<List<String>> {
    /**
     * {@inheritDoc}
     */
    public List<String> evaluate(final OptionStore optionStore) {
      return Arrays.asList("-r and -url must display at the same time");
    }
  }
  
  public static class Rule2Condition implements Expression<Boolean> {
    /**
     * {@inheritDoc}
     */
    public Boolean evaluate(final OptionStore optionStore) {
      return ((ie.ucd.clops.runtime.options.StringOption)optionStore.getOptionByIdentifier("RegisterServerURL")).hasValue() & !((ie.ucd.clops.runtime.options.BooleanOption)optionStore.getOptionByIdentifier("RegisterServer")).getRawValue();
    }
  }
    
  public static class Rule2Expression implements Expression<List<String>> {
    /**
     * {@inheritDoc}
     */
    public List<String> evaluate(final OptionStore optionStore) {
      return Arrays.asList("-url option active  only when -r has set to true");
    }
  }
  

  protected final boolean shouldApplyFlyRulesTransitively() {
    return false;
  }
}
