package club.spiritsapp.model;

import com.google.gson.JsonObject;

/**
 * Created by lnanek on 2/28/15.
 */
public class HotelLocation {

    public String StreetAddress;
    public String City;
    public String Province;
    public String Country;
    public JsonObject GeoLocation;

    @Override
    public String toString() {
        String result = "";
        if (null != StreetAddress) {
            result += StreetAddress;
        }
        result = result.trim();


        if (null != City) {
            result += " " + City;
        }
        result = result.trim();
        if (null != Province) {
            result += " " + Province;
        }
        result = result.trim();
        if (null != Country) {
            result += " " + Country;
        }
        result = result.trim();

        return result;
    }

}
