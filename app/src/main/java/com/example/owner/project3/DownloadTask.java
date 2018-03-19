package com.example.owner.project3;

/**
 *
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


/**
 * An async task to download small text files
 * First param is the URL to download
 * Third param will be the result of that download, or null if failure
 *
 * Has not been tested on binary data, or large files, a bit delicate
 */
public abstract class DownloadTask extends AsyncTask<String, Void, String> {
    private static final String     TAG = "DownloadTask";
    private static final int        TIMEOUT = 1000;    // 1 second
    private String                  myQuery = "";
    protected int                   statusCode = 0;
    protected String                myURL;

    /**
     * @param name
     * @param value
     * @return this allows you to build a safe URL with all spaces and illegal
     *         characters URLEncoded usage mytask.setnameValuePair("param1",
     *         "value1").setnameValuePair("param2",
     *         "value2").setnameValuePair("param3", "value3")....
     */
    public DownloadTask setnameValuePair(String name, String value) {
        try {
            if (name.length() != 0 && value.length() != 0) {

                // if 1st pair that include ? otherwise use the joiner char &
                if (myQuery.length() == 0)
                    myQuery += "?";
                else
                    myQuery += "&";

                myQuery += name + "=" + URLEncoder.encode(value, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    /**
     *
     * @param params  just the single url of the site to download from
     * @return null failed
     *         string the url contents
     */
    @Override
    protected String doInBackground(String... params) {
        // site we want to connect to
        myURL = params[0];

        try {
            URL url = new URL(myURL + myQuery);

            // this does no network IO
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // can further configure connection before getting data
            // cannot do this after connected
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            // wrap in finally so that stream bis is sure to close
            // and we disconnect the HttpURLConnection
            BufferedReader in = null;
            try {

                // this opens a connection, then sends GET & headers
                connection.connect();

                // lets see what we got make sure its one of
                // the 200 codes (there can be 100 of them
                // http_status / 100 != 2 does integer div any 200 code will = 2
                statusCode = connection.getResponseCode();
                if (statusCode / 100 != 2) {
                    Log.e(TAG, "Error-connection.getResponseCode returned "
                            + Integer.toString(statusCode));
                    return null;
                }

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8096);

                // the following buffer will grow as needed
                String myData;
                StringBuffer sb = new StringBuffer();

                while ((myData = in.readLine()) != null) {
                    sb.append(myData);
                }
                return sb.toString();

            } finally {
                // close resource no matter what exception occurs
                in.close();
                connection.disconnect();
            }
        } catch (Exception exc) {
            return null;
        }
    }

    /**
     *
     * @param result null if failure or text of the html page
     *               override this method in subclass to customize it to calling app
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    /**
     * not implemented above, once you start the download you are in it for the long haul
     * Not a good idea, what if its a giant file?
     * override this method in subclass to customize it to calling app
     */
    @Override
    protected void onCancelled() {
        //override to handle this
        super.onCancelled();
    }
};
