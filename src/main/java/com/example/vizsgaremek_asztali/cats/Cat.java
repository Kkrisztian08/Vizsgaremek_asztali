package com.example.vizsgaremek_asztali.cats;

public class Cat {
    private int id;
    private String name;
    private String gender;
    private String likely_bday;
    private String external_property;
    private String description;
    private int interest;
    private Integer adoption_id;

    public Cat(int id, String name, String gender, String likely_bday, String external_property, String description, int interest, Integer adoption_id) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.likely_bday = likely_bday;
        this.external_property = external_property;
        this.description = description;
        this.interest = interest;
        this.adoption_id = adoption_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLikely_bday() {
        return likely_bday;
    }

    public void setLikely_bday(String likely_bday) {
        this.likely_bday = likely_bday;
    }

    public String getExternal_property() {
        return external_property;
    }

    public void setExternal_property(String external_property) {
        this.external_property = external_property;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public Integer getAdoption_id() {
        return adoption_id;
    }

    public void setAdoption_id(Integer adoption_id) {
        this.adoption_id = adoption_id;
    }

    @Override
    public String toString() {
        return String.format("%s",this.getName());
    }
}
