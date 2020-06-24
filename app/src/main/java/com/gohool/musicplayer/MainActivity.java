package com.gohool.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img;
    private TextView song;
    private TextView artist;
    private SeekBar skbr;
    private TextView lt;
    private TextView rt;
    private Button prevs;
    private Button plays;
    private Button nexts;
    private MediaPlayer mediaPlayer;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        skbr.setMax(mediaPlayer.getDuration());
        skbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
//                SimpleDateFormat dateFormat=new SimpleDateFormat("mm:ss");
                int currentpos=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                int lm=currentpos/1000/60;
                int ls=(currentpos/1000)%60;
                int rm=(duration-currentpos)/1000/60;
                int rs=((duration-currentpos)/1000)%60;
                lt.setText(lm+":"+ls);
                rt.setText(rm+":"+rs);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


    }
    public void setupUI(){
        mediaPlayer=new MediaPlayer();
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.mu);

        img=(ImageView) findViewById(R.id.artistimage);
        song=(TextView) findViewById(R.id.songnameid);
        artist=(TextView) findViewById(R.id.artistid);
        skbr=(SeekBar) findViewById(R.id.seekBar);
        lt=(TextView) findViewById(R.id.left);
        rt=(TextView) findViewById(R.id.right);
        prevs=(Button) findViewById(R.id.prev);
        plays=(Button) findViewById(R.id.play);
        nexts=(Button) findViewById(R.id.next);

        prevs.setOnClickListener(this);
        plays.setOnClickListener(this);
        nexts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev:
                prevMusic();

                break;

            case R.id.play:
                if(mediaPlayer.isPlaying()){
                    pauseMusic();
                }
                else
                    playMusic();
                break;

            case R.id.next:
                nextMusic();

                break;

        }
    }
    public void pauseMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            plays.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }
    public void playMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
            updateThread();
            plays.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }
    public void nextMusic(){

            mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);


    }
    public void prevMusic(){

            mediaPlayer.seekTo(0);

    }
    public void updateThread(){
        thread=new Thread(){
            @Override
            public void run() {
                try{

                        while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            Thread.sleep(50);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int newpos = mediaPlayer.getCurrentPosition();
                                    int newmax = mediaPlayer.getDuration();
                                    skbr.setMax(newmax);
                                    skbr.setProgress(newpos);
                                    int nlm = newpos / 1000 / 60;
                                    int nls = (newpos / 1000) % 60;
                                    int nrm = (newmax - newpos) / 1000 / 60;
                                    int nrs = ((newmax - newpos) / 1000) % 60;
                                    lt.setText(nlm + ":" + nls);
                                    rt.setText(nrm + ":" + nrs);
                                }

                            });
                        }
                    }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying()&&mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        thread.interrupt();
        thread=null;


        super.onDestroy();
    }
}