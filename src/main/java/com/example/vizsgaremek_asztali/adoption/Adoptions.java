package com.example.vizsgaremek_asztali.adoption;

public class Adoptions {
    private int id;
    private int adoptionTypeId;
    private int userId;
    private String begin;

    public Adoptions(int id, int adoptionTypeId, int userId, String begin) {
        this.id = id;
        this.adoptionTypeId = adoptionTypeId;
        this.userId = userId;
        this.begin = begin;
    }

    public int getId() {
        return id;
    }

    public int getAdoptionTypeId() {
        return adoptionTypeId;
    }

    public void setAdoptionTypeId(int adoptionTypeId) {
        this.adoptionTypeId = adoptionTypeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }
}
