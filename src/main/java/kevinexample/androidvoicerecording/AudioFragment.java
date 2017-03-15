package kevinexample.androidvoicerecording;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class AudioFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";
    public final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/somedir";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, hh-mm ");

    private String fileName = "/Audio " + dateFormat.format(Calendar.getInstance(TimeZone.getDefault()).getTime());
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private File output = null;

    private TabHost tabHost;

    private PlayButton mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private Boolean switchText = false;

    private File[] files = null;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();

        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(output.getPath());
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        switchText = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(path + fileName + ".3gp");
        output = new File(path + fileName + ".3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed, io problems while preparing [" +
                    fileName + " at " + path + " ]: " + e.getMessage());

        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        listAudioAdapter.mDataSource.add(output);
        //dynamically updates listviewer
        MainActivity.adapter.notifyDataSetChanged();
    }

    class RecordButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }


    class PlayButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
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

    public AudioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_audio,
                container, false);
        mRecordButton = new RecordButton(getContext());
        mPlayButton = new PlayButton(getContext());
        mLinearLayout.addView(mRecordButton);
        mLinearLayout.addView(mPlayButton);

        return mLinearLayout;
    }
}



