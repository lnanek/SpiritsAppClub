package club.spiritsapp.model;

import com.google.gson.Gson;

public class Vineyard {

	public String id;
	public String name;
	public String address;
	public boolean appointmentRequired;

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
