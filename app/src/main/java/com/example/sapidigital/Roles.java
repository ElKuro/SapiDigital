package com.example.sapidigital;

public class Roles {

    public  String nama;
    public  String roles;

    public  Roles(String nama, String roles){
        this.roles = roles;
        this.nama = nama;

    }

    @Override
    public String toString() {
        return roles;
    }
}
