package com.example.vizsgaremek_asztali.adoption;

import com.google.gson.annotations.SerializedName;


public class Adoption {
    private int id;
    @SerializedName("adoption_type_id")
    private int adoptionTypeId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("adoption_beginning")
    private String begin;

    public Adoption(int id, int adoptionTypeId, int userId, String begin) {
        this.id = id;
        this.adoptionTypeId = adoptionTypeId;
        this.userId = userId;
        this.begin = begin;
    }

    public Adoption() {
        this.id = id;
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

    public String getFormazottType() {
        String tipus;
        if (adoptionTypeId == 1) {
            tipus = "ált. örökbefogadás";
        } else {
            tipus = "virtuális örökbefogadás";
        }
        return tipus;
    }

    @Override
    public String toString() {
        return String.format("%s, %s",this.getUserId(),this.getBegin());
    }
}
