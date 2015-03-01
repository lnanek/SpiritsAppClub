/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.content.Intent;
import android.util.Log;

import com.example.android.wearable.synchronizednotifications.common.Constants;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * A {@link com.google.android.gms.wearable.WearableListenerService} that will be invoked when a
 * DataItem is added or deleted. The creation of a new DataItem will be interpreted as a request to
 * create a new notification and the removal of that DataItem is interpreted as a request to
 * dismiss that notification.
 */
public class NotificationUpdateService extends WearableListenerService
         {

    private static final String TAG = NotificationUpdateService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            String action = intent.getAction();
            Log.i(TAG, "stated with intent: " + action);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i(TAG, "onDataChanged dataEvents = " + dataEvents);

        for(DataEvent dataEvent: dataEvents) {
            Log.i(TAG, "onDataChanged dataEvent = " + dataEvent);

            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                if (Constants.START_RATINGS_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {

                    //DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
                    //String title = dataMapItem.getDataMap().getString(Constants.START_RATINGS_TITLE);
                    //String content = dataMapItem.getDataMap().getString(Constants.START_RATINGS_CONTENT);

                    final Intent intent = new Intent(getApplicationContext(), WearableActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                    Log.i(TAG, "starting: " + intent);

                }
            }
        }
    }

}
