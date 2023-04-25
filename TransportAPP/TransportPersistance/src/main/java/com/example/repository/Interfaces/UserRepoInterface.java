package com.example.repository.Interfaces;

import com.example.domain.User;
import com.example.repository.Repository;

public interface UserRepoInterface extends Repository<Long, User> {
    User findByUsername(String username);
}
