package club.spiritsapp.activity;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import club.spiritsapp.R;

public class ViewVineyardActivity extends Activity {

	private static final String TAG = ViewVineyardActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_vineyard);
		
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

	}

}
