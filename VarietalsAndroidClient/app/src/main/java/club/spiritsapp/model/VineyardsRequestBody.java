package club.spiritsapp.model;

import java.util.Set;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterSession;

public class VineyardsRequestBody {
    
	public Set<String> types;

	public Set<String> varietals;

    public TwitterSession twitter;
	
	public VineyardsRequestBody() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
