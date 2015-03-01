package club.spiritsapp.model;

import com.google.gson.Gson;

public class Wine {

	public String id;
	public String name;
	public Varietal varietal;
    public VarietalType varietalType;

	public Wine() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
