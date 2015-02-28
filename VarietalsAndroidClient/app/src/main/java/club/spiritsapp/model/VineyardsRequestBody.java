package club.spiritsapp.model;

import java.util.Set;

import com.google.gson.Gson;

public class VineyardsRequestBody {
    
	public Set<String> types;

	public Set<String> varietals;
	
	public VineyardsRequestBody() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
