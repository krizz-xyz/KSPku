package com.example.pbp;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
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

public class CariPelanggan extends Activity implements SearchView.OnQueryTextListener{
	private SearchView searchView;
	private MenuItem searchMenuItem;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	TextView textView;
	String Value;
	public static CariPelanggan cp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cari_pelanggan);
		
		dbHelper = new mySqlHelper(this);
		cp=this;
		textView = (TextView)findViewById(R.id.textViewCariPelanggan);
		lv=(ListView)findViewById(R.id.listViewCariPelanggan);
		tampilkanData("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cari_pelanggan, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Isi nama pelanggan");
		searchView.onActionViewExpanded();
		menu.findItem(R.id.action_search).setActionView(searchView);
		searchView.setOnQueryTextListener(this);
		return true;
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		tampilkanData(newText);
		return true;
	}
	
	public void tampilkanData(String cari){
		// Buka file untuk dibaca
    	ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	c = db.rawQuery("select nama, kode, alamat, telepon from pelanggan where nama LIKE '%"+ cari + "%'", null);
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
    	textView.setText("Cari Nama Pelanggan ("+Integer.toString(jmlrec)+"Record)");
    	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
    			orderListdb, R.layout.list_2baris4kata,
    			new String[] {"nama", "kode", "alamat", "telepon"},
    			new int[] {R.id.namaPelanggan, R.id.kdPelanggan, R.id.alamatPelanggan, R.id.teleponPelanggan});
    	lv.setAdapter(adapter2);
    	lv.setOnItemClickListener(new OnItemClickListener() {
    		
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			final String selectionnama = ((TextView) view.findViewById(R.id.namaPelanggan)).getText().toString();
    			final String selectionkodeangg = ((TextView) view.findViewById(R.id.kdPelanggan)).getText().toString();
    			Toast.makeText(getApplicationContext(), "nama: "+selectionnama, Toast.LENGTH_SHORT).show();
    			Transaksi.pj.skodePinj=selectionkodeangg;
    			Transaksi.pj.inputTextKodePelanggan.setText(selectionkodeangg);
    			finish();
    		}
    	});
	}

}
