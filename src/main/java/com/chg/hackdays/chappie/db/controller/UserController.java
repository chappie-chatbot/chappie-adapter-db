package com.chg.hackdays.chappie.db.controller;

import com.chg.hackdays.chappie.db.entity.UserEntity;
import com.chg.hackdays.chappie.db.repository.UserRepository;
import com.chg.hackdays.chappie.model.ListResponse;
import com.chg.hackdays.chappie.model.User;
import com.chg.hackdays.chappie.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
public class UserController extends BaseController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<ListResponse> getUsers(
            @RequestParam("name") String name) {
        return respond(new ListResponse(), resp -> {
            if (!StringUtil.isBlank(name)) {
                UserEntity user = getOrCreateUser(name);
                resp.getItems().add(mapUser(user));
            }
        });
    }

    private User mapUser(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setCreated(ZonedDateTime.ofInstant(entity.getCreatedDate(), ZoneId.systemDefault()));
        return user;
    }

    private UserEntity getOrCreateUser(String userName) {
        if (StringUtil.isBlank(userName))
            return null;
        UserEntity user = userRepository.findOneByName(userName);
        if (user == null) {
            user = new UserEntity(userName);
            userRepository.save(user);
        }
        return user;
    }
}
