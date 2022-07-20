package com.example.kspku;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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

public class Peminjaman extends Activity {
	final Context context = this;
	Button  buttonKembali, buttonCari;
	mySqlHelper dbHelper;
	protected Cursor c,d,e;
	DatePickerDialog picker;
	ListView lv;
	String Value;
	String stgljual;
	
	final Calendar cldr = Calendar.getInstance();
	int day = cldr.get(Calendar.DAY_OF_MONTH);
	int month = cldr.get(Calendar.MONTH);
	int year = cldr.get(Calendar.YEAR);
	int Bunga=0;
	int XAngs=0;
	long NPinj=0;
	long NAngs=0;
	long Total=0;
	
	EditText inputTextTanggal, inputTextKode, inputTextNPinj, inputTextBunga, 
	inputTextXAngs, inputTextNAngs, inputTextTotal, editNama;
	
	String skodeAng, TextNopinj, TextTanggal, TextKode, TextNPinj, TextBunga,
	TextNama, TextXAngs, TextAngske, TextNAngs, TextTotal, TextSisapinj;
	public static Peminjaman pj;
	
	//** deklarasi angsuran	
		EditText inputTextSisaPinj, inputTextNBayar;	
		String TextNBayar;
		long NPokok, NBunga;	
		String TextNPokok,TextNBunga;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peminjaman);
		dbHelper = new mySqlHelper(this);
		pj=this;
		TextAngske="0";
		lv=(ListView)findViewById(R.id.listViewPeminjaman);
		editNama=(EditText)findViewById(R.id.editTextPeminjamanNama);
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
		buttonKembali = (Button)findViewById(R.id.buttonKembaliPeminjaman);
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
		getMenuInflater().inflate(R.menu.peminjaman, menu);
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
		View promptView2 = layoutInflater2.inflate(R.layout.input_peminjaman, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
		// set prompt.xml to be the layout file of the alertdialog builder
		alertDialogBuilder2.setView(promptView2);
		inputTextTanggal = (EditText) promptView2.findViewById(R.id.inputTextPinjamanTanggal);
		inputTextKode = (EditText) promptView2.findViewById(R.id.inputTextPinjamanKode);
		inputTextNPinj = (EditText) promptView2.findViewById(R.id.inputTextPinjamanNPinj);
		inputTextBunga = (EditText) promptView2.findViewById(R.id.inputTextPinjamanBunga);
		inputTextXAngs = (EditText) promptView2.findViewById(R.id.inputTextPinjamanXAngs);
		inputTextNAngs = (EditText) promptView2.findViewById(R.id.inputTextPinjamanNAngs);
		inputTextTotal = (EditText) promptView2.findViewById(R.id.inputTextPinjamanTotal);
		buttonCari = (Button) promptView2.findViewById(R.id.buttonCari);
		
		String smonth;
		String sday;
		//** edit 10 des 2020
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
		inputTextBunga.setText("12");
		inputTextXAngs.setText("10");
		inputTextNAngs.setEnabled(false);
		inputTextTotal.setEnabled(false);
		
		inputTextNPinj.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (Value !="") {
					Toast toast = Toast.makeText(getApplicationContext(), MainActivity.ma.tandaPemisahKoma(Value), Toast.LENGTH_SHORT);
					toast.show();
					hitungTotal();
				}
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
				if (inputTextNPinj.length() > 0) {
					Value = inputTextNPinj.getText().toString();
				}
			}
		});
		
		inputTextBunga.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (inputTextBunga.length() > 0) {
					Value = inputTextBunga.getText().toString();
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (Value !="") {
					Toast toast = Toast.makeText(getApplicationContext(), MainActivity.ma.tandaPemisahKoma(Value), Toast.LENGTH_SHORT);
					toast.show();
					hitungTotal();
				}
			}
		});
		
		inputTextXAngs.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (inputTextXAngs.length() > 0) {
					Value = inputTextXAngs.getText().toString();
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (Value !="") {
					Toast toast = Toast.makeText(getApplicationContext(), MainActivity.ma.tandaPemisahKoma(Value), Toast.LENGTH_SHORT);
					toast.show();
					hitungTotal();
				}
			}
		});
			
		inputTextTanggal.setInputType(InputType.TYPE_NULL);
		inputTextTanggal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picker = new DatePickerDialog(Peminjaman.this, 
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
		
		buttonCari.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent a = new Intent(Peminjaman.this, CariAnggota.class);
				startActivity(a);
			}
		});
		
		// setup a dialog window
		alertDialogBuilder2.setTitle("INPUT PINJAMAN");
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
						TextKode = inputTextKode.getText().toString();
						TextNPinj = inputTextNPinj.getText().toString();
						TextBunga = inputTextBunga.getText().toString();
						TextXAngs = inputTextXAngs.getText().toString();
						TextNAngs = MainActivity.ma.hilangPemisahKoma(inputTextNAngs.getText().toString());
						TextTotal = MainActivity.ma.hilangPemisahKoma(inputTextTotal.getText().toString());
						
						String sthn=TextTanggal.substring(2,4);
						String sbln=TextTanggal.substring(5,7);
						String stgl=TextTanggal.substring(8,10);
						stgljual=sthn+sbln+stgl;
						
						MainActivity.ma.noPinj=getnonota(stgljual);
						addpinjaman(MainActivity.ma.noPinj);
						tampilkanData("");
						
					}	
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int id) {
								
								dialog.cancel();
							}
						});
		// create an alert dialog
		AlertDialog alertD2 = alertDialogBuilder2.create();
		alertD2.show();
	}
	
		private void hitungTotal() {
			// TODO Auto-generated method stub
			if ((inputTextBunga.length() > 0) && (inputTextXAngs.length() > 0) && (inputTextNPinj.length() > 0)) {
				Bunga = Integer.valueOf(inputTextBunga.getText().toString());
				XAngs = Integer.valueOf(inputTextXAngs.getText().toString());
				NPinj = Long.valueOf(inputTextNPinj.getText().toString());
				NAngs = (NPinj/XAngs)+((NPinj*Bunga)/1200);
				Total = ((NPinj/XAngs)+((NPinj*Bunga)/1200))*XAngs;
				inputTextNAngs.setText(MainActivity.ma.tandaPemisahKoma(Long.toString(NAngs)));
				inputTextTotal.setText(MainActivity.ma.tandaPemisahKoma(Long.toString(Total)));
			}
		}
		
		private void addpinjaman(String noPinj) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			c = db.rawQuery("select * from pinjaman where no_pinjaman='" +noPinj+"';", null);
			
			if (!c.moveToFirst()) {
				
				String sql = "insert into pinjaman(no_pinjaman, tgl_pinjaman, kode_anggota,"+"nilai_pinjaman, bunga, jml_angsuran, nilai_angsuran, nilai_total) values('"+noPinj+"','"
						+TextTanggal+"','"+TextKode+"','"+TextNPinj+"','"+TextBunga+"','"+TextXAngs+"','"+TextNAngs+"','"+TextTotal+"')";
				
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Insert pinjaman berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
			}else {
				String sql="update pinjaman set tgl_pinjaman='"+TextTanggal+
						"',kode_anggota='"+TextKode+"',nilai_pinjaman='"+TextNPinj+
						"',bunga='"+TextBunga+"',jml_angsuran='"+TextXAngs+
						"',nilai_angsuran='"+TextNAngs+"',nilai_total='"+TextTotal+
						"' where no_pinjaman='"+noPinj+"';";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Update pinjaman berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
		}
		}
		
		private String getnonota(String tglpinj) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			int nNo;
			String sNo="";
			try{
				c = db.rawQuery("select max(substr(no_pinjaman,7,3)) from pinjaman where substr(no_pinjaman,1,6)='"+tglpinj+"'", null);
				if(c.moveToFirst()) {
					sNo=c.getString(0);
					nNo=Integer.valueOf(sNo);
				} else {
					nNo=0;
				}
			}
			catch(Exception e)
			{
				nNo=0;
			}
			nNo=nNo+1;
			String sNo1="00"+Integer.toString(nNo);
			String sNo2=sNo1.substring(sNo1.length()-3, sNo1.length());
			return tglpinj+sNo2;
		}
	
		public void tampilkanData(String cari) {
			// TODO Auto-generated method stub
			// Buka file untuk dibaca
	    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    		c = db.rawQuery("select no_pinjaman, tgl_pinjaman, kode_anggota," +
	    			"(select nama from anggota where kode=a.kode_anggota) as nama," +
	    			"nilai_pinjaman, bunga, jml_angsuran, nilai_angsuran, nilai_total " +
	    			"from pinjaman a where nama LIKE '%"+cari+"%' order by no_pinjaman",null);
	    	
	    		if (c.moveToFirst()) {
	    		do {
	    			HashMap<String, String> map = new HashMap<String, String>();
	    			
	    			String noPinjaman = c.getString(0);
	    			String namaAnggota = c.getString(3);
	    			String nilaiPinjaman = c.getString(4);
	    			
	    			String angsKe = "0";
	    			String sisaPinjaman = c.getString(8);
	    			String ttlAngsuran = "0";
	    			
	    			d = db.rawQuery("select no_pinjaman, angs_ke, tgl_angsuran, pokok, bunga, sisa_pinjaman "+
	    							"from angsuran a where no_pinjaman ='" +noPinjaman+ "' order by angs_ke desc", null);
	    			if (d.moveToFirst()) {
	    				angsKe = d.getString(1);
	    				sisaPinjaman = d.getString(5);
	    			}
	    			
	    			angsKe = angsKe+"/"+c.getString(6);
	    			e = db.rawQuery("select sum(pokok+bunga) as total "+
	    							"from angsuran a where no_pinjaman ='" +noPinjaman+ "' group by no_pinjaman", null);
	    			if (e.moveToFirst()) {
	    				ttlAngsuran = e.getString(0);
	    			}
	    			
	    			String ttlAngsuranRp = "Rp. "+MainActivity.ma.tandaPemisahKoma(ttlAngsuran);
	    			String nilaiPinjamanRp = "Rp. "+MainActivity.ma.tandaPemisahKoma(nilaiPinjaman);
	    			String sisaPinjamanRp = "Rp. "+MainActivity.ma.tandaPemisahKoma(sisaPinjaman);
	    			map.put("noPinjaman", noPinjaman);
	    			map.put("namaAnggota", namaAnggota);
	    			map.put("nilaiPinjaman", nilaiPinjamanRp);
	    			map.put("ttlAngsuran", ttlAngsuranRp);
	    			map.put("angsKe", angsKe);
	    			map.put("sisaPinjaman", sisaPinjamanRp);
	    			orderListdb.add(map);
	    		} while (c.moveToNext());
	    	}
	    
	    		  	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
	    			orderListdb, R.layout.list_2baris6kata,
	    			new String[] {"noPinjaman", "nilaiPinjaman", "sisaPinjaman", "namaAnggota", "angsKe", "ttlAngsuran"},
	    			new int[] {R.id.tglPinjaman, R.id.nilaiPinjaman, R.id.ttlAngsuran, R.id.namaAnggota, R.id.angsKe, R.id.sisaPinjaman});
	    	lv.setAdapter(adapter2);
	    	lv.setOnItemClickListener(new OnItemClickListener() {
	    		@SuppressWarnings("unused")
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    			final String snopinjaman = ((TextView) view.findViewById(R.id.tglPinjaman)).getText().toString();
	    			final String snamaanggota = ((TextView) view.findViewById(R.id.namaAnggota)).getText().toString();
	    			final String ssisapinjaman = ((TextView) view.findViewById(R.id.ttlAngsuran)).getText().toString();
	    			MainActivity.ma.noPinj=snopinjaman;
	    			
	    			final CharSequence[] dialogitem = {"Bayar Angsuran", "Data Pembayaran", "Hapus Pinjaman"};
	    			AlertDialog.Builder builder = new AlertDialog.Builder(Peminjaman.this);
	    			builder.setTitle("pilih ?");
	    			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							// TODO Auto-generated method stub
							switch(item){
							case 0:
								if (MainActivity.ma.hilangPemisahKoma(ssisapinjaman).equalsIgnoreCase("0")) {
									Toast.makeText(getApplicationContext(), "Pinjaman sudah lunas, proses angsuran tidak dapat dilanjutkan...",
									Toast.LENGTH_SHORT).show();
								} else {
									inputAngsuran("input");
								}
								break;
							case 1:
									Intent b = new Intent(Peminjaman.this, BayarAngsuran.class);
									startActivity(b);
								break;
							case 2:
									ShowConfirmDelete(snamaanggota);
								break;
							}
						}
	    			});
	    			builder.create().show();
	    		}
	    	}); 
		}
	//*** angsuran
		private void ShowConfirmDelete(String nama) {
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			ad.setTitle("Sistem Informasi KSP");
			ad.setMessage("Proses ini akan menghapus semua angsuran yang berhubungan ..,"+(char)10+
					"Yakin ingin hapus pinjaman nomor: "+MainActivity.ma.noPinj+", nama: "+nama+" ?");
			ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					try{
						db.execSQL("delete from angsuran where no_pinjaman='"+MainActivity.ma.noPinj+"'");
					}
					catch(Exception e){}
					try{
						db.execSQL("delete from pinjaman where no_pinjaman='"+MainActivity.ma.noPinj+"'");
					}
					catch(Exception e){}
					tampilkanData("");
				}
			});
			ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.show();
		}
		
		private void inputAngsuran(String jns_input) {
			// get input_ip_addres.xml view
			LayoutInflater layoutInflater2 = LayoutInflater.from(context);
			View promptView2 = layoutInflater2.inflate(R.layout.input_angsuran, null);
			AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
			alertDialogBuilder2.setView(promptView2);
			
			inputTextTanggal = (EditText) promptView2.findViewById(R.id.inputTextAngsuranTanggal);
			inputTextSisaPinj = (EditText) promptView2.findViewById(R.id.inputTextAngsuranSisaPinj);
			inputTextNBayar = (EditText) promptView2.findViewById(R.id.inputTextAngsuranNBayar);
			
			String smonth;
			String sday;
			if((month+1) > 9){
				smonth=Integer.toString(month+1);
			}else{
				smonth="0"+Integer.toString(month+1);
			}
			if(day > 9){
				sday=Integer.toString(day);
			}else{
				sday="0"+Integer.toString(day);
			}
			inputTextTanggal.setText(year+"-"+smonth+ "-"+sday);
				inputTextSisaPinj.setText("10");
				inputTextNBayar.setText("12");
				inputTextSisaPinj.setEnabled(false);
				inputTextNBayar.setEnabled(false);
				
			SQLiteDatabase db = dbHelper.getWritableDatabase();
				c = db.rawQuery("select no_pinjaman,tgl_pinjaman,kode_anggota," +
						"nilai_pinjaman,bunga,jml_angsuran,nilai_angsuran,nilai_total " +
						"from pinjaman a where no_pinjaman ='" + MainActivity.ma.noPinj + "'", null);
				
				if(c.moveToFirst()) {
					inputTextSisaPinj.setText(MainActivity.ma.tandaPemisahKoma(c.getString(7)));
					inputTextNBayar.setText(MainActivity.ma.tandaPemisahKoma(c.getString(6)));
					
					Bunga=Integer.valueOf(c.getString(4));
					XAngs=Integer.valueOf(c.getString(5));
					NPinj=Long.valueOf(c.getString(3));
					
					NPokok=(NPinj/XAngs);
					NBunga=((NPinj*Bunga)/1200);
					
					NAngs=(NPinj/XAngs)+((NPinj*Bunga)/1200);
					Total=((NPinj/XAngs)+((NPinj*Bunga)/1200));
				}
					d = db.rawQuery("select no_pinjaman,angs_ke,tgl_angsuran,pokok,bunga,sisa_pinjaman " +
							"from angsuran a where no_pinjaman ='" +MainActivity.ma.noPinj+ "' order by angs_ke desc limit 1", null);
				if(d.moveToFirst()) {
					inputTextSisaPinj.setText(MainActivity.ma.tandaPemisahKoma(d.getString(5)));
					TextAngske=d.getString(1);
				}else{
					TextAngske="0";
				}
					inputTextTanggal.setInputType(InputType.TYPE_NULL);
					inputTextTanggal.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// date picker dialog
							picker = new DatePickerDialog(Peminjaman.this,
									new DatePickerDialog.OnDateSetListener() {
										@Override
										public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
											String smonth;
											String sday;
											if((monthOfYear+1) > 9) {
												smonth=Integer.toString(monthOfYear+1);
											}else{
												smonth="0"+Integer.toString(monthOfYear+1);
											}
											if(dayOfMonth > 9) {
												sday=Integer.toString(dayOfMonth);
											}else{
												sday="0"+Integer.toString(dayOfMonth);
											}
											inputTextTanggal.setText(year+"-"+smonth+ "-" +sday);
										}
									}, year, month, day);
							picker.show();
						}
					});
					alertDialogBuilder2.setTitle("BAYAR ANGSURAN");
					if(jns_input=="update"){
					//	inputTextTanggal.setText(TexttANGGAL);
					}
					alertDialogBuilder2.setCancelable(false).setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							TextTanggal=inputTextTanggal.getText().toString();
							String sthn=TextTanggal.substring(2,4);
							String sbln=TextTanggal.substring(5,7);
							String stgl=TextTanggal.substring(8,10);
							stgljual=sthn+sbln+stgl;
								addangsuran(MainActivity.ma.noPinj);
							tampilkanData("");
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog alertD2 = alertDialogBuilder2.create();
			alertD2.show();
		}
		
		private void addangsuran(String nopinj) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			TextAngske=Integer.toString(Integer.valueOf(TextAngske)+1);
			TextSisapinj=Long.toString(Long.valueOf(MainActivity.ma.hilangPemisahKoma(inputTextSisaPinj.getText().toString()))-NAngs);
			
			c = db.rawQuery("select * from angsuran where no_pinjaman='"+nopinj+"' and angs_ke='"+TextAngske+"';", null);
			if(!c.moveToFirst()) {
				String sql="insert into angsuran(no_pinjaman,angs_ke,tgl_angsuran,"+
						"pokok,bunga,sisa_pinjaman) values('"+nopinj+"','"
						+TextAngske+"','"+TextTanggal+"','"+TextNPokok+"','"+TextNBunga+"','"+TextSisapinj+"')";
				try{
					db.execSQL(sql);
					Toast toast=Toast.makeText(getApplicationContext(), "Insert Angsuran Berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}catch(Exception e) {}
			}else{
				String sql="update angsuran set tgl_angsuran='"+TextTanggal+
						"',pokok='"+TextNPokok+"',bunga='"+TextBunga+"',sisa_pinjaman='"+TextSisapinj+
						"' where no_pinjaman='"+nopinj+"' and angs_ke='"+TextAngske+"';";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Update Angsuran Berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}catch(Exception e) {}
			}
		}
}
