package com.solace.ChatApplication;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HashMapCredentialRepository implements ICredentialsRepository {

    // A HashMap representing the username/password
    private final Map<String,String> credentialsMap;

    public HashMapCredentialRepository(){
        credentialsMap = new HashMap<>();
        credentialsMap.put("Leah","solace");
        credentialsMap.put("Ush","solace");
        credentialsMap.put("Thomas","solace");
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
