package com.mwu.demo2.dCommandPattern;

public abstract class Command {

    protected RequirementGroup requirementGroup = new RequirementGroup();
    protected CodeGroup codeGroup = new CodeGroup();
    protected PageGroup pageGroup = new PageGroup();


    public abstract void execute();
}
