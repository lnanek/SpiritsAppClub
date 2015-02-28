package club.spiritsapp;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuizActivity extends Activity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_quiz);
		
		final TextView mTextView = (TextView) findViewById(R.id.prompt);

		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);
				
		// Request a string response from the provided URL.
		GsonRequest<TypesResponse> stringRequest = new GsonRequest<TypesResponse>(TYPES_URL, TypesResponse.class, null, 
		            new Response.Listener<TypesResponse>() {
		    @Override
		    public void onResponse(TypesResponse response) {
		        // Display the first 500 characters of the response string.
		        //mTextView.setText("Response is: "+ response);
		        
		        mTextView.setText("Response is: "+ response);
		        
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		        mTextView.setText("That didn't work!");
		    }
		});
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
		
	}

}
