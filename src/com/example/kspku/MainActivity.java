package com.example.kspku;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	public MenuItem item1, item2, item3, item4, item5, item6;
	public static MainActivity ma;
	public String noPinj;
	protected Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ma=this;
	}
	
	public String tandaPemisahKoma(String a){
    	Boolean _minus = false;	
    	String sDes="";
    	int pos=a.indexOf(".");    	
    	int len=a.length();
    	if (pos >= 0) {   
 //** bisa digunakan , atau .   		
        	sDes=","+a.substring(pos+1,len);
    		a=a.substring(0,pos);    	
    	}		
    	Double d = Double.valueOf(a);
    	if (d<0) { _minus = true; }
    	a=a.replace("-","");

    	int panjang=a.length();
    	String c = "";
    	int j=0;
    	
    	for (int i = panjang; i > 0; i--){
    		j++;
    		if (((j % 3) == 1) && (j != 1)){
    			 //** bisa digunakan . atau ,   		
    			c = a.substring(i-1,i) + "." + c;
    		} else {
    			c = a.substring(i-1,i) + c;	
    		}
    	}
    	
    	if (_minus) { c = "-" + c ; }
    	return c+sDes;
    }     

    public String hilangPemisahKoma(String a){
    	a=a.replace("Rp. ","");
    	a=a.replace(".","");
    	return a;
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
		item4 = menu.findItem(R.id.anggota);
		item4.setVisible(true);
		item5 = menu.findItem(R.id.peminjaman);
		item5.setVisible(true);
		item6 = menu.findItem(R.id.keluar);
		item6.setVisible(true);
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
			MainActivity.ma.item6.setVisible(true);
			break;
		case R.id.pengguna:
			Intent b = new Intent(MainActivity.this, Pengguna.class);
			startActivity(b);
			break;
		case R.id.anggota:
			Intent c = new Intent(MainActivity.this, Anggota.class);
			startActivity(c);
			break;
		case R.id.peminjaman:
			Intent d = new Intent(MainActivity.this, Peminjaman.class);
			startActivity(d);
			break;
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
