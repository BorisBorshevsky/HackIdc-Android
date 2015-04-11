package hackidc.com.ede;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;


public class Request {
    private String description;
    private Location location;
    private String startTime;
    private String closeTime;
    private String category;
    private Integer amount;

    public Request(String description, Location location, String startTime, String closeTime, String category, Integer amount) {
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.closeTime = closeTime;
        this.category = category;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    public Bitmap getBitmap(Resources res){
      if(getCategory().toLowerCase() == "car"){
          return BitmapFactory.decodeResource(res, R.drawable.getter);
      }else {
          return BitmapFactory.decodeResource(res, R.drawable.givver);
      }

    };

    public Request(JSONObject obj){
        try {
            description = obj.getString("desc");
            location = new Location(obj.getJSONArray("location"));
            startTime = obj.getString("startTime");
//            closeTime = obj.getString("closeTime");
            category = obj.getString("category");
            amount = obj.getInt("amount");
        }
        catch(Exception ex){

        }

    }
}
