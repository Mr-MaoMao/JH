package com.zjh.crosdemo.controller;

import com.zjh.crosdemo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {
    @GetMapping("/{name}/{age}/{address}")
    public User getUser(@PathVariable("age") Integer age, @PathVariable("name") String name
            , @PathVariable("address") String address) {
        return new User(name, age, address);
    }
}
