package com.tssyonder.gogreen.services.impl;

import com.tssyonder.gogreen.entities.User;
import com.tssyonder.gogreen.repositories.UserRepository;
import com.tssyonder.gogreen.services.EncryptDecrypt;
import com.tssyonder.gogreen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        String pwd = user.getPassword();
        final String secretKey = "secretKey";
        String encryptPwd = EncryptDecrypt.encrypt(pwd, secretKey);
        user.setPassword(encryptPwd);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public User getByCitizenId(Long id) {
        return userRepository.findByCitizenCitizenId(id);
    }

    @Override
    public User getByCompanyId(Long id) {
        return userRepository.findByCompanyCompanyId(id);
    }

}