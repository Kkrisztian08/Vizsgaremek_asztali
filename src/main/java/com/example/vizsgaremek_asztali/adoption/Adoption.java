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
        switch (adoptionTypeId) {
            case 1:
                tipus = "örökbefogadás";
                break;
            case 2:
                tipus = "virtuális örökbefogadás";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + adoptionTypeId);
        }

        return tipus;
    }
}
