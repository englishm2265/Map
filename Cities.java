package com.example.mengl03.map;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Cities {

    public static final String CITY_KEY = "city";
    public static final float MIN_CONFIDENCE = 0.5f;

    public static final String DEFAULT_CITY = "Washington";
    public static final double DEFAULT_LATITUDE = 38.8977;
    public static final double DEFAULT_LONGITUDE = -77.0366;
    public static final String MESSAGE = "Thanks for visiting";

    public String matchedCity;
    private Hashtable<String, String> places;   // a private Hashtable object called places is declared in the Cities class

        // places is assigned the Hashtable called places in the Cities method
    public Cities( Hashtable<String, String> newPlaces ){
        places = newPlaces;
    }

    public String firstMatchWithMinConfidence(ArrayList<String> words, float[] confidLevels){
        if(words == null || confidLevels == null)
            return DEFAULT_CITY;

        int numberOfWords = words.size();
        Enumeration<String> cities;
        for(int i = 0; i < numberOfWords && i < confidLevels.length; i++){
            if(confidLevels[i] < MIN_CONFIDENCE)    // if the conf
                break;
            String word = words.get(i);
            cities = places.keys();         // this is where the words that were recognized

            while (cities.hasMoreElements()) {

                String city = cities.nextElement();
                if (word.equalsIgnoreCase(city))
                    return word;    // this is where the desired city is determined
            }
            return word;
        }
        return DEFAULT_CITY;
    }


    public String getAttraction(String city){return (String) places.get(city);}
}
