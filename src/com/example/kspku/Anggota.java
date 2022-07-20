package com.example.kspku;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Anggota<SqlLiteDatabase> extends Activity implements OnClickListener {
	final Context context = this;
	Button buttonKodeAnggota, buttonKembali;
	EditText inputTextKode, inputTextNama, inputTextAlamat, inputTextTelepon, editNama;
	String TextKode, TextNama, TextAlamat, TextTelepon;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	String skode, Value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anggota);
		dbHelper = new mySqlHelper(this);
		editNama=(EditText) findViewById(R.id.editTextAnggotaNama);
		lv=(ListView) findViewById(R.id.listViewAnggota);
		editNama.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tampilkanData(Value);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Value = editNama.getText().toString();
			}
		});
		
		buttonKembali = (Button) findViewById(R.id.buttonAnggotaKembali);
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
		getMenuInflater().inflate(R.menu.anggota, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			inputanggota("insert");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void inputanggota(String jns_input) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater2 = LayoutInflater.from(context);
		View promptView2 = layoutInflater2.inflate(R.layout.input_anggota, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
		//set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder2.setView(promptView2);
		inputTextNama = (EditText) promptView2.findViewById(R.id.inputTextAnggotaNama);
		inputTextKode = (EditText) promptView2.findViewById(R.id.inputTextAnggotaKode);
		inputTextAlamat = (EditText) promptView2.findViewById(R.id.inputTextAnggotaAlamat);
		inputTextTelepon = (EditText) promptView2.findViewById(R.id.inputTextAnggotaTelepon);
		buttonKodeAnggota = (Button) promptView2.findViewById(R.id.buttonScan);
		buttonKodeAnggota.setOnClickListener(this);
		//setup a dialog window
		alertDialogBuilder2.setTitle("INPUT DATA ANGGOTA");
		if (jns_input=="update") {
			inputTextNama.setText(TextNama);
			inputTextKode.setText(TextKode);
			inputTextAlamat.setText(TextAlamat);
			inputTextTelepon.setText(TextTelepon);
		}
		alertDialogBuilder2
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				TextKode=inputTextKode.getText().toString();
				TextNama=inputTextNama.getText().toString();
				TextAlamat=inputTextAlamat.getText().toString();
				TextTelepon=inputTextTelepon.getText().toString();
				addanggota();
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
		
		private void addanggota() {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			c = db.rawQuery("select * from anggota where kode='" +TextKode+"';",
					null);
			if (!c.moveToFirst()) {
				String sql = "insert into anggota(kode, nama, alamat, telepon) values('"+TextKode+"','"
						+TextNama+"','"+TextAlamat+"','"+TextTelepon+"')";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Insert anggota berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
			}else {
				String sql="update anggota set nama='"+TextNama+
						"',alamat='"+TextAlamat+"',telepon='"+TextTelepon+"'where kode='"+TextKode+"';";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Update anggota berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
		}
		}
		
		public void tampilkanData(String cari) {
	    	// Buka file untuk dibaca
	    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	c = db.rawQuery("select nama, kode, alamat, telepon from anggota where nama LIKE '%"+ cari + "%'", null);
	    	
	    	if (c.moveToFirst()) {
	    		do {
	    			HashMap<String, String> map = new HashMap<String, String>();
	    			
	    			String nama = c.getString(0);
	    			String kode = c.getString(1);
	    			String alamat = c.getString(2);
	    			String telepon = c.getString(3);
	    			
	    			map.put("nama", nama);
	    			map.put("kode", kode);
	    			map.put("alamat", alamat);
	    			map.put("telepon", telepon);
	    			orderListdb.add(map);
	    		} while (c.moveToNext());
	    	}
	    	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
	    			orderListdb, R.layout.list_2baris4kata,
	    			new String[] {"nama", "kode", "alamat", "telepon"},
	    			new int[] {R.id.tglPinjaman, R.id.ttlAngsuran, R.id.namaAnggota, R.id.sisaPinjaman});
	    	lv.setAdapter(adapter2);
	    	
	    	lv.setOnItemClickListener(new OnItemClickListener() {
	    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    			final String selectionnama = ((TextView) view.findViewById(R.id.tglPinjaman)).getText().toString();
	    			final String selectionkode = ((TextView) view.findViewById(R.id.ttlAngsuran)).getText().toString();
	    			final String selectionalamat = ((TextView) view.findViewById(R.id.namaAnggota)).getText().toString();
	    			final String selectiontelepon = ((TextView) view.findViewById(R.id.sisaPinjaman)).getText().toString();
	    			skode = selectionkode;
	    			
	    			final CharSequence[] dialogitem = {"Edit", "Delete"};
	    			AlertDialog.Builder builder = new AlertDialog.Builder(Anggota.this);
	    			builder.setTitle("pilih ?");
	    			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							// TODO Auto-generated method stub
							switch(item){
							case 0:
								TextKode=selectionkode;
								TextNama=selectionnama;
								TextAlamat=selectionalamat;
								TextTelepon=selectiontelepon;
								inputanggota("update");
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
    	ad.setTitle("Sistem Informasi KSP");
    	ad.setMessage("Yakin Hapus Anggota Nama: "+nama+" ?");
    	ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try{
					Toast toast = Toast.makeText(getApplicationContext(), "delete from pengguna where nama='"+skode+"'", Toast.LENGTH_SHORT);
					toast.show();
					
					db.execSQL("delete from anggota where kode='"+skode+"'");
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
			inputTextKode.setText(scanContent);
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	};
}