package com.example.urguard;

public class user {
    private int id;
    private String name;
    private String contact_1;
    private String contact_2;
    private String contact_3;
    private String message;

    public user(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public user(String name, String contact_1, String contact_2, String contact_3, String message){
        this.name = name;
        this.contact_1 = contact_1;
        this.contact_2 = contact_2;
        this.contact_3 = contact_3;
        this.message = message;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_1() {
        return contact_1;
    }

    public void setContact_1(String contact_1) {
        this.contact_1 = contact_1;
    }

    public String getContact_2() {
        return contact_2;
    }

    public void setContact_2(String contact_2) {
        this.contact_2 = contact_2;
    }

    public String getContact_3() {
        return contact_3;
    }

    public void setContact_3(String contact_3) {
        this.contact_3 = contact_3;
    }


}
