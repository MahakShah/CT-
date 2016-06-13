package com.example.cavatina;

import java.util.ArrayList;
import java.util.Random;

import com.example.cavatina.adapters.SongsAdapter.Song;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

public class MusicService extends Service implements 
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {

	//media player
	private MediaPlayer player;
	//song list
	public ArrayList<Song> songs;
	//current position
	private int songPosn;
	//binder
	private final IBinder musicBind = new MusicBinder();
	//title of current song
	private String songTitle="";
	//notification id
	private static final int NOTIFY_ID=1;
	//shuffle flag and random
	private boolean shuffle=false;
	private Random rand;
	public SensorManager sensorManager;
	public Sensor proximitySensor;
	public SensorEventListener l;
	boolean isPause=false;
public class ProximitySensorEventListener implements SensorEventListener{
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		
		boolean timerOn = false;
		CountDownTimer timer = new CountDownTimer(1000,200){

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				playpause();
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
				
			}
			
		};

		@Override
		public void onSensorChanged(SensorEvent se) {
			// TODO Auto-generated method stub
			System.out.println(se.values[0]);
			
				if (se.values[0] < proximitySensor.getMaximumRange()){
					
						
						timer.start();
					
				}
				else{
					
						playNext();
					
					//close = false;
					//timerOn = false;
					//timer.cancel();
				}
			}
		
			
	}
	


	public void onCreate(){
		//create the service
		super.onCreate();
		//initialize position
		songPosn=0;
		//random
		rand=new Random();
		//create player
		player = new MediaPlayer();
		//initialize
		initMusicPlayer();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		l = new ProximitySensorEventListener();
		//turn on sensor if needed
			sensorManager.registerListener(l, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void initMusicPlayer(){
		//set player properties
		player.setWakeMode(getApplicationContext(), 
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//set listeners
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	//pass song list
	public void setList(ArrayList<Song> theSongs){
		songs=theSongs;
	}

	//binder
	public class MusicBinder extends Binder {
		MusicService getService() { 
			return MusicService.this;
		}
	}

	//activity will bind to service
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	//release resources when unbind
	@Override
	public boolean onUnbind(Intent intent){
		player.stop();
		player.release();
		return false;
	}

	//play a song
	public void playSong(){
		//play
		player.reset();
		//get song
		if(songs!=null)
		{
			Song playSong = songs.get(songPosn);
		
		//get title
		songTitle=playSong.getTitle();
		//get id
		long currSong = playSong.getID();
		//set uri
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currSong);
		//set the data source
		try{ 
			player.setDataSource(getApplicationContext(), trackUri);
			player.prepareAsync(); 
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		//player.prepareAsync(); 
		}
	}

	//set the song
	public void setSong(int songIndex){
		songPosn=songIndex;	
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		//check if playback has reached the end of a track
		if(player.getCurrentPosition()>0){
			mp.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.v("MUSIC PLAYER", "Playback Error");
		mp.reset();
		return false;
	}

	@SuppressLint("NewApi") @Override
	public void onPrepared(MediaPlayer mp) {
		//start playback
		  // Broadcast intent to activity to let it know the media player has been prepared
	    Intent onPreparedIntent = new Intent("MEDIA_PLAYER_PREPARED");
	    LocalBroadcastManager.getInstance(this).sendBroadcast(onPreparedIntent);
	    Log.d("mp","prepared");
		mp.start();
		//notification
		Intent notIntent = new Intent(this, MainActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
				notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(pendInt)
		.setSmallIcon(R.drawable.defalbart)
		.setTicker(songTitle)
		.setOngoing(true)
		.setContentTitle("Playing")
		.setContentText(songTitle);
		Notification not = builder.build();
		startForeground(NOTIFY_ID, not);
		

		    
		}
	

	//playback methods
	public int getPosn(){
		try
		{
			if(player!=null)
		
		return player.getCurrentPosition();
		else
			return 0;}
		catch(IllegalStateException e)
		{}
		return 0;
		
	}

	public int getDur(){
		try
		{
			if(player!=null)
		
		return player.getDuration();
		else
			return 0;}
		catch(IllegalStateException e)
		{
			return 0;
		}
		
	}

	public boolean isPng(){
		try 
		{
			return player.isPlaying();
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public void pausePlayer(){
		player.pause();
		isPause=true;
	}

	public void seek(int posn){
		player.seekTo(posn);
	}

	public void go(){
		player.start();
	}

	//skip to previous track
	public void playPrev(){
		if(songs==null)
			return;
		else
		{
			songPosn--;
		
		if(songPosn<0) songPosn=songs.size()-1;
		playSong();
		}
	}

	//skip to next
	public void playNext(){
		if(shuffle){
			int newSong = songPosn;
			while(newSong==songPosn){
				newSong=rand.nextInt(songs.size());
			}
			songPosn=newSong;
		}
		else{
			songPosn++;
			if(songs!=null)
			{
			if(songPosn>=songs.size()) songPosn=0;
			}
		}
		playSong();
	}

	@Override
	public void onDestroy() {
		//player.stop();
		player.release();
		stopForeground(true);
	}
	void playpause ()
	{
		
			 try 
			 {if (isPause){
				player.start();
				
			}
			else{
				player.pause();
				isPause = true;
			}}
			 catch(Exception e)
			 {
				 
			 }
		
	}

	//toggle shuffle
	public void setShuffle(){
		if(shuffle) shuffle=false;
		else shuffle=true;
	}

}
