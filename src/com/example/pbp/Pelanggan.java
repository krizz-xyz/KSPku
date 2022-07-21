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

//** JSON
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pelanggan<SqlLiteDatabase> extends Activity implements OnClickListener {
	final Context context = this;
	Button buttonKodePelanggan, buttonKembali;
	EditText inputTextKode, inputTextNamaPelanggan, inputTextAlamat, inputTextTelepon, editNamaPelanggan;
	String TextKode, TextNamaPelanggan, TextAlamat, TextTelepon;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	String skode, Value;
	
	//** JSON
    final static String NAMA_FILE = "catatan";
    String noImei;
    private String urlupload,urldownload;	
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;	
    Button buttonDownload,buttonUpload,buttonSave;
    ProgressDialog pDialog;
    JSONArray orders = null;
    String[] daftarkode;
    String[] daftarnoimei;
    String[] daftarnama;
    String[] daftaralamat;
    String[] daftartelepon;
    public int lengthdaftar=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pelanggan);
		dbHelper = new mySqlHelper(this);
		editNamaPelanggan=(EditText) findViewById(R.id.editTextPelangganNama);
		lv=(ListView) findViewById(R.id.listViewPelanggan);
		editNamaPelanggan.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Value = editNamaPelanggan.getText().toString();
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
		
		buttonKembali = (Button) findViewById(R.id.buttonPelangganKembali);
		buttonKembali.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		//** JSON
		SharedPreferences pref = getSharedPreferences(NAMA_FILE, MODE_PRIVATE);
	String ServerName=pref.getString("ip", "");	
	urlupload="http://"+ServerName+"/json_pbp_digital/addpelanggan.php";
	urldownload="http://"+ServerName+"/json_pbp_digital/listpelanggan.php";
   	
   		buttonDownload = (Button) findViewById(R.id.buttonPelangganDownload);
   		buttonDownload.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			downloadAnggota();
		}
	});
  		buttonUpload = (Button) findViewById(R.id.buttonPelangganUpload);
   		buttonUpload.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			uploadAnggota();			}
	});
  		buttonSave = (Button) findViewById(R.id.buttonPelangganSave);
   		buttonSave.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (lengthdaftar==0) {
	        	Toast.makeText(getBaseContext(),"Data Array Masih Kosong, Tekan Button DLoad ...",Toast.LENGTH_SHORT).show();	
			} else {
			  SaveLoad();				
			}
		}
	}); 
		tampilkanData("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pelanggan, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			inputpelanggan("insert");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void inputpelanggan(String jns_input) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater2 = LayoutInflater.from(context);
		View promptView2 = layoutInflater2.inflate(R.layout.input_pelanggan, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
		//set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder2.setView(promptView2);
		inputTextNamaPelanggan = (EditText) promptView2.findViewById(R.id.inputTextPelangganNama);
		inputTextKode = (EditText) promptView2.findViewById(R.id.inputTextPelangganKode);
		inputTextAlamat = (EditText) promptView2.findViewById(R.id.inputTextPelangganAlamat);
		inputTextTelepon = (EditText) promptView2.findViewById(R.id.inputTextPelangganTelepon);
		buttonKodePelanggan = (Button) promptView2.findViewById(R.id.buttonScan);
		buttonKodePelanggan.setOnClickListener(this);
		//setup a dialog window
		alertDialogBuilder2.setTitle("INPUT DATA PELANGGAN");
		if (jns_input=="update") {
			inputTextNamaPelanggan.setText(TextNamaPelanggan);
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
				TextNamaPelanggan=inputTextNamaPelanggan.getText().toString();
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
			c = db.rawQuery("select * from pelanggan where kode='" +TextKode+"';",
					null);
			if (!c.moveToFirst()) {
				String sql = "insert into pelanggan(kode, nama, alamat, telepon) values('"+TextKode+"','"
						+TextNamaPelanggan+"','"+TextAlamat+"','"+TextTelepon+"')";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Insert pelanggan berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
			}else {
				String sql="update pelanggan set nama='"+TextNamaPelanggan+
						"',alamat='"+TextAlamat+"',telepon='"+TextTelepon+"' where kode='"+TextKode+"';";
				try{
					db.execSQL(sql);
					Toast toast = Toast.makeText(getApplicationContext(), "Update pelanggan berhasil...", Toast.LENGTH_SHORT);
					toast.show();
				}
				catch(Exception e) {}
		}
		}
		
		public void tampilkanData(String cari) {
	    	// Buka file untuk dibaca
	    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	c = db.rawQuery("select nama, kode, alamat, telepon from pelanggan where nama LIKE '%"+ cari + "%'", null);
	    	
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
	    			new int[] {R.id.namaPelanggan, R.id.kdPelanggan, R.id.alamatPelanggan, R.id.teleponPelanggan});
	    	lv.setAdapter(adapter2);
	    	
	    	lv.setOnItemClickListener(new OnItemClickListener() {
	    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    			final String selectionnama = ((TextView) view.findViewById(R.id.namaPelanggan)).getText().toString();
	    			final String selectionkode = ((TextView) view.findViewById(R.id.kdPelanggan)).getText().toString();
	    			final String selectionalamat = ((TextView) view.findViewById(R.id.alamatPelanggan)).getText().toString();
	    			final String selectiontelepon = ((TextView) view.findViewById(R.id.teleponPelanggan)).getText().toString();
	    			skode = selectionkode;
	    			
	    			final CharSequence[] dialogitem = {"Edit", "Delete"};
	    			AlertDialog.Builder builder = new AlertDialog.Builder(Pelanggan.this);
	    			builder.setTitle("pilih ?");
	    			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							// TODO Auto-generated method stub
							switch(item){
							case 0:
								TextKode=selectionkode;
								TextNamaPelanggan=selectionnama;
								TextAlamat=selectionalamat;
								TextTelepon=selectiontelepon;
								inputpelanggan("update");
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
    	ad.setMessage("Yakin Hapus Pelanggan Nama: "+nama+" ?");
    	ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try{
					db.execSQL("delete from pelanggan where kode='"+skode+"'");
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
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	//**** untuk menyimpan data Pelanggan di tabel Pelanggan pada web server 
    //**** dan untuk mendapatkan kode Pelanggan yang merupakan nomor terakhir dari data 
    public void uploadAnggota() {
	try{	
	   // TODO Auto-generated method stub
		
	   TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	   // mendapatkan nomor imei dan menyimpanya dalam variabel
	   noImei = telephonyManager.getDeviceId();			
	        
	   httpclient=new DefaultHttpClient();
             HttpPost httppost = new HttpPost(urlupload);
     	   SQLiteDatabase db = dbHelper.getReadableDatabase();
       	   c = db.rawQuery("select nama,kode,alamat,telepon from pelanggan order by kode",null);
            if (c.moveToFirst()) {
                do {              	            	
                   String nama = c.getString(0);
                   String kode = c.getString(1);
                   String alamat = c.getString(2);
                   String telepon = c.getString(3);                                       
    				
    	          //add your data
    		nameValuePairs = new ArrayList<NameValuePair>(2);
    		// Always use the same variable name for posting i.e the android side variable name and 				php side variable name should be similar, 
    		nameValuePairs.add(new BasicNameValuePair("noimei", noImei));  
		// $Edittext_value = $_POST['Edittext_value'];
    		nameValuePairs.add(new BasicNameValuePair("kode",kode));
    		nameValuePairs.add(new BasicNameValuePair("nama",nama));
    		nameValuePairs.add(new BasicNameValuePair("alamat",alamat));
    		nameValuePairs.add(new BasicNameValuePair("telepon",telepon));
    			
        		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        		//Execute HTTP Post Request   		
        		ResponseHandler<String> responseHandler = new BasicResponseHandler();
        		final String sresponse = httpclient.execute(httppost, responseHandler);
        		Toast.makeText(Pelanggan.this,"CEK Berhasil...! ", Toast.LENGTH_SHORT).show();
                } while (c.moveToNext());
              }
   	}catch(Exception e){
	     System.out.println("Exception : " + e.getMessage());
	}
	    	
    }	
	
    public void downloadAnggota() {   	
   	new ambilDataAnggota().execute();
    }
	
    public class ambilDataAnggota extends AsyncTask<String, String, String> {    	
	ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onPreExecute() {
	super.onPreExecute();			
	     pDialog = new ProgressDialog(Pelanggan.this);
	     pDialog.setMessage("Loading Data ...");
	     pDialog.setIndeterminate(false);
	     pDialog.setCancelable(false);
	     pDialog.show();			
	}

	@Override
	protected String doInBackground(String... arg0) {
	    JSONParser jParser = new JSONParser();
	    JSONObject json = jParser.getJSONFromUrl(urldownload);

	    try {
		orders = json.getJSONArray("pelanggan");									lengthdaftar = orders.length();
		daftarkode = new String[lengthdaftar];
		daftarnoimei = new String[lengthdaftar];
		daftarnama = new String[lengthdaftar];
		daftaralamat = new String[lengthdaftar];
		daftartelepon = new String[lengthdaftar];

		for (int i = 0; i < orders.length(); i++) {
			JSONObject c = orders.getJSONObject(i);
			HashMap<String, String> map = new HashMap<String, String>();
			String kode = c.getString("kode").trim();
			String noimei = c.getString("noimei").trim();
			String nama = c.getString("nama").trim();
			String alamat = c.getString("alamat").trim();
			String telepon = c.getString("telepon").trim();
				
			daftarkode[i] = kode;
			daftarnoimei[i] = noimei;
			daftarnama[i] = nama;
			daftaralamat[i] = alamat;
			daftartelepon[i] = telepon;
						
			map.put("kode", kode);				
			map.put("nama", nama);
			map.put("alamat", alamat);
			map.put("telepon", telepon);	
			orderList.add(map);
		}
			
	     } catch (JSONException e) {
	    	Toast.makeText(getBaseContext(),"Error ..."+e,Toast.LENGTH_SHORT).show();
	     }
	     return null;
	}

	@Override
	protected void onPostExecute(String result) {
	     // TODO Auto-generated method stub
	     super.onPostExecute(result);
	     pDialog.dismiss();
	     ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
	     	orderList, R.layout.list_2baris4kata,
		new String[] { "nama","kode","alamat" ,"telepon"}, 
		new int[] {R.id.namaPelanggan, R.id.kdPelanggan, R.id.alamatPelanggan, R.id.teleponPelanggan});
	     lv.setAdapter(adapter2);
	 }
  }
	   
    public void SaveLoad() {
    	String sql;    
    	String kode="";
	SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (int i = 0; i < lengthdaftar ; i++) {
        	   kode=daftarkode[i];
    	   c = db.rawQuery("select * from pelanggan where kode='"+kode+"';",null);
    	   if (!c.moveToFirst()) {
    	      sql="insert into pelanggan(kode,nama,alamat,telepon) values('"+daftarkode[i]+"','"
    	           +daftarnama[i]+"','"+daftaralamat[i]+"','"+daftartelepon[i]+"')";
    	      try{
    	         	db.execSQL(sql);
    	         Toast.makeText(getApplicationContext(),"Insert Pelanggan Berhasil...", Toast.LENGTH_SHORT).show(); 	      	      } catch(Exception e) {
        	        	Toast.makeText(getBaseContext(),"Error ..."+e,Toast.LENGTH_SHORT).show();
    	      }        
    	   }else {
    	      sql="update pelanggan set nama='"+daftarnama[i]+
    	          "',alamat='"+daftaralamat[i]+"',telepon='"+daftartelepon[i]+"' where kode='"+daftarkode[i]+"';";
    	      try{
    	        	db.execSQL(sql);
    	         Toast.makeText(getApplicationContext(),"Update Pelanggan Berhasil...", Toast.LENGTH_SHORT).show();
    	      } catch(Exception e) {
        	        Toast.makeText(getBaseContext(),"Error ..."+e,Toast.LENGTH_SHORT).show();
    	      }
    	   }
    	}	
    }	
}