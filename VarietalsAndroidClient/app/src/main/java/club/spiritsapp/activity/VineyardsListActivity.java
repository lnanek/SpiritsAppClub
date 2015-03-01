package club.spiritsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import club.spiritsapp.R;
import club.spiritsapp.VarietalsApp;
import club.spiritsapp.model.SampleImages;
import club.spiritsapp.network.NetworkConstants;
import club.spiritsapp.network.VineyardsRequest;
import club.spiritsapp.model.Vineyard;
import club.spiritsapp.model.VineyardsRequestBody;

public class VineyardsListActivity extends TintedStatusBarActivity {

    private static final String TAG = VineyardsListActivity.class.getSimpleName();

    private final Gson gson = new Gson();

    private static final String URL = NetworkConstants.SERVER +
            "user/123/suggestions";

    private ViewGroup vineyardsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vineyards);

        getActionBar().setLogo(null);
        getActionBar().setIcon(null);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        vineyardsContainer = (ViewGroup) findViewById(R.id.vineyardsContainer);

        requestVineyards();

    }

    private void requestVineyards() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.show();

        VineyardsRequest typesRequest = new VineyardsRequest(
                Method.POST, URL, null,
                new Response.Listener<List<Vineyard>>() {
                    @Override
                    public void onResponse(List<Vineyard> response) {
                        progress.dismiss();
                        Log.d(TAG, "Downloaded: " + response);
                        displayVineyards(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error downloading types: ", error);
                progress.dismiss();
                errorDialog();
            }
        });

        TwitterSession session =
                Twitter.getSessionManager().getActiveSession();

        VineyardsRequestBody body = new VineyardsRequestBody();
        body.types = VarietalsApp.instance.prefs.getChosenTypes();
        body.varietals = VarietalsApp.instance.prefs.getChosenVarietals();
        body.twitter = session;

        String httpPostBody = body.toString();
        Log.i(TAG, "Setting request body: " + httpPostBody);

        typesRequest.setBody(httpPostBody);

        // Add the request to the RequestQueue.
        NetworkConstants.add(this, typesRequest);
    }

    private void errorDialog() {
        new AlertDialog.Builder(this).setTitle("Error connecting")
                .setPositiveButton("Retry", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestVineyards();
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        }).show();
    }

    private void displayVineyards(List<Vineyard> vineyards) {

        final LayoutInflater inflator = getLayoutInflater();

        for (final Vineyard vineyard : vineyards) {

            vineyard.samplePhotoResourceIds.add(SampleImages.getNextSampleResourceId());
            vineyard.samplePhotoResourceIds.addAll(Arrays.<Integer>asList(SampleImages.sampleImageResourceIds));


            final ViewGroup vineyardContainer = (ViewGroup) inflator.inflate(
                    R.layout.activity_vineyards_item, vineyardsContainer, false);

            vineyardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(VineyardsListActivity.this, ViewVineyardActivity.class);

                    final String vineyardJson = new Gson().toJson(vineyard);
                    intent.putExtra(ViewVineyardActivity.VINEYARD_EXTRA, vineyardJson);

                    startActivity(intent);
                }
            });

            final TextView vineyardNameView = (TextView) vineyardContainer.findViewById(R.id.name);
            vineyardNameView.setText(vineyard.name);

            final TextView vineyardAddressView = (TextView) vineyardContainer.findViewById(R.id.address);
            vineyardAddressView.setText(null == vineyard.address ? "" : vineyard.address.toString());

            final ImageView vineyardImage = (ImageView) vineyardContainer.findViewById(R.id.vineyard_image);

            final int sampleImageResourceId = vineyard.samplePhotoResourceIds.iterator().next();
            if ( null == vineyard.photos || vineyard.photos.isEmpty() || null == vineyard.photos.get(0).url  ) {
                vineyardImage.setImageResource(sampleImageResourceId);
            } else {
                Picasso
                        .with(VineyardsListActivity.this)
                        .load(vineyard.photos.get(0).url)
                        .fit().centerCrop()
                        .noFade()
                        .placeholder(sampleImageResourceId)
                        .into(vineyardImage);
            }

            vineyardsContainer.addView(vineyardContainer);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vineyards, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
