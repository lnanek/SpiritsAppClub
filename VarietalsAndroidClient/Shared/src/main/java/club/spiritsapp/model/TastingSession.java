package club.spiritsapp.model;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

public class TastingSession {

	public List<Rating> ratings = new LinkedList<Rating>();

	public TastingSession() {
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
