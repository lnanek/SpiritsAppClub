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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import club.spiritsapp.R;

public class WearableActivity extends Activity {

    private static final String TAG = WearableActivity.class.getSimpleName();

    ViewGroup wineRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable);

        wineRating = (ViewGroup) findViewById(R.id.wineRating);
        final int wineImageCount = wineRating.getChildCount();

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
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
