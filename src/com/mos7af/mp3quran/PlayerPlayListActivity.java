package com.mos7af.mp3quran;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class PlayerPlayListActivity extends ListActivity 
{
	public ArrayList<HashMap<String, String>> playerSurasList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_player_playlist);

		SuraslistManager plm = SuraslistManager.getInstance();
		this.playerSurasList = plm.getPlayList();
		
		PlayerPlaylistItemAdapter	playerPlaylistItemAdapter = new PlayerPlaylistItemAdapter(this );
		playerPlaylistItemAdapter.SetData(playerSurasList);
		setListAdapter(playerPlaylistItemAdapter);

	
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						PlayerActivity.class);
				in.putExtra("songIndex", position);
				setResult(100, in);
				// Closing PlayListView
				finish();
			}
		});

	}
}
