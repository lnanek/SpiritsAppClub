package club.spiritsapp.model;

import java.util.List;

import com.google.gson.Gson;

public class VineyardsResponse {
    
	public List<Vineyard> vineyards;
	
	public VineyardsResponse() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
