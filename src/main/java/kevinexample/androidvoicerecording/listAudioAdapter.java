package kevinexample.androidvoicerecording;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class listAudioAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public static ArrayList<File> mDataSource;

    public listAudioAdapter(Context context, ArrayList<File> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void delete(View view){

        final int position = MainActivity.mListView.getPositionForView((View) view.getParent());
        File removed = mDataSource.get(position);
        File dir = new File(AudioFragment.path, removed.getName());
        dir.delete();
        mDataSource.remove(position);
        this.notifyDataSetChanged();

    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_audio, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.list_title);

        // Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.list_thumbnail);

        // 2
        titleTextView.setText(mDataSource.get(position).getName());
        thumbnailImageView.setImageResource(R.mipmap.mp3_icon);

        Button deleteBtn = (Button) rowView.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                delete(view);
            }
        });

        return rowView;
    }
}
