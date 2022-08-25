package si.uni_lj.fe.tnuv.frigre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<NewsObject> {
    private Context mContext;
    private int mResource;

    public MyAdapter(Context context, int resource, ArrayList<NewsObject> newsObjects) {
        super(context, resource, newsObjects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String content = getItem(position).getContent();

        NewsObject newsObject = new NewsObject(title, content);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvContent = convertView.findViewById(R.id.tvContent);

        tvTitle.setText(title);
        tvContent.setText(content);

        return convertView;
    }
}
