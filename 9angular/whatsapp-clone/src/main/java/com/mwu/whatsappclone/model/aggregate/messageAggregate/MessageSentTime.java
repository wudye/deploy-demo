package com.mwu.whatsappclone.model.aggregate.messageAggregate;


import com.mwu.whatsappclone.security.Assert;

import java.time.Instant;

public record MessageSentTime(Instant date) {
    public MessageSentTime {
        Assert.field("date", date).notNull();
    }
}
