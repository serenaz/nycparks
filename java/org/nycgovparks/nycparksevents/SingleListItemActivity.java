package org.nycgovparks.nycparksevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.ArrayList;

import org.nycgovparks.nycparksevents.R;


public class SingleListItemActivity extends FragmentActivity implements OnMapReadyCallback {

    String title;
    ArrayList<LatLng> ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list_item);
        TextView textTitle = (TextView) findViewById(R.id.title);
        TextView textDescription = (TextView) findViewById(R.id.description);
        Intent in = getIntent();
        title = in.getStringExtra("title");
        textTitle.setText(title);
        textDescription.setText(Html.fromHtml(in.getStringExtra("description")));
        String [] coordinates = in.getStringArrayExtra("coordinates");
        String [] latlng;
        ll = new ArrayList<LatLng>();
        for (int i = 0; i < coordinates.length; i++) {
            latlng = coordinates[i].split(",");
            ll.add(new LatLng (Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.itemmap);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        for (LatLng l: ll) {
            map.addMarker(new MarkerOptions().position(l));
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll.get(0), 15));
        if (ll.size() > 1) map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll.get(0), 10));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_list_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
