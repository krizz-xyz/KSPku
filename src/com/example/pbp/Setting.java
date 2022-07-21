package com.example.pbp;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Setting extends Activity {
	final static String NAMA_FILE = "catatan";
	Button buttonKembali;
	ListView lv;
	String [] daftarSetting={"IP Address Host Server","192.168.43.100"};
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		bacaPreferensi();
		lv=(ListView)findViewById(R.id.listViewSetting);
		
		buttonKembali = (Button) findViewById(R.id.buttonSettingKembali);
		buttonKembali.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tampilkanSetting();
	}
	
	public void bacaPreferensi(){
		SharedPreferences pref = getSharedPreferences(
				NAMA_FILE, MODE_PRIVATE);
		daftarSetting[1]=pref.getString("ip", "");
	}
	
	public void simpanPreferensi(){
		SharedPreferences pref = getSharedPreferences(
				NAMA_FILE, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("ip", daftarSetting[1]);
		editor.commit();
		Toast.makeText(getApplicationContext(),
				"Preferensi sudah disimpan",
				Toast.LENGTH_SHORT).show();
	}
	
	public void tampilkanSetting(){
		ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		String items = daftarSetting[0];
		String subitems = daftarSetting[1];
		
		map.put("items", items);
		map.put("subitems", subitems);
		orderListdb.add(map);
		
		ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
				orderListdb, R.layout.list_2baris,
				new String[] { "items","subitems" },
				new int[] { R.id.Items, R.id.SubItems });
		lv.setAdapter(adapter2);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				switch(position){
				case 0 :
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					View promptView = layoutInflater.inflate(R.layout.input_ip_addres, null);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					
					alertDialogBuilder.setView(promptView);
					final EditText input = (EditText) promptView.findViewById(R.id.editTextInputIPaddress);
					
					alertDialogBuilder.setTitle(daftarSetting[0]);
					input.setText(daftarSetting[1]);
					alertDialogBuilder.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									daftarSetting[1]=input.getText().toString();
									simpanPreferensi();
									tampilkanSetting();
								}
							}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					AlertDialog alerD = alertDialogBuilder.create();
					alerD.show();
					break;
				}
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

}

