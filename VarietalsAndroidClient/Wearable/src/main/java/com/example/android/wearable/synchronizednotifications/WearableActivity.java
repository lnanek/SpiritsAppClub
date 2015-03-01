/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.wearable.synchronizednotifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wearable.synchronizednotifications.datasync.PhoneResultsStarter;

import java.util.ArrayList;
import java.util.List;

import club.spiritsapp.R;
import club.spiritsapp.model.Rating;
import club.spiritsapp.model.TastingSession;
import club.spiritsapp.model.Varietal;
import club.spiritsapp.model.VarietalType;
import club.spiritsapp.model.Wine;

public class WearableActivity extends Activity {

    private static final String TAG = WearableActivity.class.getSimpleName();

    ViewGroup wineRating;

    TextView prompt;

    SeekBar seekBar;

    View recordVoiceNoteButton;

    TextView speechResult;

    private ArrayList<Wine> wines = new ArrayList<Wine>();
    {
        {
            final Wine wine0 = new Wine();
            wine0.name = "2005 CORDORNIU NAPA GRAND RESERVE, SPARKLING";
            wine0.varietalType = new VarietalType();
            wine0.varietalType.id = "SPARKLING";
            wine0.varietal = new Varietal();
            wine0.varietal.id = "CHARDONNAY";
            wines.add(wine0);
        }

        {
            final Wine wine3 = new Wine();
            wine3.name = "1981 CARTLIDGE & BROWNE WINERY, CHARDONNAY";
            wine3.varietalType = new VarietalType();
            wine3.varietalType.id = "WHITE";
            wine3.varietal = new Varietal();
            wine3.varietal.id = "CHARDONNAY";
            wines.add(wine3);
        }

        {
            final Wine wine1 = new Wine();
            wine1.name = "2010 JAMIESON RANCH VINEYARDS, COOMBSVILLE, CABERNET SAUVIGNON";
            wine1.varietalType = new VarietalType();
            wine1.varietalType.id = "RED";
            wine1.varietal = new Varietal();
            wine1.varietal.id = "CABERNET SAUVIGNON";
            wines.add(wine1);
        }

        {
            final Wine wine2 = new Wine();
            wine2.name = "2001 ARTESA VINEYARDS & WINERY, PINOT";
            wine2.varietalType = new VarietalType();
            wine2.varietalType.id = "RED";
            wine2.varietal = new Varietal();
            wine2.varietal.id = "PINOT NOIR";
            wines.add(wine2);
        }

        {
            final Wine wine4 = new Wine();
            wine4.name = "2006 REYNOLDS FAMILY WINERY, NAUGHTY STICKY";
            wine4.varietalType = new VarietalType();
            wine4.varietalType.id = "DESSERT";
            wine4.varietal = new Varietal();
            wine4.varietal.id = "CHARDONNAY";
            wines.add(wine4);
        }
    }

    private int currentWineIndex = 0;

    private boolean currentModeIsRating = true;

    private int currentRating;

    private String currentComment;

    private TastingSession session = new TastingSession();

    private void nextPrompt() {
        if ( currentModeIsRating ) {

            Log.i(TAG, "nextPrompt switching from rating mode to voice prompt mode");

            wineRating.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            recordVoiceNoteButton.setVisibility(View.VISIBLE);
            speechResult.setVisibility(View.VISIBLE);
            speechResult.setText("");
            currentModeIsRating = false;
            return;
        }


        Log.i(TAG, "nextPrompt going to next wine");

        final Rating rating = new Rating();
        rating.wine = wines.get(currentWineIndex);
        rating.score = currentRating;
        rating.comment = currentComment;
        session.ratings.add(rating);

        currentWineIndex++;
        currentComment = null;
        currentRating = 50;
        if ( currentWineIndex == wines.size() ) {
            finish();

            final Toast toast = Toast.makeText(this, "Done rating!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            new PhoneResultsStarter().connectAndSend(this, session);

            return;
        }


        Log.i(TAG, "nextPrompt switching from voice mode to rating mode");
        wineRating.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        recordVoiceNoteButton.setVisibility(View.GONE);
        speechResult.setVisibility(View.GONE);
        currentModeIsRating = true;

        prompt.setText(wines.get(currentWineIndex).name);
        seekBar.setProgress(50);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Started with intent: " + getIntent());

        setContentView(R.layout.activity_wearable);

        speechResult = (TextView) findViewById(R.id.speechResult);

        recordVoiceNoteButton = findViewById(R.id.recordVoiceNoteButton);
        recordVoiceNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

        prompt = (TextView) findViewById(R.id.prompt);
        prompt.setText(wines.get(currentWineIndex).name);

        wineRating = (ViewGroup) findViewById(R.id.wineRating);
        final int wineImageCount = wineRating.getChildCount();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setWineRating(progress);
                currentRating = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.glass1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(20);
            }
        });
        findViewById(R.id.glass2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(40);
            }
        });
        findViewById(R.id.glass3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(60);
            }
        });
        findViewById(R.id.glass4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(80);
            }
        });
        findViewById(R.id.glass5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(100);
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPrompt();
            }
        });
    }

    private void setWineRating(int progress) {

        final float progressPercent = (float) progress / 100f;
        Log.i(TAG, "setWineRating progress = " + progressPercent);

        final int childCount = wineRating.getChildCount();
        final float progressPercentPerChild = 1.0f / childCount;

        for(int childIndex = 0 ; childIndex < childCount; childIndex++ ) {

            final ImageView currentChild = (ImageView) wineRating.getChildAt(childIndex);

            final float progressPercentToBeEmpty = progressPercentPerChild * (childIndex);
            final float progressPercentToBeFull = progressPercentPerChild * (childIndex + 1);


            Log.i(TAG, "setWineRating child = " + childIndex
                    + " progressPercentToBeEmpty = " + progressPercentToBeEmpty
                    + " progressPercentToBeFull = " + progressPercentToBeFull);

            if (progressPercent >= progressPercentToBeFull) {
                currentChild.setImageResource(R.drawable.ic_full_glass);
            } else if (progressPercent > progressPercentToBeEmpty) {
                currentChild.setImageResource(R.drawable.ic_half_glass);
            } else {
                currentChild.setImageResource(R.drawable.ic_empty_glass);
            }
        }

    }

    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Log.i(TAG, "displaySpeechRecognizer");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.i(TAG, "onActivityResult");

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            Log.i(TAG, "onActivityResult spokenText = " + spokenText);

            if ( null != spokenText ) {

                speechResult.setText(spokenText);

            }
            currentComment = spokenText;
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
