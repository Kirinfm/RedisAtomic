package com.kirin.fm.blog.controller;

import com.kirin.fm.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class IndexController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/{id}")
    public ResponseEntity getBlog(@PathVariable String id) {
        return new ResponseEntity<>(blogService.getById(id), HttpStatus.OK);
    }
}
