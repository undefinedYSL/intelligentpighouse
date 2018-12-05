package com.example.fr.sanxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class VideoActivity extends Activity implements OnTouchListener, OnGestureListener {
	 private GestureDetector mGestureDetector;
	 private static final int FLING_MIN_DISTANCE = 50;
	 private static final int FLING_MIN_VELOCITY = 0;
	 private SharedPreferences sp;
	 private static final String TAG = null;
	 private LinearLayout home_img_bn_Layout, style_img_bn_layout, cam_img_bn_layout,
	 					  shopping_img_bn_layout, show_img_bn_layout;
	 private RelativeLayout video;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.video_layout);
	        
	        sp = this.getSharedPreferences("setting", Context.MODE_WORLD_READABLE);
	        
	        home_img_bn_Layout = (LinearLayout) findViewById(R.id.bottom_home_layout_ly);
	        home_img_bn_Layout.setOnClickListener(clickListener_home);

	        style_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_style_layout_ly);
	        style_img_bn_layout.setOnClickListener(clickListener_style);
	        style_img_bn_layout.setSelected(true);
	        
	        cam_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_cam_layout_ly);
	        cam_img_bn_layout.setOnClickListener(clickListener_cam);
	        
	        shopping_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_shopping_layout_ly);
	        shopping_img_bn_layout.setOnClickListener(clickListener_shopping);
	        
	        show_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_show_layout_ly);
	        show_img_bn_layout.setOnClickListener(clickListener_show);
	        
	        mGestureDetector = new GestureDetector(this);
	        video= (RelativeLayout)findViewById(R.id.video);
	        video.setOnTouchListener(this);    
	        video.setLongClickable(true);  
	        
	        Button btn1=(Button)findViewById(R.id.ringagain1);
	        btn1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PackageManager packageManager = VideoActivity.this.getPackageManager();
			    	Intent intent=new Intent();
			    	try { 
			             intent =packageManager.getLaunchIntentForPackage("com.videogo"); 
			         } 
			       catch (Exception e)
			       { 
			    	   Log.i(TAG, e.toString());
			        } 
			    startActivity(intent); 
				}
			});
	 }
	 public boolean onTouch(View v, MotionEvent event) {
		    return mGestureDetector.onTouchEvent(event);   
		}
	 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							float velocityY) {
			// TODO Auto-generated method stub
			Log.e("view", "onFling");
			if (e1.getX() - e2.getX()> FLING_MIN_DISTANCE  
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY ) {
				Log.e("fling", "left");
				if(sp.getBoolean("IFHUADONG", true))
				{
					Intent intent = new Intent();
					intent.setClass(VideoActivity.this, CurveActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
					finish();
				}
			} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE  
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY){
				Log.e("fling", "right");
				if(sp.getBoolean("IFHUADONG", true))
				{
					Intent intent = new Intent();
					intent.setClass(VideoActivity.this, CurveActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out); 
					finish();
				}
			}
			return false;
		}
	 public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	if(keyCode== KeyEvent.KEYCODE_BACK)
	    	{
				dialog();
	    		return true;
	    	}
	        else
	        {        
	            return super.onKeyDown(keyCode, event);
	        }
	    }
	 protected void dialog() 
	    {
	    	Dialog dialog = new AlertDialog.Builder(this).setTitle("温室管理终端").setMessage(
	    		     "确认退出应用程序？").setPositiveButton("退出",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						    VideoActivity.this.finish();
						}
					}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).create();
	    			dialog.show();
	    }
	 private OnClickListener clickListener_home = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					home_img_bn_Layout.setSelected(true);
					style_img_bn_layout.setSelected(false);
					cam_img_bn_layout.setSelected(false);
					shopping_img_bn_layout.setSelected(false);
					show_img_bn_layout.setSelected(false);
					Intent intent = new Intent();
					intent.setClass(VideoActivity.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
					finish();
			}
		};
		private OnClickListener clickListener_style = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				home_img_bn_Layout.setSelected(false);
				style_img_bn_layout.setSelected(true);
				cam_img_bn_layout.setSelected(false);
				shopping_img_bn_layout.setSelected(false);
				show_img_bn_layout.setSelected(false);
			}
		};
		private OnClickListener clickListener_cam = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				home_img_bn_Layout.setSelected(false);
				style_img_bn_layout.setSelected(false);
				cam_img_bn_layout.setSelected(true);
				shopping_img_bn_layout.setSelected(false);
				show_img_bn_layout.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(VideoActivity.this, CurveActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
				finish();
			}
		};
		private OnClickListener clickListener_shopping = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				home_img_bn_Layout.setSelected(false);
				style_img_bn_layout.setSelected(false);
				cam_img_bn_layout.setSelected(false);
				shopping_img_bn_layout.setSelected(true);
				show_img_bn_layout.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(VideoActivity.this, ControlActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
				finish();
			}
		};
		private OnClickListener clickListener_show = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				home_img_bn_Layout.setSelected(false);
				style_img_bn_layout.setSelected(false);
				cam_img_bn_layout.setSelected(false);
				shopping_img_bn_layout.setSelected(false);
				show_img_bn_layout.setSelected(true);
				Intent intent = new Intent();
				intent.setClass(VideoActivity.this, ContactActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
				finish();
			}
		};
//		 public boolean onCreateOptionsMenu(Menu menu) {
//			  // TODO Auto-generated method stub
//			 menu.add(0, 1, 1, "ע��").setIcon(R.drawable.zhuxiao);;
//		     menu.add(0, 2, 2, "�˳�").setIcon(R.drawable.tuichu);;
//			  return super.onCreateOptionsMenu(menu); 
//			 }
//		 public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		        // TODO Auto-generated method stub
//		        if(item.getItemId()==1){
//		        		Intent intent = new Intent();
//		        		intent.setClass(VideoActivity.this, BMWActivity.class);
//		        		startActivity(intent);
//		        		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//		                finish();
//		              }
//		        if(item.getItemId()==2){
//	                finish();
//	              }
//		        return super.onMenuItemSelected(featureId, item);
//		    }
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
								float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

}
