package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wine {

    public String name;

    public int vintage;

    public String varietal;

    public String ava;

    @SerializedName("type")
    public String varietalType;

    public List<Photo> photos;

    public String description;

    public String winery;

    public String id;

	public Wine() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
