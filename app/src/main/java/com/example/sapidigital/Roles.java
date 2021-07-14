package com.example.sapidigital;

public class Roles {

    public  String id;
    public  String roles;

    public  Roles(String id, String roles){
        this.roles = roles;
        this.id = id;

    }

    @Override
    public String toString() {
        return roles;
    }
}
