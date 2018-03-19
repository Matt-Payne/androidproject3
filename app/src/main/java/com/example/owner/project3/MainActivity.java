package com.example.owner.project3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    JSONArray jsonArray;
    int numberentries = -1;
    int currententry = -1;
    TextView tvUse;
    private final static int SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING = 2;
    // eventually have to make MYURL the URL in the XML
    private static final String MYURL = "http://www.pcs.cnu.edu/~kperkins/pets/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUse = (TextView)findViewById(R.id.tvUse);
        ConnectivityCheck myCheck = new ConnectivityCheck(this);
        if (myCheck.isNetworkReachable() || myCheck.isWifiReachable()) {

            //A common async task
            DownloadTask_KP myTask = new DownloadTask_KP(this);

            myTask.setnameValuePair("Name1","Value1");
            myTask.setnameValuePair("Name2","Value2");

            // //////////////////////////////////////////////////// demo this
            // telescoping initilization pattern
            //myTask.setnameValuePair("screen_name", "maddow").setnameValuePair("day", "today");
            // myTask.execute(TWITTER_RACHEL);

            myTask.execute(MYURL);
        }

    }
    public void processJSON(String string) {
        try {
            JSONObject jsonobject = new JSONObject(string);

            //*********************************
            //makes JSON indented, easier to read
            //Log.d(TAG,jsonobject.toString(SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING));
            tvUse.setText(jsonobject.toString(SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING));

            // you must know what the data format is, a bit brittle
            jsonArray = jsonobject.getJSONArray("people");

            // how many entries
            numberentries = jsonArray.length();

            currententry = 0;
            setJSONUI(currententry); // parse out object currententry

            //Log.i(TAG, "Number of entries " + numberentries);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * @param i
     *            find the object i in the member var jsonArray get the
     *            firstname and lastname and set the appropriate UI elements
     */
    private void setJSONUI(int i) {
        if (jsonArray == null) {
            //Log.e(TAG, "tried to dereference null jsonArray");
            return;
        }

        // gotta wrap JSON in try catches cause it throws an exception if you
        // try to
        // get a value that does not exist
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //tvfirstname.setText(jsonObject.getString("firstname"));
            //tvlastname.setText(jsonObject.getString("lastname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //setButtons();
    }
}
