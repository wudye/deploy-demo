package com.mwu.whatsappclone.entity.builder;

import com.mwu.whatsappclone.entity.MessageContentBinaryEntity;
import com.mwu.whatsappclone.entity.MessageEntity;

public final class MessageContentBinaryEntityBuilder {

    private Long id;
    private byte[] file;
    private String fileContentType;
    private MessageEntity message;

    private MessageContentBinaryEntityBuilder() {
    }

    public static MessageContentBinaryEntityBuilder messageContentBinaryEntity() {
        return new MessageContentBinaryEntityBuilder();
    }

    public MessageContentBinaryEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MessageContentBinaryEntityBuilder file(byte[] file) {
        this.file = file;
        return this;
    }

    public MessageContentBinaryEntityBuilder fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public MessageContentBinaryEntityBuilder message(MessageEntity message) {
        this.message = message;
        return this;
    }

    public MessageContentBinaryEntity build() {
        return new MessageContentBinaryEntity(id, file, fileContentType, message);
    }
}
