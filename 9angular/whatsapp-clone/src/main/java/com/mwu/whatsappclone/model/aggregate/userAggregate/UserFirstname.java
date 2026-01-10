package com.mwu.whatsappclone.model.aggregate.userAggregate;

import com.mwu.whatsappclone.security.Assert;

public record UserFirstname(String value) {

    public UserFirstname {
        Assert.field(value, value).maxLength(255);
    }
}
