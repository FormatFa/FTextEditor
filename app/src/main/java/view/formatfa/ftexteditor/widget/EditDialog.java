package view.formatfa.ftexteditor.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class EditDialog {

    private Context context;
    private EditDialogListener listener;


    private AlertDialog.Builder dialog ;
    private EditText edit;
    public EditDialog(Context context) {
        this.context = context;
        initDialog();
    }

    public EditDialogListener getListener() {
        return listener;
    }

    public void setListener(EditDialogListener listener) {
        this.listener = listener;
    }

    public EditText getEdit() {
        return edit;
    }

    public AlertDialog.Builder getDialog()
    {
        return dialog;
    }
    private void initDialog() {
        dialog = new AlertDialog.Builder(context);
        edit = new EditText(context);

        dialog.setView(edit);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(listener!=null)listener.onInput(EditDialog.this,edit.getText().toString());

            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public  void show()
    {


        dialog.show();
    }

}
