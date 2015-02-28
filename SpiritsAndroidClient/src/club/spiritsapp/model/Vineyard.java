package club.spiritsapp.model;

import com.google.gson.Gson;

public class Vineyard {

	public String id;
	public String name;
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
	
    //types: 'array',
    //varietals: 'array',
    //ava: 'array',
    //social: 'json', // { facebook: '', twitter: '' }
    //photos: 'array', // array of json objects { caption: '', url: '' }
    //address: 'json', // { street: '', city: '', stateOrProvince: '', postalCode: '', country: '' }
    //location: 'json',
    //owners: 'array' // array of user identifiers	
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
