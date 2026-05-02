package com.bhuvi.personalTrackerAPI.service;

import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mailId) throws UsernameNotFoundException {
        User user = userRepository.findByMailId(mailId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with mailId: " + mailId);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getMailId(), // Using email as mailId for Spring Security
                user.getPassword(),
                new ArrayList<>() // No authorities/roles for now
        );
    }
}
