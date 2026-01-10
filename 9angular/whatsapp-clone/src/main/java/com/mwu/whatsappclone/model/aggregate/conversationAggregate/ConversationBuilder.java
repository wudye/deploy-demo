package com.mwu.whatsappclone.model.aggregate.conversationAggregate;


import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;

import java.util.Set;

public final class ConversationBuilder {

    private Set<Message> messages;
    private Set<User> members;
    private ConversationPublicId conversationPublicId;
    private ConversationName conversationName;
    private Long dbId;

    private ConversationBuilder() {
    }

    public static ConversationBuilder conversation() {
        return new ConversationBuilder();
    }

    public ConversationBuilder messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public ConversationBuilder members(Set<User> members) {
        this.members = members;
        return this;
    }

    public ConversationBuilder conversationPublicId(ConversationPublicId conversationPublicId) {
        this.conversationPublicId = conversationPublicId;
        return this;
    }

    public ConversationBuilder conversationName(ConversationName conversationName) {
        this.conversationName = conversationName;
        return this;
    }

    public ConversationBuilder dbId(Long dbId) {
        this.dbId = dbId;
        return this;
    }

    public Conversation build() {
        return new Conversation(
                messages,
                members,
                conversationPublicId,
                conversationName,
                dbId
        );
    }
}
