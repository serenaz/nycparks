package org.nycgovparks.nycparksevents;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Serena on 11/26/15.
 */
public interface Item {

    public int getViewType();
    public View getView (LayoutInflater inflater, View convertView);
}
