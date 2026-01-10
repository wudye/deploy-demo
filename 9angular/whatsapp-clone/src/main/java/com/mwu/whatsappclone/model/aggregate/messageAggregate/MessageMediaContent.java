package com.mwu.whatsappclone.model.aggregate.messageAggregate;

public record MessageMediaContent(byte[] file,
                                  String mimetype) {
}
