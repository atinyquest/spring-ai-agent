package com.tinyquest.agent.api.user.contorller;

import com.tinyquest.agent.api.user.model.request.UserCreateRequest;
import com.tinyquest.agent.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public String createUser(
            @RequestBody UserCreateRequest req
    ) {
        userService.createUser(req);
        return "OK";
    }
}
