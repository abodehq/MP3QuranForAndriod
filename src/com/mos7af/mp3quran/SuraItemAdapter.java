package com.mos7af.mp3quran;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SuraItemAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public SuraItemAdapter(Activity _source) {
        activity = _source;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void SetData(ArrayList<HashMap<String, String>> _suras)
    {
    	data=_suras;
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
    	HashMap<String, String> sura = new HashMap<String, String>();
        sura = data.get(position);
        
    	View vi=convertView;
    	String localPath = Environment.getExternalStorageDirectory()+"/MP3Quran/"+sura.get("reciterId");
		File file = new File(localPath,sura.get("suraId")+ ".mp3" );
		if (!file.exists()) {
			vi = inflater.inflate(R.layout.ly_sura_item, null);
		
		}else
		{
			vi = inflater.inflate(R.layout.ly_sura_item_pin, null);
		}
        

        TextView suraName = (TextView)vi.findViewById(R.id.sura_name); 
        TextView reciterName = (TextView)vi.findViewById(R.id.reciter_name); 
        ImageView pin_icon = (ImageView)vi.findViewById(R.id.pin_icon); 
        
        suraName.setText(sura.get("suraNameAr")+" - "+sura.get("suraNameEn"));
        reciterName.setText(sura.get("reciterNameAr")+" - "+sura.get("reciterNameEn"));
        

		return vi;
    }
}