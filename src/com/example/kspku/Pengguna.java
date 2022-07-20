package com.example.kspku;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Pengguna extends Activity {
	EditText editUser,editPassword,editLevel;
	Button buttonSimpan,buttonKeluar;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	String snama;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pengguna);
		dbHelper = new mySqlHelper(this);
		
		editUser=(EditText) findViewById(R.id.EditTextPenggunaNama);
		editPassword=(EditText) findViewById(R.id.EditTextPenggunaPassword);
		editLevel=(EditText) findViewById(R.id.EditTextPenggunaLevel);
		lv=(ListView) findViewById(R.id.listViewPengguna);
		
		buttonKeluar = (Button) findViewById(R.id.buttonPenggunaKeluar);
		buttonKeluar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		buttonSimpan = (Button) findViewById(R.id.buttonPenggunaSimpan);
		buttonSimpan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addpengguna();
				tampilkanData();
			}
		});
		tampilkanData();
	}
	
	private void addpengguna() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		c = db.rawQuery("select * from pengguna where nama='"+editUser.getText().toString()+"';",null);
		if (!c.moveToFirst()) {
			String sql = "insert into pengguna(nama,password,level) values('"+editUser.getText().toString()+"','"
					+editPassword.getText().toString()+"','"+editLevel.getText().toString()+"')";
			try{
				db.execSQL(sql);
				Toast toast = Toast.makeText(getApplicationContext(),"Insert pengguna berhasil...", Toast.LENGTH_SHORT);
				toast.show();
			}
			catch(Exception e) {}
		}else {
			String sql="update pengguna set password='"+editPassword.getText().toString()+
					"',level='"+editLevel.getText().toString()+"'where nama='"+editUser.getText().toString()+"';";
			try{
				db.execSQL(sql);
				Toast toast = Toast.makeText(getApplicationContext(),"Update pengguna berhasil...", Toast.LENGTH_SHORT);
				toast.show();
			}
			catch(Exception e) {}
	}
}
	
	public void tampilkanData() {
		ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		c = db.rawQuery("select * from pengguna", null);
		
		if (c.moveToFirst()){
			do{
				HashMap<String, String> map = new HashMap<String, String>();
				
				String nama = c.getString(0);
				String password = c.getString(1);
				String level = c.getString(2);
				
				map.put("nama", nama);
				map.put("password", password);
				map.put("level", level);
				
				orderListdb.add(map);
			} while (c.moveToNext());
		}
		ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
				orderListdb, R.layout.list_2baris3kata,
				new String[] { "nama","password","level"},
				new int[] { R.id.nama, R.id.password, R.id.level});
		lv.setAdapter(adapter2);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			final String selectionnama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
    			final String selectionpassword = ((TextView) view.findViewById(R.id.password)).getText().toString();
    			final String selectionlevel = ((TextView) view.findViewById(R.id.level)).getText().toString();
    			snama = selectionnama;
    			
    			final CharSequence[] dialogitem = {"Edit","Delete","Cetak"};
    			AlertDialog.Builder builder = new AlertDialog.Builder(Pengguna.this);
    			builder.setTitle("Pilih ?");
    			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						switch(item){
						case 0:
							editUser.setText(selectionnama);
							editPassword.setText(selectionpassword);
							editLevel.setText(selectionlevel);
							break;
						case 1:
							ShowConfirmDelete(selectionnama);
							break;
						case 2:
							
							break;
						}
					}
				});
    			builder.create().show();
    		}
    	});
	}
	
	private void ShowConfirmDelete(String nama){
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
    	ad.setTitle("Sistem Informasi KSP");
    	ad.setMessage("Yakin Hapus Pengguna Nama: "+snama+" ?");
    	ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try{
					db.execSQL("delete from pengguna where nama='"+snama+"'");
				}
				catch(Exception e)
				{}
				tampilkanData();
			}
    	});
    	
    	ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
    	});
    	ad.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pengguna, menu);
		return true;
	}

}
