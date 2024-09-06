package com.sap.adt.abapcleaner.rules.ddl.position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sap.adt.abapcleaner.rulebase.RuleID;
import com.sap.adt.abapcleaner.rulebase.RuleTestBase;

public class DdlPositionSelectTest extends RuleTestBase {
	private DdlPositionSelectRule rule;
	
	DdlPositionSelectTest() {
		super(RuleID.DDL_POSITION_SELECT);
		rule = (DdlPositionSelectRule)getRule();
	}

	@BeforeEach
	void setUp() {
		// setup default test configuration (may be modified in the individual test methods)
	   rule.configBreakBeforeEntityName.setEnumValue(DdlLineBreak.NEVER);
	   rule.configEntityNameIndent.setValue(2);
		
	   rule.configBreakBeforeWithParams.setEnumValue(DdlLineBreak.ALWAYS);
	   rule.configWithParamsIndent.setValue(2);
	   rule.configParamsIndent.setValue(4);
		
	   rule.configBreakBeforeAsSelectFrom.setEnumValue(DdlLineBreak.ALWAYS);
	   rule.configAsSelectFromIndent.setValue(2);
		
	   rule.configBreakBeforeSelectFrom.setEnumValue(DdlLineBreak.ALWAYS);
	   rule.configSelectFromIndent.setValue(2);
		
	   rule.configBreakBeforeAsProjectionOn.setEnumValue(DdlLineBreak.ALWAYS);
	   rule.configAsProjectionOnIndent.setValue(2);
		
	   rule.configBreakBeforeDataSource.setEnumValue(DdlLineBreak.NEVER);
	   rule.configDataSourceIndent.setValue(4);
	}

