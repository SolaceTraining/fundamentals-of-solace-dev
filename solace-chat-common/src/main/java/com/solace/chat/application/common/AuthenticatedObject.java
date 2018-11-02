package com.solace.chat.application.common;

/**
 * AuthenticatedObject is a construct that is used to validate whether a user has been authenticated
 * @author Thomas Kunnumpurath
 */

public class AuthenticatedObject {

    public boolean isAuthenticated() {
        return authenticated;
    }

    private boolean authenticated;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}