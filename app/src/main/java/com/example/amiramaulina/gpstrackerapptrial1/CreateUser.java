package com.example.amiramaulina.gpstrackerapptrial1;

/**
 * Created by Amira Maulina
 */

public class CreateUser
{
    public String name;
    public String email;
    public String password;
    public String date;
    public String circlecode;
    public String userid;
    public String isSharing;
    public String lat;
    public String lng;



    public CreateUser()
    {}

    public CreateUser(String name, String email, String password, String date, String circlecode, String userid, String isSharing, String lat, String lng) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.circlecode = circlecode;
        this.userid = userid;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;

    }
}
