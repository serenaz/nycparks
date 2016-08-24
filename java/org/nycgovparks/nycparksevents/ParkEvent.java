package org.nycgovparks.nycparksevents;

import org.nycgovparks.nycparksevents.TwoTextArrayAdapter.RowType;

import android.os.Parcelable;
import android.os.Parcel;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Serena on 11/19/15.
 */
public class ParkEvent implements Item, Parcelable {

    public final String title;
    public final String description;
    public final String[] parks;
    public final String startdate;
    public final String enddate;
    public final String starttime;
    public final String endtime;
    public final String[] categories;
    public final String location;
    public final String[] coordinates;
    DateFormat fromFormatter = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat toFormatter = new SimpleDateFormat("EEEE MMMM dd, yyyy");

    public ParkEvent(String title, String description, String[] parks, String startdate, String enddate,
                 String starttime, String endtime, String location, String categories[], String coordinates[]) {
        this.title = title;
        this.description = description;
        this.parks = parks;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.location = location;
        this.categories = categories;
        this.coordinates = coordinates;
    }

    // returns start date in format "Weekday, Month Day, Year" (Thursday, November 26, 2015)
    public String toDate() {
        Date peDate = null;
        try {
            peDate = fromFormatter.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toFormatter.format(peDate);
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    // returns view in main events list
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.simple_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView textTitle = (TextView) view.findViewById(R.id.title);
        TextView textLocation = (TextView) view.findViewById(R.id.location);
        TextView textTime = (TextView) view.findViewById(R.id.time);
        textTitle.setText(title);
        textLocation.setText(location);
        textTime.setText(starttime + "-" + endtime);

        return view;
    }

    public int describeContents() {
        return 0;
    }

    private ParkEvent (Parcel in) {

        title = in.readString();
        description = in.readString();
        parks = in.createStringArray();
        startdate = in.readString();
        enddate = in.readString();
        starttime = in.readString();
        endtime = in.readString();
        location = in.readString();
        categories = in.createStringArray();
        coordinates = in.createStringArray();

    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeStringArray(parks);
        out.writeString(startdate);
        out.writeString(enddate);
        out.writeString(starttime);
        out.writeString(endtime);
        out.writeString(location);
        out.writeStringArray(categories);
        out.writeStringArray(coordinates);
    }

    public static final Parcelable.Creator<ParkEvent> CREATOR = new Parcelable.Creator<ParkEvent>() {
        public ParkEvent createFromParcel(Parcel in) {
            return new ParkEvent(in);
        }

        public ParkEvent[] newArray(int size) {
            return new ParkEvent[size];
        }
    };


}
