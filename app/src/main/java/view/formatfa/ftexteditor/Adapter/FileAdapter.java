package view.formatfa.ftexteditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import view.formatfa.ftexteditor.R;

public class FileAdapter extends BaseAdapter {

    private Context context;
    private File[] files;

    public FileAdapter(Context context, File[] files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return files[position].hashCode();
    }

    class ViewHolder
    {

        ImageView icon;
        TextView name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;


        if(convertView==null)
        {
        holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.item_file,null);
        holder.icon=convertView.findViewById(R.id.icon);
        holder.name=convertView.findViewById(R.id.name);

        convertView.setTag(holder);

        }
        else
        {holder = (ViewHolder) convertView.getTag();

        }
        holder.name.setText(files[position].getName());

        if(files[position].isFile())
        {
            holder.icon.setImageResource(R.mipmap.file);
        }
        else if(files[position].isDirectory())
        {holder.icon.setImageResource(R.mipmap.dir);

        }
        return convertView;
    }
}
