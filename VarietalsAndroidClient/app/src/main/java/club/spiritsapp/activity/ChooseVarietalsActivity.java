package club.spiritsapp.activity;

import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.StateSet;
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

    private StateListDrawable getStateListDrawable(final int imageResourceId) {

        final Drawable imageDrawable = getResources().getDrawable(imageResourceId);

        final Drawable selectedDrawable = getResources().getDrawable(R.drawable.ic_wine_selected);

        final Drawable overlaidSelectedDrawable = getLayerListDrawable(imageDrawable, selectedDrawable);

        StateListDrawable stateListDrawable= new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, overlaidSelectedDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, imageDrawable);

        return stateListDrawable;
    }

    private Drawable getLayerListDrawable(final Drawable one, final Drawable two) {
        return new LayerDrawable(new Drawable[] {one, two});
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
        NetworkConstants.add(this, typesRequest);

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

            if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("cabernet")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_cabernet), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("cabernetsauvignon")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_cabernet), null, null, null);
            }  else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("chardonnay")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_chardonnay), null, null, null);
            }  else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("dessertwine")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_dessert_wine), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").startsWith("Gew")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_gewurztraminer), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("merlot")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_merlot), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("petitesirah")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_petitesirah), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").startsWith("PinotGrigio")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_pinotgrigio), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("pinotnoir")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_pinotnoir), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("redwine")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_red_wine), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("riesling")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_reisling), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("sparkingwine")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_sparking_wine), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").startsWith("Syrah")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_syrah), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("tempranillo")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_tempranillo), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("viognier")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_viognier), null, null, null);
            } else if (varietal.id.replaceAll(" ", "").equalsIgnoreCase("whitewine")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_white_wine), null, null, null);
            } else {
                continue;
            }

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
