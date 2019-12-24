package com.kirin.fm.blog.service.impl;

import com.kirin.fm.blog.entity.BlogEntity;
import com.kirin.fm.blog.repository.BlogRepository;
import com.kirin.fm.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public BlogEntity getById(String id) {
        return blogRepository.findById(id).orElse(null);
    }
}
