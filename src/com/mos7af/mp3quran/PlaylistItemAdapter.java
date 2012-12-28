package com.mos7af.mp3quran;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlaylistItemAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
 
    public PlaylistItemAdapter(Activity _source) {
        activity = _source;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void SetData(ArrayList<HashMap<String, String>> d)
    {
    	data=d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.ly_playlists_item, null);

        TextView playlistTextView = (TextView)vi.findViewById(R.id.address_book_item_name); 
        playlistTextView.setText(data.get(position).get("playlistName"));
        return vi;
    }
}