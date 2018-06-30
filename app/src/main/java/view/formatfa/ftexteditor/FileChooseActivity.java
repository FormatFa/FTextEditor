package view.formatfa.ftexteditor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.util.Arrays;

import view.formatfa.ftexteditor.Adapter.FileAdapter;
import view.formatfa.ftexteditor.view.read.ReadFactory;

public class FileChooseActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private File[] files;
    private File nowDir;
    private ListView list;
    private SharedPreferences sp;
    private Spinner readEng ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose);

        init();

    }

    private void init() {
        Intent i = getIntent();
        if(i.getBooleanExtra("isdir",false))
        {
            findViewById(R.id.selectdir).setVisibility(View.VISIBLE);
            findViewById(R.id.selectdir).setOnClickListener(this);
        }


    list = findViewById(R.id.list);
    list.setOnItemClickListener(this);
    findViewById(R.id.up).setOnClickListener(this);
        findViewById(R.id.cancal).setOnClickListener(this);
        readEng = findViewById(R.id.readeng);
        readEng.setAdapter(new ArrayAdapter(this,R.layout.list_item,R.id.textView, ReadFactory.reads));
    sp = PreferenceManager.getDefaultSharedPreferences(this);
    String path = sp.getString("path",null);
    if(path!=null)
    {
        nowDir = new File(path);
    }
    else

    nowDir = Environment.getExternalStorageDirectory();
    upfile(nowDir);

    }


    private void upfile(File path)
    {


        if(!path.canRead())return;
        if(!path.isDirectory())return;


        files = path.listFiles();
        Arrays.sort(files);
        FileAdapter adapter = new FileAdapter(this,files);

        list.setAdapter(adapter);
        nowDir = path;

        sp.edit().putString("path",nowDir.getAbsolutePath()).apply();
        getSupportActionBar().setSubtitle(nowDir.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.up:
                upfile(nowDir.getParentFile());
                break;
            case R.id.cancal:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.selectdir:
                selectFile(nowDir);
                break;



        }
    }
    private void selectFile(File file)
    {
        Intent intent = new Intent();
        intent.putExtra("path",file.getAbsolutePath());
        intent.putExtra("read",ReadFactory.reads[ readEng.getSelectedItemPosition()]);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = (File) parent.getItemAtPosition(position);
        if(file.isDirectory())
        {
            upfile(file);
        }
        else
        {

            selectFile(file);

        }
    }
}
