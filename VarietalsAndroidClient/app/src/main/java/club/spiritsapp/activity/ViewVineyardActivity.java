package club.spiritsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import club.spiritsapp.R;
import club.spiritsapp.model.SampleImages;
import club.spiritsapp.model.SampleWineData;
import club.spiritsapp.model.Vineyard;
import club.spiritsapp.network.GsonRequest;
import club.spiritsapp.network.NetworkConstants;

public class ViewVineyardActivity extends TintedStatusBarActivity {

    public static final String VINEYARD_EXTRA = ViewVineyardActivity.class.getName() + ".VINEYARD_EXTRA";

    private static final String TAG = ChooseTypesActivity.class.getSimpleName();

    private final Gson gson = new Gson();

    private static final String URL = NetworkConstants.SERVER +
            "winery/";

    private Vineyard vineyard;

    private String vineyardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_vineyard);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final Intent intent = getIntent();
        if (null != intent) {
            final Bundle extras = intent.getExtras();
            if (null != extras) {
                vineyardId = extras.getString(VINEYARD_EXTRA);
                //String vineyard = new Gson().fromJson(vineyardJson, Vineyard.class);
                requestVineyard(vineyardId);
                return;
            }
        }

        errorDialog();
    }

    public int getTintColor() {
        return Color.parseColor("#000000");
    }


    private void requestVineyard(final String vineyardId) {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        GsonRequest<Vineyard> typesRequest = new GsonRequest<Vineyard>(
                Request.Method.GET, URL + vineyardId, Vineyard.class, null,
                new Response.Listener<Vineyard>() {
                    @Override
                    public void onResponse(Vineyard response) {
                        Log.d(TAG, "Received vineyard from server: " + response);
                        progress.dismiss();
                        displayVineyard(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error downloading types: ", error);
                progress.dismiss();
                errorDialog();
            }
        });
        // Add the request to the RequestQueue.
        NetworkConstants.add(this, typesRequest);

    }

    private void errorDialog() {
        new AlertDialog.Builder(this).setTitle("Error connecting")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestVineyard(vineyardId);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        }).show();
    }

    private void displayVineyard(Vineyard receivedVineyard) {

        vineyard = receivedVineyard;

        final TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(vineyard.name);

        final TextView addressView = (TextView) findViewById(R.id.address);
        addressView.setText(null == vineyard.address ? "" : vineyard.address.toString());

        final ImageView vineyardImage = (ImageView) findViewById(R.id.vineyard_image);
        //vineyardImage.setImageResource(vineyard.samplePhotoResourceIds.iterator().next());
        vineyardImage.setImageResource(SampleImages.getNextSampleResourceId());

        // Ensure we have wines for the demo
        if ( null == vineyard.wines || vineyard.wines.isEmpty() ) {
            vineyard.wines = SampleWineData.wines;
        }

        // Ensure we have a photo for the demo
        if ( null == vineyard.photos || vineyard.photos.isEmpty() || null == vineyard.photos.get(0).url  ) {
            vineyardImage.setImageResource(SampleImages.getNextSampleResourceId());
        } else {
            Picasso
                    .with(ViewVineyardActivity.this)
                    .load(vineyard.photos.get(0).url)
                    .fit().centerCrop()
                    .noFade()
                    .placeholder(SampleImages.getNextSampleResourceId())
                    .into(vineyardImage);
        }


        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ViewVineyardActivity.this, ViewItineraryActivity.class);

                final String vineyardJson = new Gson().toJson(vineyard);
                intent.putExtra(ViewItineraryActivity.VINEYARD_EXTRA, vineyardJson);

                startActivity(intent);
            }
        });

    }

}

