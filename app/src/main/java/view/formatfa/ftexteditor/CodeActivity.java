package view.formatfa.ftexteditor;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import view.formatfa.color.ColorViewTest;
import view.formatfa.ftexteditor.Save.FileSave;
import view.formatfa.ftexteditor.Save.Save;
import view.formatfa.ftexteditor.Save.SaveFactory;
import view.formatfa.ftexteditor.dialog.NumberEditDialog;
import view.formatfa.ftexteditor.utils.Utils;
import view.formatfa.ftexteditor.view.DataRead;
import view.formatfa.ftexteditor.view.FileChoose;
import view.formatfa.ftexteditor.view.FileDataRead;
import view.formatfa.ftexteditor.view.ScriptView;
import view.formatfa.ftexteditor.view.ScriptViewListener;
import view.formatfa.ftexteditor.view.light.JavaLight;
import view.formatfa.ftexteditor.view.light.LightFactory;
import view.formatfa.ftexteditor.view.light.SmaliLight;
import view.formatfa.ftexteditor.view.light.XmlLight;
import view.formatfa.ftexteditor.view.nav.JavaNav;
import view.formatfa.ftexteditor.view.nav.Nav;
import view.formatfa.ftexteditor.view.nav.NavFactory;
import view.formatfa.ftexteditor.view.nav.NavListAdapter;
import view.formatfa.ftexteditor.view.nav.NavResult;
import view.formatfa.ftexteditor.view.read.BinaryStringRead;
import view.formatfa.ftexteditor.view.suggest.GeGeSuggests;
import view.formatfa.ftexteditor.view.suggest.SuggestFactory;
import view.formatfa.ftexteditor.widget.EditDialog;
import view.formatfa.ftexteditor.widget.EditDialogListener;

public class CodeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ScriptViewListener,View.OnClickListener {

    public final static int request_fileview=1,request_choose=2;

    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawLayout;


    private RelativeLayout menu1,menu2;
    private ListView navListView,navListView2;
    private ScriptView scriptView;


    private ProgressBar progressBar;

    private SharedPreferences sp;

