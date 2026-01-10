package com.mwu.whatsappclone.model.aggregate.authorityAggregate;


import com.mwu.whatsappclone.security.Assert;

public class Authority {

    private AuthorityName name;

    public Authority(AuthorityName name) {
        Assert.notNull("name", name);
        this.name = name;
    }

    public AuthorityName getName() {
        return name;
    }
}
