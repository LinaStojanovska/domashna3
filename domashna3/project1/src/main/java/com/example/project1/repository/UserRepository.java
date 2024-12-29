package com.example.project1.repository;

import com.example.project1.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountHandler extends JpaRepository<UserModel, Long> {
    Optional<UserModel> fetchByUserAlias(String userAlias);
}
