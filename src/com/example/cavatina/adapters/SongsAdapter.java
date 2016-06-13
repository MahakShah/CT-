package com.example.cavatina.adapters;


import java.util.ArrayList;
import com.example.cavatina.R;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class SongsAdapter extends BaseAdapter  {
	
	 
	Context m;
	String str1[] ;
	String songnames[];
	String playlistselsected;
	String playlistNames[];
	 private ArrayList<Song> songs;
	//private LayoutInflater songInf;
	ArrayList<String> addp;
	ArrayList<Integer>songchecked=new ArrayList<Integer>();
	
	
	public SongsAdapter(Context c, ArrayList<Song> theSongs){
		
		m=c;
		songs=theSongs;
		//songInf=LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		return songs.size();
	}
    @Override
	public Object getItem(int arg0) {
		return null;
	}
    @Override
	public long getItemId(int arg0) {
		return 0;
	}
    class ViewHolder {
        TextView tv_group_name;
        TextView tv_group_reg_id;
        ImageView art;

    }
    @SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	Song currSong = songs.get(position); 
    	View v = convertView;
         ViewHolder holder = null;
         if (v == null) {
                try{
                	LayoutInflater vi = (LayoutInflater) m
                         .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.song, null);

                 holder = new ViewHolder();

                 holder.tv_group_name = (TextView) v
                         .findViewById(R.id.song_title);
                 holder.tv_group_reg_id = (TextView) v
                         .findViewById(R.id.song_artist);
                 holder.art=(ImageView)v
                		 .findViewById(R.id.albart1);
                 v.setTag(R.string.id1,holder);
                 v.setTag(R.string.id2, position);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         } else
             holder = (ViewHolder) v.getTag(R.string.id1);

        
        holder.tv_group_name.setText(currSong.getTitle());
        holder.tv_group_reg_id.setText(currSong.getArtist());
        
         holder.art.setImageResource(R.drawable.defalbart);	 
         
        //new LoadImage(holder.art,m).execute();
         return v;
       
	
	}

public static class Song {
    Context m;
	private long id;
	private String title;
	private String artist;
	private String album;
	private Uri uri;
	Cursor c;
	public Song(long songID, String songTitle, String songArtist,String songAlbum,Context m ){
		id=songID;
		title=songTitle;
		artist=songArtist;
		album = songAlbum;
		this.m=m;
		uri=ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
		
	}
	
	public long getID()
	 {return id;}
	public String getTitle()
	 {return title;}
	public String getArtist()
	 {return artist;}
	public String getAlbum()
	 {return album;}
	public Uri getUri()
	 {return uri;}
	
	
}

}

