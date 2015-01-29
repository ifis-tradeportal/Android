package com.example.ifis.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.ifis.LoginActivity;
import com.example.ifis.*;
import com.example.ifis.R;
import com.example.ifis.SettingsActivity;
import com.example.ifis.R.id;
import com.example.ifis.adapters.OrderBookAdapter.ViewHolder;
import com.example.ifis.model.OrderBook;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsAdapter extends BaseAdapter {
	Activity activity;
	private LayoutInflater inflater = null;

	public SettingsAdapter(Activity a){
		activity = a;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if(arg0==0){
			if(view==null){
				view = inflater.inflate(R.layout.settings2,parent, false);
				vh = new ViewHolder();
				vh.settingName = (TextView) view.findViewById(id.settingsTV);
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
			
			vh.settingName.setText("Change Password");
			
			
		}
		if(arg0==1){
			if(view==null){
				view = inflater.inflate(R.layout.settings2,parent, false);
				vh = new ViewHolder();
				vh.settingName = (TextView) view.findViewById(id.settingsTV);
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
			
			vh.settingName.setText("Change Service");
			
		}
		if(arg0==2){
			if(view==null){
				view = inflater.inflate(R.layout.settings1,parent, false);
				vh = new ViewHolder();
				vh.nSwitch = (Switch) view.findViewById(R.id.switch1);
				vh.nSwitch.setChecked(true);
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
				String notify=sharedPref.getString(ConstantsStrings.NOTIF_PREF, "");
				if(notify.equals("ON")){
					vh.nSwitch.setChecked(true);
				}else{
					vh.nSwitch.setChecked(false);
				}
				vh.nSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
						SharedPreferences.Editor editor = sharedPref.edit();
						
						String trcode=sharedPref.getString(ConstantsStrings.TR_CODE_PREF, "");
						if(isChecked){
							editor.putString(ConstantsStrings.NOTIF_PREF, "ON");
							editor.commit();
							regiterParse(trcode);
							Toast.makeText(activity, "Notifications ON", Toast.LENGTH_SHORT).show();
						}else{
							
							editor.putString(ConstantsStrings.NOTIF_PREF, "OFF");
							editor.commit();
							regiterParse("");
							Toast.makeText(activity, "Notifications OFF", Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				
				
				view.setTag(vh);
			}else{
				vh = (ViewHolder) view.getTag();
			}
	
		}
		return view;
	}
	
	
	private void regiterParse(String code) {
		
		
		
		ParseInstallation.getCurrentInstallation().saveInBackground();
      
		List<String> channels= new ArrayList<String>();
		channels.add("");
		//channels.add(code.toString());
		channels.add(code);
		ParseInstallation.getCurrentInstallation().put("channels", channels);
        ParseInstallation.getCurrentInstallation().put("age", 255);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast toast = Toast.makeText(activity, "success", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					e.printStackTrace();
					Toast toast = Toast.makeText(activity, "fail", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		
	}
	
public class ViewHolder{
		
		TextView settingName;
		Switch nSwitch;
}

}
