package view.formatfa.inserttextteste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitTest {

    public static void main(String[] args)
    {
        String string = "ab\n\n";

        System.out.println(splitByLine("\n",string));
        System.out.println(splitByLine("\n",string).size());
        for(String r:splitByLine("\n",string))System.out.println("kk:"+r+"kk");

    }

    public static List<String > splitByLine(String line, String code) {
        List<String> result = new ArrayList<>();
        int start = 0;
        int index = 0;
        System.out.println(line.length());
        while( (start=code.indexOf(line,index))!=-1  )
        {

            if(index!=start) {
                result.add(code.substring(index, start));
            }
            else
             result.add("");


            index=start+line.length();
        }

        if(index!=code.length())
        result.add(code.substring(index,code.length()));

        if(code.endsWith(line))
            result.add("");

        return result;

    }

    public static List<String > splitByLine2(String line, String code)
    {
        List<String> result = new ArrayList<>();
        int p = -1;
        int lastP = 0;


        boolean isFirst = true;
        while ( (p =code.indexOf(line,lastP))!=-1  )
        {



            String item=null;



            int wi = code.indexOf(line,lastP);
            System.out.println(p+" ,"+wi);

            if(isFirst)
            {
                if(p==0)item="";
                    else
                item=code.substring(0,p);
                isFirst=false;
            }
            else {
                if (wi == -1) {
                    item=code.substring(p, code.length());
                } else {
                    item=code.substring(p, wi);
                    lastP = wi;
                }

            }
            System.out.println(item);
            if(line.equals(item))
            {
                result.add("");
            }
            else result.add(item);
            lastP+=line.length();
        }
        return result;

    }

}
