package hackidc.com.ede;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.Date;


public class RegisterTaskDialog extends Dialog {


    private EditText amountTextBox;

    public RegisterTaskDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_task);

        amountTextBox = (EditText) findViewById(R.id.amountTextBox);

        Button sendBtn = (Button) findViewById(R.id.ButtonSendFeedback);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    public String Get(String key){
        SharedPreferences prefs = getContext().getSharedPreferences("hackidc.com.ede", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }



    public void sendRequest() {

        EditText descTextBox = (EditText) findViewById(R.id.descriptionTextBox);



        Double lng = Double.parseDouble(Get("Long"));
        Double lat = Double.parseDouble(Get("Lat"));
        Location location = new Location(lng, lat);
        String category = Get("category");
        String startDate = String.valueOf(new Date().getTime());
        //String endDate = String.valueOf(new Date().getTime() + 60000);
        int amount = Integer.parseInt(amountTextBox.getText().toString());
        String desc = descTextBox.getText().toString();
        String android_id = Get("registrationId");
        String username = Get("userName");

        Request req = new Request(desc, location,startDate,null,category,amount, android_id,username);
        new createNewRequestAsyncTask(req.getJSON(),this).execute();
    }


    private class createNewRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {

        Dialog dialog;
        private JSONObject postDate;

        private createNewRequestAsyncTask(JSONObject postDate,Dialog dialog) {
            this.postDate = postDate;
            this.dialog = dialog;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            DAO restDao = new DAO();

            JSONObject obj = null;
            try {
                obj = (JSONObject)restDao.makePostRequest("newrequest",postDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            this.dialog.dismiss();
        }
    }
}
