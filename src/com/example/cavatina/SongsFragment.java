package com.example.cavatina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.example.cavatina.adapters.SongsAdapter;
import com.example.cavatina.adapters.SongsAdapter.Song;



import android.app.Activity;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SongsFragment extends ListFragment {
	

	static ArrayList<Song> songList;
    ListView lv;
	View m;
	SongsAdapter adp;
	onClick p;
	
	
	public SongsFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_song, container, false);
		//getSongList();
		songList=MainActivity.retlist();
		//sort alphabetically by title
   		Collections.sort(songList, new Comparator<Song>(){
   			public int compare(Song a, Song b){
   				return a.getTitle().compareTo(b.getTitle());
   			}
   		});
   		
   		return rootView;
		
	}
	public interface onClick{
		void click(int i,ArrayList<Song>ar);
	}
	
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        
      
        //lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL); 
        //lv1.setMultiChoiceModeListener(this);	       
		
	}
	
	
 

@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}


@Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	  if(adp==null)
      {
      	adp=new SongsAdapter(getActivity(), songList);
      }	
      ListView lv1 = getListView();
      if(lv1.getAdapter()==null)
      {
      	lv1.setAdapter(adp);
      }
      
      lv1.setScrollingCacheEnabled(false);
}

@Override
public void onListItemClick(ListView l, View v, int position, long id) {
//	mService.selectSong(position);
	
	/*Intent intent = new Intent(getActivity(),PlayerActivity.class);
	startActivity(intent);
	getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/

		p.click(position, songList);
	
}

@Override
public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
	p=(onClick)activity;
	super.onAttach(activity);
	
}


}

