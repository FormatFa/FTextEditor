package view.formatfa.ftexteditor;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import plugin.kpa.scriptparser.Script.FParser;
import plugin.kpa.scriptparser.Script.InvokeMethodResult;
import plugin.kpa.scriptparser.Script.ScriptListener;
import view.formatfa.ftexteditor.view.DataRead;
import view.formatfa.ftexteditor.view.ScriptView;

public class RunActivity extends AppCompatActivity implements DataRead {



    class run extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            String code = (String) objects[0];
            try {
                parser.parseCodes(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
    List<String> logs;
    FParser parser = new FParser();
    ScriptView view;
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        logs=new ArrayList<>();
        String code = getIntent().getStringExtra("code");
        view= new ScriptView(this);
        view.setCode(this);
        view.initFont();
        setContentView(view);
        parser.addScriptListener(new ScriptListener() {
            @Override
            public InvokeMethodResult invokeMethod(FParser parser, String name, Object[] args) {
                InvokeMethodResult result = new InvokeMethodResult();
                switch (name)
                {
                    case "print":
                        logs.add(""+args[0]);
                        view.invalidate();
                        break;
                    case "put":
                        temp+=args[0];
                        break;
                    case "putline":
                            logs.add(temp);
                        temp="";
                        break;

                    default:
                        result.setHasMethod(false);
                }
                return result;
            }

            @Override
            public void parserLog(String log) {

            }
        });
        new run().execute(code);

    }

    @Override
    public String getLine(ScriptView view, int position) {
        if(position>=logs.size())return "";
        return logs.get(position);
    }

    @Override
    public String getString(ScriptView view) {
        return null;
    }

    @Override
    public void save(ScriptView view) {

    }

    @Override
    public String getLineSep(ScriptView view) {
        return "\n";
    }

    @Override
    public int getLineSize(ScriptView view) {
        return logs.size();
    }

    @Override
    public boolean setLine(ScriptView view, int p, String str) {
        return false;
    }

    @Override
    public boolean addLine(ScriptView view, int p, String str) {
        return logs.add(str);
    }

    @Override
    public boolean removeLine(ScriptView view, int p) {
        return false;
    }

    @Override
    public String getDescript() {
        return "脚本运行输出";
    }

    @Override
    public String getName() {
        return "ff.log";
    }

    @Override
    public boolean load() {
        return false;
    }
}
