package view.formatfa.ftexteditor.Save;

public class SaveFactory {


    public static final String[] saves={"file"};


    public static Save getSave(String name)
    {

        switch (name)
        {
            case "file":
                return new FileSave();
                default:
                    return null;


        }



    }




}
