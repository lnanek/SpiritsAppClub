package club.spiritsapp;

import com.google.gson.Gson;

public class VarietalType {

	public String id;
	public String name;
	public String classification;
	
	public VarietalType() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
