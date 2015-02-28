package club.spiritsapp.model;

import java.util.List;

import com.google.gson.Gson;

public class TypesResponse {
    
	public List<VarietalType> types;
	
	public TypesResponse() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
