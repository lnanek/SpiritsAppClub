package club.spiritsapp.activity;

import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import club.spiritsapp.R;
import club.spiritsapp.model.SampleImages;
import club.spiritsapp.model.Vineyard;

public class ViewVineyardActivity extends Activity {

    public static final String VINEYARD_EXTRA = ViewVineyardActivity.class.getName() + ".VINEYARD_EXTRA";

	private static final String TAG = ViewVineyardActivity.class.getSimpleName();

    private Vineyard vineyard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_vineyard);

        final Intent intent = getIntent();
        if ( null != intent ) {
            final Bundle extras = intent.getExtras();
            if ( null != extras ) {
                final String vineyardJson = extras.getString(VINEYARD_EXTRA);
                vineyard = new Gson().fromJson(vineyardJson, Vineyard.class);
            }
        }
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			TintedStatusBarActivity.setTranslucentStatus(this, true);
		}

	    // create our manager instance after the content view is set
	    SystemBarTintManager tintManager = new SystemBarTintManager(this);
	    // enable status bar tint
	    tintManager.setStatusBarTintEnabled(true);
	    // enable navigation bar tint
	    tintManager.setNavigationBarTintEnabled(true);
	    
	 // set a custom tint color for all system bars
	    tintManager.setTintColor(Color.parseColor("#000000"));
	    
		findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

        final TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(vineyard.name);

        final TextView addressView = (TextView) findViewById(R.id.address);
        addressView.setText(null == vineyard.address ? "" : vineyard.address.toString());

        final ImageView vineyardImage = (ImageView) findViewById(R.id.vineyard_image);
        //vineyardImage.setImageResource(vineyard.samplePhotoResourceIds.iterator().next());
        vineyardImage.setImageResource(SampleImages.getNextSampleResourceId());

	}

}
