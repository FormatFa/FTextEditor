package view.formatfa.ftexteditor.Save;

import view.formatfa.ftexteditor.CodeActivity;
import view.formatfa.ftexteditor.view.ScriptView;

public interface Save {

    void save(CodeActivity context, ScriptView view);

    String getDescript();

}
