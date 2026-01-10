package com.mwu.whatsappclone.model.aggregate.userAggregate;


import com.mwu.whatsappclone.security.Assert;

public record UserEmail(String value) {

    public UserEmail {
        Assert.field(value, value).maxLength(255);
    }
}
