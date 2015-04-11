package hackidc.com.ede;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.Random;

public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    public void Set(String key, String value){
        SharedPreferences prefs = getApplication().getSharedPreferences("hackidc.com.ede", Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }

    public String Get(String key){
        SharedPreferences prefs = getApplication().getSharedPreferences("hackidc.com.ede", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        if(Get("username")=="") {
            Random randomGenerator = new Random();
            Boolean isBoris = randomGenerator.nextBoolean();
            Set("userName", "Boris");
            //Set("userName", "Ofir");
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String registrationId;
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                try {
                    registrationId = gcm.register("204834830235");
                    return registrationId;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                Set("registrationId",msg);
            }
        }.execute(null, null, null);


//        DAO _rest = new DAO();
//        _rest.newRequest("addDevice", registrationId);

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                Intent mainIntent = new Intent(SplashActivity.this,MapsActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}