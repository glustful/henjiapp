package com.heji.henjiapp;

import java.util.ArrayList;
import java.util.List;

import com.heji.henjiapp.db.DatabaseManager;
import com.heji.henjiapp.db.SqliteHelper;
import com.heji.henjiapp.http.DownThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 引导页
 */
public class GuidePageActivity extends Activity {
	ViewPager mViewPager;
	ArrayList<FragmentInfo> fInfo;
	MyPagerAdapter pagerAdapter;
	List<ImageView> mBottomXiaoyuandian;//
	LinearLayout mBottomYuandianLayout;//
	RelativeLayout main_guide;
	ImageView guide_image;
	Context mContext;
	String[] imgs = null;

	@SuppressWarnings("resource")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_page_activity);
		mContext = this;
		main_guide = (RelativeLayout) findViewById(R.id.main_guide);
		guide_image = (ImageView) findViewById(R.id.guide_image);
		DatabaseManager.initializeInstance(SqliteHelper.getInstance(mContext));
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		initUpdaeTable(db);
		Cursor c = db.rawQuery("select isupdate from update_table", null);
		
		if (c != null && c.getCount() == 1 && (c.moveToFirst() && c.getInt(0) == 1)) {
			c.close();
			db.execSQL("update update_table set isupdate=0");
			main_guide.setVisibility(View.VISIBLE);
			guide_image.setVisibility(View.GONE);
			c = db.rawQuery("select logo from guide_table", null);
			if (c != null && c.getCount() == 3) {
				imgs = new String[3];
				int i = 0;
				while (c.moveToNext()) {
					imgs[i] = c.getString(0);
					i++;
				}
			}
			c.close();
			

			fInfo = new ArrayList<FragmentInfo>();
			mBottomXiaoyuandian = new ArrayList<ImageView>();// 初始化底部小圆点
			mViewPager = (ViewPager) findViewById(R.id.guide_page_activity_viewpager);

			initBottom(imgs==null?3:imgs.length);

			pagerAdapter = new MyPagerAdapter();
			mViewPager.setAdapter(pagerAdapter);
			mViewPager
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageScrolled(int position,
								float positionOffset, int positionOffsetPixels) {

						}

						@Override
						public void onPageSelected(int position) {

							for (int i = 0; i < pagerAdapter.getCount(); i++) {
								if (i == position) {
									((ImageView) mBottomYuandianLayout
											.getChildAt(i))
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.page_focused));
								} else {
									((ImageView) mBottomYuandianLayout
											.getChildAt(i))
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.page_unfocused));
								}
							}

						}

						@Override
						public void onPageScrollStateChanged(int state) {

						}
					});
		} else {
			main_guide.setVisibility(View.GONE);
			guide_image.setVisibility(View.VISIBLE);
			c = db.rawQuery("select logo from guide_table where isdefault=1", null);
			if(c!=null && c.getCount()==1){
				c.moveToFirst();
				Uri uri = Uri.parse(c.getString(0));
				guide_image.setImageURI(uri);
				c.close();
			}else{
				guide_image.setImageResource(R.drawable.guide_01);
			}
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					startActivity(new Intent(GuidePageActivity.this,
							MainActivity.class));
					finish();
					
				}
			}, 3000);
			
		}
		DatabaseManager.getInstance().closeDatabase();
		new DownThread().start();
	}

	private void initUpdaeTable(SQLiteDatabase db) {
		Cursor c = db.rawQuery("select isupdate from update_table", null);
		if(c==null || c.getCount()==0){
			db.execSQL("insert into update_table(isupdate) values(1)");
		}
	}

	private void initBottom(int count) {// 初始化底部小圆点
		mBottomYuandianLayout = (LinearLayout) findViewById(R.id.guide_page_bottom);
		for (int i = 0; i < count; i++) {
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
					(int) getResources().getDimension(R.dimen.chat_dot_wh),
					(int) getResources().getDimension(R.dimen.chat_dot_wh));
			params.weight = 1;
			ImageView bottomIv = new ImageView(this);

			bottomIv.setLayoutParams(params);
			if (i == 0) {
				bottomIv.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
			} else {
				bottomIv.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
			}

			mBottomXiaoyuandian.add(bottomIv);
			mBottomYuandianLayout.addView(bottomIv);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	public void finish() {
		super.finish();
	}

	class MyPagerAdapter extends PagerAdapter {

		LayoutInflater inflater;

		public MyPagerAdapter() {
			inflater = LayoutInflater.from(GuidePageActivity.this);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}

		@SuppressWarnings("deprecation")
		@SuppressLint({ "InflateParams", "NewApi" })
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			View convertView = inflater.inflate(R.layout.guide_page_fragment,
					null);

			ImageView mFitView = (ImageView) convertView
					.findViewById(R.id.guidePageFitView);

			int viewResId = 0;
			Uri uri = null;
			if(imgs != null){
				uri = Uri.parse(imgs[position]);
			}
			switch (position) {
			case 0:
				viewResId = R.drawable.guide_01;
				
				break;
			case 1:
				viewResId = R.drawable.guide_02;

				break;

			case 2:
				viewResId = R.drawable.guide_03;

				((TextView) convertView.findViewById(R.id.tip))
						.setVisibility(View.VISIBLE);
				mFitView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(GuidePageActivity.this,
								MainActivity.class));
						finish();
					}
				});
				break;

			default:
				throw new AndroidRuntimeException(
						"maybe you add the view more??");
			}
			if(uri != null){
				mFitView.setImageURI(uri);
			}else{
			mFitView.setImageResource(viewResId);
			}
			container.addView(convertView, 0);
			return convertView;

		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}
}
