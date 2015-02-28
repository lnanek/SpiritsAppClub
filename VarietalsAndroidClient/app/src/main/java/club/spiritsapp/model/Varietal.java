package club.spiritsapp.model;

import com.google.gson.Gson;

public class Varietal {

	public String id;
	public String name;
	public String classification;
	
	public Varietal() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
