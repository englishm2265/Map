package com.example.mengl03.map;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final int CITY_REQUEST = 1;
    public static Cities cities;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_main);

        Hashtable<String, String> places = new Hashtable<String, String>(); // a Hashtable called places is created in the MainActivity class

        /*  // places is then filled with the specified string values using the following .put methods; disabled, the Hashtable called places
        places.put("Washington", "White House");
        places.put("New York", "Statue of Liberty");
        places.put("Paris", "Eiffel Tower");
        places.put("London", "Buckingham Palace");
        places.put("Rome", "Colosseum");
        places.put("Sangin", "Sangin District Center");
        */

        cities = new Cities(places);    // the Cities object called cities is assigned a newly constructed cities object that is being passed

        // Test if device supports recognition
        PackageManager manager = getPackageManager();
        List<ResolveInfo> listOfMatches = manager.queryIntentActivities(    // the list object 'listOfMatches' that will store the values spoken by the user is declared here
                new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);  // 'listOfMatches' is enabled to recognize speech here
        if(listOfMatches.size() > 0)    // if there is something in 'listOfMatches'
         listen();  // 'listen' to what was said and process what was said to identify what was intended
        else{   // speech recognition not supported
            Toast.makeText( this,
                        "Sorry = Your device does not support speech recognition",
                    Toast.LENGTH_LONG).show();
        }


    }

    private void listen(){
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);     // *question* why are there two instances of RecognizerIntent that can recognize speech?
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What city?");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        startActivityForResult(listenIntent, CITY_REQUEST);
    }

    protected void onActivityResult(int requestCode,
                                        int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CITY_REQUEST && resultCode == RESULT_OK){
            // retrieve list of possible words
            ArrayList<String> returnedWords =
                                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // retrieve array of scores for returnedWords
            float [] scores = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
            // retrieve first good match
            String firstMatch = cities.firstMatchWithMinConfidence(returnedWords, scores);
            // Create Intent for map
            Intent mapIntent = new Intent(this, MapsActivity.class);
            // put firstMatch in mapIntent
            mapIntent.putExtra(Cities.CITY_KEY, firstMatch);
            // start map activity
            startActivity(mapIntent);
        }

    }

}