	@Test
	void testNeverBreakBeforeEntityName() {
		buildSrc("define");
		buildSrc("view");
		buildSrc("entity C_AnyEntity");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testAlwaysBreakBeforeEntityName() {
		rule.configBreakBeforeEntityName.setEnumValue(DdlLineBreak.ALWAYS);

		buildSrc("define");
		buildSrc("view");
		buildSrc("entity C_AnyEntity");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity");
		buildExp("  C_AnyEntity");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testKeepEntityName() {
		rule.configBreakBeforeEntityName.setEnumValue(DdlLineBreak.KEEP_AS_IS);

		buildSrc("define");
		buildSrc("view");
		buildSrc("entity C_AnyEntity");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");

		copyExpFromSrc();

		testRule();
	}

	@Test
	void testAlwaysBreakBeforeWithParameters() {
		buildSrc("define view entity C_AnyEntity with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("     @Anno.subAnno: 'value'");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("          /* multi-");
		buildSrc("           * line");
		buildSrc("           * comment */");
		buildSrc("    P_ThirdParam : ThirdType as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  with parameters");
		buildExp("    // comment");
		buildExp("    P_AnyParam   : AnyType,");
		buildExp("    @Anno.subAnno: 'value'");
		buildExp("    P_OtherParam : OtherType,");
		buildExp("    /* multi-");
		buildExp("     * line");
		buildExp("     * comment */");
		buildExp("    P_ThirdParam : ThirdType");
		buildExp("");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testKeepWithParameters() {
		rule.configBreakBeforeWithParams.setEnumValue(DdlLineBreak.KEEP_AS_IS);

		buildSrc("define view entity C_AnyEntity with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("     @Anno.subAnno: 'value'");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("    /* comment in correct position */");
		buildSrc("    P_ThirdParam : ThirdType as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity with");
		buildExp("parameters");
		buildExp("// comment");
		buildExp("P_AnyParam   : AnyType,");
		buildExp("     @Anno.subAnno: 'value'");
		buildExp("  P_OtherParam : OtherType,");
		buildExp("    /* comment in correct position */");
		buildExp("    P_ThirdParam : ThirdType");
		buildExp("");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testNeverBreakBeforeWithParameters() {
		rule.configBreakBeforeWithParams.setEnumValue(DdlLineBreak.NEVER);
		rule.configParamsIndent.setValue(10);

		buildSrc("define view entity C_AnyEntity with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("     @Anno.subAnno: 'value'");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("    P_ThirdParam : ThirdType as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity with parameters");
		buildExp("          // comment");
		buildExp("          P_AnyParam   : AnyType,");
		buildExp("          @Anno.subAnno: 'value'");
		buildExp("          P_OtherParam : OtherType,");
		buildExp("          P_ThirdParam : ThirdType");
		buildExp("");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testAlwaysBreakBeforeSelectFromAfterUnion() {
		buildSrc("define view entity C_AnyEntity as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("union");
		buildSrc("  all");
		buildSrc("    select");
		buildSrc("      from");
		buildSrc("        I_ThirdEntity As ThirdAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key ThirdAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("except select");
		buildSrc("from I_FourthEntity As FourthAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key FourthAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("union all");
		buildExp("  select from I_ThirdEntity As ThirdAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key ThirdAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("except");
		buildExp("  select from I_FourthEntity As FourthAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key FourthAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testNeverBreakBeforeSelectFromAfterUnion() {
		rule.configBreakBeforeSelectFrom.setEnumValue(DdlLineBreak.NEVER);

		buildSrc("define view entity C_AnyEntity as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("union");
		buildSrc("  all");
		buildSrc("    select");
		buildSrc("      from");
		buildSrc("        I_ThirdEntity As ThirdAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key ThirdAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("except select");
		buildSrc("from I_FourthEntity As FourthAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key FourthAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("union all select from I_ThirdEntity As ThirdAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key ThirdAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("except select from I_FourthEntity As FourthAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key FourthAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testKeepSelectFromAfterUnion() {
		rule.configBreakBeforeSelectFrom.setEnumValue(DdlLineBreak.KEEP_AS_IS);

		buildSrc("define view entity C_AnyEntity as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("union");
		buildSrc("  all");
		buildSrc("    select");
		buildSrc("      from");
		buildSrc("        I_ThirdEntity As ThirdAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key ThirdAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("except select");
		buildSrc("from I_FourthEntity As FourthAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key FourthAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("union");
		buildExp("  all");
		buildExp("    select");
		buildExp("      from");
		buildExp("        I_ThirdEntity As ThirdAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key ThirdAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("except select");
		buildExp("from I_FourthEntity As FourthAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key FourthAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testAlwaysBreakBeforeDataSource() {
		rule.configBreakBeforeDataSource.setEnumValue(DdlLineBreak.ALWAYS);

		buildSrc("define view entity C_AnyEntity as select from");
		buildSrc("I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("union");
		buildSrc("  all");
		buildSrc("    select");
		buildSrc("      from");
		buildSrc("        I_ThirdEntity As ThirdAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key ThirdAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("except select");
		buildSrc("from I_FourthEntity As FourthAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key FourthAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  as select from");
		buildExp("    I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("union all");
		buildExp("  select from");
		buildExp("    I_ThirdEntity As ThirdAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key ThirdAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("except");
		buildExp("  select from");
		buildExp("    I_FourthEntity As FourthAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key FourthAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testNeverBreakBeforeDataSource() {
		buildSrc("define view entity C_AnyEntity as select from");
		buildSrc("I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("union");
		buildSrc("  all");
		buildSrc("    select");
		buildSrc("      from");
		buildSrc("        I_ThirdEntity As ThirdAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key ThirdAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("except select");
		buildSrc("from I_FourthEntity As FourthAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key FourthAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("union all");
		buildExp("  select from I_ThirdEntity As ThirdAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key ThirdAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("except");
		buildExp("  select from I_FourthEntity As FourthAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key FourthAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testKeepDataSource() {
		rule.configBreakBeforeDataSource.setEnumValue(DdlLineBreak.KEEP_AS_IS);

		buildSrc("define view entity C_AnyEntity as select from");
		buildSrc("I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("union");
		buildSrc("  all");
		buildSrc("    select");
		buildSrc("      from");
		buildSrc("        I_ThirdEntity As ThirdAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key ThirdAlias.AnyKeyField");
		buildSrc("}");
		buildSrc("");
		buildSrc("except select");
		buildSrc("from I_FourthEntity As FourthAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key FourthAlias.AnyKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  as select from");
		buildExp("I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("union all");
		buildExp("  select from");
		buildExp("        I_ThirdEntity As ThirdAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key ThirdAlias.AnyKeyField");
		buildExp("}");
		buildExp("");
		buildExp("except");
		buildExp("  select from I_FourthEntity As FourthAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key FourthAlias.AnyKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testAlwaysBreakBeforeAsProjectionOn() {
		buildSrc("@AccessControl.authorizationCheck: #NOT_ALLOWED");
		buildSrc("define transient view entity AnyView");
		buildSrc("  provider contract analytical_query with parameters p_Any : abap.cuky as projection on OtherView");
		buildSrc("{");
		buildSrc("    @AnalyticsDetails.query.axis: #FREE");
		buildSrc("    AnyField");
		buildSrc("}");

		buildExp("@AccessControl.authorizationCheck: #NOT_ALLOWED");
		buildExp("define transient view entity AnyView");
		buildExp("  provider contract analytical_query");
		buildExp("  with parameters");
		buildExp("    p_Any : abap.cuky");
		buildExp("");
		buildExp("  as projection on OtherView");
		buildExp("{");
		buildExp("    @AnalyticsDetails.query.axis: #FREE");
		buildExp("    AnyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testNeverBreakBeforeAsProjectionOn() {
		rule.configBreakBeforeAsProjectionOn.setEnumValue(DdlLineBreak.NEVER);

		buildSrc("@AccessControl.authorizationCheck: #NOT_ALLOWED");
		buildSrc("define transient view entity AnyView");
		buildSrc("  provider contract analytical_query");
		buildSrc("  as projection on OtherView");
		buildSrc("{");
		buildSrc("    @AnalyticsDetails.query.axis: #FREE");
		buildSrc("    AnyField");
		buildSrc("}");

		buildExp("@AccessControl.authorizationCheck: #NOT_ALLOWED");
		buildExp("define transient view entity AnyView");
		buildExp("  provider contract analytical_query as projection on OtherView");
		buildExp("{");
		buildExp("    @AnalyticsDetails.query.axis: #FREE");
		buildExp("    AnyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testKeepAsProjectionOn() {
		rule.configBreakBeforeAsProjectionOn.setEnumValue(DdlLineBreak.KEEP_AS_IS);

		buildSrc("@AccessControl.authorizationCheck: #NOT_ALLOWED");
		buildSrc("define transient view entity AnyView");
		buildSrc("  provider contract analytical_query");
		buildSrc("      as projection on OtherView");
		buildSrc("{");
		buildSrc("    @AnalyticsDetails.query.axis: #FREE");
		buildSrc("    AnyField");
		buildSrc("}");

		copyExpFromSrc();

		testRule();
	}

	@Test
	void testMoveAttachedComments() {
		// ensure that the attached comments are moved along with "WITH PARAMETERS" or "AS SELECT FROM"
		// and that the extra line above "AS SELECT FROM" is inserted before the first attached comment
		buildSrc("define view entity C_AnyEntity");
		buildSrc("// single-line comment");
		buildSrc("/* multi-line");
		buildSrc("   comment */");
		buildSrc("with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("    P_ThirdParam : ThirdType");
		buildSrc("      /* multi-");
		buildSrc("         line");
		buildSrc("         comment */");
		buildSrc("      // single-line comment");
		buildSrc("      as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField,");
		buildSrc("      AnyAlias.AnyNonKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("  // single-line comment");
		buildExp("  /* multi-line");
		buildExp("     comment */");
		buildExp("  with parameters");
		buildExp("    // comment");
		buildExp("    P_AnyParam   : AnyType,");
		buildExp("    P_OtherParam : OtherType,");
		buildExp("    P_ThirdParam : ThirdType");
		buildExp("");
		buildExp("  /* multi-");
		buildExp("     line");
		buildExp("     comment */");
		buildExp("  // single-line comment");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField,");
		buildExp("      AnyAlias.AnyNonKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testDoNotMoveDetachedComments() {
		// ensure that the detached comments are not moved along with "WITH PARAMETERS" or "AS SELECT FROM"
		buildSrc("define view entity C_AnyEntity");
		buildSrc("// detached comment");
		buildSrc("");
		buildSrc("with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("    P_ThirdParam : ThirdType");
		buildSrc("      /* detached multi-line");
		buildSrc("         comment */");
		buildSrc("");
		buildSrc("      // single-line comment");
		buildSrc("      as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField,");
		buildSrc("      AnyAlias.AnyNonKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("// detached comment");
		buildExp("");
		buildExp("  with parameters");
		buildExp("    // comment");
		buildExp("    P_AnyParam   : AnyType,");
		buildExp("    P_OtherParam : OtherType,");
		buildExp("    P_ThirdParam : ThirdType");
		buildExp("      /* detached multi-line");
		buildExp("         comment */");
		buildExp("");
		buildExp("  // single-line comment");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField,");
		buildExp("      AnyAlias.AnyNonKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testDoNotMoveCommentedOutAnnotations() {
		// ensure that the commented-out annotations are not moved along with "WITH PARAMETERS" or "AS SELECT FROM"
		buildSrc("define view entity C_AnyEntity");
		buildSrc("// @Anno.subAnno");
		buildSrc("      // single-line comment");
		buildSrc("with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("    P_ThirdParam : ThirdType");
		buildSrc("// @Anno.subAnno");
		buildSrc("      // single-line comment");
		buildSrc("      as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField,");
		buildSrc("      AnyAlias.AnyNonKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("// @Anno.subAnno");
		buildExp("  // single-line comment");
		buildExp("  with parameters");
		buildExp("    // comment");
		buildExp("    P_AnyParam   : AnyType,");
		buildExp("    P_OtherParam : OtherType,");
		buildExp("    P_ThirdParam : ThirdType");
		buildExp("// @Anno.subAnno");
		buildExp("");
		buildExp("  // single-line comment");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField,");
		buildExp("      AnyAlias.AnyNonKeyField");
		buildExp("}");

		testRule();
	}

	@Test
	void testDoNotMoveComments() {
		// ensure that neither the commented-out annotation nor the comment above it is being moved
		buildSrc("define view entity C_AnyEntity");
		buildSrc("      // single-line comment");
		buildSrc("// @Anno.subAnno");
		buildSrc("with");
		buildSrc("parameters");
		buildSrc("// comment");
		buildSrc("P_AnyParam   : AnyType,");
		buildSrc("  P_OtherParam : OtherType,");
		buildSrc("    P_ThirdParam : ThirdType");
		buildSrc("      // single-line comment");
		buildSrc("// @Anno.subAnno");
		buildSrc("      as select from I_AnyEntity as AnyAlias");
		buildSrc("");
		buildSrc("{");
		buildSrc("  key AnyAlias.AnyKeyField,");
		buildSrc("      AnyAlias.AnyNonKeyField");
		buildSrc("}");

		buildExp("define view entity C_AnyEntity");
		buildExp("      // single-line comment");
		buildExp("// @Anno.subAnno");
		buildExp("  with parameters");
		buildExp("    // comment");
		buildExp("    P_AnyParam   : AnyType,");
		buildExp("    P_OtherParam : OtherType,");
		buildExp("    P_ThirdParam : ThirdType");
		buildExp("      // single-line comment");
		buildExp("// @Anno.subAnno");
		buildExp("");
		buildExp("  as select from I_AnyEntity as AnyAlias");
		buildExp("");
		buildExp("{");
		buildExp("  key AnyAlias.AnyKeyField,");
		buildExp("      AnyAlias.AnyNonKeyField");
		buildExp("}");

		testRule();
	}
}