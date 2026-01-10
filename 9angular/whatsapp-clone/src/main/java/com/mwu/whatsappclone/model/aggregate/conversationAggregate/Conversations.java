package com.mwu.whatsappclone.model.aggregate.conversationAggregate;


import com.mwu.whatsappclone.security.Assert;

import java.util.List;

public record Conversations(List<Conversation> conversations) {

    public Conversations {
        Assert.field("conversations", conversations).notNull().noNullElement();
    }
}
