package com.heji.henjiapp.http;

import org.json.JSONObject;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class GuideEntity {

	private String link_id;
	private String link;
	private String logo;
	private String location;
	private String time;
	private String category_id;
	private String category;
	private int isdefault;

	public String getLink_id() {
		return link_id;
	}

	public void setLink_id(String link_id) {
		this.link_id = link_id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}

	public void init(JSONObject obj) {
		this.link_id = obj.optString("link_id").trim();
		this.link = obj.optString("link").trim();
		this.logo = obj.optString("logo").trim();
		this.location = obj.optString("location").trim();
		this.time = obj.optString("time").trim();
		this.category_id = obj.optString("category_id").trim();
		this.category = obj.optString("category").trim();
		this.isdefault = obj.optInt("default", 0);
	}

	public void insert(SQLiteDatabase db) {
		HttpDownloader downloader = new HttpDownloader();
		int result = downloader.downFile(this.logo, "henji/",
				this.logo.substring(logo.lastIndexOf("/")));
		String path = Environment.getExternalStorageDirectory() + "/henji/" + this.logo.substring(logo.lastIndexOf("/"));
		if (result == 0 || result == 1) {
			db.execSQL(
					"insert into guide_table(link_id,link,logo,location,time,category_id,category,isdefault) values(?,?,?,?,?,?,?,?)",
					new Object[] { this.link_id, link, path, location, time,
							category_id, category, isdefault });

		}
	}
}
