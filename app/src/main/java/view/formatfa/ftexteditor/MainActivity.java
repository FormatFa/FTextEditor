package view.formatfa.ftexteditor;

import android.Manifest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import view.formatfa.ftexteditor.dialog.NumberEditDialog;
import view.formatfa.ftexteditor.view.FileChoose;
import view.formatfa.ftexteditor.view.FileDataRead;
import view.formatfa.ftexteditor.view.FileUtils;
import view.formatfa.ftexteditor.view.ScriptView;
import view.formatfa.ftexteditor.view.ScriptViewListener;
import view.formatfa.ftexteditor.view.light.JavaLight;
import view.formatfa.ftexteditor.view.light.XmlLight;

public class MainActivity extends AppCompatActivity {



    public static final String[] lightways = {"不高亮","自动判断","Java","xml"};
    public static final String[] lightwaysKey = {"no","auto","java","xml"};
    ProgressDialog dialog;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {


                case 0:
                    dialog=ProgressDialog.show(MainActivity.this,"。。","加载中..");
                    break;
                case 1:
                    dialog.dismiss();
                    view.invalidate();

                    break;
            }
            super.handleMessage(msg);
        }
    };
    int fileRequestCode = 10;
    Intent intent ;
    private SharedPreferences sp;


    private final String pre_fontsize = "fontSize",pre_light="lightway",pre_autoScroll="autoScroll";
    public static String getIntentPath(Intent i)
    {
        Uri uri = i. getData();

        if(uri == null)
            return null;
        return uri.getPath();

    }
    String [] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ScriptView view ;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


       actionBar =  getSupportActionBar();


        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission,10);
            if(checkSelfPermission(permission[0])== PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this,"权限不足", Toast.LENGTH_LONG).show();
                return;
            }
        }



        view = new ScriptView(this);
        initConfig();
        view.setLight(new JavaLight());



        String[] tests = new String[]{"java2","dex2oat.cc"};

        int p = (int) (Math.random()*2);
        //  String code = FileUtils.readFile("/sdcard/ScriptView.java");
        if(getIntentPath(getIntent())!=null) {

            try {
                loadCode(new FileInputStream(getIntentPath(getIntent())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                if(p==0)view.setLight(new JavaLight());
                if(p==1)view.setLight(new XmlLight());

                loadCode(getAssets().open(tests[p]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        view.setViewListener(new ScriptViewListener() {
//            @Override
//            public void onLineClick(int p, char c, int offset) {
//                getSupportActionBar().setSubtitle(""+p+" "+offset+" "+c );
//            }
//
//            @Override
//            public void onStartLoadData() {
//             //   Toast.makeText(MainActivity.this,"start load:",Toast.LENGTH_SHORT).show();
//
//
//                handler.sendEmptyMessage(0);
//
//            }
//
//            @Override
//            public void onLoadDataDone() {
//handler.sendEmptyMessage(1);
//            }
//
//
//        });
        setContentView(view);


    }

    private String configTag = "CodeEditConfig";
    private void initConfig() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String fontSize = sp.getString("fontSize","0");
        Log.d(configTag,"get config:"+fontSize);

        int size = Integer.parseInt(fontSize);

        view.getColorSetting().setTextSize(size);
        view.getColorSetting().setAutoScroll(sp.getBoolean(pre_autoScroll,false));
        view.invalidate();
    }


    void loadCode(InputStream is)
    {

            FileDataRead read = new FileDataRead(is);
            read.setListener(new FileDataRead.LoadListener() {
                @Override
                public void onstartLoad(FileDataRead read) {
                    dialog=ProgressDialog.show(MainActivity.this,"。。","加载中..");
                }

                @Override
                public void loaded(FileDataRead read) {
                    dialog.dismiss();

                    view.setCode(read);


                }
            });
            read.start();


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.about:

                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                builder3.setTitle("beta_0.9(6-14)");
                builder3.setMessage("1.复制粘帖\n2.高亮选择\nBeta_0.8(6-13)1.选择复制。删除.、\n2.xml代码高亮测试   6-12 0.71.加入选择，点击左，选择开始。点击右，选择右.\n\nbeta 0.6\n1.加入Java代码高亮\n2.加速度滑动");
                builder3.setPositiveButton("取消",null).show();

                break;
            case R.id.open:

                FileChoose cho = new FileChoose(MainActivity.this);
                cho.setListener(new FileChooseListener() {
                    @Override
                    public void onChoose(final File file) {

                      switchLight();
                        Toast.makeText(MainActivity.this,"onchoosse:"+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                        if(!file.isFile()||!file.canRead())return;
                        try {
                            FileDataRead read = new FileDataRead(file.getAbsolutePath());
                            read.setListener(new FileDataRead.LoadListener() {
                                @Override
                                public void onstartLoad(FileDataRead read) {
                                    dialog=ProgressDialog.show(MainActivity.this,"。。","加载中..");
                                }

                                @Override
                                public void loaded(FileDataRead read) {
                                    dialog.dismiss();


                                    view.setCode(read);
                                    switchLight();

                                }
                            });
                            read.start();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancle() {
                        Toast.makeText(MainActivity.this,"选择取消",Toast.LENGTH_SHORT).show();

                    }
                });
                cho.show();
//                intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(Intent.createChooser(intent,"选择要编辑的文本"),fileRequestCode);
                break;
            case R.id.save:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                builder2.setTitle("信息");
                builder2.setMessage("暂不支持");
                builder2.setPositiveButton("取消",null).show();
                break;
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("信息");
                builder.setMessage(view.getInfo());
                builder.setPositiveButton("取消",null).show();
                break;
            case R.id.left:
                view.setSelectionStart(view.getNowSelection().getLine(),view.getNowSelection().getCharOffset());
                view.invalidate();
                break;
            case R.id.right:
                view.setSelectionEnd(view.getNowSelection().getLine(),view.getNowSelection().getCharOffset());
                view.invalidate();
                break;
            case R.id.settings:

                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.setFontSize:
                setTextSizeDialog();

                break;
            case R.id.setLight:
                setLightWay();
                break;
            case R.id.run:
                StringBuilder builder1 = new StringBuilder();
                for(int i = 0;i<view.getDataRead().getLineSize(view);i+=1)
                {
                    builder1.append(view.getDataRead().getLine(view,i));
                    builder1.append("\n");

                }
                Intent i = new Intent(MainActivity.this,RunActivity.class);
                i.putExtra("code",builder1.toString());
                startActivity(i);
                break;
            case R.id.goline:
                getLine();
                break;
            case R.id.nav:
                break;

        }
        return super.onOptionsItemSelected(item);
    }



    private void setTextSizeDialog()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(100);

        int now = Integer.parseInt( sp.getString("fontSize","55"));
        seekBar.setProgress(now);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0)return;
                view.getColorSetting().setTextSize(progress);
                view.initFont();
                view.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.setView(seekBar);
        dialog.setTitle("设置字体大小");
        dialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(seekBar.getProgress()==0)return;
                sp.edit().putString(pre_fontsize,String.valueOf(seekBar.getProgress())).apply();

            }
        });
        dialog.setNegativeButton("取消",null).show();


    }
    private void setLightWay()
    {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
ab.setTitle(sp.getString(pre_light,""));
        ab.setItems(lightways, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp.edit().putString(pre_light,lightwaysKey[which]).apply();
                switchLight();
            }
        }).setNegativeButton("取消",null).show();



    }

    private void getLine()
    {

        NumberEditDialog dialog = new NumberEditDialog(this);
        dialog.setListener(new NumberEditDialog.Listener() {
            @Override
            public void onCLick(NumberEditDialog dialog, String string) {
                try {
                    view.gotoLineAndOffset(Integer.parseInt(string), 0);
                }catch (Exception e){

                }


                }
        });
        dialog.getDialog().setTitle("跳到行");
        dialog.show();




    }
    private void switchLight()
    {
        String way = sp.getString(pre_light,"auto");
        if(view.getDataRead().getName()!=null)
        switch(way)
        {

            case "auto":
                if(view.getDataRead().getName().endsWith(".xml"))
               view.setLight(new XmlLight());
                else if(view.getDataRead().getName().endsWith(".java"))
                    view.setLight(new JavaLight());
                break;
            case "no":
                view.setLight(null);

                break;
            case "java":
                view.setLight(new JavaLight());
                break;
            case "xml":
            view.setLight(new XmlLight());
            break;




        }

        view.getColorSetting().setAutoScroll(sp.getBoolean(pre_autoScroll,false));
        view.invalidate();

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if(requestCode==fileRequestCode&&resultCode== Activity.RESULT_OK)
//        {
//
//            final String path = getIntentPath(data);
//            if(path==null)return;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String data = FileUtils.readFile(path);
//                    view.setCode(data);
//                }
//            }).start();
//
//        }
//    }
}
