package com.example.pbp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySqlHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "pbp";
	private static final int DATABASE_VERSION = 1;
	
	public mySqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table pengguna ("+
				"nama varchar(30) primary key, " +
				"password varchar(30) not null, " +
				"level varchar(10)); ";
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);
		
		sql = "create table pelanggan ("+
				"kode varchar(20) primary key, " +
				"nama varchar(30) not null, " +
				"alamat varchar(40), "+
				"telepon varchar(30)); ";
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);
		
		sql = "create table barang ("+
	            "kd_brg varchar(20) primary key, " +
	            "nama_brg varchar(30) not null, " +			
	            "harga double); ";			
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);	

		sql = "create table transaksi ("+
	            "no_transaksi varchar(20) primary key, " +
	            "tgl date, " +
	            "kd_brg varchar (20), " +
	            "jumlah varchar (11), " +
	            "kd_pelanggan varchar(20), " +
				"total double); ";		
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
