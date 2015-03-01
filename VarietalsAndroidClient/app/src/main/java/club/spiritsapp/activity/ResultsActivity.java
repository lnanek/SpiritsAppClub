package club.spiritsapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import club.spiritsapp.R;
import club.spiritsapp.R.id;
import club.spiritsapp.R.layout;

public class ResultsActivity extends Activity {

    private static final String CHART_URL =
            "https://chart.googleapis.com/chart?cht=p3&chs=200x90&chd=t:10,20,30&chco=FF0000|00FF00|0000FF";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_results);

        final ImageView chartImage = (ImageView) findViewById(R.id.chartImage);

        Picasso
                .with(ResultsActivity.this)
                .load(CHART_URL)
                .fit().centerInside()
                .noFade()
                .into(chartImage);

    }

}
