package club.spiritsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ChooseVarietalsActivity extends Activity {

	private static final String TAG = ChooseVarietalsActivity.class.getSimpleName();

	private final Gson gson = new Gson();

	private static final String URL = "http://varietals-server.cfapps.io/lookup/varietals";
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

		typesContainer = (ViewGroup) findViewById(R.id.typesContainer);
		
		final TextView prompt = (TextView) findViewById(R.id.prompt);
		prompt.setText(R.string.choose_varietals_prompt);

		requestTypes();

	}

	private void requestTypes() {
		GsonRequest<VarietalsResponse> typesRequest = new GsonRequest<VarietalsResponse>(
				URL, VarietalsResponse.class, null,
				new Response.Listener<VarietalsResponse>() {
					@Override
					public void onResponse(VarietalsResponse response) {
						displayTypes(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Error downloading types: ", error);
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

	private void displayTypes(VarietalsResponse types) {

		final LayoutInflater inflator = getLayoutInflater();

		for (final Varietal type : types.varietals) {

			final CheckBox checkbox = (CheckBox) inflator.inflate(
					R.layout.activity_choose_item, typesContainer, false);
			checkbox.setText(type.name);
			typesContainer.addView(checkbox);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.quiz, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
