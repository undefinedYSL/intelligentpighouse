package com.example.fr.sanxing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, View.OnTouchListener {
  // String path="http://192.168.43.222:8888/myApps";
  //  String path="http://192.168.1.200:8888/myApps";
//    private MyHandler mHandler1;
    private Handler handler1;
    private String[] arr;//��Ž�����������
    private TextView tempnum1;
    private TextView sun1num1;
    private TextView shidunum1;
    private TextView co2num1;
    private TextView h2snum1;
    private TextView anqinum1;
    private TextView airratenum1;
    private ListView lv;
    private SharedPreferences sp;
    private TextView date_TextView;
    private ImageButton title_logo_img, set_ImageButton;
    private ViewFlipper viewFlipper;
    private boolean showNext = true;
    private boolean isRun = true;
    private int currentPage = 0;
    private final int SHOW_NEXT = 0011;
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;
    private GestureDetector mGestureDetector;
    private LinearLayout home_img_bn_Layout, shopping_img_bn_layout, show_img_bn_layout, style_img_bn_layout, cam_img_bn_layout;
    private RelativeLayout relativesearch;
    private String mResponse;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sp = this.getSharedPreferences("setting", Context.MODE_PRIVATE);
        sp.edit().putBoolean("IFHUADONG", false).commit();

        date_TextView = (TextView) findViewById(R.id.home_date_tv);
        date_TextView.setText(getDate());


        relativesearch = (RelativeLayout) findViewById(R.id.relativesearch1);

        relativesearch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LineActivity.class);
                startActivity(intent);
            }
        });

        home_img_bn_Layout = (LinearLayout) findViewById(R.id.bottom_home_layout_ly);
        home_img_bn_Layout.setOnClickListener(clickListener_home);
        home_img_bn_Layout.setSelected(true);

        shopping_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_shopping_layout_ly);
        shopping_img_bn_layout.setOnClickListener(clickListener_shopping);

        show_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_show_layout_ly);
        show_img_bn_layout.setOnClickListener(clickListener_show);

        style_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_style_layout_ly);
        style_img_bn_layout.setOnClickListener(clickListener_style);

        cam_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_cam_layout_ly);
        cam_img_bn_layout.setOnClickListener(clickListener_cam);

        shopping_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_shopping_layout_ly);
        shopping_img_bn_layout.setOnClickListener(clickListener_shopping);

        Looper looper = Looper.myLooper();//ȡ�õ�ǰ�߳����looper,�������̵߳�looper
//        mHandler1 = new MyHandler(looper);//����һ��handlerʹ֮����looperͨ��

        HandlerThread handlerThread1 = new HandlerThread("myHandlerThread1");//����scoket�����߳�
        handlerThread1.start();
        handler1 = new Handler(handlerThread1.getLooper());
        handler1.post(new MyRunnable());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        viewFlipper = (ViewFlipper) findViewById(R.id.mViewFliper_vf);
        mGestureDetector = new GestureDetector(this);
        viewFlipper.setOnTouchListener(this);
        viewFlipper.setLongClickable(true);
        viewFlipper.setOnClickListener(clickListener);
        displayRatio_selelct(currentPage);
