package club.spiritsapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wearable.synchronizednotifications.common.Constants;
import com.google.gson.Gson;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import club.spiritsapp.R;
import club.spiritsapp.R.id;
import club.spiritsapp.R.layout;
import club.spiritsapp.model.Rating;
import club.spiritsapp.model.TastingSession;

public class ResultsActivity extends Activity {

    private static final String TAG = ResultsActivity.class.getSimpleName();

    // As per https://developers.google.com/chart/image/docs/gallery/pie_charts

    private String url =
            "https://chart.googleapis.com/chart?cht=p&chs=500x250&chd=t:10,10,30,50&chdl=Red|White|Dessert|Sparkling&chco=87042f|e8e8be|8b0369|d7d4d4";

    private TastingSession tasting;

    private TextView history;

    private TextView journal;

    private TextView wines;

    private TextView stats;

    private View journalContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(layout.activity_results);

        journalContent = findViewById(R.id.journalContent);
        journalContent.setVisibility(View.GONE);

        final View view = findViewById(R.id.statsPage);

        history = (TextView) findViewById(id.history);
        history.setVisibility(View.GONE);

        journal = (TextView) findViewById(id.journal);
        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journal.setTextColor(Color.parseColor("#c8c8c8"));
                wines.setTextColor(Color.WHITE);
                stats.setTextColor(Color.WHITE);
                history.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                journalContent.setVisibility(View.VISIBLE);
            }
        });
        wines = (TextView) findViewById(id.wines);
        wines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journal.setTextColor(Color.WHITE);
                wines.setTextColor(Color.parseColor("#c8c8c8"));
                stats.setTextColor(Color.WHITE);
                history.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                journalContent.setVisibility(View.GONE);
            }
        });
        stats = (TextView) findViewById(id.stats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journal.setTextColor(Color.WHITE);
                wines.setTextColor(Color.WHITE);
                stats.setTextColor(Color.parseColor("#c8c8c8"));
                history.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                journalContent.setVisibility(View.GONE);
            }
        });

        final ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);

        final TextView username = (TextView) findViewById(id.username);
        final TwitterSession session =
                Twitter.getSessionManager().getActiveSession();
        if ( null != session ) {
            username.setText("@" + session.getUserName());

            MyTwitterApiClient client = new MyTwitterApiClient(session);

            client.getCustomService().show(session.getUserId(), new Callback<User>() {
                @Override
                public void success(Result<User> userResult) {
                    Log.d(TAG, "userResult: " + userResult.data.profileImageUrl);
                    username.setText(userResult.data.name + "\n@" + session.getUserName());

                    Transformation transformation = new RoundedTransformationBuilder()
                            //.borderColor(Color.BLUE)
                            .borderWidthDp(0)
                            .cornerRadiusDp(15)
                            .oval(false)
                            .build();


                    Picasso
                            .with(ResultsActivity.this)
                            .load(userResult.data.profileImageUrl)
                            .fit().centerInside()
                            .transform(transformation)
                            .noFade()
                            .placeholder(R.drawable.sample_hotel)
                            .into(profilePicture);
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, "Error getting user", e);

                }
            });
        }

        getActionBar().setTitle("");
        getActionBar().setLogo(null);
        getActionBar().setIcon(null);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);


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

        displayPieChart();
        displayPieChart2();
        displayBarChart();
    }

    private void displayPieChart() {


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
                    scoreByTypeId.put(rating.wine.varietalType.id.intern(), 1);
                } else {
                    scoreByTypeId.put(rating.wine.varietalType.id.intern(), currentScore + 1);
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

            Log.i(TAG, "Made pie chart URL: " + url);

        }



        final ImageView chartImage = (ImageView) findViewById(R.id.chartImage);

        Picasso
                .with(ResultsActivity.this)
                .load(url)
                .fit().centerInside()
                .noFade()
                .into(chartImage);
    }

    private void displayPieChart2() {


        if ( null != tasting ) {

            final Map<String, Integer> scoreByTypeId = new LinkedHashMap<String, Integer>();
            for (final Rating rating : tasting.ratings) {

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

            Log.i(TAG, "Made pie chart URL: " + url);

        }



        final ImageView chartImage3 = (ImageView) findViewById(R.id.chartImage3);

        Picasso
                .with(ResultsActivity.this)
                .load(url)
                .fit().centerInside()
                .noFade()
                .into(chartImage3);
    }

    private void displayBarChart() {


        if ( null != tasting ) {

            final Map<String, Integer> scoreByTypeId = new LinkedHashMap<String, Integer>();
            for (final Rating rating : tasting.ratings) {

                final Integer currentScore = scoreByTypeId.get(rating.wine.varietal.id.intern());
                if (null == currentScore) {
                    scoreByTypeId.put(rating.wine.varietal.id.intern(), rating.score);
                } else {
                    scoreByTypeId.put(rating.wine.varietal.id.intern(), currentScore + rating.score);
                }
            }

            final String joinedLabels = concatStringsWSep(scoreByTypeId.keySet(), "|");

            List<String> values = new LinkedList<String>();
            for(Map.Entry<String, Integer> entry : scoreByTypeId.entrySet() ) {
                values.add(Integer.toString(entry.getValue()));
            }
            final String joinedSumes = concatStringsWSep(values, ",");


            final String url =  "https://chart.googleapis.com/chart?cht=bhs&chs=500x150&chd=t:"
                    //+ "10,10,30,50"
                    + joinedSumes
                    + "&chdl="
                    //+ "Red|White|Dessert|Sparkling"
                    + joinedLabels
                    + "&chco=c7bc6c";

            Log.i(TAG, "Made bar chart URL: " + url);

            final ImageView barChartImage = (ImageView) findViewById(R.id.barChartImage);

            Picasso
                    .with(ResultsActivity.this)
                    .load(url)
                    .fit().centerInside()
                    .noFade()
                    .into(barChartImage);

        }




    }

    public static String concatStringsWSep(Iterable<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s.replaceAll(" ", "%20"));
            sep = separator;
        }
        return sb.toString();
    }

}
