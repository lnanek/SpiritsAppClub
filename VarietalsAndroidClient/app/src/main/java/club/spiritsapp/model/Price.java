package club.spiritsapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by lnanek on 2/28/15.
 */
public class Price {

    public Value BaseRate;
    public Value TaxRcAndFees;
    public Value TotalRate;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
