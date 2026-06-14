package org.example.backend.service;

import jakarta.servlet.http.HttpSession;
import org.example.backend.entity.User;
import org.springframework.stereotype.Service;

@Service
public class SessionUserService {

    public User getLoggedInUser(HttpSession session) {
        Object userObj = session.getAttribute("loggedInUser");

        if (userObj instanceof User user) {
            return user;
        }

        return null;
    }

    public User getLoggedInUserOrThrow(HttpSession session) {
        User user = getLoggedInUser(session);

        if (user == null) {
            throw new RuntimeException("User belum login");
        }

        return user;
    }
}