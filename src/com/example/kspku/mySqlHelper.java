package com.example.kspku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySqlHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "kspku";
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
		
		sql = "create table anggota ("+
				"kode varchar(20) primary key, " +
				"nama varchar(30) not null, " +
				"alamat varchar(40), "+
				"telepon varchar(30)); ";
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);
		
		sql = "create table pinjaman ("+
	            "no_pinjaman varchar(20) primary key, " +
	            "tgl_pinjaman date not null, " +
	            "kode_anggota varchar(20) not null, " +
        		"nilai_pinjaman double, "+			
        		"bunga double, "+			
        		"jml_angsuran integer, "+			
        		"nilai_angsuran double, "+			
	            "nilai_total double); ";			
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);	

		sql = "create table angsuran ("+
	            "no_pinjaman varchar(20) , " +
	            "angs_ke integer, " +
	            "tgl_angsuran date, " +
	            "pokok double, " +
	            "bunga double, " +
	            "sisa_pinjaman double, " +
	            "primary key(no_pinjaman,angs_ke)); ";			
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
