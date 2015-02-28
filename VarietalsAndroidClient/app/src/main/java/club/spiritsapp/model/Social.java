package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Social {

	public String id;
	public String facebook;
	public String twitter;
    public String youtube;

    @SerializedName("winery blog")
    public String blog;

	public Social() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
