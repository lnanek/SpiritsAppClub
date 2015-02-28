package club.spiritsapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import club.spiritsapp.R;
import club.spiritsapp.model.SampleImages;
import club.spiritsapp.model.Vineyard;

public class ViewItineraryActivity extends TintedStatusBarActivity {

    public static final String VINEYARD_EXTRA = ViewItineraryActivity.class.getName() + ".VINEYARD_EXTRA";

	private static final String TAG = ViewItineraryActivity.class.getSimpleName();

    private Vineyard vineyard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_itinerary);

        getActionBar().setLogo(null);
        getActionBar().setIcon(null);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        final Intent intent = getIntent();
        if ( null != intent ) {
            final Bundle extras = intent.getExtras();
            if ( null != extras ) {
                final String vineyardJson = extras.getString(VINEYARD_EXTRA);
                vineyard = new Gson().fromJson(vineyardJson, Vineyard.class);
            }
        }

        //final TextView nameView = (TextView) findViewById(R.id.name);
        //nameView.setText(vineyard.name);

        //final TextView addressView = (TextView) findViewById(R.id.address);
        //addressView.setText(null == vineyard.address ? "" : vineyard.address.toString());

        final ImageView vineyardImage = (ImageView) findViewById(R.id.vineyard_image);
        //vineyardImage.setImageResource(vineyard.samplePhotoResourceIds.iterator().next());
        vineyardImage.setImageResource(SampleImages.getNextSampleResourceId());

        findViewById(R.id.bookHotelButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ViewItineraryActivity.this, ChooseHotelActivity.class);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.bookCarButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ViewItineraryActivity.this, ChooseHotelActivity.class);
                startActivity(intent);
                finish();
            }
        });
	}

}
