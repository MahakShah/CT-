package com.example.cavatina.adapters;

import java.util.ArrayList;
import com.example.cavatina.R;
import com.example.cavatina.utils.NavDrawerItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;;

public class NavDrawerListAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
     
    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        //TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset( context.getAssets(), "fonts/Ace Crikey.ttf");
 
        // Applying font
        txtTitle.setTypeface(tf);
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());
         
        // displaying count
        // check whether it set visible or not
        /*if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
         */
        return convertView;
    }

	
 
}
