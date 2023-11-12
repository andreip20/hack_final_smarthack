package com.example.myapplication;



public class ActivitateSkill {
    private int id_user;
    private String data;
    private String categorie;
    private String nr_ore;

    @Override
    public String toString() {
        return "ActivitateSkill{" +
                ", id_user=" + id_user +
                ", data=" + data +
                ", categorie='" + categorie + '\'' +
                ", nr_ore=" + nr_ore +
                '}';
    }




    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNr_ore() {
        return nr_ore;
    }

    public void setNr_ore(String nr_ore) {
        this.nr_ore = nr_ore;
    }

    public ActivitateSkill( String  data, String categorie, String nr_ore) {
        this.data = data;
        this.categorie = categorie;
        this.nr_ore = nr_ore;
    }
}
