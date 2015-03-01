package club.spiritsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

import club.spiritsapp.R;
import club.spiritsapp.VarietalsApp;
import club.spiritsapp.model.HotelInfo;
import club.spiritsapp.model.HotelsResponse;
import club.spiritsapp.model.TypesResponse;
import club.spiritsapp.model.VarietalType;
import club.spiritsapp.network.GsonRequest;
import club.spiritsapp.network.NetworkConstants;

public class ChooseHotelActivity extends TintedStatusBarActivity {

	private static final String TAG = ChooseHotelActivity.class.getSimpleName();

    private static final String NAPA_VALLEY_LAT = "38.4298199";

    private static final String NAPA_VALLEY_LON = "-122.4208311";

    private static final String EXPEDIA_KEY = "1x9AmFfvGLFrcUMmimLZGNdEb2WFNzqb";

    private static final String EXPEDIA_SECRET = "eklkGA3OTgxJpxiF";

	private final Gson gson = new Gson();

	private static final String URL = "http://terminal2.expedia.com/hotels?location="
            + NAPA_VALLEY_LAT
            + ","
            + NAPA_VALLEY_LON
            + "&radius=15km&dates=2015-03-15,2015-03-18&apikey="
            + EXPEDIA_KEY;

	private ViewGroup typesContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_book_hotels);

        getActionBar().setLogo(null);
        getActionBar().setIcon(null);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
		
		typesContainer = (ViewGroup) findViewById(R.id.container);

		requestTypes();

	}

	private void requestTypes() {
		
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(true);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.show();
		
		GsonRequest<HotelsResponse> typesRequest = new GsonRequest<HotelsResponse>(
				Method.GET, URL, HotelsResponse.class, null,
				new Response.Listener<HotelsResponse>() {
					@Override
					public void onResponse(HotelsResponse response) {
						progress.dismiss();
						displayTypes(response);

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
				.setPositiveButton("Retry", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						requestTypes();
					}
				}).setNegativeButton("Cancel", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				}).show();
	}

	private void displayTypes(HotelsResponse types) {

		final LayoutInflater inflator = getLayoutInflater();

		for (final HotelInfo type : types.HotelInfoList.HotelInfo) {

            final ViewGroup vineyardContainer = (ViewGroup) inflator.inflate(
					R.layout.activity_book_hotels_item, typesContainer, false);

            vineyardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHotelDetails(type);
                }
            });

            final TextView priceView = (TextView) vineyardContainer.findViewById(R.id.price);
            priceView.setText(null == type.Price ? "" :
                    null == type.Price.TotalRate ? "" :
                            type.Price.TotalRate.toString());

            final TextView nameView = (TextView) vineyardContainer.findViewById(R.id.name);
            nameView.setText(type.Name);

            final TextView addressView = (TextView) vineyardContainer.findViewById(R.id.address);
            addressView.setText(type.Location.toString());

            final ImageView imageView = (ImageView) vineyardContainer.findViewById(R.id.image);
            Picasso
                    .with(ChooseHotelActivity.this)
                    .load(convertToLargeImage(type.ThumbnailUrl))
                    .fit().centerCrop()
                    .noFade()
                    .placeholder(R.drawable.sample_hotel)
                    .into(imageView);

            typesContainer.addView(vineyardContainer);

		}

	}

    private String convertToLargeImage(final String imageUrl) {
        Log.i(TAG, "convertToLargeImage imageUrl: " + imageUrl);
        if ( null == imageUrl ) {
            return null;
        }

        final String largeUrl = imageUrl.replace("_t.jpg", "_z.jpg");
        Log.i(TAG, "convertToLargeImage largeUrl: " + largeUrl);
        return largeUrl;
    }

    private void viewHotelDetails(HotelInfo type) {

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(type.DetailsUrl));
        startActivity(intent);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.choose, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.next) {
			final Intent intent = new Intent(this, ChooseVarietalsActivity.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}

}
