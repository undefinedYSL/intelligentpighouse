package com.example.fr.sanxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * Created by FR on 2017/5/3.
 */
public class ControlActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;
    private Handler handler1;
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;
    private SharedPreferences sp;
    boolean flag;
    String path="http://192.168.43.222:8888/myApps";
//    private GoogleApiClient client;
    HandlerThread handlerThread1 = new HandlerThread("myHandlerThread1");//����scoket����
    //温室1

    //手自动指令
    private String shoudongcommand01_on = "MCMD@03010001";
    private String zidongcommand01_on = "MCMD@03010002";
    //手自动按钮
    private RadioButton shoudongbutton_01_on = null;
    private RadioButton zidongbutton_01_on = null;

    //风扇指令
    private String fengshancommand01_on = "MCMD@03010101";
    private String fengshancommand01_off = "MCMD@03010102";

    private RadioButton fengshan_01_on = null;
    private RadioButton fengshan_01_off = null;

    //暖风扇
    private String nuanfengshancommand01_on = "MCMD@03010103";//开
    private String nuanfengshancommand01_off = "MCMD@03010104";//关

    private RadioButton nuanfengshan_01_on = null;
    private RadioButton nuanfengshan_01_off = null;

    //电热板
    private String dianrebancommand01_on = "MCMD@03010105";//开
    private String dianrebancommand01_off = "MCMD@03010106";//关

    private RadioButton dianreban_01_on = null;
    private RadioButton dianreban_01_off = null;

    private static final String TAG = null;
//        private Handler handler;
//    private MyHandler mHandler1;
    private LinearLayout home_img_bn_Layout, style_img_bn_layout, cam_img_bn_layout,shopping_img_bn_layout, show_img_bn_layout;

    private String mResponse;
    private String zidong_on = "MCMD@03010001";//启动
    private String zidong_off = "MCMD@03010002";//停止
    private String fengshan_on = "MCMD@03010101";//启动
    private String fengshan_off = "MCMD@03010102";//停止
    private String nuanfengshan_on = "MCMD@03010103";//启动
    private String nuanfengshan_off = "MCMD@03010104";//停止
    private String dianreban_on = "MCMD@03010105";//启动
    private String dianreban_off = "MCMD@03010106";//停止



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x456) {
                // Toast出服务器响应的字符串
                //Toast.makeText(MainActivity.this, mResponse, Toast.LENGTH_SHORT).show();

            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_layout);
        Looper looper = Looper.myLooper();//ȡ�õ�ǰ�߳����looper,�������̵߳�looper
//        mHandler1 = new MyHandler(looper);//����һ��handlerʹ֮����looperͨ��
//        shoudongbutton_01_on = (RadioButton) findViewById(R.id.shoudongkongzhi_01_on);
        zidongbutton_01_on = (RadioButton) findViewById(R.id.zidongkongzhi_01_on);
        fengshan_01_on = (RadioButton) findViewById(R.id.fengshan_01_on);
        fengshan_01_off = (RadioButton) findViewById(R.id.fengshan_01_off);
        nuanfengshan_01_on = (RadioButton) findViewById(R.id.nuanfengshan_01_on);
        nuanfengshan_01_off = (RadioButton) findViewById(R.id.nuanfengshan_01_off);
        dianreban_01_on = (RadioButton) findViewById(R.id.dianreban_01_on);
        dianreban_01_off = (RadioButton) findViewById(R.id.dianreban_01_off);

        home_img_bn_Layout = (LinearLayout) findViewById(R.id.bottom_home_layout_ly);
        home_img_bn_Layout.setOnClickListener(clickListener_home);

        shopping_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_shopping_layout_ly);
        shopping_img_bn_layout.setOnClickListener(clickListener_shopping);

        cam_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_cam_layout_ly);
        cam_img_bn_layout.setOnClickListener(clickListener_cam);

        style_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_style_layout_ly);
        style_img_bn_layout.setOnClickListener(clickListener_style);

        show_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_show_layout_ly);
        show_img_bn_layout.setOnClickListener(clickListener_show);
        HandlerThread handlerThread = new HandlerThread("myHandlerThread");//�̵߳��ú���
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
         handler.post(new MyRunnable());
    }

    class MyRunnable implements Runnable
    {
        @Override
        public void run() {
            zidongbutton_01_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                       path, zidong_on);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
//            shoudongbutton_01_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    // TODO Auto-generated method stub
//                    if (buttonView.isChecked()) {
//                        new Thread() {
//                            @Override
//                            public void run() {
//
//                                mResponse = GetPostUtil.sendPost(
//                                        "http://192.168.1.200:8888/myApps"
//                                        , zidong_off);
//                                Log.d("1111", mResponse);
//                            }
//                        }.start();
//                    }
//                }
//            });
            fengshan_01_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                        path, fengshancommand01_on);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
            fengshan_01_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                       path,fengshancommand01_off);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
            nuanfengshan_01_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                        path
                                        , nuanfengshancommand01_on);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
            nuanfengshan_01_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                       path, nuanfengshancommand01_off);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
            dianreban_01_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                      path, dianreban_on);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
            dianreban_01_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        new Thread() {
                            @Override
                            public void run() {

                                mResponse = GetPostUtil.sendPost(
                                       path, dianreban_off);
                                Log.d("1111", mResponse);
                            }
                        }.start();
                    }
                }
            });
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("view", "onFling");
        if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY){
            Log.e("fling", "right");
            if(sp.getBoolean("IFHUADONG", true))
            {
                Intent intent = new Intent();
                intent.setClass(ControlActivity.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        }
        return false;
    }


    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    protected void dialog()
    {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("温室管理终端").setMessage(
                "确认退出应用程序？").setPositiveButton("退出",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                ControlActivity.this.finish();
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
    protected void dialog1()
    {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("温室管理终端").setMessage(
                "确认注销当前帐号？").setPositiveButton("确定",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(ControlActivity.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                ControlActivity.this.finish();
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

    private View.OnClickListener clickListener_home = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            home_img_bn_Layout.setSelected(true);
            style_img_bn_layout.setSelected(false);
            cam_img_bn_layout.setSelected(false);
            shopping_img_bn_layout.setSelected(false);
            show_img_bn_layout.setSelected(false);
            Intent intent = new Intent();
            intent.setClass(ControlActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();
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
            intent.setClass(ControlActivity.this, VideoActivity.class);
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
            intent.setClass(ControlActivity.this, CurveActivity.class);
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
            intent.setClass(ControlActivity.this, ContactActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
            finish();
        }
    };
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
//  @Override
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
}
