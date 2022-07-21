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

public class CariBarang extends Activity implements SearchView.OnQueryTextListener{
	private SearchView searchView;
	private MenuItem searchMenuItem;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	TextView textView;
	String Value;
	public static CariBarang cp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cari_barang);
		
		dbHelper = new mySqlHelper(this);
		cp=this;
		textView = (TextView)findViewById(R.id.textViewCariBarang);
		lv=(ListView)findViewById(R.id.listViewCariBarang);
		tampilkanData("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cari_barang, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Isi nama barang");
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
    	c = db.rawQuery("select kd_brg, nama_brg, harga from barang where nama_brg LIKE '%"+ cari + "%'", null);
    	int jmlrec=c.getCount();
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
    	textView.setText("Cari Nama Barang ("+Integer.toString(jmlrec)+"Record)");
    	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
    			orderListdb, R.layout.list_2baris3kata,
    			new String[] {"nama_brg", "kd_brg", "harga"},
    			new int[] {R.id.nama, R.id.password, R.id.level});
    	lv.setAdapter(adapter2);
    	lv.setOnItemClickListener(new OnItemClickListener() {
    		
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			final String selectionnama_brg = ((TextView) view.findViewById(R.id.nama)).getText().toString();
    			final String selectionkd_brg = ((TextView) view.findViewById(R.id.password)).getText().toString();
    			Toast.makeText(getApplicationContext(), "nama: "+selectionnama_brg, Toast.LENGTH_SHORT).show();
    			Transaksi.pj.skodePinj=selectionkd_brg;
    			Transaksi.pj.inputTextKodeBarang.setText(selectionkd_brg);
    			finish();
    		}
    	});
	}

}