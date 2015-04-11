package hackidc.com.ede;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user on 4/7/2015.
 */
public interface IDataAccess {

    public JSONArray getAllRequest();
    public JSONObject newRequest(JSONObject object);

}
