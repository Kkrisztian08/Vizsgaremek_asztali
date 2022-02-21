package com.example.vizsgaremek_asztali.dogs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dogs {
    private int id;
    private String name;
    private String gender;
    private Date likely_bday;
    private String species;
    private String external_property;
    private int interest;
    private int adoption_id;

    public Dogs(int id, String name, String gender, Date birthday, String species, String external_property, int interest, int adoption_id) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.likely_bday = birthday;
        this.species = species;
        this.external_property = external_property;
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

    public Date getLikely_bday() {
        return likely_bday;
    }

    public String getBdayFormated() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(likely_bday);
    }

    public void setLikely_bday(Date likely_bday) {
        this.likely_bday = likely_bday;
    }



    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getExternal_property() {
        return external_property;
    }

    public void setExternal_property(String external_property) {
        this.external_property = external_property;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public int getAdoption_id() {
        return adoption_id;
    }

    public void setAdoption_id(int adoption_id) {
        this.adoption_id = adoption_id;
    }

    @Override
    public String toString() {
        return "Dogs{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + likely_bday +
                ", species='" + species + '\'' +
                ", external_property='" + external_property + '\'' +
                ", interest=" + interest +
                ", adoption_id=" + adoption_id +
                '}';
    }
}
