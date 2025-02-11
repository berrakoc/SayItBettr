package com.sib.sayitbettr.Repository;

import com.sib.sayitbettr.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMailAndPassword(String mail, String password);
}
