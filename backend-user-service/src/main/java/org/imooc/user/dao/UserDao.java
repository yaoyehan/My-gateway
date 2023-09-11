package org.yyh.user.dao;

import org.yyh.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
