package com.mwu.whatsappclone.model.aggregate.userAggregate;


import com.mwu.whatsappclone.security.Assert;

public record UserImageUrl(String value) {

    public UserImageUrl {
        Assert.field(value, value).maxLength(255);
    }
}
