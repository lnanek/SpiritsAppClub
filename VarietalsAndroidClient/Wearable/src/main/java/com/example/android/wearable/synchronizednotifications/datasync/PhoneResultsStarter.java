package com.example.android.wearable.synchronizednotifications.datasync;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.android.wearable.synchronizednotifications.common.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

import club.spiritsapp.model.TastingSession;

/**
 * Created by lnanek on 2/28/15.
 */
public class PhoneResultsStarter {


    private static final String TAG = PhoneResultsStarter.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    public void connectAndSend(Context context, final TastingSession tasting) {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);


                        sendNotification(tasting);
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();


        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    private String now(final Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(new Date());
    }

    private void sendNotification(final TastingSession tasting) {
        if (mGoogleApiClient.isConnected()) {
            PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Constants.START_RESULTS_PATH);
            // Make sure the data item is unique. Usually, this will not be required, as the payload
            // (in this case the title and the content of the notification) will be different for almost all
            // situations. However, in this example, the text and the content are always the same, so we need
            // to disambiguate the data item by adding a field that contains teh current time in milliseconds.
            dataMapRequest.getDataMap().putDouble(Constants.START_RESULTS_TIMESTAMP, System.currentTimeMillis());

            final String json = new Gson().toJson(tasting);
            Log.i(TAG, "sending phone: " + json);

            dataMapRequest.getDataMap().putString(Constants.START_RESULTS_TASTING, json);

            PutDataRequest putDataRequest = dataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest);

            disconnect();
        }
        else {
            Log.e(TAG, "No connection to wearable available!");
        }
    }


}
