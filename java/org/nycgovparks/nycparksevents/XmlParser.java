package org.nycgovparks.nycparksevents;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * This class parses XML feeds
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single item in the XML feed.
 */

/**
 * Created by Serena on 10/18/15.
 */
public class XmlParser {
    private static final String ns = null;

    // We don't use namespaces

    public ArrayList<ParkEvent> parse(InputStream in) throws XmlPullParserException, IOException {
        System.out.println("parse");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<ParkEvent> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<ParkEvent> items = new ArrayList<ParkEvent>();

        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd h:mm aa");


        parser.require(XmlPullParser.START_TAG, ns, "rss");
        int whileloop = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            whileloop++;
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the Item tag
            if (name.equals("item")) {
                ParkEvent pe = readItem(parser);
                Date peDate = null;
                try {
                    peDate = formatter.parse(pe.enddate + " " + pe.endtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (rightNow.getTime().before(peDate))
                    items.add(pe);
            }
            else if (name.equals("channel")) {
                continue;
            }
            else {
                skip(parser);
            }
        }
        System.out.println("end of readFeed() " + whileloop + " loops");
        return items;
    }


    // Parses the contents of an Item. If it encounters a relevant tag, hands them off
    // to their respective read methods for processing. Otherwise, skips the tag.
    private ParkEvent readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String[] parks = null;
        String startdate = null;
        String enddate = null;
        String starttime = null;
        String endtime = null;
        String location = null;
        String[] categories = null;
        String[] coordinates = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            }
            else if (name.equals("description")) {
                description = readDescription(parser);
            }
            else if (name.equals("event:parknames")) {
                parks = readParks(parser);
            }
            else if (name.equals("event:startdate")) {
                startdate = readStartDate(parser);
            }
            else if (name.equals("event:enddate")){
                enddate = readEndDate(parser);
            }
            else if (name.equals("event:starttime")) {
                starttime = readStartTime(parser);
            }
            else if (name.equals("event:endtime")){
                endtime = readEndTime(parser);
            }
            else if (name.equals("event:location")){
                location = readLocation(parser);
            }
            else if (name.equals("event:categories")){
                categories = readCategories(parser);
            }
            else if (name.equals("event:coordinates")){
                coordinates = readCoordinates(parser);
            }
            else {
                skip(parser);
            }
        }
        return new ParkEvent(title, description, parks, startdate, enddate, starttime, endtime, location, categories, coordinates );
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    // Processes parknames tags in the feed.
    private String[] readParks(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:parknames");
        String[] results = readText(parser).split(";");
        parser.require(XmlPullParser.END_TAG, ns, "event:parknames");
        return results;
    }

    // Processes startdate tag in the feed.
    private String readStartDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:startdate");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "event:startdate");
        return result;
    }

    // Processes enddate tag in the feed.
    private String readEndDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:enddate");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "event:enddate");
        return result;
    }

    // Processes starttime tag in the feed.
    private String readStartTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:starttime");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "event:starttime");
        return result;
    }

    // Processes endtime tag in the feed.
    private String readEndTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:endtime");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "event:endtime");
        return result;
    }

    // Processes location tags in the feed.
    private String readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:location");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "event:location");
        return result;
    }

    // Processes categories tags in the feed.
    private String[] readCategories(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:categories");
        String[] results = readText(parser).split(";");
        parser.require(XmlPullParser.END_TAG, ns, "event:categories");
        return results;
    }

    // Processes coordinates tags in the feed.
    private String[] readCoordinates(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "event:coordinates");
        String[] title = readText(parser).split(";");
        parser.require(XmlPullParser.END_TAG, ns, "event:coordinates");
        return title;
    }

    // extracts text values
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
