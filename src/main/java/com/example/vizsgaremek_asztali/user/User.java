package com.example.vizsgaremek_asztali.user;

public class User {
    private int id;
    private int admin;
    private String name;
    private String username;
    private String birthday;
    private String address;
    private String phone_number;
    private String email;
    private String password;

    public User(int id, int admin, String name, String username, String birthday, String address, String phone_number, String email, String password) {
        this.id = id;
        this.admin = admin;
        this.name = name;
        this.username = username;
        this.birthday = birthday;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFormazottAdmin() {
        String jog;
        switch (admin) {
            case 1:
                jog = "admin";
                break;
            case 2:
                jog = "super admin";
                break;
            default:
                jog = "felhasználó";
        }
        return jog;
    }

    @Override
    public String toString() {
        return String.format("%s",this.getName());
    }
}
