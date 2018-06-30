package view.formatfa.ftexteditor.dialog;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import view.formatfa.ftexteditor.R;

public class NumberEditDialog {


    public interface  Listener
    {
        public void onCLick(NumberEditDialog dialog,String string);

    }

    class numerListener implements View.OnClickListener{

        int i ;

        public numerListener(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            text.append(String.valueOf(i));
        }
    }
    Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Context context;

    private AlertDialog.Builder dialog;

    private TextView text;
     int j;
    int[] ids = {R.id.number0,R.id.number1,R.id.number2,R.id.number3,R.id.number4,R.id.number5,R.id.number6,R.id.number7,R.id.number8,R.id.number9};
    public NumberEditDialog(Context context) {
        this.context = context;
        dialog= new AlertDialog.Builder(context);
        LayoutInflater in = LayoutInflater.from(context);

        View view = in.inflate(R.layout.numberedit,null);
        text = view.findViewById(R.id.number);
        text.setText("");
        view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });


      j = 0;
        for(int i : ids)
        {
            view.findViewById(i).setOnClickListener(new numerListener(j));

            j+=1;
        }
        dialog.setView(view);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(getListener()!=null)getListener().onCLick(NumberEditDialog.this,text.getText().toString());
            }
        });
        checkClipBoard();

    }

    private void checkClipBoard()
    {

        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

       if(clipboardManager.getPrimaryClip()==null)return;
        String text2 = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();

        int i = -1;
        try {
            i = Integer.parseInt(text2);
        }
        catch(NumberFormatException e)
        {
            return;
        }
        text.setText(text2);


    }
    public void show()
    {
        dialog.show();
    }
    public AlertDialog.Builder getDialog()
    {

        return  dialog;
    }
}
