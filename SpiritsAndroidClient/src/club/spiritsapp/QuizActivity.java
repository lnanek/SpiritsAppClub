package club.spiritsapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class QuizActivity extends Activity {
	
	private static final String TAG = QuizActivity.class.getSimpleName();

    private final Gson gson = new Gson();
	
	private static final String TYPES_URL = "http://varietals-server.cfapps.io/lookup/types";
	/*
/appelations
/countries
/states
/regions
/types
/varietals
	 */

	private ViewGroup typesContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_quiz);
		
		getActionBar().setTitle("");
		
		typesContainer = (ViewGroup) findViewById(R.id.typesContainer);

		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);
				
		// Request a string response from the provided URL.
		GsonRequest<TypesResponse> stringRequest = new GsonRequest<TypesResponse>(TYPES_URL, TypesResponse.class, null, 
		            new Response.Listener<TypesResponse>() {
		    @Override
		    public void onResponse(TypesResponse response) {
		        // Display the first 500 characters of the response string.
		        //mTextView.setText("Response is: "+ response);
		        
		        //mTextView.setText("Response is: "+ response);
		    	
		    	displayTypes(response);
		        
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	Log.e(TAG, "Error downloading types: ", error);
		        //mTextView.setText("That didn't work!");
		    }
		});
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
		
	}
	
	private void displayTypes(TypesResponse types) {
		
		final LayoutInflater inflator = getLayoutInflater();
		
		for(final VarietalType type : types.types) {
			
			final CheckBox checkbox = (CheckBox) inflator.inflate(R.layout.activity_quiz_type, typesContainer, false);
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
