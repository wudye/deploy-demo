package com.mwu.whatsappclone.entity.builder;


import com.mwu.whatsappclone.entity.ConversationEntity;
import com.mwu.whatsappclone.entity.MessageEntity;
import com.mwu.whatsappclone.entity.UserEntity;

import java.util.Set;
import java.util.UUID;

public final class ConversationEntityBuilder {

    private Long id;
    private UUID publicId;
    private String name;
    private Set<MessageEntity> messages;
    private Set<UserEntity> users;

    private ConversationEntityBuilder() {
    }

    public static ConversationEntityBuilder conversationEntity() {
        return new ConversationEntityBuilder();
    }

    public ConversationEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ConversationEntityBuilder publicId(UUID publicId) {
        this.publicId = publicId;
        return this;
    }

    public ConversationEntityBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ConversationEntityBuilder messages(Set<MessageEntity> messages) {
        this.messages = messages;
        return this;
    }

    public ConversationEntityBuilder users(Set<UserEntity> users) {
        this.users = users;
        return this;
    }

    public ConversationEntity build() {
        return new ConversationEntity(id, publicId, name, messages, users);
    }
}
