package com.tssyonder.gogreen.services;


import com.tssyonder.gogreen.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<User> getAllUsers();

    User getByEmail(String email);

    User saveUser(User user);

    User getUserById(Long id);

    User getByCitizenId(Long id);

    User getByCompanyId(Long id);

}