package com.mwu.whatsappclone.model.aggregate.authorityAggregate;

import com.mwu.whatsappclone.security.Assert;

public record AuthorityName(String name) {

    public AuthorityName {
        Assert.field("name", name).notNull();
    }
}
