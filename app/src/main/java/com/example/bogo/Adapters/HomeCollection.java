package com.example.bogo.Adapters;

import java.util.ArrayList;

public class HomeCollection {
    public String date="";
    public String name="";
    public String subject="";
    public String description="";


    public static ArrayList<HomeCollection> date_collection_arr;
    public HomeCollection(String date, String name, String subject, String description){

        this.date=date;
        this.name=name;
        this.subject=subject;
        this.description= description;

    }
}