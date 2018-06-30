package view.formatfa.ftexteditor.Save;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import view.formatfa.ftexteditor.CodeActivity;
import view.formatfa.ftexteditor.FileChooseListener;
import view.formatfa.ftexteditor.R;
import view.formatfa.ftexteditor.view.DataRead;
import view.formatfa.ftexteditor.view.ScriptView;

public class FileSave implements Save , View.OnClickListener{

    private File out;
    private EditText path,name;
    private CodeActivity act;
    private ScriptView view;
    private Spinner spinner ;
    private String [] sep={"\n"},sepname={"\\n (Linux)"};
    private Dialog close;
    private Button save;
    @Override
    public void save(CodeActivity context, ScriptView view) {

        if(view.getDataRead()==null)return;

        this.view = view;
        String p = view.getDataRead().getName();

        act = context;
        AlertDialog.Builder ba = new AlertDialog.Builder(context);

        LinearLayout layout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.save_file,null);

        path = layout.findViewById(R.id.path);
        name = layout.findViewById(R.id.name);

        if(p!=null)
        {
            File temp = new File(p);
            path.setText(temp.getParentFile().getAbsolutePath());
            name.setText(temp.getName());
        }

        layout.findViewById(R.id.selectdir).setOnClickListener(this);
        save = layout.findViewById(R.id.save);
        save.setOnClickListener(this);


        spinner = layout.findViewById(R.id.sep);
        spinner.setAdapter(new ArrayAdapter(context,R.layout.list_item,R.id.textView,sepname));

        ba.setView(layout);
        ba.setPositiveButton(android.R.string.cancel,null);
        close= ba.show();





    }

    @Override
    public String getDescript() {
        return "普通文本文件保存";
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.save)
        {

            out = new File(path.getText().toString(),name.getText().toString());
            if(!out.getParentFile().canWrite())
            {
                Toast.makeText(act,"目录不可写，请选择其他的",Toast.LENGTH_SHORT).show();return;
            }
            new savaTask().execute(0);
        }
        else if(v.getId()==R.id.selectdir)
        {

            act.startChooseFile(true, new FileChooseListener() {
                @Override
                public void onChoose(File file) {
                    path.setText(file.getAbsolutePath());
                }

                @Override
                public void onCancle() {

                }
            });
        }
    }


    class savaTask extends AsyncTask
    {

        int max;
        DataRead rea= view.getDataRead();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            save.setText("保存中，请稍后......");

        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            save.setText(String.valueOf(values[0])+"/"+String.valueOf(max));
        }

        @Override
        protected Object doInBackground(Object[] objects) {



            String sp  =sep[ spinner.getSelectedItemPosition()];


             max = rea.getLineSize(view);

            try {
                OutputStream os = new FileOutputStream(out);
                String temp ;
                int i = 0;
                temp = rea.getLine(view,i);
                while( temp!=null)
                {
                    os.write(temp.getBytes());

                    if(max!=-1)
                    {
                        publishProgress(i);
                    }

                    i+=1;

                    temp = rea.getLine(view,i);
                    //最后一行不换行
                    if(temp!=null) os.write(sp.getBytes());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
          //  close.dismiss();
            Toast.makeText(act,"保存完成.",Toast.LENGTH_SHORT).show();
        }
    }
    private void save()
    {






    }
}
