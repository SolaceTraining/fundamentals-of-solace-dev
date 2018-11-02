package com.solace.chat.application.auth.server;

/**
 * The ICredentialsRepository interface is a construct that houses the validation function
 * for the user and password
 * @author: Thomas Kunnumpurath
 */
public interface ICredentialsRepository {

    // Simple validation function
    public boolean isValidUser(String user, String password);
}
