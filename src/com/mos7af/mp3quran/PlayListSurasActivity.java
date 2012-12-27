package com.mos7af.mp3quran;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PlayListSurasActivity extends ListActivity 
{
	public ArrayList<HashMap<String, String>> surasList = new ArrayList<HashMap<String, String>>();
	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	private final int MENU_ITEM_1_ACTION = 1;
	private final int MENU_ITEM_2_ACTION = 2;
	private DatabaseHandler db ;
	private int selectedIndex = 0;
	private ListView lv ;
	private PlayerPlaylistItemAdapter	playerPlaylistItemAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_player_playlist);
		db = new DatabaseHandler(getApplicationContext());
		playerPlaylistItemAdapter = new PlayerPlaylistItemAdapter(this );
		
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SuraslistManager suraslistManager=SuraslistManager.getInstance();
				suraslistManager.deletAllSuras();
				suraslistManager.SetSongs(surasList);
				Intent in = new Intent(getApplicationContext(),
						PlaylistActivity.class);
				in.putExtra("songIndex", position);
				setResult(200, in);
				finish();
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				selectedIndex = pos;
				showDialog(CONTEXT_MENU_ID);
				return false;
			}});
		
		  Resources res = getResources();
        
          iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
          iconContextMenu.addItem(res, "Play", R.drawable.ic_1, MENU_ITEM_1_ACTION);
          iconContextMenu.addItem(res, "Remove from playlist", R.drawable.ic_2, MENU_ITEM_2_ACTION);

          //set onclick listener for context menu
          iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
     			@Override
     			public void onClick(int menuId) {
     				
     				SuraslistManager suraslistManager=SuraslistManager.getInstance();
     				switch(menuId) 
     				{
	     				case MENU_ITEM_1_ACTION:
	     					
	     					suraslistManager.deletAllSuras();
	     					suraslistManager.SetSongs(surasList);
	     					MP3Quran.tabIndex =1;
	     					Intent in = new Intent(getApplicationContext(),
	     							PlaylistActivity.class);
	     					// Sending songIndex to PlayerActivity
	     					in.putExtra("songIndex", selectedIndex);
	     					setResult(200, in);
	     					// Closing PlayListView
	     					finish();
	     					
	     					break;
	     				case MENU_ITEM_2_ACTION:
	     					boolean result =  db.deletePlaylistSura(surasList.get(selectedIndex).get("playlistId"),surasList.get(selectedIndex).get("suraId"));
	     					if(result)
	     						Toast.makeText(getApplicationContext(), "1 Sura deleted from playlist!!",
	         							Toast.LENGTH_SHORT).show();
	     					ArrayList<HashMap<String, String>> _surasList= db.getPlaylistSuras(surasList.get(0).get("playlistId"));
	     					if(_surasList.isEmpty())
	     					{
	     						 in = new Intent(getApplicationContext(),
	         							PlaylistActivity.class);
	         					setResult(300, in);
	         					finish();
	     					}
	     					else
	     					{
	     						suraslistManager.SetSurasList(_surasList);
	     						UpdatePlayList();	
	     					}
	     				
	     					break;
     				
     				}
     			}});
          UpdatePlayList();
         
        
	}
	private void UpdatePlayList()
	{
		SuraslistManager suraslistManager = SuraslistManager.getInstance();
		this.surasList = suraslistManager.getPlayListSuras();
		playerPlaylistItemAdapter.SetData(surasList);
		setListAdapter(playerPlaylistItemAdapter);
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return iconContextMenu.createMenu("Sura");
		}
		
		return super.onCreateDialog(id);
	}
}
