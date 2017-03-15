package kevinexample.androidvoicerecording;

import android.media.AudioManager;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;



public class MainActivity extends AppCompatActivity
{

    public static ListView mListView;

    public static listAudioAdapter adapter = null;

    private static final String LOG_TAG = "AudioRecordTest";
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/somedir";
    /*
     * ReservedChars = {"|", "\\", "?", "*", "<", "\"", ":", ">"}
     * CANNOT HAVE IN FILE NAME, WILL CAUSE MediaPlayer: error (1, -2147483648)
    */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, hh-mm ");

    private String fileName = "/Audio " + dateFormat.format(Calendar.getInstance(TimeZone.getDefault()).getTime());

    private MediaRecorder mRecorder = null;

    private TabHost tabHost;

    private MediaPlayer mPlayer = null;

    private ArrayList<File> filesList = null;

    public ArrayList<File> getFiles()
    {
        Log.d("Files", "Path: " + path);

        File f = new File(path);
        //this works too File f = new File(Environment.getExternalStorageDirectory(), "somedir");
        File[] files  = f.listFiles();
        /**
         Log.d("Files", "Size: "+ file.length);
         for (int i=0; i < file.length; i++)
         {
         Log.d("Files", "FileName:" + file[i].getName());
         }*/
        filesList = new ArrayList<File>(Arrays.asList(files));

        return filesList;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        //used to show the list of recordings
        mListView = (ListView) findViewById(R.id.list_view);

        //make directory
        boolean exists = (new File(path)).exists();
        if (!exists){new File(path).mkdirs();}

        adapter = new listAudioAdapter(this,getFiles());
        mListView.setAdapter(adapter);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Three");
        host.addTab(spec);

        mListView.setItemsCanFocus(true);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFile = filesList.get(position).getAbsolutePath();
                if(mPlayer == null) {
                    try {
                        mPlayer = new MediaPlayer();
                        mPlayer.setDataSource(selectedFile);
                        mPlayer.prepare();
                        mPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, selectedFile);
                    }
                }
                else {
                    mPlayer.pause();
                    mPlayer.release();
                    mPlayer = null;
                }
            }

        });


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}