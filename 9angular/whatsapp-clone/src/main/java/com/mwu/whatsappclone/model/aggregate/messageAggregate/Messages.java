package com.mwu.whatsappclone.model.aggregate.messageAggregate;


import com.mwu.whatsappclone.security.Assert;

import java.util.List;

public record Messages(List<Messages> messages) {
    public Messages {
        Assert.field("messages", messages).notNull().noNullElement();
    }
}
