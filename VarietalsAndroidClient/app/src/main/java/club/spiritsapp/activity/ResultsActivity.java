package club.spiritsapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wearable.synchronizednotifications.common.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import club.spiritsapp.R;
import club.spiritsapp.R.id;
import club.spiritsapp.R.layout;
import club.spiritsapp.model.Rating;
import club.spiritsapp.model.TastingSession;
import club.spiritsapp.model.Vineyard;

public class ResultsActivity extends Activity {

    private static final String TAG = ResultsActivity.class.getSimpleName();

    // As per https://developers.google.com/chart/image/docs/gallery/pie_charts

    private String url =
            "https://chart.googleapis.com/chart?cht=p&chs=500x250&chd=t:10,10,30,50&chdl=Red|White|Dessert|Sparkling&chco=87042f|e8e8be|8b0369|d7d4d4";

    private TastingSession tasting;

    private TextView history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(layout.activity_results);

        history = (TextView) findViewById(id.history);


        final Intent intent = getIntent();
        if ( null != intent ) {
            final Bundle extras = intent.getExtras();
            if ( null != extras ) {
                final String tastingJson = extras.getString(Constants.START_RESULTS_TASTING);
                if ( null != tastingJson ) {
                    tasting = new Gson().fromJson(tastingJson, TastingSession.class);
                }
                Log.i(TAG, "onCreate received tasting: " + tasting);
            }
        }

        if ( null != tasting ) {

            final Map<String, Integer> scoreByTypeId = new LinkedHashMap<String, Integer>();
            for (final Rating rating : tasting.ratings) {

                history.append("Rated: " + rating.wine.name + " " + rating.score + "\n");
                if ( null != rating.comment ) {
                    history.append("Commented: " + rating.comment + "\n");
                }
                history.append("\n");

                final Integer currentScore = scoreByTypeId.get(rating.wine.varietalType.id.intern());
                if (null == currentScore) {
                    scoreByTypeId.put(rating.wine.varietalType.id.intern(), rating.score);
                } else {
                    scoreByTypeId.put(rating.wine.varietalType.id.intern(), currentScore + rating.score);
                }
            }

            final String joinedLabels = concatStringsWSep(scoreByTypeId.keySet(), "|");

            List<String> values = new LinkedList<String>();
            for(Map.Entry<String, Integer> entry : scoreByTypeId.entrySet() ) {
                values.add(Integer.toString(entry.getValue()));
            }
            final String joinedSumes = concatStringsWSep(values, ",");


            url =  "https://chart.googleapis.com/chart?cht=p&chs=500x250&chd=t:"
                    //+ "10,10,30,50"
                    + joinedSumes
                    + "&chdl="
                    //+ "Red|White|Dessert|Sparkling"
                    + joinedLabels
                    + "&chco=87042f|e8e8be|8b0369|d7d4d4";

            Log.i(TAG, "Made chart URL: " + url);

        }

        getActionBar().setLogo(null);
        getActionBar().setIcon(null);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);


        final ImageView chartImage = (ImageView) findViewById(R.id.chartImage);

        Picasso
                .with(ResultsActivity.this)
                .load(url)
                .fit().centerInside()
                .noFade()
                .into(chartImage);

    }

    public static String concatStringsWSep(Iterable<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }
        return sb.toString();
    }

}
