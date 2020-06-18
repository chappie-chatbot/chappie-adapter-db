package com.chg.hackdays.chappie.db.controller;

import com.chg.hackdays.chappie.db.entity.ConversationEntity;
import com.chg.hackdays.chappie.db.entity.UserEntity;
import com.chg.hackdays.chappie.db.repository.ConversationRepository;
import com.chg.hackdays.chappie.db.repository.UserRepository;
import com.chg.hackdays.chappie.model.Conversation;
import com.chg.hackdays.chappie.model.ListResponse;
import com.chg.hackdays.chappie.server.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ConversationController extends BaseController {
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/conversation")
    public ResponseEntity<ListResponse> getConversations(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(name = "participant", required = false) String participant) {
        return respond(new ListResponse(), resp -> {
            resp.getItems().addAll(mapConversations(findConversations(id, participant)));
        });
    }

    private List<ConversationEntity> findConversations(Long id, String participant) {
        List<ConversationEntity> result = new LinkedList<>();
        if (id != null) {
            ConversationEntity conversation = conversationRepository.findById(id).orElse(null);
            if (conversation != null && participant != null) {
                if (conversation.getParticipants().stream().noneMatch(user -> user.getName().equals(participant)))
                    conversation = null;
            }
            if (conversation != null) {
                result.add(conversation);
            }
        } else {
            if (participant != null) {
                UserEntity participantUser = userRepository.findOneByName(participant);
                if (participantUser != null) {
                    result.addAll(conversationRepository.findByParticipants(participantUser));
                }
            }
        }
        return result;
    }

    private List<Conversation> mapConversations(Collection<ConversationEntity> conversations) {
        return conversations.stream().map(this::mapConversation).collect(Collectors.toList());
    }

    // TODO: Use ModelMapper
    private Conversation mapConversation(ConversationEntity entity) {
        Conversation conversation = new Conversation();
        conversation.setId(entity.getId());
        conversation.setStartTime(ZonedDateTime.ofInstant(entity.getCreatedDate(), ZoneId.systemDefault()));
        conversation.setParticipants(entity.getParticipants().stream().map(UserEntity::getName).collect(Collectors.toList()));
        return conversation;
    }
}
