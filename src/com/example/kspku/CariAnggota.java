package com.example.kspku;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CariAnggota extends Activity implements SearchView.OnQueryTextListener {
	private SearchView searchView;
	private MenuItem searchMenuItem;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	TextView textView;
	String Value;
	public static CariAnggota ca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cari_anggota);
		
		dbHelper = new mySqlHelper(this);
		ca=this;
		textView = (TextView)findViewById(R.id.textViewCariAnggota);
		lv=(ListView)findViewById(R.id.listViewCariAnggota);
		tampilkanData("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cari_anggota, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Isi nama anggota");
		searchView.onActionViewExpanded();
		menu.findItem(R.id.action_search).setActionView(searchView);
		searchView.setOnQueryTextListener(this);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void tampilkanData(String cari) {
    	// Buka file untuk dibaca
    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	c = db.rawQuery("select nama, kode, alamat, telepon from anggota where nama LIKE '%"+ cari + "%'", null);
    	int jmlrec=c.getCount();
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
    	textView.setText("Cari Nama Anggota ("+Integer.toString(jmlrec)+"Record)");
    	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
    			orderListdb, R.layout.list_2baris4kata,
    			new String[] {"nama", "kode", "alamat", "telepon"},
    			new int[] {R.id.tglPinjaman, R.id.ttlAngsuran, R.id.namaAnggota, R.id.sisaPinjaman});
    	lv.setAdapter(adapter2);
    	lv.setOnItemClickListener(new OnItemClickListener() {
    		
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			final String selectionnama = ((TextView) view.findViewById(R.id.tglPinjaman)).getText().toString();
    			final String selectionkode = ((TextView) view.findViewById(R.id.ttlAngsuran)).getText().toString();
    			Toast.makeText(getApplicationContext(), "nama: "+selectionnama, Toast.LENGTH_SHORT).show();
    			Peminjaman.pj.skodeAng=selectionkode;
    			Peminjaman.pj.inputTextKode.setText(selectionkode);
    			finish();
    		}
    	});
	}
}
