package club.spiritsapp.model;

import com.google.gson.Gson;

public class Address {

    public static final String NOT_FOUND_STRING = "Address not available";

    public static final String DEFAULT_COUNTRY = "USA";

	public String street;
    public String city;
    public String stateOrProvince;
    public String postalCode;
    public String country;

	public Address() {
	}

	@Override
	public String toString() {
		String result = "";
        if ( null != street ) {
            result += street;
        }
        result = result.trim();

        if (!NOT_FOUND_STRING.equals(street)) {

            if (null != city) {
                result += " " + city;
            }
            result = result.trim();
            if (null != stateOrProvince) {
                result += " " + stateOrProvince;
            }
            result = result.trim();
            if (null != postalCode) {
                result += " " + postalCode;
            }
            result = result.trim();

        }

        if ( null != country && !country.equals(DEFAULT_COUNTRY) ) {
            result += " " + country;
        }
        result = result.trim();

        return result;
	}
	
	

}
