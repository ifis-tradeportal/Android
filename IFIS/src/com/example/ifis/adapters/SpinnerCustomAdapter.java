package com.example.ifis.adapters;

import java.util.ArrayList;

import com.example.ifis.AccountActivity;
import com.example.ifis.ConstantsStrings;
import com.example.ifis.OrderEntryActivity;
import com.example.ifis.R;
import com.example.ifis.model.AccountModel;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerCustomAdapter extends ArrayAdapter<String>{
    
   private Activity activity;
   private ArrayList data;
   public Resources res;
   AccountModel tempValues=null;
   LayoutInflater inflater;
    
   /*************  CustomAdapter Constructor *****************/
   public SpinnerCustomAdapter(
                         OrderEntryActivity activitySpinner, 
                         int textViewResourceId,   
                         ArrayList objects,
                         Resources resLocal
                        ) 
    {
       super(activitySpinner, textViewResourceId, objects);
        
       /********** Take passed values **********/
       activity = activitySpinner;
       data     = objects;
       res      = resLocal;
   
       /***********  Layout inflator to call external xml layout () **********************/
       inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
     }
   
   public SpinnerCustomAdapter(AccountActivity activitySpinner, 
           int textViewResourceId,   
           ArrayList objects,
           Resources resLocal){
	   
	   super(activitySpinner, textViewResourceId, objects);
       
       /********** Take passed values **********/
       activity = activitySpinner;
       data     = objects;
       res      = resLocal;
   
       /***********  Layout inflator to call external xml layout () **********************/
       inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   
	   
   }
   
   
   
   @Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
	 tempValues = (AccountModel) data.get(position);
	 return tempValues.getAccountId();
	}

   @Override
   public View getDropDownView(int position, View convertView,ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   // This funtion called for each row ( Called data.size() times )
   public View getCustomView(int position, View convertView, ViewGroup parent) {

       /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
       View row = inflater.inflate(R.layout.spinner_row, parent, false);
        
       /***** Get each Model object from Arraylist ********/
       tempValues = null;
       tempValues = (AccountModel) data.get(position);
        
       TextView label        = (TextView)row.findViewById(R.id.company);
       //TextView sub          = (TextView)row.findViewById(R.id.sub);
      
        
       if(position==0){
            
           // Default selected Spinner item 
           label.setText(ConstantsStrings.SELECT_ACCOUNT);
          // sub.setText("");
       }
       
       else
       {
           // Set values for spinner each row 
           label.setText(tempValues.getAccountName());
  
       
      }   

       return row;
     }
}
