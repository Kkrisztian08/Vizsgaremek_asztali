package com.example.vizsgaremek_asztali.dogs;

import java.util.Date;

public class Dogs {
    private int id;
    private String name;
    private String gender;
    private Date birthday;
    private String species;
    private String external_property;
    private int interest;
    private int adoption_id;
    private int virtual_adoption_id;

    public Dogs(int id, String name, String gender, Date birthday, String species, String external_property, int interest, int adoption_id, int virtual_adoption_id) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.species = species;
        this.external_property = external_property;
        this.interest = interest;
        this.adoption_id = adoption_id;
        this.virtual_adoption_id = virtual_adoption_id;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public int getVirtual_adoption_id() {
        return virtual_adoption_id;
    }

    public void setVirtual_adoption_id(int virtual_adoption_id) {
        this.virtual_adoption_id = virtual_adoption_id;
    }
}
