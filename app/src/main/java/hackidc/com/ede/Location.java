package hackidc.com.ede;

import org.json.JSONArray;

/**
 * Created by user on 4/7/2015.
 */
public class Location {
    private double longitude;
    private double latitude;

    public Location(double lng, double lat){
        longitude = lng;
        latitude = lat;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Location(JSONArray obj){
        try{
            longitude = obj.getDouble(0);
            latitude = obj.getDouble(1);
        }
        catch(Exception ex){

        }
    }

    public JSONArray getJSONArray(){
        JSONArray jsonArray = new JSONArray();
        try{
            jsonArray.put(0,this.longitude);
            jsonArray.put(1,this.latitude);
        }
        catch(Exception ex){

        }

        return jsonArray;
    }
}
