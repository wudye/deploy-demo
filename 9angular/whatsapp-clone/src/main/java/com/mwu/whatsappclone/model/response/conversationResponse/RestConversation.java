package com.mwu.whatsappclone.model.response.conversationResponse;



import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.response.messageResponse.RestMessage;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RestConversation(UUID publicId, String name,
                               List<RestUserForConversation> members,
                               List<RestMessage> messages) {

    public static RestConversation from(Conversation conversation) {
        RestConversationBuilder restConversationBuilder = RestConversation.builder();
        restConversationBuilder
                .name(conversation.getConversationName().name())
                .publicId(conversation.getConversationPublicId().value())
                .members(RestUserForConversation.from(conversation.getMembers()));

        if (conversation.getMessages() != null) {
            restConversationBuilder.messages(RestMessage.from(conversation.getMessages()));
        }

        return restConversationBuilder.build();
    }

}
