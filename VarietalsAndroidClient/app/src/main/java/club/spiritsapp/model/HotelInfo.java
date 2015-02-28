package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by lnanek on 2/28/15.
 */
public class HotelInfo {

    public String HotelID;
    public String Name;
    public HotelLocation Location;
    public String Description;
    public String StatusCode;
    public Price Price;
    public String DetailsUrl;
    public String StarRating;
    public String ThumbnailUrl;
    public String GuestRating;
    public String GuestReviewCount;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
