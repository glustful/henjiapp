package com.heji.henjiapp.http;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.heji.henjiapp.db.DatabaseManager;
import com.heji.henjiapp.db.SqliteHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownThread extends Thread{
	private static final String url = "http://www.hsh101.com/api/getAd.php?alias=appyindao";
	private String downloadText() {
		HttpDownloader downloader = new HttpDownloader();
		return downloader.download(url);
	}

	
	public void run(){
		try{
		String json = downloadText();
		JSONArray objs = new JSONArray(json);
		if(objs!=null){
			ArrayList<GuideEntity> entitys = new ArrayList<GuideEntity>();
			for(int i=0;i<objs.length();i++){
				GuideEntity entity = new GuideEntity();
				entity.init(objs.optJSONObject(i));
				entitys.add(entity);
			}
			initDatabase(entitys);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void initDatabase(ArrayList<GuideEntity> entitys) {
		
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		Cursor c = db.rawQuery("select link_id from guide_table", null);
		db.beginTransaction();
		if(c == null || c.getCount()==0){
			db.execSQL("delete from guide_table");
			for(GuideEntity entity:entitys){
				entity.insert(db);
			}
			db.execSQL("update update_table set isupdate=1");
		}else{
			while(c.moveToNext()){
				boolean flag = false;
				for(GuideEntity entity:entitys){
					if(c.getString(0).equalsIgnoreCase(entity.getLink_id())){
						flag = true;
						break;
					}
				}
				if(!flag){
					db.execSQL("delete from guide_table");
					for(GuideEntity entity:entitys){
						entity.insert(db);
					}
					db.execSQL("update update_table set isupdate=1");
					return;
				}
			}
		}
		c.close();
		db.setTransactionSuccessful();
		db.endTransaction();
	}

}
