package com.mwu.whatsappclone.model.aggregate.userAggregate;

import com.mwu.whatsappclone.security.Assert;

public record UserLastName(String value) {
    public UserLastName {
        Assert.field(value, value).maxLength(255);
    }
}
