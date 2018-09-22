package com.solace.ChatApplication;

public interface ICredentialsRepository {

    public boolean isValidUser(String user, String password);
}
