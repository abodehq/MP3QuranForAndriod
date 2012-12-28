package com.mos7af.mp3quran;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MP3Quran extends TabActivity 
{
	
	public static int tabIndex = 0;
	private TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_mp3quran);
        if(Utils.isConnectingToInternet(MP3Quran.this))
        {
        	AddAppTabs();
        }else
        {
        	showAlertDialog(this, "No Internet Connection",
					"You don't have internet connection.", false);
        }
    }
    public void AddAppTabs()
    {
    	tabHost = getTabHost();
    	tabHost.setOnTabChangedListener(new OnTabChangeListener() 
    	{
			public void onTabChanged(String tabId)
			{
				
				int tabIndex = tabHost.getCurrentTab();
				if(tabIndex==2 && SuraslistManager.reciterId == null)
				{
					 tabHost.setCurrentTab(0);
					 Toast.makeText(MP3Quran.this,"You need to select a reciter!!!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        // Tab for reciters
        TabSpec recitersSpec = tabHost.newTabSpec("reciters");
        recitersSpec.setIndicator("reciters", getResources().getDrawable(R.drawable.icon_reciters_tab));
        Intent recitersIntent = new Intent(this, RecitersActivity.class);
        recitersSpec.setContent(recitersIntent);
        
        // Tab for player
        TabSpec playerSpec = tabHost.newTabSpec("player");
        playerSpec.setIndicator("player", getResources().getDrawable(R.drawable.icon_player_tab));
        Intent  playerIntent = new Intent(this, PlayerActivity.class);
        playerSpec.setContent(playerIntent);
      
        // Tab for suras
        TabSpec surasSpec = tabHost.newTabSpec("suras");
        surasSpec.setIndicator("suras", getResources().getDrawable(R.drawable.icon_suras_tab));
        Intent  surasIntent = new Intent(this, SurasActivity.class);
        surasSpec.setContent(surasIntent);
        
        // Tab for playlists
        TabSpec playlistsSpec = tabHost.newTabSpec("playlists");
        playlistsSpec.setIndicator("playlists", getResources().getDrawable(R.drawable.icon_playlists_tab));
        Intent playlistIntent = new Intent(this, PlaylistActivity.class);
        playlistsSpec.setContent(playlistIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(recitersSpec); // Adding reciters tab
        tabHost.addTab(playerSpec); // Adding player tab
        tabHost.addTab(surasSpec); // Adding suras tab
        tabHost.addTab(playlistsSpec); // Adding playlists tab
        
        tabHost.setCurrentTab(tabIndex);
    }
    public void switchTab(int tab)
    {
        tabHost.setCurrentTab(tab);
    }
    public void loadSuras()
    {
    	tabHost.setCurrentTab(2);
    }
    public void loadMediaPlayer(int songIndex)
    {
    	PlayerActivity activity =(PlayerActivity) getLocalActivityManager().getActivity("player"); 
    	if(activity!=null)
    		activity.LoadNewSong(songIndex);
    	tabHost.setCurrentTab(1);
    }
    public void ShowErrorDialog()
    {
    	showAlertDialog(this, "No Internet Connection",
				"You don't have internet connection.", false);
    }
    private void showAlertDialog(Context context, String title, String message, Boolean status) 
    {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCancelable(false);
		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		// Setting alert dialog icon
		alertDialog.setIcon( R.drawable.fail);
			
		 alertDialog.setButton("Try Again!!", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		 
		    	  if(Utils.isConnectingToInternet(MP3Quran.this))
		          {
		          	AddAppTabs();
		          }else
		          {
		        	  ShowErrorDialog();
		          }
		 
		    } });
		alertDialog.show();
	}

  
}