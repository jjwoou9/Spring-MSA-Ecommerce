package com.amp.userservice.controller;

import com.amp.userservice.dto.UserDto;
import com.amp.userservice.jpa.UserEntity;
import com.amp.userservice.service.UserService;
import com.amp.userservice.vo.Greeting;
import com.amp.userservice.vo.RequestUser;
import com.amp.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
public class UserController {

    Environment env;
    private Greeting greeting;
    private UserService userService;

    @Autowired
    public UserController(Environment env, Greeting greeting, UserService userService) {
        this.env = env;
        this.greeting = greeting;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String healthCheck() {
        // 할당받은 포트번호 출력
        return String.format("OK ... Port num = %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();// application.yml의 greeting.message의 값을 리턴
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        // RequestUser -> UserDto
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);

        // createUser 서비스를 호출
        userService.createUser(userDto);

        // UserDto -> ResponseUser
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        // 201 created 상태코드를 반환하도록 수정
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users") // 전체 회원 정보조회
    public ResponseEntity<List<ResponseUser>> getUsers() {
        // 회원 정보 조회
        Iterable<UserEntity> userList = userService.getUserByAll();

        // 조회 결과 리스트의 UserEntity -> ResponseUser
        List<ResponseUser> resultList = new ArrayList();
        // Iterable의 요소 하나씩 ResponseUser로 변환, List에 add
        userList.forEach(v->{
            resultList.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @GetMapping("/users/{userId}") // 해당 회원 정보 조회
    public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId) {
        // 회원 정보 조회
        UserDto userDto = userService.getUserByUserId(userId);

        // UserDto -> ResponseUser
        ResponseUser responseUser = new ModelMapper().map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

}
