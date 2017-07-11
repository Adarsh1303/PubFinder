package com.example.petr_panda.application;

/**
 * Created by petr_panda on 11/7/17.
 */
public class Place {

    private  int _id;
    private String place_name;
    private String place_address;
    private String place_type;
    private String place_rating;
    private String place_number;

    public String getPlace_number() {
        return this.place_number;
    }

    public String getPlace_rating() {
        return this.place_rating;
    }

    public String getPlace_type() {
        return this.place_type;
    }

    public String getPlace_address() {
        return this.place_address;
    }

    public String getPlace_name() {
        return this.place_name;
    }




    public Place(String place_name,String place_address,String place_type,String place_rating,String place_number )
    {
        this.place_name = place_name;
        this.place_address = place_address;
        this.place_type = place_type;
        this.place_rating = place_rating;
        this.place_number = place_number;
    }


}
