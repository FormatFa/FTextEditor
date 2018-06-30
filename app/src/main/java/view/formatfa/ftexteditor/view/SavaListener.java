package view.formatfa.ftexteditor.view;

import view.formatfa.ftexteditor.view.DataSave;
import view.formatfa.ftexteditor.view.ScriptView;

public interface SavaListener {

    void onSaveStart(ScriptView view,DataSave save);

    void onSaveDone(ScriptView view,DataSave save);
}
