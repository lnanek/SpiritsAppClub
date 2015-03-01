package club.spiritsapp.model;

import com.google.gson.Gson;

public class Rating {

	public Wine wine;
	public int score;
    public String comment;

	public Rating() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
