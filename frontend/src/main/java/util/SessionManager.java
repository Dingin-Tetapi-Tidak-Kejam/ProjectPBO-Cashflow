package util;

import model.User;

public class SessionManager {

    // Eager initialization — thread-safe tanpa synchronized
    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
    }
}
