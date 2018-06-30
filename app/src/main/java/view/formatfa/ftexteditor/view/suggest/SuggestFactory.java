package view.formatfa.ftexteditor.view.suggest;

public class SuggestFactory {

    public static final String suggests []= new String[]{"gege","smali"};

    public static Suggests getSuggest(String name) throws Exception {
        switch(name)
        {

            case "gege":
                return  new GeGeSuggests();
            case "smali":
                    return null;
                default:
                    throw new Exception ("no such suggestss exception");


        }
    }


}
