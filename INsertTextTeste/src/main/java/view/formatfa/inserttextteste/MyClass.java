package view.formatfa.inserttextteste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyClass {

    public static void main(String[] args)
    {

        String s="feo\ndj\n\n";

        System.out.println(s.split("\n").length);
        System.out.println(Arrays.toString(s.split("\n")));
        System.out.println("len:"+"import android.graphics.Color;a".length());
String []seps  ={"package view.formatfa.ftexteditor.view;","lll"};
List list = new ArrayList<>();
list.add("forst");
list.add("package view.formatfa.ftexteditor.view;");
list.add("2");
list.add("forma");
        int absoluteLine=1;
        list.set(absoluteLine,seps[seps.length-1]);
        for(int i =0;i<seps.length-1;i+=1)
        {
            list.add(absoluteLine,seps[seps.length-2-i]);
        }
System.out.println(list);
        char ch[]=new char[]{ 'A','N','D','R'};
        String line = "I am FormatFa";
        System.out.println("input char:"+ch+ " line :"+line);
        char[] buff = line.toCharArray();
        char[] newbuff = new char[buff.length+ch.length];
        int position = 0;
        System.out.println(line.length());
        for(int p = 0;p<line.length()+1;p+=1) {
         position=p;
            System.arraycopy(buff, 0, newbuff, 0, position);
            System.arraycopy(ch,0,newbuff,position,ch.length);

            System.arraycopy(buff, position, newbuff, position + ch.length, buff.length - position);


            System.out.println(new String(newbuff));
        }
    }
}
