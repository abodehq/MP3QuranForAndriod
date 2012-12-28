package com.mos7af.mp3quran;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SuraslistManager {
	public static String reciterId = null;
	private static SuraslistManager instance = null;
	private ArrayList<HashMap<String, String>> playerSurasList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> surasList = new ArrayList<HashMap<String, String>>();
   public static SuraslistManager getInstance() {
      if(instance == null) {
         instance = new SuraslistManager();
      }
      return instance;
   }
	// Constructor
	public SuraslistManager(){
		if(playerSurasList.isEmpty())
		{
			HashMap<String, String> sura = new HashMap<String, String>();			
			sura.put("suraId", "1");
			sura.put("suraNameAr", "الفاتحة");
			sura.put("suraNameEn", "alfatiha");
			sura.put("suraPlaceLookupAr", "meeka");
			sura.put("suraPlaceLookupEn", "medanian");
  			sura.put("suraSoundPath", "http://s3.amazonaws.com/quranfiles/Sounds/1/1/001.mp3");
  			sura.put("reciterId", "1");
  			sura.put("reciterNameAr", "صلاح بو خاطر");
  			sura.put("reciterNameEn", "salah bu khater");
  			sura.put("reciterImage", "https://s3.amazonaws.com/quranfiles/Images/Readers/1.jpg");
  			playerSurasList.add(sura);
		}
	}
	public ArrayList<HashMap<String, String>> getPlayList()
	{
		return playerSurasList;
	}
	public void AddNewSura(HashMap<String, String> sura)
	{
		playerSurasList.add(sura);
	
	}
	public void AddNewSuraAt(int index,HashMap<String, String> sura)
	{
		playerSurasList.add(index,sura);
	
	}
	public void deletAllSuras()
	{
		playerSurasList.clear();
	}
	public void SetSongs(ArrayList<HashMap<String, String>> suras )
	{
		playerSurasList.clear();
		playerSurasList = suras;
	}
	public void SetSurasList(ArrayList<HashMap<String, String>> suras )
	{
		surasList.clear();
		surasList = suras;
	}
	public ArrayList<HashMap<String, String>> getPlayListSuras()
	{
		
		return surasList;
	}
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
