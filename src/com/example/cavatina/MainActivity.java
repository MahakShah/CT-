package com.example.cavatina;

import java.util.ArrayList;

import com.example.cavatina.MusicService.LocalBinder;
import com.example.cavatina.SongsFragment.onClick;
import com.example.cavatina.adapters.NavDrawerListAdapter;
import com.example.cavatina.adapters.SongsAdapter.Song;
import com.example.cavatina.utils.NavDrawerItem;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;



public class MainActivity extends Activity implements onClick {
	static ArrayList<Song> songList=new ArrayList<Song> () ;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    MusicService mService;
	boolean mBound = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //songs
        getSongList();
        
        //artists
        //getArtistList();
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.listslide);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        // Recycle the typed array
        navMenuIcons.recycle();
        View header=getLayoutInflater().inflate(R.layout.header, null);
        //mDrawerList.addHeaderView(header);
        mDrawerList.addFooterView(header);
        //listener setting
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawers, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }	
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId()==R.id.NowPlaying)
        {
        	Intent intent = new Intent(this,PlayerActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
       
            return super.onOptionsItemSelected(item);
        
    }
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        
        getActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new SongsFragment();
            String backStateName= fragment.getClass().getName();
            FragmentManager fm= getFragmentManager();
            android.app.FragmentTransaction ft= fm.beginTransaction();
            Fragment fpop= fm.findFragmentByTag(backStateName);
            if(fpop!=null)
            {
            	fm.popBackStack(backStateName, 0);
            }
            else
            {
            	ft.replace(R.id.frame_container, fragment);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //ft.addToBackStack(backStateName);
            ft.commit();
            
            break;
       
        case 1:
            Fragment fragment2 = new AlbumsFragment();
            FragmentManager fragmentManager2 = getFragmentManager();
 			

			fragmentManager2.beginTransaction()
					.replace(R.id.frame_container, fragment2).commit();
            break;
        case 2:
        	Fragment fragment3 = new FeedbackFragment();
            FragmentManager fragmentManager3 = getFragmentManager();
 			

			fragmentManager3.beginTransaction()
					.replace(R.id.frame_container, fragment3).commit();
            break;
        case 3:
            Fragment fragment4 = new FeedbackFragment();
            FragmentManager fragmentManager4 = getFragmentManager();
 			

			fragmentManager4.beginTransaction()
					.replace(R.id.frame_container, fragment4).commit();
            break;
        case 4:
            Fragment fragment5 = new DeveloperFragment();
            FragmentManager fragmentManager5 = getFragmentManager();
 			

			fragmentManager5.beginTransaction()
					.replace(R.id.frame_container, fragment5).commit();
            break;
        default:
        	break;
             }
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        
			
    }
    static ArrayList<Song> retlist()
	{
		return songList;
	}
  //method to retrieve song info from device
    public  void  getSongList(){
	
		//query external audio
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		//iterate over results if valid
		if(musicCursor!=null && musicCursor.moveToFirst()){
			//get columns
			int titleColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ARTIST);
			int albumColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ALBUM_ID);
			//add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				String thisAlbum = musicCursor.getString(albumColumn);
				songList.add(new Song(thisId, thisTitle, thisArtist,thisAlbum,getApplicationContext()));
			} 
			while (musicCursor.moveToNext());
			musicCursor.close();
			
		}
		
	}
    
    

	@Override
	public void onStart(){
		super.onStart();
		//bind to music service
		Intent intent = new Intent(this,MusicService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		startService(intent);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		//remove bind from the music service
		if (mBound){
			unbindService(mConnection);
			mBound = false;
		}
	}
	private ServiceConnection mConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName className, IBinder service){
			LocalBinder binder =(LocalBinder)service;
			mService = binder.getService();
			mBound = true;
			if(mService!=null)
			{
				mService.setList(songList);
			
			Log.d("service","allocated");
			//noSongs = mService.getIsNoSongs();
			}
			}
		
		@Override
		public void onServiceDisconnected(ComponentName arg0){
			mBound = false;
		}
	};


	@Override
	public void click(int i, ArrayList<Song> ar) {
		// TODO Auto-generated method stub
		//mService.setList(ar);
		Log.d("call","click call");
		mService.selectSong(i);
		Intent it=new Intent(this,PlayerActivity.class);
		startActivity(it);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		
	}
   
}
 /*//method to retrieve artists list
 public void getArtistList()
 {
	 
      
      ContentResolver cr = getContentResolver();
      final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
      final String artist_id = MediaStore.Audio.Artists._ID; 
      final String artist_name =MediaStore.Audio.Artists.ARTIST;
      final String[]columns={artist_id,artist_name};
      Cursor cursor = cr.query(uri,columns,null,null, null);
      if(cursor.moveToFirst())
      {
             do{
            	 Uri.Builder builder = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.buildUpon();
            	 builder.appendPath(cursor.getString(cursor.getColumnIndex(artist_id)));
            	 builder.appendPath("albums");
                 arts.add(new Artist(cursor.getLong(cursor.getColumnIndex(artist_id)),
                		 cursor.getString(cursor.getColumnIndex(artist_name)),
                		 getApplicationContext(),builder.build()));
                
               }while(cursor.moveToNext());
            }
       }
 }
*/
