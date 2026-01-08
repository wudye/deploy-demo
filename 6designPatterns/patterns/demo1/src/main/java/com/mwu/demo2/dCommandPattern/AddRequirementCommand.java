package com.mwu.demo2.dCommandPattern;

public class AddRequirementCommand extends Command{
    @Override
    public void execute() {
        super.requirementGroup.add();
        super.codeGroup.add();
        super.pageGroup.add();

    }
}
