package com.nucleus.picpay.services;

import com.nucleus.picpay.domain.user.User;
import com.nucleus.picpay.domain.user.UserType;
import com.nucleus.picpay.dto.UserDTO;
import com.nucleus.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() != UserType.DEFAULT) throw new Exception("Shopkeeper aren't authorized to make transactions");

        if(sender.getBalance().compareTo(amount) > 0) throw new Exception("Insufficient balance");
    }

    public User findUserByDocument(String document) throws Exception {
        return this.userRepository.findByDocument(document).orElseThrow(() -> new Exception("User not found"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
