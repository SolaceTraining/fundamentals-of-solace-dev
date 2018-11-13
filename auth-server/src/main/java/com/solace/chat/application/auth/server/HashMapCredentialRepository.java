package com.solace.chat.application.auth.server;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HashMapCredentialRepository implements ICredentialsRepository {

    // A HashMap representing the username/password
    private final Map<String,String> credentialsMap;

    public HashMapCredentialRepository(){
        credentialsMap = new HashMap<>();
        credentialsMap.put("ValidUser","solace");
        credentialsMap.put("SolaceUser","solace");
        credentialsMap.put("TestUser","solace");
    }

    @Override
    public boolean isValidUser(String user, String password) {
        if(credentialsMap.get(user)!=null && credentialsMap.get(user).equals(password)) {
            return true;
        }else{
            return false;
        }
    }
}
