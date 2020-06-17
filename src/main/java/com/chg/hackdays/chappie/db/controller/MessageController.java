package com.chg.hackdays.chappie.db.controller;

import com.chg.hackdays.chappie.db.entity.DocumentEntity;
import com.chg.hackdays.chappie.db.entity.MessageAttributeEntity;
import com.chg.hackdays.chappie.db.entity.MessageEntity;
import com.chg.hackdays.chappie.db.repository.MessageRepository;
import com.chg.hackdays.chappie.model.ListResponse;
import com.chg.hackdays.chappie.model.Message;
import com.chg.hackdays.chappie.model.MessageId;
import com.chg.hackdays.chappie.util.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MessageController extends BaseController {
    @Autowired
    MessageRepository messageService;

    @GetMapping("/message")
    public ResponseEntity<ListResponse> getMessages(
            @RequestParam("topic") String topic,
            @RequestParam(name = "start", required = false) Long start,
            @RequestParam(name = "count", required = false) Integer count) {
        return respond(new ListResponse(), resp -> {
            if (count != null && count > 0) {
                Pageable pageable = PageRequest.of(0, count);
                resp.getItems().addAll(mapMessages(messageService.findByTopicIdAndMinOffset(topic, start == null ? 0L : start, pageable)));
            } else {
                resp.getItems().addAll(mapMessages(messageService.findByTopicIdAndMinOffset(topic, start == null ? 0L : start)));
            }
        });
    }

    @GetMapping("/message/{ids}")
    public ResponseEntity<ListResponse> getMessagesById(@PathVariable("ids") String idsStr) {
        return respond(new ListResponse(), resp -> {
            List<String> ids = Arrays.asList(idsStr.split("[,;\\s]+")).stream().collect(Collectors.toList());
            if (ids.size() > 1)
                throw new UnsupportedOperationException("TODO: Support multiple IDs");
            resp.getItems().add(mapMessage(messageService.findOneByMessageId(new MessageId(ids.iterator().next()))));
        });
    }

    @GetMapping("/message/{id}/content")
    public void getMessageContent(HttpServletResponse response, @PathVariable("id") String idStr) {
        MessageEntity message = messageService.findOneByMessageId(new MessageId(idStr));
        DocumentEntity document = message.getDocument();
        try (OutputStream os = response.getOutputStream()) {
            if (document == null) {
                response.setContentType("text/plain");
                os.write(message.getText().getBytes());
            } else {
                response.setContentType(document.getMime());
                os.write(document.getData());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    List<Message> mapMessages(Iterable<MessageEntity> messageEntities) {
        List<Message> result = new LinkedList<>();
        for (MessageEntity messageEntity : messageEntities) {
            result.add(mapMessage(messageEntity));
        }
        return result;
    }

    Message mapMessage(MessageEntity entity) {
        Message message = new Message();
        message.setId(new MessageId(entity.getTopicName(), entity.getTopicOffset()));
        message.setSource(entity.getSourceName());
        message.setTopic(entity.getTopicName());
        MessageEntity replyTo = entity.getReplyTo();
        if (replyTo != null) {
            message.setReplyTo(new MessageId(replyTo.getTopicName(), replyTo.getTopicOffset()).toString());
        }
        message.setConversation(entity.getConversationId());
        message.setTarget(entity.getTargetName());
        if (entity.getDocument() == null) {
            message.setType("text");
            message.setMime("text/plain");
            message.setText(entity.getText());
        } else {
            message.setType(EncodeUtil.DEFAULT_ENCODING);
            message.setMime(entity.getDocument().getMime());
            message.setText(EncodeUtil.encode(entity.getDocument().getData()));
        }
        message.setTimestamp(ZonedDateTime.ofInstant(entity.getSentTime(), ZoneId.systemDefault()));
        for (Map.Entry<String, MessageAttributeEntity> e : entity.getAttributes().entrySet()) {
            message.getAttributes().put(e.getKey(), e.getValue().getValue());
        }
        return message;
    }
}
