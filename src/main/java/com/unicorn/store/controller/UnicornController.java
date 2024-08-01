package com.unicorn.store.controller;

import com.unicorn.store.exceptions.ResourceNotFoundException;
import com.unicorn.store.model.Unicorn;
import com.unicorn.store.service.UnicornService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class UnicornController {
    private final UnicornService unicornService;
    private static final Logger logger = LoggerFactory.getLogger(UnicornController.class);

    public UnicornController(UnicornService unicornService) {
        this.unicornService = unicornService;
    }

//    @GetMapping("/")
//    public String getWelcomeMessage() {
//        return "board";
//    }

}
