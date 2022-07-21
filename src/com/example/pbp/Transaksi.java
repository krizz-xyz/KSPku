package com.example.pbp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Transaksi extends Activity {
	final Context context = this;
	Button buttonKembali, buttonCariBarang, buttonCariPelanggan;
	mySqlHelper dbHelper;
	protected Cursor c,d,e,f;
	DatePickerDialog picker;
	ListView lv;
	String Value;
	String stgljual;
	
	final Calendar cldr = Calendar.getInstance();
	int day = cldr.get(Calendar.DAY_OF_MONTH);
	int month = cldr.get(Calendar.MONTH);
	int year = cldr.get(Calendar.YEAR);
	
	EditText inputTextTanggal,inputTextNo,inputTextKodePelanggan,
	inputTextJumlah,inputTextKodeBarang,inputTextTotal,
	editJudul, editNama;
	
	String skodePinj,TextNo,TextTanggal,TextKodePelanggan,TextJumlah,
	TextKodeBarang,TextTotal;
	public static Transaksi pj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaksi);
		dbHelper = new mySqlHelper(this);
		pj=this;
		lv=(ListView)findViewById(R.id.listViewTransaksi);
		editNama=(EditText)findViewById(R.id.editTextTransaksinNama);
		editNama.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Value = editNama.getText().toString();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tampilkanData(Value);
			}
		});
		buttonKembali = (Button)findViewById(R.id.buttonKembaliTransaksi);
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
		getMenuInflater().inflate(R.menu.transaksi, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_add:
			inputPeminjaman("insert");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void inputPeminjaman(String jns_input) {
		// get input_ip_addres.xml view
		LayoutInflater layoutInflater2 = LayoutInflater.from(context);
		View promptView2 = layoutInflater2.inflate(R.layout.input_transaksi, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
		// set prompt.xml to be the layout file of the alertdialog builder
		alertDialogBuilder2.setView(promptView2);
		inputTextTanggal = (EditText) promptView2.findViewById(R.id.inputTextTransaksiTanggal);
		inputTextNo = (EditText) promptView2.findViewById(R.id.inputTextTransaksiNomor);
		inputTextKodeBarang = (EditText) promptView2.findViewById(R.id.inputTextKodeBarang);
		inputTextJumlah = (EditText) promptView2.findViewById(R.id.inputTextTransaksiJumlah);
		inputTextKodePelanggan = (EditText) promptView2.findViewById(R.id.inputTextKodePelanggan);
		inputTextTotal = (EditText) promptView2.findViewById(R.id.inputTextTransaksiTotal);
		buttonCariBarang = (Button) promptView2.findViewById(R.id.buttonCariBarang);
		buttonCariPelanggan = (Button) promptView2.findViewById(R.id.buttonCariPelanggan);
		
		String smonth;
		String sday;
		if ((month+1) > 9) {
			smonth=Integer.toString(month+1);
		} else {
			smonth="0"+Integer.toString(month+1);
		}
		if (day > 9) {
			sday=Integer.toString(day);
		} else {
			sday="0"+Integer.toString(day);
		}
		inputTextTanggal.setText(year+"-"+smonth+"-"+sday);
	
	inputTextTanggal.setInputType(InputType.TYPE_NULL);
	inputTextTanggal.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			picker = new DatePickerDialog(Transaksi.this, 
					 new DatePickerDialog.OnDateSetListener() {
						
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							String smonth;
							String sday;
							if ((monthOfYear+1) > 9) {
								smonth=Integer.toString(monthOfYear+1);
							} else {
								smonth="0"+Integer.toString(monthOfYear+1);
							}
							if (dayOfMonth > 9) {
								sday=Integer.toString(dayOfMonth);
							} else {
								sday="0"+Integer.toString(dayOfMonth);
							}
							inputTextTanggal.setText(year+"-"+smonth+"-"+sday);
						}
					}, year, month, day);
			picker.show();
		}
	});
	
	buttonCariBarang.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent a = new Intent(Transaksi.this, CariBarang.class);
			startActivity(a);
		}
	});
	
	buttonCariPelanggan.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent a = new Intent(Transaksi.this, CariPelanggan.class);
			startActivity(a);
		}
	});

	// setup a dialog window
	alertDialogBuilder2.setTitle("INPUT TRANSAKSI ");
	if (jns_input=="update") {
		/* inputTextTanggal.setText(TextTanggal); */
	}
	alertDialogBuilder2
			.setCancelable(false)
			.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// TODO Auto-generated method stub
					TextTanggal = inputTextTanggal.getText().toString();
					TextNo = inputTextNo.getText().toString();
					TextKodeBarang = inputTextKodeBarang.getText().toString();
					TextJumlah = inputTextJumlah.getText().toString();
					TextKodePelanggan = inputTextKodePelanggan.getText().toString();
					TextTotal = inputTextTotal.getText().toString();
					
					String sthn=TextTanggal.substring(2,4);
					String sbln=TextTanggal.substring(5,7);
					String stgl=TextTanggal.substring(8,10);
					stgljual=sthn+sbln+stgl;
					
			//		MainActivity.ma.noPinj=getnonota(stgljual);
			//		addpinjaman(MainActivity.ma.noPinj);
					addpinjaman();
					tampilkanData("");
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					}
				});
				// create an alert dialog
	AlertDialog alertD2 = alertDialogBuilder2.create();
	alertD2.show();		
	}

