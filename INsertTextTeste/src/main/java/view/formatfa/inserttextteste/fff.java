package view.formatfa.inserttextteste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fff {

    public static  String getPositionFromNoAbc(String s,int p)
    {
        if(p>=s.length())return null;
        if(p==0)return null;
       List<Character> i = new ArrayList<Character>();

        char[] data = s.toCharArray();
        int len = 0;
        for(int j = p;j>=0;j-=1)
        {
            if((data[j]>='a'&&data[j]<='z')||(data[j]>='A'&&data[j]<='Z'))
            {
                System.out.println("push:"+data[j]);
                i.add(data[j]);
            }
            else
                break;
            len +=1;
        }
        Character [] result = i.toArray(new Character[0]);
        System.out.println(Arrays.toString(result));
        return  new String(result);

    }
    public static void main(String[] args) {


        String s = "afdfdgdde";

        System.out.println(getPositionFromNoAbc(s,5));

        String fs ="Landroid/app/Activity;ff";
        Pattern fpattern = Pattern.compile("L[\\w|/]+;");
        Matcher ms = fpattern.matcher(fs);
        if(ms.find())
        {
            System.out.println("呵呵哈哈哈或:"+ms.group());
        }
        else
            System.out.println("多看看端口打开");
        System.out.println(fs.substring(1,3));
        String str ="a899fdsfdsfds";
        Pattern pattern = Pattern.compile("a(\\d)");
        Matcher m = pattern.matcher(str);
        if(m.find()) {

            System.out.println(m.groupCount());
            System.out.println(m.start(0));

        }else
            System.exit(0);
        float f1=-7482.3916f;
        float f2 = -7537.3916f;
        int speedd = 15;
        speedd= (int) ((f1/f2)*speedd);
        System.out.println(f1/f2);
        System.out.println(speedd);

        System.out.println("ff"+(int) (Math.random()*3));
String ds = "forma";
String now ="I am Formatf";
int position=00;
int len=12;
if(position+len<=now.length())
System.out.println(        now.substring(0,position)+now.substring(position+len));

System.out.println(new String(charInsert(s.toCharArray(),5,new char[]{'\n'})));


    }

    public static char[] charInsert(char[] input,int position,char[] aim)
    {

        char[] newbuff = new char[input.length+aim.length];

        System.arraycopy(input, 0, newbuff, 0, position);
        System.arraycopy(aim,0,newbuff,position,aim.length);

        System.arraycopy(input, position, newbuff, position + aim.length, input.length - position);

        return newbuff;

    }
}
