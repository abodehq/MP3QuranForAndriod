package com.mos7af.mp3quran;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerPlaylistItemAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> surasList;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public PlayerPlaylistItemAdapter(Activity _source) {
        activity = _source;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    public void SetData(ArrayList<HashMap<String, String>> _sura)
    {
    	surasList = _sura;
    }

    public int getCount() {
        return surasList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.ly_player_playlist_item, null);

        TextView title = (TextView)vi.findViewById(R.id.songTitle); 
        TextView artist = (TextView)vi.findViewById(R.id.artist); 
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.address_book_item_call); 
        
        HashMap<String, String> sura = new HashMap<String, String>();
        sura = surasList.get(position);
        
        title.setText(sura.get("suraNameAr")+" - "+sura.get("suraNameEn"));
        artist.setText(sura.get("reciterNameAr")+" - "+sura.get("reciterNameEn"));
        imageLoader.DisplayImage(sura.get("reciterImage"), thumb_image);
        
        return vi;
    }
}