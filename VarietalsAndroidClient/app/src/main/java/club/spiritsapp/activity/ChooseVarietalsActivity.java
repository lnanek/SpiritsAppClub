package club.spiritsapp.activity;

import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import club.spiritsapp.model.VarietalType;
import club.spiritsapp.network.GsonRequest;
import club.spiritsapp.R;
import club.spiritsapp.VarietalsApp;
import club.spiritsapp.model.Varietal;
import club.spiritsapp.model.VarietalsResponse;
import club.spiritsapp.network.NetworkConstants;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ChooseVarietalsActivity extends TintedStatusBarActivity {

	private static final String TAG = ChooseVarietalsActivity.class.getSimpleName();

	private final Gson gson = new Gson();

    private static final String URL = NetworkConstants.SERVER +
            "lookup/varietals";

	private ViewGroup typesContainer;

	// Instantiate the RequestQueue.
	RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		queue = Volley.newRequestQueue(this);
		
		setContentView(R.layout.activity_choose);

        getActionBar().setLogo(R.drawable.transparent);
        getActionBar().setIcon(R.drawable.transparent);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Api21Elevation.set(getActionBar(), 0f);
        }

		findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {


                final Set<String> currentTypes = VarietalsApp.instance.prefs.getChosenTypes();
                   if ( currentTypes.isEmpty() ) {
                       skip();
                       return;
                   }


                final Intent intent = new Intent(ChooseVarietalsActivity.this, VineyardsListActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		typesContainer = (ViewGroup) findViewById(R.id.typesContainer);
		
		final TextView prompt = (TextView) findViewById(R.id.prompt);
		prompt.setText(R.string.choose_varietals_prompt);

        if ( null == savedInstanceState ) {
            VarietalsApp.instance.prefs.setChosenVarietals(new HashSet<String>());
        }

		requestTypes();

	}

	private void requestTypes() {
		
		final ProgressDialog progress = new ProgressDialog(this);
		progress.show();
		
		GsonRequest<VarietalsResponse> typesRequest = new GsonRequest<VarietalsResponse>(
				Method.GET, URL, VarietalsResponse.class, null,
				new Response.Listener<VarietalsResponse>() {
					@Override
					public void onResponse(VarietalsResponse response) {
						progress.dismiss();
						displayVarietals(response);

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
		queue.add(typesRequest);

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

    private VarietalsResponse lastResponse;

	private void displayVarietals(VarietalsResponse varietals) {

        lastResponse = varietals;

		final LayoutInflater inflator = getLayoutInflater();

		final Set<String> currentTypes = VarietalsApp.instance.prefs.getChosenTypes();
		Log.i(TAG, "Users chosen types: " + currentTypes);
		
		for (final Varietal varietal : varietals.varietals) {
			
			if (!currentTypes.contains(varietal.classification)) {
				Log.i(TAG, "Hiding varietal due to not a user selected type: " + varietal);
				continue;
			}

			final CheckBox checkbox = (CheckBox) inflator.inflate(
					R.layout.activity_choose_item, typesContainer, false);
			checkbox.setText(varietal.name);

            checkbox.setChecked(currentTypes.contains(varietal.id));


            checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					final Set<String> currentVarietals = VarietalsApp.instance.prefs.getChosenVarietals();
										
					if (isChecked) {
						currentVarietals.add(varietal.id);
					} else {
						currentVarietals.remove(varietal.id);
					}
					
					VarietalsApp.instance.prefs.setChosenVarietals(currentVarietals);
					
				}
			});			
			
			
			typesContainer.addView(checkbox);

		}

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
            skip();
		}
		
		return super.onOptionsItemSelected(item);
	}


    private void skip() {
        final Set<String> currentTypes = VarietalsApp.instance.prefs.getChosenVarietals();

        for (final Varietal type : lastResponse.varietals) {

            currentTypes.add(type.id);

        }
        VarietalsApp.instance.prefs.setChosenVarietals(currentTypes);

        final Intent intent = new Intent(this, VineyardsListActivity.class);
        startActivity(intent);
    }

}
