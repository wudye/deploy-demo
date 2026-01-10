package com.mwu.whatsappclone.model.response.conversationResponse;


import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationName;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationToCreate;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationToCreateBuilder;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record RestConversationToCreate(Set<UUID> members, String name) {

    public static ConversationToCreate toDomain(RestConversationToCreate restConversationToCreate) {
        RestConversationToCreateBuilder restConversationToCreateBuilder = RestConversationToCreate.builder();

        Set<UserPublicId> userUUIDs = restConversationToCreate.members
                .stream()
                .map(UserPublicId::new)
                .collect(Collectors.toSet());

        return ConversationToCreateBuilder.conversationToCreate()
                .name(new ConversationName(restConversationToCreate.name()))
                .members(userUUIDs)
                .build();
    }
}
