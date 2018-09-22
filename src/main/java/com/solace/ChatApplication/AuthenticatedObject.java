package com.solace.ChatApplication;

public class AuthenticatedObject {

    public boolean isAuthenticated() {
        return authenticated;
    }

    private boolean authenticated;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
