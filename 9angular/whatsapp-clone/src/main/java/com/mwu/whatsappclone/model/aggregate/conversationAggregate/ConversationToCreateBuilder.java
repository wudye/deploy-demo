package com.mwu.whatsappclone.model.aggregate.conversationAggregate;


import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;

import java.util.Set;

public final class ConversationToCreateBuilder {

    private Set<UserPublicId> members;
    private ConversationName name;

    private ConversationToCreateBuilder() {
    }

    public static ConversationToCreateBuilder conversationToCreate() {
        return new ConversationToCreateBuilder();
    }

    public ConversationToCreateBuilder members(Set<UserPublicId> members) {
        this.members = members;
        return this;
    }

    public ConversationToCreateBuilder name(ConversationName name) {
        this.name = name;
        return this;
    }

    public ConversationToCreate build() {
        return new ConversationToCreate(members, name);
    }
}