    private Save save;
    private final String pre_fontsize = "fontSize",pre_light="lightway",pre_autoScroll="autoScroll",pre_simplecursor="isSimpleCursor",pre_color="color",pre_theme="themes";
    Nav nav;
    String [] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission,10);
            if(checkSelfPermission(permission[0])== PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this,"权限不足", Toast.LENGTH_LONG).show();
                return;
            }
        }


        initDrawer();
     scriptView = findViewById(R.id.script);
     scriptView.setViewListener(this);
        progressBar = findViewById(R.id.progress);
        scriptView.getColorSetting().setTextSize ( Integer.parseInt(sp.getString(pre_fontsize,"55")));
        scriptView.initFont();
        scriptView.setSuggests(new GeGeSuggests());


        save = new FileSave();
        setConfig();





    }

    private void loadPre() {

        File dir  = new File(Environment.getExternalStorageDirectory(),"apktoolhelper");
        if(dir.exists()==false)dir.mkdirs();

        if(new File(dir,"a.smali").exists())return;


        Utils.upassets(getAssets(),"a.smali",new File(dir,"a.smali"));
        Utils.upassets(getAssets(),"a.xml",new File(dir,"a.xml"));

        Utils.upassets(getAssets(),"a.java",new File(dir,"a.java"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        loadPre();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initDrawer() {

        sp= PreferenceManager.getDefaultSharedPreferences(this);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        drawLayout = findViewById(R.id.drawlayout);

        navListView = findViewById(R.id.nav);
        navListView2 = findViewById(R.id.nav2);

        navListView.setOnItemClickListener(this);
        navListView2.setOnItemClickListener(this);
        navListView.setDividerHeight(18);
        navListView2.setDividerHeight(18);

        navListView.setDivider(new ColorDrawable(Color.BLUE));
        navListView2.setDivider(new ColorDrawable(Color.BLUE));
        menu1= findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);

        menu1.setBackgroundColor(Color.WHITE);
        menu2.setBackgroundColor(Color.WHITE);
        toggle = new ActionBarDrawerToggle(this,drawLayout,R.string.opendraw,R.string.closedraw);
        toggle.syncState();
        drawLayout.addDrawerListener(toggle);
        drawLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if(drawerView.getId()==R.id.menu1)
                {
                loadNav1();
                }
                else
                if(drawerView.getId()==R.id.menu2)
                {
                    loadNav2();
                }





            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        ((TextView)findViewById(R.id.navtip)).setOnClickListener(this);
        ((TextView)findViewById(R.id.navtip2)).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch(item.getItemId())
       {
           case android.R.id.home:
               if(drawLayout.isDrawerOpen(menu1))
               {
                   drawLayout.closeDrawer(menu1);
               }

               else if (drawLayout.isDrawerOpen(menu2))
               {
                   drawLayout.closeDrawer(menu2);
               }
               else
                   drawLayout.openDrawer(menu1);

               break;
           case R.id.filechoose:
                Intent intent = new Intent(CodeActivity.this, FileChooseActivity.class);
                startActivityForResult(intent,request_fileview);
               break;

           case R.id.info:
               AlertDialog.Builder builder = new AlertDialog.Builder(CodeActivity.this);
               builder.setTitle("信息");
               builder.setMessage(scriptView.getInfo());
               builder.setPositiveButton("取消",null).show();
               break;
           case R.id.left:
               scriptView.setSelectionStart(scriptView.getNowSelection().getLine(),scriptView.getNowSelection().getCharOffset());
               scriptView.invalidate();
               break;
           case R.id.right:
               scriptView.setSelectionEnd(scriptView.getNowSelection().getLine(),scriptView.getNowSelection().getCharOffset());
               scriptView.invalidate();
               break;
           case R.id.setFontSize:
               setTextSizeDialog();

               break;
           case R.id.settings:

               Intent intent2 = new Intent(CodeActivity.this,SettingsActivity.class);
               startActivity(intent2);
               break;
           case R.id.save:
               if(scriptView.getDataRead()==null)
               {
                   Toast.makeText(CodeActivity.this,"没有找到文件读取器",Toast.LENGTH_SHORT).show();
                return false;
               }
               new saveTask().execute(0);
               break;
           case R.id.goline:
//               AlertDialog.Builder ab = new AlertDialog.Builder(CodeActivity.this);
//               ab.setTitle("dd");
//               ColorViewTest test = new ColorViewTest(CodeActivity.this) ;
//               ab.setView(test);
//               ab.show();

               getLine();
               break;
           case R.id.naveng:
               setNavEng();
               break;
               case R.id.lighteng:
           setLightEng();
           break;
           case R.id.suggesteng:
               setSuggestEng();
               break;
           case R.id.saveeng:
               setSaveEng();
               break;
           case R.id.search:
               search();
               break;
           case R.id.searchnext:
               nextSearch();
               break;
           case R.id.saveas:
               if(save==null)
               {
                   Toast.makeText(CodeActivity.this,"保存引擎为空",Toast.LENGTH_SHORT).show();return false ;
               }
               save.save(this,scriptView);
               break;
       }

        return super.onOptionsItemSelected(item);
    }
    private void getLine()
    {

        NumberEditDialog dialog = new NumberEditDialog(this);
        dialog.setListener(new NumberEditDialog.Listener() {
            @Override
            public void onCLick(NumberEditDialog dialog, String string) {
                try {
                    scriptView.gotoLineAndOffset(Integer.parseInt(string), 0);
                }catch (Exception e){

                }


            }
        });
        dialog.getDialog().setTitle("跳到行");
        dialog.show();




    }
    private void setConfig()
    {
        if(light!=null)
        try {
            scriptView.setLight(LightFactory.getLight(light));
        } catch (Exception e) {
            e.printStackTrace();
        }
        else
            scriptView.setLight(null);

        if(suggest!=null)
        {
            try {
                scriptView.setSuggests(SuggestFactory.getSuggest(suggest));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            scriptView.setSuggests(null);

        scriptView.getColorSetting().setAutoScroll(sp.getBoolean(pre_autoScroll,false));
        scriptView.getColorSetting().setTextSize( Integer.parseInt(sp.getString(pre_fontsize,"55")));

        int theme = Integer.parseInt(sp.getString(pre_theme,"1"));
        Toast.makeText(this,"theme:"+theme,Toast.LENGTH_SHORT).show();
        if(theme==0)

            scriptView.getColorSetting().parseConfig(getString(R.string.lightcolor));
        else if(theme==1)

            scriptView.getColorSetting().parseConfig(getString(R.string.darkcolor));
        else
        {
            scriptView.getColorSetting().parseConfig(sp.getString(pre_color
                    ,null));
        }
        scriptView.setIsismpleCursor(sp.getBoolean(pre_simplecursor,true));
    }

    @Override
    public void onLineClick(int p, char c, int offset) {
        getSupportActionBar().setSubtitle("line:"+p+" char:"+c+ " co:"+offset);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.navtip)
        {
            isNav1=false;
            ((TextView)findViewById(R.id.navtip)).setText("....");
            loadNav1();
        }
        else if(v.getId()==R.id.navtip2)
        {
            isNav2=false;
            ((TextView)findViewById(R.id.navtip2)).setText("....");
            loadNav2();
        }
    }

    class saveTask extends AsyncTask
    {


        @Override
        protected void onPreExecute() {
            Toast.makeText(CodeActivity.this,"开始保存....",Toast.LENGTH_SHORT).show();
            findViewById(R.id.progress).setVisibility(View.VISIBLE);

        }

        @Override
        protected Object doInBackground(Object[] objects) {


            scriptView.getDataRead().save(scriptView);

            return 1;
        }

        @Override
        protected void onPostExecute(Object o) {

            findViewById(R.id.progress).setVisibility(View.INVISIBLE);
            Toast.makeText(CodeActivity.this,"保存完成",Toast.LENGTH_SHORT).show();

        }
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
                scriptView.getColorSetting().setTextSize(progress);
                scriptView.setSetting(null);
                scriptView.invalidate();
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
    private String light;
    private String suggest;
    private String savestr;


    class loadCodeTask extends AsyncTask{
        DataRead read ;
        public loadCodeTask(DataRead read) throws FileNotFoundException {
            this.read=read;


            path=read.getName();
        }

        String path;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            read.load();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            progressBar.setVisibility(View.INVISIBLE);

            if (path.endsWith(".xml"))
                light="xml";
            else if (path.endsWith(".smali")) {
                nav1="smali";
                nav2="smali";
                light="smali";
            }else if (path.endsWith(".java")){
                light="java";
                nav2="java";
                nav1="java";
            }
            else {
                light=null;
                nav1=null;
                nav2=null;
            }

            setConfig();
            scriptView.setCode(read);
        }
    }
    public void newFileRead(String way,final String path)
    {
        isNav1=false;
        isNav2=false;
        DataRead read = null;
        if("text".equals(way)) {
            try {
                read = new FileDataRead(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
            if("binary".equals(way))
            {
                read = new BinaryStringRead(path);
            }
        try {
            new loadCodeTask(read).execute(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void loadCode(DataRead read)
    {
        scriptView.initFont();
     //   scriptView.setCode(null);




    }

    private void setNavEng() {

        int now = -1;
        for (int i = 0; i < NavFactory.navs.length; i += 1)
        {
            if(NavFactory.navs[i].equals(nav1)){
                now = i;break;
            }

        }
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setSingleChoiceItems(NavFactory.navs, now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nav1=NavFactory.navs[which];
                nav2=NavFactory.navs[which];
                dialog.dismiss();

                setConfig();
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("无", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nav1=null;
                nav2=null;

            }
        }).show();




    }
    private void setSaveEng() {

        int now = -1;
        for (int i = 0; i < SaveFactory.saves.length; i += 1)
        {
            if( SaveFactory.saves[i].equals(savestr)){
                now = i;break;
            }

        }
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setSingleChoiceItems(SaveFactory.saves, now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                savestr=SaveFactory.saves[which];
                dialog.dismiss();
                save = SaveFactory.getSave(savestr);

            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("无", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save=null;

            }
        }).show();




    }

    private void setLightEng() {

        int now = -1;
        for (int i = 0; i < LightFactory.lights.length; i += 1)
        {
            if( LightFactory.lights[i].equals(light)){
                now = i;break;
            }

        }
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setSingleChoiceItems(LightFactory.lights, now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                light=LightFactory.lights[which];
                dialog.dismiss();
                setConfig();

            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("无", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                light=null;

            }
        }).show();




    }
    private void setSuggestEng() {

        int now = -1;
        for (int i = 0; i < SuggestFactory.suggests.length; i += 1)
        {
            if( SuggestFactory.suggests[i].equals(light)){
                now = i;break;
            }

        }
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setSingleChoiceItems(SuggestFactory.suggests, now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                suggest=SuggestFactory.suggests[which];
                dialog.dismiss();
                setConfig();

            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("无", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                suggest=null;
                setConfig();

            }
        }).show();




    }


    private List<ScriptView. Selection> tempSearchResult;
    private int searchResultIndex ;
    private String searchText;
    private void search()
    {

        EditDialog dialog = new EditDialog(this);
        if(searchText!=null)
        {
            dialog.getEdit().setText(searchText);
        }
        dialog.setListener(new EditDialogListener() {
            @Override
            public void onInput(EditDialog dialog, String str) {
                tempSearchResult = scriptView.search(str);
                searchResultIndex = 0;

                if(tempSearchResult==null||tempSearchResult.size()==0)
                {
                    Toast.makeText(CodeActivity.this,"没有了",Toast.LENGTH_SHORT).show();
                    return;
                }

                searchText=str;

                nextSearch();

            }
        });

        dialog.getDialog().setTitle("搜索");
        dialog.getDialog().setNeutralButton("下一个",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nextSearch();

            }
        });
        dialog.show();



    }
    private void nextSearch()
    {
        if(tempSearchResult==null||tempSearchResult.size()==0)
            return;
        if(searchResultIndex==tempSearchResult.size())searchResultIndex=0;
        scriptView.gotoLineAndOffset(tempSearchResult.get(searchResultIndex).getLine(),tempSearchResult.get(searchResultIndex).getCharOffset());


        searchResultIndex+=1;
    }
    boolean isNav1=false;
    boolean isNav2=false;
    String nav1 = null;
    String nav2=null;
    public void loadNav1()
    {
        if(isNav1)return;


        try {
            nav =  NavFactory.getNav(nav1);
        } catch (Exception e) {
            e.printStackTrace();
       return;
        }
        if(nav1==null)return;
        ((TextView)findViewById(R.id.navtip)).setText(nav.getNavTitle()[0]);
        new AsyncTask(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                findViewById(R.id.navprogress).setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] objects) {

              //  nav = new JavaNav();
               List<NavResult> re =  nav.getNav(scriptView,null,0);
             if(re==null)return null;
                NavListAdapter adapter = new NavListAdapter(CodeActivity.this,re);

                return adapter;
            }

            @Override
            protected void onPostExecute(Object o) {

                if(o!=null) {
                    NavListAdapter adapter = (NavListAdapter) o;

                    navListView.setAdapter((NavListAdapter) o);
                    Toast.makeText(CodeActivity.this,"setAdapter:"+adapter.getCount(),Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(CodeActivity.this,"null",Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.navprogress).setVisibility(View.INVISIBLE);
                isNav1 = true;
            }
        }.execute(0);

    }
    public void loadNav2()
    {

        if(isNav2)return;
        try {
            nav = NavFactory.getNav(nav2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(nav==null)return;

        ((TextView)findViewById(R.id.navtip2)).setText(nav.getNavTitle()[1]);
        new AsyncTask(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                findViewById(R.id.navprogress2).setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] objects) {


                List<NavResult> re =  nav.getNav(scriptView,null,1);
                if(re==null)return null;
                NavListAdapter adapter = new NavListAdapter(CodeActivity.this,re);

                return adapter;
            }

            @Override
            protected void onPostExecute(Object o) {

                if(o!=null) {
                    NavListAdapter adapter = (NavListAdapter) o;

                    navListView2.setAdapter((NavListAdapter) o);
                    Toast.makeText(CodeActivity.this,"setAdapter2:"+adapter.getCount(),Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(CodeActivity.this,"null",Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.navprogress2).setVisibility(View.INVISIBLE);
                isNav2 = true;
            }
        }.execute(0);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.code,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode== Activity.RESULT_OK)
        {

            switch (requestCode)
            {
                case request_fileview:


                    String path = data.getStringExtra("path");
                    String way = data.getStringExtra("read");

                    if(way==null)way="text";
                    newFileRead(way,path);
                   // Toast.makeText(this,"path:"+path,Toast.LENGTH_SHORT).show();
                   // Toast.makeText(this,"2:"+data.getStringExtra("path"),Toast.LENGTH_SHORT).show();
                    break;

                case request_choose:
                    String p = data.getStringExtra("path");

                    if(fileChooseListener!=null)fileChooseListener.onChoose(new File(p));
                    break;


                    default:









            }







        }
        else
        {
            Toast.makeText(this,"取消了",Toast.LENGTH_SHORT).show();
        }


    }



    private void exit()
    {










    }
    FileChooseListener fileChooseListener;

    public void startChooseFile(boolean isdir,FileChooseListener lis)
    {
        fileChooseListener = lis;
        Intent intent = new Intent(CodeActivity.this, FileChooseActivity.class);
        intent.putExtra("isdir",isdir);

        startActivityForResult(intent,request_choose);

    }
    @Override
    public void onBackPressed() {

        if(drawLayout.isDrawerOpen(menu1))
        {
            drawLayout.closeDrawer(menu1);
        }
        else
            super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.nav)
        {
            drawLayout.closeDrawer(menu1);
            NavResult re = (NavResult) parent.getItemAtPosition(position);
            scriptView.gotoLineAndOffset(re.getLine(),re.getCharOffset());

        }
        else if(parent.getId()==R.id.nav2)
        {
            drawLayout.closeDrawer(menu2);
            NavResult re = (NavResult) parent.getItemAtPosition(position);
            scriptView.gotoLineAndOffset(re.getLine(),re.getCharOffset());
        }
    }
}
