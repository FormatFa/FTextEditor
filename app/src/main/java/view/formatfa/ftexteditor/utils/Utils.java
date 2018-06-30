package view.formatfa.ftexteditor.utils;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class Utils {

    public static String getIntentPath(Intent i)
    {
        if(i==null)return  null;
        Uri uri = i. getData();

        if(uri == null)
            return null;
        String path = uri.getPath();

        if(path==null)return  i.getStringExtra("path");
        else return  path;

    }

    public static void cut(File in,File out)
    {
        if(in.isDirectory())
        {
            out.mkdirs();
            in.delete();return;
        }

        if(out.exists())out.delete();
        if(in.renameTo(out)==false)
        {

            copy(in,out);
            in.delete();
        }
    }
    public static void copy(File source, File target) {

        if(source.isDirectory())
        {
            target.mkdirs();
        }
        if(!target.getParentFile().exists())
            target.getParentFile().mkdirs();

        FileInputStream fi = null;
        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {
            fi = new FileInputStream(source);

            fo = new FileOutputStream(target);

            in = fi.getChannel();// 得到对应的文件通道

            out = fo.getChannel();// 得到对应的文件通道

            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }

                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public static boolean deletedir(final File f)
    {

        if(f==null)return false;
        if(f.isFile())
        {
            return f.delete();
        }
        else if(f.isDirectory())
        {
            if(!f.canRead())return false;
            File[] fs=f.listFiles();

            for(File o:fs)
                deletedir(o);
            return f.delete();
        }

        return true;
    }
    public static byte[] readFileToByteArray(File file) throws FileNotFoundException
    {

        return getBytesByInputStream(new FileInputStream(file));
    }

    public static void upassets(AssetManager am,String a,File out)
    {
        try {
            writeByteArrayToFile(out,getBytesByInputStream(am.open(a)));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void writeByteArrayToFile(File file, byte[] bs) throws IOException
    {
        OutputStream os = new FileOutputStream(file);
        os.write(bs);
    }




    public static byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }


}
