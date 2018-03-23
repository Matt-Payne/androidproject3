package com.example.parsejson;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSONActivity extends AppCompatActivity {
	private static final String TAG = "ParseJSON";

	private static final String MYURL = "http://www.pcs.cnu.edu/~kperkins/pets/pets.json";
	private static final String PULLURL = "http://www.pcs.cnu.edu/~kperkins/pets/";
	public static final int MAX_LINES = 15;
	private static final int SPACES_TO_INDENT_FOR_EACH_LEVEL_OF_NESTING = 2;
	private Toolbar toolbar;
	private TextView tvRaw;
	private TextView tvfirstname;
	private TextView tvlastname;
	private Button bleft;
	private Button bright;
	JSONArray jsonArray;

	int numberentries = -1;
	int currententry = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// This is the simple circular progress bar which works in the window
		// title
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_parse_json);


		tvRaw = (TextView) findViewById(R.id.tvRaw);
		tvfirstname = (TextView) findViewById(R.id.tvfirstname);
		tvlastname = (TextView) findViewById(R.id.tvlastname);
		bleft = (Button) findViewById(R.id.bleft);
		bright = (Button) findViewById(R.id.bright);



		toolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// make sure the network is up before you attempt a connection
		// notify user of problem? Not very good, maybe wait a little while and
		// try later? remember make users life easier
		ConnectivityCheck myCheck = new ConnectivityCheck(this);
		if (myCheck.isNetworkReachable() || myCheck.isWifiReachable()) {
			
			//A common async task
			DownloadTask_KP myTask = new DownloadTask_KP(this);

			//myTask.setnameValuePair("Name1","Value1");
			myTask.setnameValuePair("name","file");



			myTask.execute(MYURL);
		} else {

		    // throw error
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
			//jsonArray = jsonobject.getJSONArray("people");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_parse_json, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {





        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent myIntent = new Intent(this, SettingsActivity.class);
                startActivity(myIntent);

                break;

            case R.id.pets_spinner:


                break;



            default:
                break;
        }
        return true;
    }

	public void doRefresh(View view) {
		ConnectivityCheck myCheck = new ConnectivityCheck(this);
		if (myCheck.isNetworkReachable())
			Toast.makeText(this,"Hurray The network works!",Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(this,"No Networking",Toast.LENGTH_SHORT).show();
	}
}
