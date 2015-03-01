package club.spiritsapp.model;

import com.google.gson.Gson;

public class Photo {

    public String id;
	public String caption;
	public String url;

	public Photo() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
