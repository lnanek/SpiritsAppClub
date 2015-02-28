package club.spiritsapp.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import club.spiritsapp.R;
import club.spiritsapp.VarietalsApp;
import club.spiritsapp.model.Vineyard;
import club.spiritsapp.model.VineyardsResponse;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class VineyardsActivity extends Activity {

	private static final String TAG = VineyardsActivity.class.getSimpleName();

	private final Gson gson = new Gson();

	private static final String URL = "http://varietals-server.cfapps.io/lookup/types";
	/*
	 * /appelations /countries /states /regions /types /varietals
	 */

	private ViewGroup vineyardsContainer;

	// Instantiate the RequestQueue.
	RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		queue = Volley.newRequestQueue(this);

		setContentView(R.layout.activity_vineyards);

		getActionBar().setTitle("");

		vineyardsContainer = (ViewGroup) findViewById(R.id.vineyardsContainer);

		VarietalsApp.instance.prefs.setChoseTypes(new HashSet<String>());

		requestVineyards();

	}

	private void requestVineyards() {

		final VineyardsResponse response = new VineyardsResponse();

		final List<Vineyard> vineyards = new LinkedList<Vineyard>();

		final Vineyard vineyard1 = new Vineyard();
		vineyard1.name = "Jamieson Ranch Vineyards";
		vineyard1.address = "1 Kirkland Ranch RD, Napa CA 94558";
		vineyards.add(vineyard1);
		
		final Vineyard vineyard2 = new Vineyard();
		vineyard2.name = "Acacia Vineyard";
		vineyard2.address = "2750 Las Amigas Road, Napa CA 94559";
		vineyards.add(vineyard2);		
		
		response.vineyards = vineyards;

		displayVineyards(response);
	}

	private void errorDialog() {
		new AlertDialog.Builder(this).setTitle("Error connecting")
				.setPositiveButton("Retry", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						requestVineyards();
					}
				}).setNegativeButton("Cancel", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				}).show();
	}

	private void displayVineyards(VineyardsResponse types) {

		final LayoutInflater inflator = getLayoutInflater();

		for (final Vineyard type : types.vineyards) {

			final ViewGroup vineyardContainer = (ViewGroup) inflator.inflate(
					R.layout.activity_vineyards_item, vineyardsContainer, false);
			
			final TextView vineyardNameView = (TextView) vineyardContainer.findViewById(R.id.name);
			vineyardNameView.setText(type.name);

			final TextView vineyardAddressView = (TextView) vineyardContainer.findViewById(R.id.address);
			vineyardAddressView.setText(type.address);
			
			vineyardsContainer.addView(vineyardContainer);

		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.vineyards, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
