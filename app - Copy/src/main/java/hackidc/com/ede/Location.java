package hackidc.com.ede;

import org.json.JSONArray;

/**
 * Created by user on 4/7/2015.
 */
public class Location {
    public double Longitude;
    public double Latitude;

    public Location(double lng,double lat){
        Longitude = lng;
        Latitude = lat;
    }

    public Location(JSONArray obj){
        try{
            Longitude = obj.getDouble(0);
            Latitude = obj.getDouble(1);
        }
        catch(Exception ex){

        }
    }
}
