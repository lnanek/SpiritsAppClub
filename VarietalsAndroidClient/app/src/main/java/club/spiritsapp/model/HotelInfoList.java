package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by lnanek on 2/28/15.
 */
public class HotelInfoList {

    public List<HotelInfo> HotelInfo;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
