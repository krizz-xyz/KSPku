package com.example.pbp;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	public MenuItem item1, item2, item3, item4, item5, item6, item7, item8;
	public static MainActivity ma;
	protected Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ma=this;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		item1 = menu.findItem(R.id.login);
		item1.setVisible(true);
		item2 = menu.findItem(R.id.logout);
		item2.setVisible(false);
		item3 = menu.findItem(R.id.pengguna);
		item3.setVisible(true);
		item4 = menu.findItem(R.id.pelanggan);
		item4.setVisible(false);
		item5 = menu.findItem(R.id.barang);
		item5.setVisible(false);
		item6 = menu.findItem(R.id.transaksi);
		item6.setVisible(false);
		item7 = menu.findItem(R.id.setting);
        item7.setVisible(true);       
        item8 = menu.findItem(R.id.keluar);
        item8.setVisible(true);
		return true;
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login:
			Intent a = new Intent(MainActivity.this, Login.class);
			startActivity(a);
			break;
		case R.id.logout:
			MainActivity.ma.item1.setVisible(true);
			MainActivity.ma.item2.setVisible(false);
			MainActivity.ma.item3.setVisible(false);
			MainActivity.ma.item4.setVisible(false);
			MainActivity.ma.item5.setVisible(false);
			MainActivity.ma.item6.setVisible(false);
			MainActivity.ma.item7.setVisible(true);
			break;
		case R.id.pengguna:
			Intent b = new Intent(MainActivity.this, Pengguna.class);
			startActivity(b);
			break;
		case R.id.pelanggan:
			Intent c = new Intent(MainActivity.this, Pelanggan.class);
			startActivity(c);
			break;
		case R.id.barang:
			Intent d = new Intent(MainActivity.this, Barang.class);
			startActivity(d);
			break;
		case R.id.transaksi:
			Intent e = new Intent(MainActivity.this, Transaksi.class);
			startActivity(e);
			break;
		case R.id.setting:
 			Intent f = new Intent(MainActivity.this, Setting.class);
			startActivity(f);
		case R.id.keluar:
			ShowExitDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void ShowExitDialog(){
		AlertDialog.Builder kotakDialog = 
			new AlertDialog.Builder(MainActivity.this);
		kotakDialog.setCancelable(false);
		kotakDialog.setMessage("Anda akan menutup aplikasi ini?");
		kotakDialog.setTitle("Konfirmasi");
		//Mengatur tombol Ya
		kotakDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
		//Menangani kejadian onClick tombol Ya
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
		//Mengatur tombol tidak
		kotakDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
		//Menangani kejadian onClick tombol tidak
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		kotakDialog.create().show();
	}
}