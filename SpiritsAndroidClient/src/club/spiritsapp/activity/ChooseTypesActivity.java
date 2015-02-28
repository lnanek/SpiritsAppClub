package club.spiritsapp.activity;

import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import club.spiritsapp.GsonRequest;
import club.spiritsapp.R;
import club.spiritsapp.VarietalsApp;
import club.spiritsapp.model.TypesResponse;
import club.spiritsapp.model.VarietalType;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ChooseTypesActivity extends TintedStatusBarActivity {

	private static final String TAG = ChooseTypesActivity.class.getSimpleName();

	private final Gson gson = new Gson();

	private static final String URL = "http://varietals-server.cfapps.io/lookup/types";
	/*
	 * /appelations /countries /states /regions /types /varietals
	 */

	private ViewGroup typesContainer;

	// Instantiate the RequestQueue.
	RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		queue = Volley.newRequestQueue(this);
		
		setContentView(R.layout.activity_choose);

		getActionBar().setTitle("");

		findViewById(R.id.skipButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(ChooseTypesActivity.this, ChooseVarietalsActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		typesContainer = (ViewGroup) findViewById(R.id.typesContainer);

		VarietalsApp.instance.prefs.setChosenTypes(new HashSet<String>());
		
		requestTypes();

	}

	private void requestTypes() {
		
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(true);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.show();
		
		GsonRequest<TypesResponse> typesRequest = new GsonRequest<TypesResponse>(
				Method.GET, URL, TypesResponse.class, null,
				new Response.Listener<TypesResponse>() {
					@Override
					public void onResponse(TypesResponse response) {
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

	private void displayTypes(TypesResponse types) {

		final LayoutInflater inflator = getLayoutInflater();

		for (final VarietalType type : types.types) {

			final CheckBox checkbox = (CheckBox) inflator.inflate(
					R.layout.activity_choose_item, typesContainer, false);
			checkbox.setText(type.name);
			
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					final Set<String> currentTypes = VarietalsApp.instance.prefs.getChosenTypes();
										
					if (isChecked) {
						currentTypes.add(type.id);
					} else {
						currentTypes.remove(type.id);
					}
					
					VarietalsApp.instance.prefs.setChosenTypes(currentTypes);
					
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
			final Intent intent = new Intent(this, ChooseVarietalsActivity.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}

}
