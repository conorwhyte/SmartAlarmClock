package com.example.conorwhyte.smartalarmclock;

/**
 * This is an individual card which will be passed into the ArrayAdapter. Here the name, time and
 * image id of the card is set.
 * <p>
 * Created with the help of following tutorial:
 * http://javapapers.com/android/android-cards-list-view/
 * <p>
 * Created by conorwhyte on 16/11/2016.
 */

public class Card {
    private String line1;
    private String line2;
    private int id;

    public Card(String line1, String line2, int id) {
        this.line1 = line1;
        this.line2 = line2;
        this.id = id;
    }


    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public int getId() {
        return id;
    }


}