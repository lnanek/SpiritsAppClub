package club.spiritsapp.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.ViewOutlineProvider;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shamanland.fab.FloatingActionButton;

import java.util.HashSet;
import java.util.Set;

import club.spiritsapp.R;
import club.spiritsapp.VarietalsApp;
import club.spiritsapp.model.TypesResponse;
import club.spiritsapp.model.VarietalType;
import club.spiritsapp.network.GsonRequest;
import club.spiritsapp.network.NetworkConstants;

public class ChooseTypesActivity extends TintedStatusBarActivity {

    private static final String TAG = ChooseTypesActivity.class.getSimpleName();

    private final Gson gson = new Gson();

    private static final String URL = NetworkConstants.SERVER +
            "lookup/types";

    private ViewGroup typesContainer;

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
                if (currentTypes.isEmpty()) {
                    skip();
                    return;
                }

                final Intent intent = new Intent(ChooseTypesActivity.this, ChooseVarietalsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        typesContainer = (ViewGroup) findViewById(R.id.typesContainer);

        if ( null == savedInstanceState ) {
            VarietalsApp.instance.prefs.setChosenTypes(new HashSet<String>());
        }

        requestTypes();

    }

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

    private TypesResponse lastResponse;

    private void displayTypes(TypesResponse types) {

        lastResponse = types;

        final Set<String> currentTypes = VarietalsApp.instance.prefs.getChosenTypes();


        final LayoutInflater inflator = getLayoutInflater();

        for (final VarietalType type : types.types) {

            final CheckBox checkbox = (CheckBox) inflator.inflate(
                    R.layout.activity_choose_item, typesContainer, false);
            checkbox.setText(type.name);

            checkbox.setChecked(currentTypes.contains(type.id));

            if (type.id.equals("White")) {
                checkbox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        getStateListDrawable(R.drawable.ic_white_wine), null, null, null);
            } else if (type.id.equals("Red")) {
                checkbox.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_red_wine_checkbox, 0, 0, 0);
            } else if (type.id.equals("Sparkling")) {
                checkbox.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_sparkling_wine_checkbox, 0, 0, 0);
            } else if (type.id.equals("Dessert")) {
                checkbox.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_dessert_wine_checkbox, 0, 0, 0);
            }


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

            skip();
        }

        return super.onOptionsItemSelected(item);
    }

    private void skip() {
        final Set<String> currentTypes = VarietalsApp.instance.prefs.getChosenTypes();

        for (final VarietalType type : lastResponse.types) {

            currentTypes.add(type.id);

        }
        VarietalsApp.instance.prefs.setChosenTypes(currentTypes);

        final Intent intent = new Intent(this, ChooseVarietalsActivity.class);
        startActivity(intent);
    }


}
