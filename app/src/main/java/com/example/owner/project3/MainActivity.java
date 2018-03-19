package com.example.owner.project3;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Toast;

import com.example.owner.project3.ConnectivityCheck;
import com.example.owner.project3.DownloadTask_KP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final String TAG = "ParseJSON";
    private static final String MYURL = "http://www.pcs.cnu.edu/~kperkins/pets/pets.json";

    public static final int MAX_LINES = 15;
    private static final int SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING = 2;

    private TextView tvRaw;
    private TextView tvfirstname;
    private TextView tvlastname;
    private Button bleft;
    private Button bright;
    private ImageView petPic;
    JSONArray jsonArray;

    int numberentries = -1;
    int currententry = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is the simple circular progress bar which works in the window
        // title
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        tvRaw = (TextView) findViewById(R.id.tvRaw);
        tvfirstname = (TextView) findViewById(R.id.tvfirstname);
        tvlastname = (TextView) findViewById(R.id.tvlastname);
        bleft = (Button) findViewById(R.id.bleft);
        bright = (Button) findViewById(R.id.bright);

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // make sure the network is up before you attempt a connection
        // notify user of problem? Not very good, maybe wait a little while and
        // try later? remember make users life easier
        ConnectivityCheck myCheck = new ConnectivityCheck(this);
        if (myCheck.isNetworkReachable()) {

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

    public void setText(String string) {
        setProgressBarIndeterminateVisibility(false);
        tvRaw.setText(string);

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // add scrolling to the textbox
        // first restrict lines to number visible (full screen in for this case
        // tvRaw.setMaxLines(tvRaw.getLineCount()))
        tvRaw.setMaxLines(MAX_LINES);
        tvRaw.setMovementMethod(new ScrollingMovementMethod());
    }

    public void processJSON(String string) {
        try {
            JSONObject jsonobject = new JSONObject(string);

            //*********************************
            //makes JSON indented, easier to read
            Log.d(TAG,jsonobject.toString(SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING));
            tvRaw.setText(jsonobject.toString(SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING));

            // you must know what the data format is, a bit brittle
            jsonArray = jsonobject.getJSONArray("pets");

            // how many entries
            numberentries = jsonArray.length();

            currententry = 0;
            setJSONUI(currententry); // parse out object currententry

            Log.i(TAG, "Number of entries " + numberentries);

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
            Log.e(TAG, "tried to dereference null jsonArray");
            return;
        }

        // gotta wrap JSON in try catches cause it throws an exception if you
        // try to
        // get a value that does not exist
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            tvfirstname.setText(jsonObject.getString("name"));
            tvlastname.setText(jsonObject.getString("file"));
            //petPic.setImageResource(jsonObject.getInt("file"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setButtons();
    }

    private void setButtons() {
        // make sure that appropriate buttons enabled only
        bleft.setEnabled(numberentries != -1 && currententry != 0);
        bright.setEnabled(numberentries != -1
                && currententry != numberentries - 1);
    }

    public void doLeft(View v) {
        if (numberentries != -1 && currententry != 0) {
            currententry--;
            setJSONUI(currententry);
        }
    }

    public void doRight(View v) {
        if (numberentries != -1 && currententry != numberentries) {
            currententry++;
            setJSONUI(currententry);
        }
    }



//	public void doRefresh(View view) {
//		ConnectivityCheck myCheck = new ConnectivityCheck(this);
//		if (myCheck.isNetworkReachable())
//			Toast.makeText(this,"Hurray The network works!",Toast.LENGTH_SHORT).show();
//		else
//			Toast.makeText(this,"No Networking",Toast.LENGTH_SHORT).show();
//	}
}
