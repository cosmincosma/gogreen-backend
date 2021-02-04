package com.tssyonder.gogreen.repositories;

import com.tssyonder.gogreen.entities.Role;
import com.tssyonder.gogreen.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByRole(Role role);

    void deleteByEmail(String email);

    User findByUserId(Long id);

    User findByCitizenCitizenId(Long id);

    User findByCompanyCompanyId(Long id);
}
