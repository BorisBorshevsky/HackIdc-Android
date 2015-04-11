package hackidc.com.ede;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    DAO restDao;
    final Context context = this;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();


    // Create the hash map on the beginning
    private HashMap<Marker, Request> requestMarkerMap;

    public void Set(String key, String value){
        SharedPreferences prefs = getApplication().getSharedPreferences("hackidc.com.ede", Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }

    public String Get(String key){
        SharedPreferences prefs = getApplication().getSharedPreferences("hackidc.com.ede", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        new getAllFromServer().execute();
                    }
                });
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        zoom(mMap);
        final Dialog dialogAfter = new RegisterTaskDialog(context);
        dialogAfter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAfter.setContentView(R.layout.activity_register_task);

        dialogAfter.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                new getAllFromServer().execute();
            }
        });

        Button showGridMenuButton = (Button) findViewById(R.id.showMenuButton);
        showGridMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popupmenu);

                ImageButton foodButton = (ImageButton) dialog.findViewById(R.id.foodButton);
                ImageButton clothingButton = (ImageButton) dialog.findViewById(R.id.clothesButton);
                ImageButton carButton = (ImageButton) dialog.findViewById(R.id.carButton);
                ImageButton exchangeButton = (ImageButton) dialog.findViewById(R.id.exchangeButton);
                ImageButton homeButton = (ImageButton) dialog.findViewById(R.id.homeButton);
                ImageButton offerButton = (ImageButton) dialog.findViewById(R.id.otherButton);

                // if button is clicked, close the custom dialog
                foodButton.setOnClickListener(new categoryClickListener(Category.FOOD, dialog, dialogAfter));
                clothingButton.setOnClickListener(new categoryClickListener(Category.CLOTHING, dialog, dialogAfter));
                carButton.setOnClickListener(new categoryClickListener(Category.CAR, dialog, dialogAfter));
                exchangeButton.setOnClickListener(new categoryClickListener(Category.CASH, dialog, dialogAfter));
                homeButton.setOnClickListener(new categoryClickListener(Category.HOME, dialog, dialogAfter));
                offerButton.setOnClickListener(new categoryClickListener(Category.OTHER, dialog, dialogAfter));

                dialog.show();
            }
        });
        startTimer();

    }

    class categoryClickListener implements View.OnClickListener{
        private final Category category;
        private final Dialog dialogAfter;
        private final Dialog dialog;

        categoryClickListener(Category category, Dialog dialog, Dialog dialogAfter) {
            this.category = category;
            this.dialog = dialog;
            this.dialogAfter = dialogAfter;
        }

        @Override
        public void onClick(View v) {
            Set("category", category.toString());
            dialog.dismiss();
            dialogAfter.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {

        super.onStart();

        requestMarkerMap = new HashMap<Marker, Request>();

    }



    private class getAllFromServer extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            restDao = new DAO();
            JSONArray array = restDao.getAllRequest();
            return array;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            populateMap(jsonArray);
        }
    }




    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                zoom(mMap);
                if(location != null) {
                    Set("Long", String.valueOf(location.getLongitude()));
                    Set("Lat", String.valueOf(location.getLatitude()));
                }
            }
        });



        Location location = mMap.getMyLocation();
        if (location != null) {
            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(newLatLng);
            markerOptions.title("YOU");

            mMap.addMarker(markerOptions);
        }

        new getAllFromServer().execute();
    }


    private void populateMap(JSONArray array) {

        try {
            //todo: test this as interval

            mMap.clear();
            for(int i=0 ; i<array.length() ; i++){
                Request req = new Request(array.getJSONObject(i));
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(req.getLocation().getLatitude(), req.getLocation().getLongitude()))
                        .title(req.getDescription())
                        .icon(BitmapDescriptorFactory.fromBitmap(req.getBitmap(getResources()))));

                requestMarkerMap.put(marker,req);
            }
            MarkerInfoWindowAdapter miwa = new MarkerInfoWindowAdapter();
            mMap.setInfoWindowAdapter(miwa);
            mMap.setOnInfoWindowClickListener(miwa);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void zoom(GoogleMap map){

        Location location = map.getMyLocation();
//        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 15));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            mMap.setOnMyLocationChangeListener(null);

        }


    }

    class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

        private final View myContentsView;
        TextView user;
        TextView title;
        TextView amount;
        Button btn;

        private HashMap <Marker, Request> map;

        MarkerInfoWindowAdapter(){
            map = requestMarkerMap;
            myContentsView = getLayoutInflater().inflate(R.layout.mapoverlay, null);
            user = (TextView) myContentsView.findViewById(R.id.mapoverlayusername);
            title = (TextView) myContentsView.findViewById(R.id.mapoverlaydescription);
            amount = (TextView) myContentsView.findViewById(R.id.mapoverlayamount);
            btn = (Button) myContentsView.findViewById(R.id.mapoverlaybutton);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if(map != null) {
                final Request req = map.get(marker);
                if (req != null) {
                    final TextView user = (TextView) myContentsView.findViewById(R.id.mapoverlayusername);
                    user.setText(req.getUserName());

                    final ImageView pic = (ImageView)myContentsView.findViewById(R.id.pic);
                    Resources resources = getResources();
                    if(req.getUserName()=="Boris"){

                        pic.setImageDrawable(resources.getDrawable(R.drawable.user1));
                    }
                    else if(req.getUserName()=="Michal"){

                        pic.setImageDrawable(resources.getDrawable(R.drawable.pic1));
                    }
                    else
                    {

                        pic.setImageDrawable(resources.getDrawable(R.drawable.user2));
                    }

                    final TextView title = (TextView) myContentsView.findViewById(R.id.mapoverlaydescription);
                    title.setText(req.getDescription());

                    final TextView amount = (TextView) myContentsView.findViewById(R.id.mapoverlayamount);
                    amount.setText("$" + String.valueOf(req.getAmount()));

                    final Button btn = (Button) myContentsView.findViewById(R.id.mapoverlaybutton);
                    btn.setText("Provide Now!");


                }
            }
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {

//            Request req = requestMarkerMap.get(marker);
//            if(req != null) {
//                TextView title = (TextView) myContentsView.findViewById(R.id.mapoverlaydescription);
//                title.setText(req.getDescription());
//
//                TextView amount = (TextView) myContentsView.findViewById(R.id.mapoverlayamount);
//                amount.setText(req.getAmount());
//
//                TextView btn = (TextView) myContentsView.findViewById(R.id.mapoverlaybutton);
//                amount.setText(req.getAmount());
//            }
//
//            return myContentsView;
            return null;
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            if(map != null) {
                final Request req = map.get(marker);
                new sendPushNotification(req.getDeviceToken(), Get("userName")).execute();
//                user.setVisibility(View.GONE);
//                title.setVisibility(View.GONE);
//                amount.setVisibility(View.GONE);
//                btn.setVisibility(View.GONE);
            }
        }
    }

    private class sendPushNotification extends AsyncTask<String, Void, JSONObject> {

        String token;
        String name;

        private sendPushNotification(String token,String name) {
            this.token = token;
            this.name = name;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            DAO restDao = new DAO();

            JSONObject obj = null;
            try {
                obj = (JSONObject)restDao.makeGetRequest("sendpushnotification?token="+token+"&name="+name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject response) {

        }
    }
}
