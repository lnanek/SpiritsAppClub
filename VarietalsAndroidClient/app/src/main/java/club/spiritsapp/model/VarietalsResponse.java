package club.spiritsapp.model;

import java.util.List;

import com.google.gson.Gson;

public class VarietalsResponse {
    
	public List<Varietal> varietals;
	
	public VarietalsResponse() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
