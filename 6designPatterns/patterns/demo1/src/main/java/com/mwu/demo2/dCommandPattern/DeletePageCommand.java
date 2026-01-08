package com.mwu.demo2.dCommandPattern;

public class DeletePageCommand extends Command{

    @Override
    public void execute() {
        super.requirementGroup.add();
        super.pageGroup.delete();
        super.codeGroup.plan();
    }
}
