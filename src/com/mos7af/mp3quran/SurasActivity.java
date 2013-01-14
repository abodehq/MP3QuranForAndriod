
package com.mos7af.mp3quran;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import flex.messaging.io.amf.client.AMFConnection;
import flex.messaging.io.amf.client.exceptions.ClientStatusException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import android.widget.Toast;

public class SurasActivity extends Activity {
	
	
	
	private SuraItemAdapter suraItemAdapter = null;
    private SurasActivity _scope;
	private GetTask getTask;
	
	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	
	private final int MENU_ITEM_1_ACTION = 1;
	private final int MENU_ITEM_2_ACTION = 2;
	private final int MENU_ITEM_3_ACTION = 3;
	private final int MENU_ITEM_4_ACTION = 4;
	
	private ArrayList<HashMap<String, String>> surasList;
	
	private static String reciter_id = "-1";
	public static String suraPath = "-1";
	private ProgressDialog mProgressDialog;
	private DatabaseHandler db ;
	
	private ListView listView ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.ly_suras);
        listView = (ListView)findViewById(R.id.list);
       // onResume1();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        Resources res = getResources();
        //init the menu
          iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
          iconContextMenu.addItem(res, "play only", R.drawable.ic_1, MENU_ITEM_1_ACTION);
          iconContextMenu.addItem(res, "play", R.drawable.ic_2, MENU_ITEM_2_ACTION);
          iconContextMenu.addItem(res, "Add to playlist", R.drawable.ic_1, MENU_ITEM_3_ACTION);
          iconContextMenu.addItem(res, "download to play locally", R.drawable.ic_3, MENU_ITEM_4_ACTION);
          
          
          //set onclick listener for context menu
          iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
     			@Override
     			public void onClick(int menuId) {
     				SuraslistManager suraslistManager=SuraslistManager.getInstance();
     				HashMap<String, String> _sura;
     				MP3Quran mP3Quran;
     				switch(menuId) {
     				case MENU_ITEM_1_ACTION:
     					_sura = surasList.get(selecteduraIndex);
     					suraslistManager.deletAllSuras();
     					suraslistManager.AddNewSura(_sura);
     					MP3Quran.tabIndex =1;
     					mP3Quran= (MP3Quran)getParent();
     					mP3Quran.loadMediaPlayer(0);
     					break;
     				case MENU_ITEM_2_ACTION:
     					_sura = surasList.get(selecteduraIndex);
     					suraslistManager.AddNewSuraAt(0,_sura);
     					MP3Quran.tabIndex =1;
     					mP3Quran= (MP3Quran)getParent();
     					mP3Quran.loadMediaPlayer(0);
     					break;
     				case MENU_ITEM_3_ACTION:
     					showAddToPlaylist();
     					break;
     				case MENU_ITEM_4_ACTION:
     					_sura = surasList.get(selecteduraIndex);
     					String localPath = Environment.getExternalStorageDirectory()+"/MP3Quran/"+_sura.get("reciterId");
     					File file = new File(localPath,_sura.get("suraId")+ ".mp3" );
     					if (file.exists()) {
     						ShowRenameDialog(_sura);
     	            	}else
     	            	{
     	            		DownloadSura(_sura);
     	            	}
     					break;
     				
     				}
     			}
     		});
          db = new DatabaseHandler(getApplicationContext());
          RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.footer);
          relativeclic1.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v){
            	  reciter_id = "-1";
            	  onResume() ;
              }
          });
    }
    private void DownloadSura(HashMap<String, String> _sura)
    {
    	Toast.makeText(getApplicationContext(), "Start Download "+_sura.get("suraNameAr")+" - "+_sura.get("suraNameEn"), 1000).show();
			SurasActivity.suraPath =_sura.get("suraSoundPath").toString(); 
		 	mProgressDialog.setMessage(_sura.get("suraNameAr")+" - "+_sura.get("suraNameEn"));
		 	DownloadFile downloadFile = new DownloadFile();
			downloadFile.execute(SurasActivity.suraPath,_sura.get("suraNameAr")+" - "+_sura.get("suraNameEn"),_sura.get("reciterId"),_sura.get("suraId"));
    }
 
   // @Override
    public void onResume() 
    {
	     super.onResume();
	     if(SuraslistManager.reciterId!=null && SuraslistManager.reciterId != reciter_id)
	     {
	    	 loadSuras();
	    
	     }else
	     {
	    	  if( reciter_id !="-1" && surasList!=null && !surasList.isEmpty())
	    	    Toast.makeText(SurasActivity.this,surasList.get(0).get("reciterNameAr")+ " - "+surasList.get(0).get("reciterNameEn"), Toast.LENGTH_SHORT).show();
	    	  if(reciter_id !="-1" && (surasList==null || surasList.isEmpty()))
	    	  {
	    		  loadSuras();
	    	  }
	     }
    }
   private void loadSuras()
   {
	   listView.setAdapter(null);
  	 reciter_id = SuraslistManager.reciterId;
  	 _scope = this;
		 getTask = new GetTask();
		 getTask.execute();
   }
        
    /**
     * Initializes the List View
     */
    public void initListView() 
    {   
 
    }
    int selecteduraIndex = 0;
    private class GetTask extends AsyncTask<Void, Void, ReturnModel> {
        @Override
        protected ReturnModel doInBackground(Void... params) {
          return GetData();
        }

        @Override
        protected void onPostExecute(ReturnModel result)
        {
        	listView = (ListView)findViewById(R.id.list);
        	surasList = result.getheadlines();
        	if(surasList==null || surasList.isEmpty())
        	{
        		 ShowErrorDialog();
        	}
        	else
        	{
        	Toast.makeText(SurasActivity.this,surasList.get(0).get("reciterNameAr")+" - "+surasList.get(0).get("reciterNameEn"), Toast.LENGTH_SHORT).show();
        	listView = (ListView)findViewById(R.id.list);
        	if(suraItemAdapter==null)
        		suraItemAdapter = new SuraItemAdapter(_scope );
			
        	suraItemAdapter.SetData(surasList);
			listView.setAdapter(suraItemAdapter);
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					selecteduraIndex = pos;
					showDialog(CONTEXT_MENU_ID);
					return false;
				}});
        	}
        	listView.setOnItemClickListener(new OnItemClickListener() {
	        	  @Override
	        	  public void onItemClick(AdapterView<?> parent, View view,
	        	    int position, long id) {
	        	    
					SuraslistManager suraslistManager=SuraslistManager.getInstance();
					suraslistManager.deletAllSuras();
					suraslistManager.AddNewSura(surasList.get(position));
						
					MP3Quran.tabIndex =1;
					MP3Quran mP3Quran= (MP3Quran)getParent();
					mP3Quran.loadMediaPlayer(0);
	        	  }
	        	});
        	//View footerView = ((LayoutInflater) SurasActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ly_sura_footer, null, false);
        	//listView.addFooterView(footerView);

        }
      }
    
    private ReturnModel GetData()
    {
    	surasList = new ArrayList<HashMap<String, String>>();

  		
  		AMFConnection amfConnection= new AMFConnection();
  		try {   
  	        amfConnection.connect("http://mos7af.com/HolyQuranApi/index.php/amf/gateway");
  		} catch (ClientStatusException cse) {
  			Toast.makeText(getApplicationContext(), "Error !!", 1000).show();
  		}
  		                
  		try {
  				
  			
  			Object result = amfConnection.call("SurasServices.getAllSurasByReciterId",new Object[] {SuraslistManager.reciterId});
  		      
  		        try {
  		            JSONArray jsonArray = new JSONArray(result.toString()) ;
  		            JSONObject reciter = jsonArray.getJSONArray(0).getJSONObject(0);
  		            jsonArray =   jsonArray.getJSONArray(1);
  		          
  		            for (int i = 0; i < jsonArray.length(); i++) 
  		            {
  		             
  		            	
  		            	JSONObject jsonObject = jsonArray.getJSONObject(i);
  			           // creating new HashMap
  			  			HashMap<String, String> _sura = new HashMap<String, String>();
  			  			_sura.put("suraId", jsonObject.getString("suraId"));
  			  			_sura.put("suraNameAr", jsonObject.getString("suraNameAr"));
  			  			_sura.put("suraNameEn", jsonObject.getString("suraNameEn"));
			  			_sura.put("suraPlaceLookupAr", jsonObject.getString("suraPlaceLookupAr"));
			  			_sura.put("suraPlaceLookupEn", jsonObject.getString("suraPlaceLookupEn"));
			  			_sura.put("suraSoundPath", jsonObject.getString("suraSoundPath"));
			  			_sura.put("reciterId", reciter.getString("reciterId"));
			  			_sura.put("reciterNameAr", reciter.getString("reciterNameAr"));
			  			_sura.put("reciterNameEn", reciter.getString("reciterNameEn"));
			  			_sura.put("reciterImage", reciter.getString("reciterImage"));
			  			surasList.add(_sura);
  		            
  		            }
  		          } catch (Exception e) {
  		            e.printStackTrace();
  		          Toast.makeText(getApplicationContext(), "Error !!", 1000).show();
  		          }
  		        
  		        
  		     
  		        
  		        
  		     
  		} catch (Exception e) {
  		        System.out.println("Error while calling remote method");
  		    
  		}

  		ReturnModel returnModel = new ReturnModel();
  		returnModel.setheadlines(surasList);
         return returnModel;
    }
    
    
    private class ReturnModel {
        private ArrayList<HashMap<String, String>> surasList ;    

        public ArrayList<HashMap<String, String>> getheadlines() {
          return surasList;
        }

        public void setheadlines(ArrayList<HashMap<String, String>> _surasList) {
          this.surasList = _surasList;
        }

        
      }
    
   // @Override
   // public boolean onCreateOptionsMenu(Menu menu)
   // {
      //  MenuInflater menuInflater = getMenuInflater();
     //   menuInflater.inflate(R.layout.surasmenu, menu);
      //  return true;
    //}
    @Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return iconContextMenu.createMenu("Sura");
		}
		return super.onCreateDialog(id);
	}
    public void ReCallData()
    {
    	finish();
    	startActivity(getIntent());
    }
    private DownloadFile _downloadFile;
    private class DownloadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            try {
            	File folder = new File(Environment.getExternalStorageDirectory() + "/MP3Quran");
            	boolean success = true;
            	if (!folder.exists()) {
            	    success = folder.mkdir();
            	}
            	folder = new File(Environment.getExternalStorageDirectory() + "/MP3Quran/"+sUrl[2]);
            	
            	if (!folder.exists()) {
            	    success = folder.mkdir();
            	}
            	 
            	 
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/MP3Quran/"+sUrl[2]+"/"+sUrl[3]+".mp3");

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _downloadFile =  DownloadFile.this;
            mProgressDialog.show();
            
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }
       
        @Override
        protected void onPostExecute(String result) 
        {
        	mProgressDialog.cancel();
        	Toast.makeText(getApplicationContext(), "successfully download sura !!", 1000).show();
        }
    }
    ArrayList<HashMap<String, String>> playlists;
    private void showAddToPlaylist()
    {
    	final Dialog dialog = new Dialog(this);
    	dialog.setTitle("Add to playlist");
    	ListView modeList = new ListView(this);
    	modeList.setOnItemClickListener(new OnItemClickListener() {
      	  @Override
      	  public void onItemClick(AdapterView<?> parent, View view,
      	    int position, long id) {
      	    
      	    int playlistIndex = position;
      	    HashMap<String, String>  sura = surasList.get(selecteduraIndex);
      	    HashMap<String, String>  playlist = playlists.get(playlistIndex);
      	    db.InsertPlayListSura(sura,playlist.get("playlistId"));
      	    Toast.makeText(getApplicationContext(), "1 Sura added to playlist.", 1000).show();
			dialog.cancel();
      	  }
      	});
    
    	  playlists = db.getAllPlaylists();
    	  if(playlists.isEmpty())
    	  {
    		  Toast.makeText(getApplicationContext(), "No playlists!!", 1000).show();
    		  dialog.cancel();
    		  MP3Quran.tabIndex = 3;
    		  MP3Quran mP3Quran= (MP3Quran)getParent();
    		  mP3Quran.switchTab(3);
    	  }else
    	  {
    		  PlaylistItemAdapter adapter1 = new PlaylistItemAdapter(this );
    		  adapter1.SetData(playlists);
    		  modeList.setAdapter(adapter1);
    		  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    		  dialog.setContentView(modeList);
    		  dialog.show();
    	  }
    }
    public void ShowErrorDialog()
    {
    	showAlertDialog(SurasActivity.this, "No Internet Connection",
				"You don't have internet connection.", false);
    }
    private void showAlertDialog(Context context, String title, String message, Boolean status) 
    {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		// Setting alert dialog icon
		alertDialog.setIcon( R.drawable.fail);
			
		 alertDialog.setButton("Try Again!!", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		 
		    	  if(Utils.isConnectingToInternet(SurasActivity.this))
		          {
		    		  onResume();
		          	
		          }else
		          {
		        	  ShowErrorDialog();
		          }
		 
		    } });
		alertDialog.show();
	}
private void  ShowRenameDialog(HashMap<String, String> _sura) {
    	
	    final HashMap<String, String> __sura = _sura;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sura "+_sura.get("suraNameAr")+" - "+_sura.get("suraNameEn")+" is already exist!!");
        
         
         builder.setPositiveButton("Re-Download", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownloadSura(__sura);
				 dialog.cancel();
				
			}
		});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
                return;
            }
        });
        AlertDialog alertDialog = builder.create();
        
		// show it
		alertDialog.show();
      
    }
   

    
}