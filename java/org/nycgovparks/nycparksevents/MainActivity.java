package org.nycgovparks.nycparksevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<ParkEvent> events;

    ArrayAdapter adapter;

    ListView myListView;


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            events = intent.getParcelableArrayListExtra("park_events");
            ArrayList<String> errors = intent.getStringArrayListExtra("errors");
            if (events.size() > 0) {
                List<Item> items = new ArrayList<Item>();
                String day = "";

                for (ParkEvent pe: events) {
                    if (!day.equals(pe.startdate)) {
                        day = pe.startdate;
                        items.add(new Header(pe.toDate()));
                    }
                    items.add(pe);

                }

                adapter = new TwoTextArrayAdapter(getApplicationContext(), items);

                setContentView(R.layout.main);
                myListView = (ListView) findViewById(R.id.listview);
                myListView.setAdapter(adapter);

                //Intent intent = new Intent(getApplicationContext(), LocationService.class);
                //startService(intent);

                Intent i = new Intent(getApplicationContext(), GeofenceManagerService.class);
                i.putParcelableArrayListExtra("park_events", events);
                startService(i);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // getting ParkEvent from selected ListItem
                        ParkEvent pe = (ParkEvent) myListView.getItemAtPosition(position);

                        // Starting new intent
                        Intent in = new Intent(getApplicationContext(), SingleListItemActivity.class);
                        in.putExtra("title", pe.title);
                        in.putExtra("description", pe.description);
                        in.putExtra("coordinates", pe.coordinates);
                        startActivity(in);
                    }
                });

            } else {

                adapter = new ArrayAdapter(getApplicationContext(), 0,errors);

                setContentView(R.layout.main);
                myListView = (ListView) findViewById(R.id.listview);
                myListView.setAdapter(adapter);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent i = new Intent(this, DownloadXMLService.class);
        startService(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
