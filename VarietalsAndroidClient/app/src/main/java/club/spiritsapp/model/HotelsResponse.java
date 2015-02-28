package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by lnanek on 2/28/15.
 */
public class HotelsResponse {

    public int MatchingHotelCount;
    public int HotelCount;
    public JsonObject StayDates;
    public HotelInfoList HotelInfoList;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
