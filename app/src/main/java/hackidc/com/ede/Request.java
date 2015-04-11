package hackidc.com.ede;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;


public class Request {
    private String deviceToken;
    private String description;
    private Location location;
    private String startTime;
    private String closeTime;
    private String category;
    private Integer amount;
    private String userName;

    public Request(String description, Location location, String startTime, String closeTime, String category, Integer amount, String token,String userName) {
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.closeTime = closeTime;
        this.category = category;
        this.amount = amount;
        this.deviceToken = token;
        this.userName = userName;
    }

    public JSONObject getJSON(){
        JSONObject obj= new JSONObject();
        try {
            obj.put("desc",this.getDescription());

            obj.put("longitude",this.location.getLongitude());
            obj.put("latitude",this.location.getLatitude());
            obj.put("startTime",this.getStartTime());
            obj.put("category",this.getCategory());
            obj.put("userName",this.getUserName());
            obj.put("deviceToken",this.deviceToken);
            obj.put("amount",this.getAmount());
        }
        catch(Exception ex){

        }
        return obj;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public Bitmap getBitmap(Resources res){
        Bitmap b;
        switch (Category.valueOf(getCategory().toUpperCase())){
            case CAR:
                b =  BitmapFactory.decodeResource(res, R.drawable.pointer_green);
                break;
            case CLOTHING:
                b = BitmapFactory.decodeResource(res, R.drawable.pointer_blue);
                break;
            case CASH:
                b = BitmapFactory.decodeResource(res, R.drawable.pointer_orange);
                break;
            default:
                b = BitmapFactory.decodeResource(res, R.drawable.pointer_default);
                break;
        }

        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth()/3,b.getHeight()/3, false);

        return bitmap;
    };

    public Request(JSONObject obj){
        try {
            description = obj.getString("desc");
            location = new Location(obj.getJSONArray("location"));
            //startTime = obj.getString("startTime");
//            closeTime = obj.getString("closeTime");
            category = obj.getString("category");
            //userName = obj.getString("firstName") + " " + obj.getString("lastName");
            amount = obj.getInt("amount");
            deviceToken = obj.getString("deviceToken");
            userName = obj.getString("userName");
        }
        catch(Exception ex){
//APA91bFdWVzltXmvoInEAb00YkhK6-SNojG0ySZMotez86FdiPc58UBAGD47RO2OJjXizaYV1H0Z5SH9KTfFz3EXi4bz7xju-eW7y0K4uxfaPZ5lADEPHvZofbwlkIgZd4r0uzijFfO-
        }

    }
}
