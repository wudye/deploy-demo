package com.mwu.whatsappclone.model.aggregate.messageAggregate;

import com.mwu.whatsappclone.model.enums.MessageType;

public record MessageContent(String text,
                             MessageType type,
                             MessageMediaContent media) {
}
