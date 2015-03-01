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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import club.spiritsapp.R;

public class WearableActivity extends Activity {

    private static final String TAG = WearableActivity.class.getSimpleName();

    ViewGroup wineRating;

    TextView prompt;

    SeekBar seekBar;

    private String[] wineDescriptions = new String[] {
            "2010 Jamieson Ranch Vineyards, Coombsville, Cabernet Sauvignon",
            "2001 Acacia Vineyards, Pinot Noir",
            "2014 Tusk Reserve Red"
    };

    private int currentWineIndex = 0;

    private void nextPrompt() {
        currentWineIndex++;
        if ( currentWineIndex == wineDescriptions.length ) {
            finish();

            final Toast toast = Toast.makeText(this, "Done rating!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            return;
        }

        prompt.setText(wineDescriptions[currentWineIndex]);
        seekBar.setProgress(50);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Started with intent: " + getIntent());

        setContentView(R.layout.activity_wearable);

        prompt = (TextView) findViewById(R.id.prompt);
        prompt.setText(wineDescriptions[0]);

        wineRating = (ViewGroup) findViewById(R.id.wineRating);
        final int wineImageCount = wineRating.getChildCount();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setWineRating(progress);
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
}
