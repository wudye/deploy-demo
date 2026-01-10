package com.mwu.whatsappclone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.MessageSendNew;
import com.mwu.whatsappclone.model.enums.StatusNotification;
import com.mwu.whatsappclone.model.response.messageResponse.RestMessage;
import com.mwu.whatsappclone.service.MessageApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageApplicationService messageApplicationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public MessageController(MessageApplicationService messageApplicationService) {
        this.messageApplicationService = messageApplicationService;
    }

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestMessage> send(@RequestPart(value = "file", required = false)
                                                MultipartFile file,
                                            @RequestPart("dto") String messageRaw) throws IOException {
        RestMessage restMessage = objectMapper.readValue(messageRaw, RestMessage.class);
        if(restMessage.hasMedia()) {
            restMessage.setMediaAttachment(file.getBytes(), file.getContentType());
        }

        MessageSendNew messageSendNew = RestMessage.toDomain(restMessage);

        State<Message, String> sendState = messageApplicationService.send(messageSendNew);
        if(sendState.getStatus().equals(StatusNotification.OK)) {
            return ResponseEntity.ok(RestMessage.from(sendState.getValue()));
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, sendState.getError());
            return ResponseEntity.of(problemDetail).build();
        }
    }
}
