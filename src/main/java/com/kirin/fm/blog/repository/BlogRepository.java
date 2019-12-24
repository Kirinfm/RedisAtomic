package com.kirin.fm.blog.repository;

import com.kirin.fm.blog.entity.BlogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<BlogEntity, String> {
}
