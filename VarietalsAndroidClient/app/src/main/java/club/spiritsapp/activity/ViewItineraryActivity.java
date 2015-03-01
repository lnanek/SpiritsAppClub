package club.spiritsapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.shamanland.fab.FloatingActionButton;

import club.spiritsapp.R;
import club.spiritsapp.datasync.WearableAppStarter;
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


        findViewById(R.id.callButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phone = "+7079448355";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });


        findViewById(R.id.sendAMessageButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "members@tuskestates.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I would like to attend!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


        findViewById(R.id.startEventButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ViewItineraryActivity.this);
                dialog.getWindow().setBackgroundDrawable(null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_begin_tasting);

                final View view = dialog.findViewById(R.id.container);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //final Intent intent = new Intent("club.spiritsapp.ACTION.openWearActivity");
                        //Log.i(TAG, "Starting wearable intent: " + intent);
                        //startActivity(intent);
                        //dialog.dismiss();

                        new WearableAppStarter().connectAndSend(ViewItineraryActivity.this);

                    }
                });

                dialog.show();
            }
        });



        FloatingActionButton shareButton = (FloatingActionButton) findViewById(R.id.shareButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I'm going to the " + vineyard + " Vineyard!");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Api21Outliner.setOutline(this, shareButton);
        }
	}

    @TargetApi(21)
    public static class Api21Outliner {

        public static void setOutline(final Context context, final FloatingActionButton shareButton) {

            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    // Or read size directly from the view's width/height
                    int size = context.getResources().getDimensionPixelSize(R.dimen.diameter);
                    outline.setOval(0, 0, size, size);
                }
            };
            shareButton.setOutlineProvider(viewOutlineProvider);
        }
    }

}
