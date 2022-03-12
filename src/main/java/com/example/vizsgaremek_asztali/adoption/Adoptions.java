package com.example.vizsgaremek_asztali.adoption;

public class Adoptions {
    private int id;
    private int adoptionTypeId;
    private String begin;

    public Adoptions(int id, int adoptionTypeId, String begin) {
        this.id = id;
        this.adoptionTypeId = adoptionTypeId;
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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }
}
