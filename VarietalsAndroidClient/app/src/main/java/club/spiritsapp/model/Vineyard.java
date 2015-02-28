package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.LinkedHashSet;
import java.util.List;

public class Vineyard {

	public String id;
	public String name;
	public String url;
    public String website;
	public String logo;
	public String region;
	public String phone;
	public String email;
	public String status;
	public String hours;
    public int rating;
    public int likes;
    public int dislikes;
	public String description;
    public boolean visible;
    public Social social;
    public Address address;
    public List<String> types;
    public List<String> varietals;
    public List<String> appelations;
    public JsonArray owners;
    public JsonObject location;
    public JsonArray ava;
    public List<Photo> photos;
    public LinkedHashSet<Integer> samplePhotoResourceIds = new LinkedHashSet<>();

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
