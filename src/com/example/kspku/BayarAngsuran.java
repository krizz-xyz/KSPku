package com.example.kspku;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class BayarAngsuran extends Activity {
	Button buttonCetak,buttonKembali;
	mySqlHelper dbHelper;
	protected Cursor c;
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bayar_angsuran);
		dbHelper = new mySqlHelper(this);
		lv=(ListView) findViewById(R.id.listViewBayarAngsuran);
		buttonCetak = (Button) findViewById(R.id.buttonBayarAngsCetak);
		buttonCetak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		buttonKembali = (Button) findViewById(R.id.buttonBayarAngsKembali);
		buttonKembali.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tampilkanData(MainActivity.ma.noPinj);
	}

	public void tampilkanData(String cari) {
		// Buat array dinamis
		ArrayList<HashMap<String, String>> orderListdb = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		c = db.rawQuery("select no_pinjaman,angs_ke,tgl_angsuran,pokok,bunga,sisa_pinjaman " +
				"from angsuran a where no_pinjaman LIKE '%" + cari + "%' order by angs_ke", null);
		if(c.moveToFirst()) {
			do{
				HashMap<String, String> map = new HashMap<String, String>();
				String noPinjaman = c.getString(0);
				String angsKe = c.getString(1);
				String tglAngsuran = c.getString(2);
				String pokok = c.getString(3);
				String bunga = c.getString(4);
				String sisaPinjaman = c.getString(5);
				angsKe = "Angs Ke-"+angsKe;
				String pokokRp="Rp. "+MainActivity.ma.tandaPemisahKoma(pokok);
				String bungaRp="Rp. "+MainActivity.ma.tandaPemisahKoma(bunga);
				String sisaPinjamanRp="Rp. "+MainActivity.ma.tandaPemisahKoma(sisaPinjaman);
				
				map.put("noPinjaman", noPinjaman);
				map.put("angsKe", angsKe);
				map.put("tglAngsuran", tglAngsuran);
				map.put("pokok", pokokRp);
				map.put("bunga", bungaRp);
				map.put("sisaPinjaman", sisaPinjamanRp);
				orderListdb.add(map);
			}while(c.moveToNext());
		}
	
	ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
			orderListdb, R.layout.list_2baris6kata,
			new String[] { "noPinjaman","tglAngsuran" ,"sisaPinjaman" ,"angsKe","bunga" ,"pokok"},
			new int[] { R.id.tglPinjaman,R.id.nilaiPinjaman,R.id.namaAnggota, R.id.angsKe, R.id.sisaPinjaman });
	lv.setAdapter(adapter2);
	
	lv.setOnItemClickListener(new OnItemClickListener(){
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final String snopinjaman = ((TextView) view.findViewById(R.id.tglPinjaman)).getText().toString();
			final String sangske = ((TextView) view.findViewById(R.id.namaAnggota)).getText().toString();
		//	nopinj=snopinjaman;
			
			final CharSequence[] dialogitem = {"Edit","Delete"};
			AlertDialog.Builder builder = new AlertDialog.Builder(BayarAngsuran.this);
			builder.setTitle("Pilih ?");
			builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch(item){
					case 0 :
						break;
					case 1 :
						break;
					}
				}
			});
			builder.create().show();
		}
	});
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bayar_angsuran, menu);
		return true;
	}

}
