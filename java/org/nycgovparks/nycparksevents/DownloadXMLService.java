package org.nycgovparks.nycparksevents;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DownloadXMLService extends IntentService {

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "http://www.nycgovparks.org/xml/events_300_rss.xml";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;

    // The user's current network preference setting.
    public static String sPref = null;

    ArrayList<ParkEvent> events = new ArrayList<ParkEvent>();
    ArrayList<String> errors = new ArrayList<String>();

    public DownloadXMLService() {

        super("DownloadXMLService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            // Gets the user's network preference settings
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            // Retrieves a string value for the preferences. The second parameter
            // is the default value to use if a preference value is not found.
            sPref = sharedPrefs.getString("listPref", "Wi-Fi");

            updateConnectedFlags();


            if (wifiConnected || mobileConnected) {
                try {
                    loadXmlFromNetwork(URL);
                } catch (IOException e) {
                    errors.add(getResources().getString(R.string.connection_error));
                } catch (XmlPullParserException e) {
                    errors.add(getResources().getString(R.string.xml_error));
                }
            } else {
                errors.add(getResources().getString(R.string.connection_error));
            }

            Intent i = new Intent();
            intent.putParcelableArrayListExtra("park_events",events);
            intent.putStringArrayListExtra("errors", errors);
            sendBroadcast(i);

        }

        stopSelf();
    }

    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }


    // Uploads XML from NYC Parks, parses it
    // creates adapter with date headers for listview
    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser xmlp = new XmlParser();
        try {
            stream = downloadUrl(urlString);
            events = xmlp.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null){
                stream.close();
            }
        }

    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }


}
