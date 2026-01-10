package com.mwu.whatsappclone.model.aggregate.conversationAggregate;

import com.mwu.whatsappclone.security.Assert;

public record ConversationName(String name) {

    public ConversationName {
        Assert.field("name", name).minLength(3).maxLength(255);
    }
}
