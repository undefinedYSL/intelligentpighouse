package com.example.fr.sanxing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by FR on 2017/5/3.
 */
public class Login extends AppCompatActivity {

    private Button login;
    private Button quit;
    private EditText UserName,PassWord;
    private String mResponse;
    private CheckBox checkbox1;
    private SharedPreferences sp;
    private String service_LoginMessage;
    private  String username;
    public static String admin;

    public String getService_LoginMessage() {
        return service_LoginMessage;
    }
    public void setService_LoginMessage(String service_LoginMessage) {
        this.service_LoginMessage = service_LoginMessage;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.setTitle("智能化母猪养殖管控终端");
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        login = (Button) findViewById(R.id.login_btn_login);
        UserName = (EditText) findViewById(R.id.login_edit_account);
        PassWord = (EditText) findViewById(R.id.login_edit_pwd);
        checkbox1 = (CheckBox) findViewById(R.id.login_cb_savepwd);
        quit = (Button) findViewById(R.id.login_btn_quit);
        if (sp.getBoolean("ISCHECK", true)) {
            //记录密码的状态
            checkbox1.setChecked(true);
            UserName.setText(sp.getString("USER_NAME", ""));
            PassWord.setText(sp.getString("PASSWORD", ""));

            if (sp.getBoolean("AUTO_ISCHECK", true)) {
                //设置界面为自动登陆
                //
                checkbox1.setChecked(true);
                String a = UserName.getText().toString();
                String b = PassWord.getText().toString();
            }
            quit.setOnClickListener(new MyListener2());
            checkbox1.setOnCheckedChangeListener(new MyListener3());
        }
    }

    public void click(View view){
        SharedPreferences.Editor editor=sp.edit();
        final String username=UserName.getText().toString().trim();
        final String password=PassWord.getText().toString().trim();
        if(checkbox1.isChecked()){
            editor.putString("USER_NAME", username);
            editor.putString("PASSWORD", password);
            editor.commit();
        }

        final String sendMessage="login"+"@"+UserName.getText()+"@"+PassWord.getText();//login@OK@username@password+"@"

        Toast.makeText(Login.this, "正在连接服务器",Toast.LENGTH_SHORT ).show();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(Login.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }
        else{
            new Thread(){
                public void run(){
//                    final String result=GetPostUtil.sendPost("http://192.168.43.222:8888/myApps",sendMessage);
                    final String result=send_ToService.send_Message(sendMessage);

                    if(result!=null){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
//                                Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
                                setService_LoginMessage(result);
                                check_Login();
                                Login.this.finish();
                            }

                        });
                    }else{
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "服务器连接超时,用户请求失败",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.putExtra("str", "NO");
                                intent.setClass(Login.this,MainActivity.class);
                                startActivity(intent);
                                Login.this.finish();
                            }
                        });
                    }
                };
            }.start();
        }

    }

    public  void check_Login(){
        String str=getService_LoginMessage();//login@OK@username@password
        String []strs=str.split("@");
        String enter=strs[1];
        if("OK".equals(enter)){
            Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();;
            username=strs[2];
            Intent intent=new Intent();
            intent.putExtra("str", "OK");
            intent.setClass(Login.this,MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(Login.this, "您输入的用户名或密码有误，请重新输入", Toast.LENGTH_LONG).show();
        }
    }

class MyListener2 implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Login.this.finish();
    }

}
    class MyListener3 implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if(checkbox1.isChecked()){
                sp.edit().putBoolean("ISCHECK", true).commit();
            }else {
                sp.edit().putBoolean("ISCHECK", false).commit();
            }

        }
    }
}
