package com.mwu.whatsappclone.service.conversiationService;

import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;

import java.util.List;

public record ConversationIdWithUsers(ConversationPublicId conversationPublicId,
                                      List<UserPublicId> users) {
}