//        MyScrollView myScrollView = (MyScrollView) findViewById(R.id.viewflipper_scrollview);
//        myScrollView.setOnTouchListener(onTouchListener);
//        myScrollView.setGestureDetector(mGestureDetector);
        home_img_bn_Layout.setSelected(true);
        thread.start();
    }

    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            isRun = false;
            dialog();
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

    class MyRunnable implements Runnable
    {
        @Override
        public void run() {
            mResponse = GetPostUtil.sendPost(
                    "http://192.168.43.222:8888/myApps"
                    , "data@");
            Log.d("1111",mResponse);
            // 发送消息通知UI线程更新UI组件
            handler.sendEmptyMessage(0x456);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0x456) {
                // Toast出服务器响应的字符串
                //Toast.makeText(MainActivity.this, mResponse, Toast.LENGTH_SHORT).show();
                tempnum1 = (TextView) findViewById(R.id.temp1);
                shidunum1 = (TextView) findViewById(R.id.humi1);
                co2num1 = (TextView) findViewById(R.id.co21);
                sun1num1 = (TextView) findViewById(R.id.sun1);
                anqinum1 = (TextView) findViewById(R.id.anqi1);
                h2snum1 = (TextView) findViewById(R.id.H2S1);
                airratenum1 = (TextView) findViewById(R.id.AirRate1);
                arr=mResponse.split(",");
                String temp=arr[0];
                String humi=arr[1];
                String co2=arr[2];
                String light=arr[3];
                String nh3=arr[4];
                String h2s=arr[5];
                String floot=arr[6];
                tempnum1.setText(temp+"℃");
                shidunum1.setText(humi+"%");
                co2num1.setText(co2+"ppm");
                sun1num1.setText(light+"Lux");
                anqinum1.setText(nh3+"ppm");
                h2snum1.setText(h2s+"ppm");
                airratenum1.setText(floot+"m/s");
            }
            else {

            }
        }
    };

    private View.OnClickListener clickListener_home = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(true);
            style_img_bn_layout.setSelected(false);
            cam_img_bn_layout.setSelected(false);
            shopping_img_bn_layout.setSelected(false);
            show_img_bn_layout.setSelected(false);
        }
    };
    private View.OnClickListener clickListener_style = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(false);
            style_img_bn_layout.setSelected(true);
            cam_img_bn_layout.setSelected(false);
            shopping_img_bn_layout.setSelected(false);
            show_img_bn_layout.setSelected(false);
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, VideoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();

        }
    };
    private View.OnClickListener clickListener_cam = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(false);
            style_img_bn_layout.setSelected(false);
            cam_img_bn_layout.setSelected(true);
            shopping_img_bn_layout.setSelected(false);
            show_img_bn_layout.setSelected(false);
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CurveActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();
        }
    };
    private View.OnClickListener clickListener_shopping = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(false);
            style_img_bn_layout.setSelected(false);
            cam_img_bn_layout.setSelected(false);
            shopping_img_bn_layout.setSelected(true);
            show_img_bn_layout.setSelected(false);
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ControlActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();
        }
    };

    private View.OnClickListener clickListener_air = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(false);
            style_img_bn_layout.setSelected(false);
            cam_img_bn_layout.setSelected(false);
            shopping_img_bn_layout.setSelected(false);
            show_img_bn_layout.setSelected(false);
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AirconditionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();
        }
    };
    private View.OnClickListener clickListener_show = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(false);
            style_img_bn_layout.setSelected(false);
            cam_img_bn_layout.setSelected(false);
            shopping_img_bn_layout.setSelected(false);
            show_img_bn_layout.setSelected(true);
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ContactActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();
        }
    };
    protected void dialog() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("温室管理终端").setMessage(
                "确认退出应用程序？").setPositiveButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                MainActivity.this.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }

    private String getDate(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1 ;
        if (w < 0) {
            w = 0;
        }
        int month=(int)c.get(Calendar.MONTH)+1;
        String mDate = c.get(Calendar.YEAR)+"年" + month + "月" + c.get(Calendar.DATE) + "日  " + weekDays[w];
        return mDate;
    }

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case SHOW_NEXT:
                    if (showNext) {
                        showNextView();
                    } else {
                        showPreviousView();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    Thread thread = new Thread(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(isRun){
                try {
                    Thread.sleep(60 * 60);
                    Message msg = new Message();
                    msg.what = SHOW_NEXT;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    };

    private void showNextView(){

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        viewFlipper.showNext();
        currentPage ++;
        if (currentPage == viewFlipper.getChildCount()) {
            displayRatio_normal(currentPage - 1);
            currentPage = 0;
            displayRatio_selelct(currentPage);
        } else {
            displayRatio_selelct(currentPage);
            displayRatio_normal(currentPage - 1);
        }
        Log.e("currentPage", currentPage + "");

    }
    private void showPreviousView(){
        displayRatio_selelct(currentPage);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        viewFlipper.showPrevious();
        currentPage --;
        if (currentPage == -1) {
            displayRatio_normal(currentPage + 1);
            currentPage = viewFlipper.getChildCount() - 1;
            displayRatio_selelct(currentPage);
        } else {
            displayRatio_selelct(currentPage);
            displayRatio_normal(currentPage + 1);
        }
        Log.e("currentPage", currentPage + "");
    }
    private void displayRatio_selelct(int id){
        int[] ratioId = {R.id.home_ratio_img_04, R.id.home_ratio_img_03, R.id.home_ratio_img_02, R.id.home_ratio_img_01};
        ImageView img = (ImageView)findViewById(ratioId[id]);
        img.setSelected(true);
    }
    private void displayRatio_normal(int id){
        int[] ratioId = {R.id.home_ratio_img_04, R.id.home_ratio_img_03, R.id.home_ratio_img_02, R.id.home_ratio_img_01};
        ImageView img = (ImageView)findViewById(ratioId[id]);
        img.setSelected(false);

    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        Log.e("view", "onFling");
        if (e1.getX() - e2.getX()> FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY ) {
            Log.e("fling", "left");
            showNextView();
            if(sp.getBoolean("IFHUADONG", true))
            {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ControlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
            showNext = true;
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY){
            Log.e("fling", "right");
            showPreviousView();
            showNext = false;
        }
        return false;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //toastInfo("����¼�");
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            return mGestureDetector.onTouchEvent(event);
        }
    };

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
