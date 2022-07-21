package com.example.pbp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class Barang<SqlLiteDatabase> extends Activity implements OnClickListener {
	final Context context = this;
	Button buttonKodeBarang, buttonKembali;
	EditText inputTextKodeBarang, inputTextNamaBarang, inputTextHarga, editNamaBarang;
	String TextKodeBarang, TextNamaBarang, TextHarga;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	String skode, Value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barang);
		dbHelper = new mySqlHelper(this);
		editNamaBarang=(EditText) findViewById(R.id.editTextBarang);
		lv=(ListView) findViewById(R.id.listViewBarang);
		editNamaBarang.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Value = editNamaBarang.getText().toString();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tampilkanData(Value);
			}
		
		});
		
		buttonKembali = (Button) findViewById(R.id.buttonBarangKembali);
		buttonKembali.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		tampilkanData("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barang, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			inputbarang("insert");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void inputbarang(String jns_input) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater2 = LayoutInflater.from(context);
		View promptView2 = layoutInflater2.inflate(R.layout.input_barang, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
		//set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder2.setView(promptView2);
		inputTextNamaBarang = (EditText) promptView2.findViewById(R.id.inputTextBarangNama);
		inputTextKodeBarang = (EditText) promptView2.findViewById(R.id.inputTextBarangKode);
		inputTextHarga = (EditText) promptView2.findViewById(R.id.inputTextBarangHarga);
		buttonKodeBarang = (Button) promptView2.findViewById(R.id.buttonScan);
		buttonKodeBarang.setOnClickListener(this);
		//setup a dialog window
		alertDialogBuilder2.setTitle("INPUT DATA BARANG");
		if (jns_input=="update") {
			inputTextNamaBarang.setText(TextNamaBarang);
			inputTextKodeBarang.setText(TextKodeBarang);
			inputTextHarga.setText(TextHarga);
		}
		alertDialogBuilder2
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				TextKodeBarang=inputTextKodeBarang.getText().toString();
				TextNamaBarang=inputTextNamaBarang.getText().toString();
				TextHarga=inputTextHarga.getText().toString();
				addbarang();
				tampilkanData("");
			}
			})
		.setNegativeButton("Cancel", 
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		//create an alert dialog
		AlertDialog alert02 = alertDialogBuilder2.create();
		alert02.show();
	}		
		
		private void addbarang() {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			c = db.rawQuery("select * from barang where kd_brg='" +TextKodeBarang+"';",
					null);
			if (!c.moveToFirst()) {
				String sql = "insert into barang(kd_brg, nama_brg, harga) values('"+TextKodeBarang+"','"
						+TextNamaBarang+"','"+TextHarga+"')";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Insert barang berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
			}else {
				String sql="update barang set nama_brg='"+TextNamaBarang+
						"',harga='"+TextHarga+"' where kd_brg='"+TextKodeBarang+"';";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Update barang berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
		}
		}
		
		public void tampilkanData(String cari) {
	    	// Buka file untuk dibaca
	    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	c = db.rawQuery("select nama_brg, kd_brg, harga from barang where nama_brg LIKE '%"+ cari + "%'", null);
	    	
	    	if (c.moveToFirst()) {
	    		do {
	    			HashMap<String, String> map = new HashMap<String, String>();
	    			
	    			String nama_brg = c.getString(0);
	    			String kd_brg = c.getString(1);
	    			String harga = c.getString(2);
	    			
	    			map.put("nama_brg", nama_brg);
	    			map.put("kd_brg", kd_brg);
	    			map.put("harga", harga);
	    			orderListdb.add(map);
	    		} while (c.moveToNext());
	    	}
	    	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
	    			orderListdb, R.layout.list_2baris3kata,
	    			new String[] {"nama_brg", "kd_brg", "harga"},
	    			new int[] {R.id.nama, R.id.password, R.id.level});
	    	lv.setAdapter(adapter2);
	    	
	    	lv.setOnItemClickListener(new OnItemClickListener() {
	    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    			final String selectionnama = ((TextView) view.findViewById(R.id.nama)).getText().toString();
	    			final String selectionkode = ((TextView) view.findViewById(R.id.password)).getText().toString();
	    			final String selectionharga = ((TextView) view.findViewById(R.id.level)).getText().toString();
	    			skode = selectionkode;
	    			
	    			final CharSequence[] dialogitem = {"Edit", "Delete"};
	    			AlertDialog.Builder builder = new AlertDialog.Builder(Barang.this);
	    			builder.setTitle("pilih ?");
	    			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							// TODO Auto-generated method stub
							switch(item){
							case 0:
								TextKodeBarang=selectionkode;
								TextNamaBarang=selectionnama;
								TextHarga=selectionharga;
								inputbarang("update");
								break;
							case 1:
								ShowConfirmDelete(selectionnama);
								break;
							}
						}
	    			});
	    			builder.create().show();
	    		}
	    	});
		}

	private void ShowConfirmDelete(String nama) {
		// TODO Auto-generated method stub
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
    	ad.setTitle("Sistem Informasi Penjualan Benih Padi");
    	ad.setMessage("Yakin Hapus Benih Nama: "+nama+" ?");
    	ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try{
					db.execSQL("delete from barang where kd_brg='"+skode+"'");
				}
				catch(Exception e)
				{}
				tampilkanData("");
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.buttonScan) {
			//scan
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}
	}
		
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrive scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult !=null) {
			//we have a result
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			//formatTxt.setText("FORMAT: "+ scanFormat);
			inputTextKodeBarang.setText(scanContent);
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}