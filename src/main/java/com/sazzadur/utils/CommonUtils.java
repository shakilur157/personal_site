package com.sazzadur.utils;

import com.sazzadur.enums.UserType;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class CommonUtils {
    public Boolean findValidUser(List<String> roles, String userType){
        for (String role : roles) {
            if (role.toLowerCase().equals(userType.toLowerCase())) {
                return true;
            }
        }
        return  false;
    }
    public String getJWTFromHeader(Map<String, String> headers){
        String jwt = "";
        int size = headers.size();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if(entry.getKey().equals("authorization")){
                if(entry.getValue().startsWith("Bearer ")){
                    jwt = getJWT(entry.getValue());
                    return jwt;
                }
            }
        }

        return null;
    }
    public String getJWT(String bearer){
        return bearer.substring(7, bearer.length());
    }

    public UserType getUserRoleFromUserType(String type){
        String uType = type.toUpperCase();
        return UserType.valueOf(type);
    }

    public String generateRandomName(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
