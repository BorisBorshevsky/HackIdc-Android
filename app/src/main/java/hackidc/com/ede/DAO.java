package hackidc.com.ede;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by user on 4/7/2015.
 */
public class DAO {
    private final String pathPrefix;
    private String ip;
    private int port;
    DefaultHttpClient httpclient = new DefaultHttpClient();


    public JSONArray getAllRequest() {
        try {
            Object response = makeGetRequest("getallrequests");
            return new JSONArray(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public JSONObject newRequest(JSONObject object, String param) {
        try {
            Object response = makePostRequest(param,object);
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public DAO() {
        //this.ip = "10.10.0.117";
        this.ip = "54.69.34.206";
        this.port = 3000;
        this.pathPrefix = "http://" + this.ip + ":" + this.port + "/";
    }

    public Object makePostRequest(String path, JSONObject params) throws Exception
    {
        String fullPath = pathPrefix + path;
        HttpPost httpost = new HttpPost(fullPath);
        StringEntity se = new StringEntity(params.toString());
        httpost.setEntity(se);
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        ResponseHandler responseHandler = new BasicResponseHandler();
        return httpclient.execute(httpost, responseHandler);
    }


    public Object makeGetRequest(String path) throws Exception
    {
        String fullPath = pathPrefix + path;
        HttpGet httpGet = new HttpGet(fullPath);

        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");

        ResponseHandler responseHandler = new BasicResponseHandler();

        Object response = httpclient.execute(httpGet, responseHandler);
        return response;
    }




}
