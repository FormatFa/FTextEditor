package view.formatfa.ftexteditor.view.light;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import view.formatfa.ftexteditor.view.FileUtils;

public class JavaLffight implements Light {


    class LightPattern
    {
        public LightPattern(Pattern pattern, int color) {
            this.pattern = pattern;
            this.color = color;
        }

        Pattern pattern;
        int color;

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
    String[] keyWord ={"private","protected","public",
            "abstract","class","extends","final","implements","interface","native","new",
            "static","strictfp","synchronized","transient","volatile",
            "break","continue","return","do","while","if","else","for","instanceof","switch",
            "case","default",
            "try","cathc","throw","throws",
            "import","package",
            "boolean","byte","char","double","float","int","long","short","null","true","false",
            "super","this","void",
            "goto","const"};
    public final String anno ="@(.*?)",comment ="//(.*)",pString="\"(.*?)\"",pString2="'(.*?)'",field="\\w+\\.(.*?)(?=[^\\w])",integer="\\d+";

    private List<LightPattern > patterns ;
    public JavaLight() {
        patterns = new ArrayList<>();
//        for(String key:keyWord)
//        {
//            Pattern pattern = Pattern.compile("[^\\w]"+key+"[^\\w]");
//            System.out.println("pattern:"+pattern.toString());
//            patterns.add(new LightPattern(pattern,Color.rgb(0x9d,0x6a,0x31)));
//        }


        Pattern   temp = Pattern.compile(integer);
        patterns.add(new LightPattern(temp,Color.rgb(0x28,0xaa,0xed)));

       temp = Pattern.compile(pString);
        patterns.add(new LightPattern(temp,Color.GREEN));
        temp = Pattern.compile(pString2);
        patterns.add(new LightPattern(temp,Color.GREEN));

        temp = Pattern.compile(comment);
        patterns.add(new LightPattern(temp,Color.GRAY));

        temp = Pattern.compile(anno);
        patterns.add(new LightPattern(temp,Color.rgb(0xcc,0xee,0x6a)));

        temp = Pattern.compile(field);
        patterns.add(new LightPattern(temp,Color.rgb(0x6b,0x70,0xc8)));

    }

    @Override
    public List<LightClip> light(String code) {
        List<LightClip> lights = new ArrayList<LightClip>();



       for(LightPattern pattern:patterns) {
           Matcher matcher = pattern.getPattern().matcher(code);
           while (matcher.find())
           {
               int s,e;
               if(matcher.groupCount()==0){
                   s=matcher.start();
                   e=matcher.end();
               }
               else
               {
                   s=matcher.start(0);
                   e=matcher.end(0);
               }
               LightClip clip = new LightClip();
        clip.setColor(pattern.getColor());
            clip.setStart(s);
           clip.setEnd(e);
           lights.add(clip);


           }


       }


       char [] data = code.toCharArray();
        for(String key:keyWord)
        {
            int p = 0;
            while( (p=code.indexOf(key+" ",p))!=-1 )
            {
                boolean can=true;
                if(p!=0&& FileUtils.isW( data[p-1]))
                {
                   can=false;
                }
              //  if(p+key.length()!=data.length-1&& FileUtils.isW( data[p+key.length()+1]))
               // can=false;
                if(can) {

                    LightClip clip = new LightClip();
                    clip.setColor(Color.rgb(0x9d, 0x6a, 0x31));
                    clip.setStart(p);
                    clip.setEnd(p + key.length());
                    lights.add(clip);

                } p += key.length();

            }

        }
//
//        int p = code.indexOf(comment);
//        if(p!=-1)
//        {
//            LightClip clip = new LightClip();
//            clip.setColor(Color.GRAY);
//            clip.setStart(p);
//            clip.setEnd(code.length());
//            lights.add(clip);
//        }
//
//        Pattern pattern = Pattern.compile(pString);
//        Matcher matcher = pattern.matcher(code);
//        while(matcher.find())
//        {
//            LightClip clip = new LightClip();
//            clip.setColor(Color.GREEN);
//            clip.setStart(matcher.start());
//            clip.setEnd(matcher.end());
//            lights.add(clip);
//        }
        return lights;
    }


}
