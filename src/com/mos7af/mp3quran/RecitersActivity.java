package com.mos7af.mp3quran;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import flex.messaging.io.amf.client.AMFConnection;
import flex.messaging.io.amf.client.exceptions.ClientStatusException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;
public class RecitersActivity extends Activity {
		
	ListView list;
    ReciterItemAdapter reciterItemAdapter;
    private RecitersActivity _scope;
    private GetTask getTask;
    private ArrayList<HashMap<String, String>> recitersList;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_reciters);
        _scope = this;
        getTask = new GetTask();
        getTask.execute(); 
    }
    
    private class GetTask extends AsyncTask<Void, Void, ReturnModel> {
        @Override
        protected ReturnModel doInBackground(Void... params) {
          return GetData();
        }

        @Override
        protected void onPostExecute(ReturnModel result) {

          
        	ProgressBar loading=(ProgressBar)findViewById(R.id.loading);;
        	loading.setVisibility(View.GONE);
        	recitersList = result.getheadlines();
			list= (ListView)findViewById(R.id.list);
	  		
			 reciterItemAdapter = new ReciterItemAdapter(_scope, recitersList);    
	        
	         list.setAdapter(reciterItemAdapter);
	         
	         list.setOnItemClickListener(new OnItemClickListener() {
	        	  @Override
	        	  public void onItemClick(AdapterView<?> parent, View view,
	        	    int position, long id) 
	        	  {
					// Starting new intent
					Intent in = new Intent(getApplicationContext(),
							MP3Quran.class);
				    SuraslistManager.reciterId =  recitersList.get(position).get("reciterId");
					MP3Quran.tabIndex =2;
					MP3Quran mP3Quran= (MP3Quran)getParent();
					mP3Quran.loadSuras();
	        	  }
	        	});

          
        }
      }
    
    
    
    private ReturnModel GetData()
    {
    	
    	ProgressBar loading=(ProgressBar)findViewById(R.id.loading);;
    	loading.setVisibility(View.VISIBLE);
    	recitersList = new ArrayList<HashMap<String, String>>();

  		
  		AMFConnection amfConnection= new AMFConnection();
  		try {   
  	        amfConnection.connect("http://mos7af.com/HolyQuranApi/index.php/amf/gateway");
  		} catch (ClientStatusException cse) {
  		        System.out.println("Error while connecting");
  		       // return false;
  		}
  		                
  		try {
  		        Object result = amfConnection.call("RecitersServices.getAllReciters");
  		        System.out.println(result);
  		        try {
  		            JSONArray jsonArray = new JSONArray(result.toString()) ;
  		            System.out.println("Number of entries " + jsonArray.length());
  		            for (int i = 0; i < jsonArray.length(); i++) {
  		              JSONObject jsonObject = jsonArray.getJSONObject(i);
  		              System.out.println( jsonObject.getString("reciterId"));
  			  			HashMap<String, String> reciter = new HashMap<String, String>();
	  			  		reciter.put("reciterId", jsonObject.getString("reciterId"));
	  			  		reciter.put("reciterNameAr", jsonObject.getString("reciterNameAr"));
	  			  		reciter.put("reciterNameEn", jsonObject.getString("reciterNameEn"));
	  			  		reciter.put("reciterImage", jsonObject.getString("reciterImage"));
	  			  		recitersList.add(reciter);
  		            }
  		          } catch (Exception e) {
  		            e.printStackTrace();
  		          }
  		        
  		        
  		        
  		        
  		        
  		     
  		} catch (Exception e) {
  		        System.out.println("Error while calling remote method");
  		       // return false;
  		}

  		ReturnModel returnModel = new ReturnModel();
  		returnModel.setheadlines(recitersList);
         return returnModel;
    }
    private class ReturnModel {
        private ArrayList<HashMap<String, String>> recitersList ;
     

        public ArrayList<HashMap<String, String>> getheadlines() {
          return recitersList;
        }

        public void setheadlines(ArrayList<HashMap<String, String>> _songsList) {
          this.recitersList = _songsList;
        }

        
      }
    
    
    
    
}
