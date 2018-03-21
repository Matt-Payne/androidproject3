package com.example.parsejson;

/**
 * Created by keith on 3/6/18.
 */

public class DownloadTask_KP extends DownloadTask {
    ParseJSONActivity myActivity;


    DownloadTask_KP(ParseJSONActivity activity) {
        attach(activity);
    }

    @Override
    protected void onPostExecute(String result) {
        if (myActivity != null) {
            myActivity.processJSON(result);
        }
    }

    /**
     * important do not hold a reference so garbage collector can grab old
     * defunct dying activity
     */
    void detach() {
        myActivity = null;
    }

    /**
     * @param activity
     *            grab a reference to this activity, mindful of leaks
     */
    void attach(ParseJSONActivity activity) {
        this.myActivity = activity;
    }
}