//	private void addpinjaman(String TextNopinj) {
	// TODO Auto-generated method stub
	private void addpinjaman(){
	SQLiteDatabase db = dbHelper.getWritableDatabase();
	c = db.rawQuery("select * from transaksi where no_transaksi='" +TextNo+"';", null);
	if (!c.moveToFirst()) {
		String sql = "insert into transaksi(no_transaksi, tgl, kd_brg, jumlah, " +
				"kd_pelanggan, total) values('"+TextNo+"','"
				+TextTanggal+"','"+TextKodeBarang+"','"+TextJumlah+"','"+TextKodePelanggan+"','"+
				TextTotal+"')";
		try{
			db.execSQL(sql);
			Toast toast = Toast.makeText(getApplicationContext(), "Insert transaksi berhasil...", 
					Toast.LENGTH_SHORT);
			toast.show();
		}
		catch(Exception e) {}
	}else {
		String sql="update transaksi set tgl='"+TextTanggal+
				"',kd_brg='"+TextKodeBarang+"',jumlah='"+TextJumlah+
				"',kd_pelanggan='"+TextKodePelanggan+"',total='"+TextTotal+
				"' where no_transaksi='"+TextNo+"';";
		try{
			db.execSQL(sql);
			Toast toast = Toast.makeText(getApplicationContext(), "Update transaksi berhasil...", Toast.LENGTH_SHORT);
			toast.show();
		}
		catch(Exception e) {}
	}
}
	
	public void tampilkanData(String cari) {
		// TODO Auto-generated method stub
		// Buka file untuk dibaca
    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    		c = db.rawQuery("select no_transaksi, tgl, kd_brg, jumlah, kd_pelanggan, total" +
    	//		"(select nama from anggota where kode_angggota=a.kode_anggota) as nama," +
    			" from transaksi where total LIKE '%"+cari+"%' order by no_transaksi",null);
    		if (c.moveToFirst()) {
    		do {
    			HashMap<String, String> map = new HashMap<String, String>();
   
    			String tglTransaksi = c.getString(0);
    			String noTransaksi = c.getString(1);
    			String kodeBarang = c.getString(2);
    			String jumlah = c.getString(3);
    			String kodePelanggan = c.getString(4);
    			String total = c.getString(5);
    			
    			map.put("tgl", tglTransaksi);
    			map.put("no_transaksi", noTransaksi);
    			map.put("kd_brg", kodeBarang);
    			map.put("jumlah", jumlah);
    			map.put("kd_pelanggan", kodePelanggan);
    			map.put("total", total);
    			orderListdb.add(map);
    		} while (c.moveToNext());
    	}
    	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
    			orderListdb, R.layout.list_2baris6kata,
    			new String[] {"tgl", "no_transaksi", "kd_brg", "jumlah", "kd_pelanggan", "total"},
    			new int[] {R.id.tglTransaksi, R.id.noTransaksi, R.id.kodeBarang, R.id.jumlah, R.id.kdPelanggan, R.id.Total});
    	lv.setAdapter(adapter2);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			final String stglTransaksi = ((TextView) view.findViewById(R.id.tglTransaksi)).getText().toString();
    			final String snoTransaksi = ((TextView) view.findViewById(R.id.noTransaksi)).getText().toString();
    			final String skodeBarang = ((TextView) view.findViewById(R.id.kodeBarang)).getText().toString();
    			final String sjumlah = ((TextView) view.findViewById(R.id.jumlah)).getText().toString();
    			final String skodePelanggan = ((TextView) view.findViewById(R.id.kdPelanggan)).getText().toString();
    			final String stotal = ((TextView) view.findViewById(R.id.Total)).getText().toString();
    		//	MainActivity.ma.noPinj=snopinjaman;
    			skodePinj=snoTransaksi;
    			final CharSequence[] dialogitem = {"Edit", "Delete"};
    			AlertDialog.Builder builder = new AlertDialog.Builder(Transaksi.this);
    			builder.setTitle("pilih ?");
    			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						switch(item){
						case 0:
							TextTanggal=stglTransaksi;
							TextNo=snoTransaksi;
							TextKodeBarang=skodeBarang;
							TextJumlah=sjumlah;
							TextKodePelanggan=skodePelanggan;
							TextTotal=stotal;
							inputPeminjaman("update");
							break;
						case 1:
							ShowConfirmDelete(sjumlah);
							break;
						}
					}
    			});
    			builder.create().show();
    		}
    	}); 
	}
	
	private void ShowConfirmDelete(String Transaksi) {
		// TODO Auto-generated method stub
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
    	ad.setTitle("Sistem Informasi PBP");
    	ad.setMessage("Yakin Hapus Transaksi: "+Transaksi+" ?");
    	ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try{
					db.execSQL("delete from transaksi where no_ransaksi='"+skodePinj+"'");
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
}
