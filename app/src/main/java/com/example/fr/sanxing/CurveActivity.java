package com.example.fr.sanxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurveActivity extends Activity implements OnTouchListener, OnGestureListener {
	private GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	 
	private GraphicalView chart11;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqlLiteDatabase;
	
	private SharedPreferences sp;
	
	private LinearLayout home_img_bn_Layout, style_img_bn_layout, cam_img_bn_layout,
						shopping_img_bn_layout, show_img_bn_layout;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.curve_layout);		
		sp = this.getSharedPreferences("setting", Context.MODE_PRIVATE);

		home_img_bn_Layout = (LinearLayout) findViewById(R.id.bottom_home_layout_ly);
        home_img_bn_Layout.setOnClickListener(clickListener_home);


        style_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_style_layout_ly);
        style_img_bn_layout.setOnClickListener(clickListener_style);
        
        cam_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_cam_layout_ly);
        cam_img_bn_layout.setOnClickListener(clickListener_cam);
        cam_img_bn_layout.setSelected(true);
        
        shopping_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_shopping_layout_ly);
        shopping_img_bn_layout.setOnClickListener(clickListener_shopping);
        
        show_img_bn_layout = (LinearLayout) findViewById(R.id.bottom_show_layout_ly);
        show_img_bn_layout.setOnClickListener(clickListener_show);
        
        mGestureDetector = new GestureDetector(this);
        //����������
        RelativeLayout cure= (RelativeLayout)findViewById(R.id.cure);
        cure.setOnTouchListener(this);    
        cure.setLongClickable(true);  
        
        final Button btn1=(Button)findViewById(R.id.ringagain1);
        btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final CharSequence[] items =
				{ "节点一空气温度", "节点一空气湿度","节点一光照","节点一CO2浓度","节点一土壤温度", "节点一土壤湿度","节点二空气温度","节点二空气湿度","节点二光照","节点二CO2浓度","节点二土壤温度","节点二土壤湿度" };
				AlertDialog dlg = new AlertDialog.Builder(CurveActivity.this).setTitle("请选择").setItems(items,
						new DialogInterface.OnClickListener()
						{
							public void onClick (DialogInterface dialog , int item )
							{
								if (item == 0)
								{
									showwendu();
									btn1.setText("            历史曲线（节点一空气温度");
								} 
								else if(item == 1)
								{
									showhumi();
									btn1.setText("            历史曲线（节点一空气湿度）");
								}
								else if(item == 2)
								{
									showlumi();
									btn1.setText("            历史曲线（节点一光照强度");
								}
								else if(item == 3)
								{
									showco2();
									btn1.setText("            历史曲线（节点一CO2浓度");
								}
//								else if (item == 4)
//							{
//								showturangwendu();
//								btn1.setText("            历史曲线（节点一土壤温度）");
//							}
//							else if(item == 5)
//							{
//								showturangshidu();
//								btn1.setText("            历史曲线（节点一土壤湿度）");
//							}
//							else if(item == 6)
//							{
//								showwendu1();
//								btn1.setText("            历史曲线（节点二空气温度）");
//							}
//							else if(item ==7)
//							{
//								showhumi1();
//								btn1.setText("            历史曲线（节点二空气湿度）");
//							}
//							else if (item == 8)
//							{
//								showlumi1();
//								btn1.setText("            历史曲线（节点二光照强度）");
//							}
//							else if(item == 8)
//							{
//								showco21();
//								btn1.setText("            历史曲线（节点二CO2浓度）");
//							}
//							else if(item == 10)
//							{
//								showturangwendu1();
//								btn1.setText("            历史曲线（节点二土壤温度）");
//							}
//							else
//							{
//								showturangshidu1();
//								btn1.setText("            历史曲线（节点二土壤湿度）");
//							}
							}
						}).create();
				dlg.show();
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
				intent.setClass(CurveActivity.this, ControlActivity.class);
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
				intent.setClass(CurveActivity.this, VideoActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out); 
				finish();
			}
		}
		return false;
	}
	/*public Cursor query(String typeName,int num)
	{
		dbHelper=new DatabaseHelper(CurveActivity.this,"information");
		sqlLiteDatabase=dbHelper.getReadableDatabase();
		//Cursor cursor=sqlLiteDatabase.rawQuery("select * from "+typeName+" where id="+num, null);//time>'2012/8/21 13:50'..time<'2012/8/21 16:00'  where time between '"+startdata+"' and '"+enddata+"'
		Cursor cursor=sqlLiteDatabase.rawQuery("select * from "+typeName,null);
		return cursor;
	}*/
	
	public Cursor query(String typeName)
	{
		dbHelper=new DatabaseHelper(CurveActivity.this,"information");
		sqlLiteDatabase=dbHelper.getReadableDatabase();
		//Cursor cursor=sqlLiteDatabase.rawQuery("select * from "+typeName+" where id="+num, null);//time>'2012/8/21 13:50'..time<'2012/8/21 16:00'  where time between '"+startdata+"' and '"+enddata+"'
		Cursor cursor=sqlLiteDatabase.rawQuery("select * from "+typeName, null);
		return cursor;
	}
	//���ݶ���	
	private XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues, List<Double[]> yValues)
	{
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();/*创建图标数据集*/
		int length = titles.length;    /* 获取曲线个数, 每个曲线都有一个标题 */
		for (int i = 0; i < length; i++)
		{
			TimeSeries series = new TimeSeries(titles[i]);   /* 带日期的单条曲线数据 */
			Date[] xV =xValues.get(i);           /* 获取该条曲线数据的 日期数组 */
			Double[] yV =yValues.get(i);          /* 获取该条曲线数据的 值数组 */
			int seriesLength = xV.length;         /* 获取该条曲线的值的个数, 即x轴点个数 */
			for (int k = 0; k < seriesLength; k++)    //每条线里有几个点
			{ 
				series.add(xV[k], yV[k]);          /* 将日期数组 和 值数组设置给 带日期的单条曲线数据 */
			}
			dataset.addSeries(series);            /* 将单条曲线数据设置给 图标曲线数据集 */

		}
		return dataset;
	}
	//ͼ����Ⱦ��
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill)
	{ 
		
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(); 
		  renderer.setAxisTitleTextSize(16);                     //����XY����������С   
		  renderer.setChartTitleTextSize(20);                    //���ñ��������ִ�С   
		  renderer.setLabelsTextSize(15);                        //���ñ�ǩ���ִ�С   
		  renderer.setLegendTextSize(15);                       // ����˵�����ִ�С   
		  renderer.setPointSize(5f);                             //���õ�Ĵ�С   
		  renderer.setMargins(new int[] { 20, 30, 15, 20 });    // ���� margin, ��λ����  
		int length = colors.length;      //��ȡ��Ⱦ���ĸ���, ���ж���������   
		for (int i = 0; i < length; i++) 
		{ 
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]); 
			r.setPointStyle(styles[i]); 
			r.setFillPoints(fill); 
			/*r.setChartValuesTextSize(15);
		    r.setChartValuesSpacing(3);
		    r.setFillBelowLine(true);
		    r.setFillBelowLineColor(Color.WHITE);*/
			renderer.addSeriesRenderer(r);
		} 
		return renderer; 
	}
	//����������
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title,
									String xTitle, String yTitle, double xMin,
									double xMax, double yMin, double yMax, int axesColor, int labelsColor)
	{ 
		renderer.setChartTitle(title); 
		renderer.setChartTitleTextSize(20);
		renderer.setAxisTitleTextSize(15);
		renderer.setXTitle(xTitle); 
		renderer.setYTitle(yTitle); 
		renderer.setXAxisMin(xMin);  //����x����Сֵ
		renderer.setXAxisMax(xMax); 
		renderer.setAxesColor(axesColor); 
		renderer.setLabelsColor(labelsColor); 
		renderer.setXLabels(10);
		renderer.setXLabels(6);
		renderer.setYLabels(10);
	    renderer.setLegendTextSize(15);//ͼ�����ִ�С 
	    renderer.setShowGrid(true);
	    renderer.setPanEnabled(false,false); 
	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setZoomButtonsVisible(true);
//	    renderer.setPanLimits(new double[] { 1, 12, -10, 100 });
//	    renderer.setZoomLimits(new double[] { -10, 20, -10, 100 });
	    renderer.setMargins(new int[]{30,35,30,5}); // ����4������
	    renderer.setBackgroundColor(Color.TRANSPARENT);//���ñ���ɫ͸��
	    renderer.setApplyBackgroundColor(true);//ʹ������ɫ��Ч
	    renderer.setAxesColor(Color.BLACK);//�������������ɫ
	    renderer.setLabelsColor(Color.BLACK);
	    renderer.setMarginsColor(Color.WHITE);
	}
	private void showwendu()
	{
		String[] tit = new String[] {"�����¶�"};
        List<Date[]> x = new ArrayList<Date[]>();
   	    List<Double[]> y =new ArrayList<Double[]>();
        List<Date> xV2=new ArrayList<Date>();
        List<Double> yV2=new ArrayList<Double>();

   	  	Cursor cursor2=query("temp where id=020101");
   	  	int len=cursor2.getCount();
   	  	if(len>0)
   	  	{
   	  		while(cursor2.moveToNext())
   	  		{
   	  			String mydate=cursor2.getString(cursor2.getColumnIndex("time"));
   	  			Log.v(ACTIVITY_SERVICE, mydate);
   	  			Double value2=cursor2.getDouble(cursor2.getColumnIndex("Temp"));
   	  			SimpleDateFormat simformat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
   	  			Date date = null;
   	  			try {
   	  				date = simformat.parse(mydate);//��time�ַ���ת��ΪData��
   	  				Log.v(ACTIVITY_SERVICE, "����try");
   	  				} catch (ParseException e)
   	  				{
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				} catch (java.text.ParseException e) {
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				}
   	  			xV2.add(date);
   	  			yV2.add(value2);
   	  			
   	  		}
   	  		
   	  	    Date xxV2[]=new Date[xV2.size()];
   	  		Double yyV2[]=new Double[yV2.size()];
   	  		xV2.toArray(xxV2);
   	  		yV2.toArray(yyV2);
   	  		x.add(xxV2);
   	  		y.add(yyV2); 
   	  		XYMultipleSeriesDataset dataset = buildDateDataset(tit,x,y);
   	  		int[] colors = new int[] { Color.RED};
   	  	    PointStyle[] styles = new PointStyle[] {PointStyle.DIAMOND}; 
   	  		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles,true);
   	  	    setChartSettings(renderer,"温度随时间统计曲线", "时间(X)", "温度(Y)",x.get(0)[0].getTime(),x.get(0)[len-1].getTime(), 0, 4 , Color.WHITE, Color.WHITE);
   	  		//��ʾͼ��
 	  	    if(chart11==null)
   	  	    {
   	  			LinearLayout layout=(LinearLayout)findViewById(R.id.chart_show);
   	  			//����ͼ��
   	  			chart11 = ChartFactory.getTimeChartView(CurveActivity.this,dataset,renderer, "yyyy-MM-dd HH:mm");
   	  			layout.removeAllViewsInLayout();
   	  			//����ͼ��Ĵ�С
   	  			layout.addView(chart11, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
   	  			chart11=null;
   	  		}
   	  		else
   	  		{
   	  			chart11.repaint();
   	  		}
   	  		//��ӵ��ͼ���¼�����
   	  		renderer.setClickEnabled(true);
   	  		renderer.setSelectableBuffer(100);
   	  	}
   	  	else
   	  	{
   	  		Toast.makeText(CurveActivity.this, "数据库为空！", Toast.LENGTH_LONG).show();
   	  	}
    }
	private void showhumi()
	{
		String[] titles = new String[] {"节点一空气湿度"};
        List<Date[]> x = new ArrayList<Date[]>();
   	    List<Double[]> y =new ArrayList<Double[]>();

        List<Date> xV2=new ArrayList<Date>();
        List<Double> yV2=new ArrayList<Double>();
 	   
   	  	//Cursor cursor2=query("humi",2);
   	    Cursor cursor2=query("humi where id=020101");
   	  	int len=cursor2.getCount();
   	  	if(len>0)
   	  	{
   	  		while(cursor2.moveToNext())
   	  		{
   	  			String mydate=cursor2.getString(cursor2.getColumnIndex("time"));
   	  			Double value2=cursor2.getDouble(cursor2.getColumnIndex("Humi"));
   	  			SimpleDateFormat simformat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
   	  			Date date = null;
   	  			try {
   	  				date = simformat.parse(mydate);
   	  				} catch (ParseException e)
   	  				{
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				} catch (java.text.ParseException e) {
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				}
   	  			xV2.add(date);
   	  			yV2.add(value2);
   	  		}
   	  		Date xxV2[]=new Date[xV2.size()];
   	  		Double yyV2[]=new Double[yV2.size()];
   	  		xV2.toArray(xxV2);
   	  		yV2.toArray(yyV2);
   	  		x.add(xxV2);
   	  		y.add(yyV2); 	   
   	  		XYMultipleSeriesDataset dataset = buildDateDataset(titles, x, y);
   	  		int[] colors = new int[] { Color.BLUE};
   	  		PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND}; 
   	  		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);
   	  		setChartSettings(renderer,"湿度随时间统计曲线", "时间(X)", "湿度(Y)",x.get(0)[0].getTime(),x.get(0)[len-1].getTime(), 0, 4 , Color.WHITE, Color.WHITE);
   	  		//��ʾͼ��
   	  		if(chart11==null)
   	  		{
   	  			LinearLayout layout=(LinearLayout)findViewById(R.id.chart_show);
   	  			chart11 =ChartFactory.getTimeChartView(CurveActivity.this,dataset,renderer, "MM-dd HH:mm ");
   	  			layout.removeAllViewsInLayout();
   	  			layout.addView(chart11, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
   	  			chart11=null;
   	  		}
   	  		else
   	  		{
   	  			chart11.repaint();
   	  		}
   	  		//��ӵ��ͼ���¼�����
   	  		renderer.setClickEnabled(true);
   	  		renderer.setSelectableBuffer(100);
   	  	}
   	  	else
   	  	{
   	  		Toast.makeText(CurveActivity.this, "数据库为空！", Toast.LENGTH_LONG).show();
   	  	}
    }
	private void showlumi()
	{
		String[] titles = new String[] {"节点一光照强度"};
        List<Date[]> x = new ArrayList<Date[]>();
   	    List<Double[]> y =new ArrayList<Double[]>();

        List<Date> xV2=new ArrayList<Date>();
        List<Double> yV2=new ArrayList<Double>();
 	   
   	  	//Cursor cursor2=query("lumi",4);
   	    Cursor cursor2=query("lumi where id=020101");
   	  	int len=cursor2.getCount();
   	  	if(len>0)
   	  	{
   	  		while(cursor2.moveToNext())
   	  		{
   	  			String mydate=cursor2.getString(cursor2.getColumnIndex("time"));
   	  			Double value2=cursor2.getDouble(cursor2.getColumnIndex("Lumi"));
   	  			SimpleDateFormat simformat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
   	  			Date date = null;
   	  			try {
   	  				date = simformat.parse(mydate);
   	  				} catch (ParseException e)
   	  				{
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				} catch (java.text.ParseException e) {
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				}
   	  			xV2.add(date);
   	  			yV2.add(value2);
   	  		}
   	  		Date xxV2[]=new Date[xV2.size()];
   	  		Double yyV2[]=new Double[yV2.size()];
   	  		xV2.toArray(xxV2);
   	  		yV2.toArray(yyV2);
   	  		x.add(xxV2);
   	  		y.add(yyV2); 	   
   	  		XYMultipleSeriesDataset dataset = buildDateDataset(titles, x, y);
   	  		int[] colors = new int[] { Color.CYAN};
   	  		PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND}; 
   	  		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);
   	  		setChartSettings(renderer,"光照强度随时间统计曲线", "时间(X)", "光照强度(Y)",x.get(0)[0].getTime(),x.get(0)[len-1].getTime(), 0, 4 , Color.WHITE, Color.WHITE);
   	  		//��ʾͼ��
   	  		if(chart11==null)
   	  		{
   	  			LinearLayout layout=(LinearLayout)findViewById(R.id.chart_show);
   	  			chart11 =ChartFactory.getTimeChartView(CurveActivity.this,dataset,renderer, "MM-dd HH:mm ");
   	  			layout.removeAllViewsInLayout();
   	  			layout.addView(chart11, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
   	  			chart11=null;
   	  		}
   	  		else
   	  		{
   	  			chart11.repaint();
   	  		}
   	  		//��ӵ��ͼ���¼�����
   	  		renderer.setClickEnabled(true);
   	  		renderer.setSelectableBuffer(100);
   	  	}
   	  	else
   	  	{
   	  		Toast.makeText(CurveActivity.this, "数据库为空！", Toast.LENGTH_LONG).show();
   	  	}
    }
	@SuppressLint("SimpleDateFormat") private void showco2()
	{
		String[] titles = new String[] {"节点一CO2浓度"};
        List<Date[]> x = new ArrayList<Date[]>();
   	    List<Double[]> y =new ArrayList<Double[]>();

        List<Date> xV2=new ArrayList<Date>();
        List<Double> yV2=new ArrayList<Double>();
 	   
   	  //	Cursor cursor2=query("co2",3);
   	  	Cursor cursor2=query("co2 where id=020101");
   	  	int len=cursor2.getCount();
   	  	if(len>0)
   	  	{
   	  		while(cursor2.moveToNext())
   	  		{
   	  			String mydate=cursor2.getString(cursor2.getColumnIndex("time"));
   	  			Double value2=cursor2.getDouble(cursor2.getColumnIndex("Co2"));
   	  			SimpleDateFormat simformat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
   	  			Date date = null;
   	  			try {
   	  				date = simformat.parse(mydate);
   	  				} catch (ParseException e)
   	  				{
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				} catch (java.text.ParseException e) {
   	  					// TODO Auto-generated catch block
   	  					e.printStackTrace();
   	  				}
   	  			xV2.add(date);
   	  			yV2.add(value2);
   	  		}
   	  		Date xxV2[]=new Date[xV2.size()];
   	  		Double yyV2[]=new Double[yV2.size()];
   	  		xV2.toArray(xxV2);
   	  		yV2.toArray(yyV2);
   	  		x.add(xxV2);
   	  		y.add(yyV2); 	   
   	  		XYMultipleSeriesDataset dataset = buildDateDataset(titles, x, y);
   	  		int[] colors = new int[] { Color.GREEN};
   	  		PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND}; 
   	  		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);
   	  		setChartSettings(renderer,"CO2浓度随时间统计曲线", "时间(X)", "CO2浓度(Y)",x.get(0)[0].getTime(),x.get(0)[len-1].getTime(), 0, 4 , Color.WHITE, Color.WHITE);
   	  		//��ʾͼ��
   	  		if(chart11==null)
   	  		{
   	  			LinearLayout layout=(LinearLayout)findViewById(R.id.chart_show);
   	  			chart11 =ChartFactory.getTimeChartView(CurveActivity.this,dataset,renderer, "MM-dd HH:mm ");
   	  			layout.removeAllViewsInLayout();
   	  			layout.addView(chart11, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
   	  			chart11=null;
   	  		}
   	  		else
   	  		{
   	  			chart11.repaint();
   	  		}
   	  		//��ӵ��ͼ���¼�����
   	  		renderer.setClickEnabled(true);
   	  		renderer.setSelectableBuffer(100);
   	  	}
   	  	else
   	  	{
   	  		Toast.makeText(CurveActivity.this, "数据库为空！", Toast.LENGTH_LONG).show();
   	  	}
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
					    CurveActivity.this.finish();
					}
				}).setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
					
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
				intent.setClass(CurveActivity.this,MainActivity.class);
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
			Intent intent = new Intent();
			intent.setClass(CurveActivity.this, VideoActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
			finish();
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
			intent.setClass(CurveActivity.this,   ControlActivity.class);
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
			intent.setClass(CurveActivity.this, ContactActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoommin, R.anim.zoomout);
			finish();
		}
	};
	public boolean onCreateOptionsMenu(Menu menu) {
		  // TODO Auto-generated method stub
		 menu.add(0, 1, 1, "注销").setIcon(R.drawable.zhuxiao);;
	     menu.add(0, 2, 2,  "退出").setIcon(R.drawable.tuichu);;
		  return super.onCreateOptionsMenu(menu); 
		 }
	 public boolean onMenuItemSelected(int featureId, MenuItem item) {
	        // TODO Auto-generated method stub
	        if(item.getItemId()==1){
	        		Intent intent = new Intent();
	        		intent.setClass(CurveActivity.this,Login.class);
	        		startActivity(intent);
	        		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	                finish();
	              }
	        if(item.getItemId()==2){
              finish();
            }
	        return super.onMenuItemSelected(featureId, item);
	    }
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
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
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

