package com.esgi.framework_JEE.user.Domain.repository;

import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(int userId);

    User findByEmail(String email);

    //User findUserByEmailAndPassword(String email, String password);
}
