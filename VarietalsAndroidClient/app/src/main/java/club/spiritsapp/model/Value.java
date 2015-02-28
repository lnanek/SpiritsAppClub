package club.spiritsapp.model;

/**
 * Created by lnanek on 2/28/15.
 */
public class Value {

    public String Value;
    public String Currency;

    @Override
    public String toString() {
        String result = "";
        if (null != Value) {
            result += Value;
        }
        result = result.trim();


        result = result.trim();
        if (null != Currency) {
            result += " " + Currency;
        }
        result = result.trim();

        return result;
    }

}
