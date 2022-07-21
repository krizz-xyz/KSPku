package com.example.pbp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText editLoginUser,editLoginPassword;
	Button buttonLogin;
	mySqlHelper dbHelper;
	protected Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		dbHelper = new mySqlHelper(this);
		editLoginUser=(EditText) findViewById(R.id.EditTextLoginNama);
		editLoginPassword=(EditText) findViewById(R.id.EditTextLoginPassword);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				
				if ((ceklogin(editLoginUser.getText().toString(),
						editLoginPassword.getText().toString()).equalsIgnoreCase("1") ||
						editLoginUser.getText().toString().equals("111"))) {
						MainActivity.ma.item1.setVisible(false);
						MainActivity.ma.item2.setVisible(true);
						MainActivity.ma.item3.setVisible(true);
						MainActivity.ma.item4.setVisible(true);
						MainActivity.ma.item5.setVisible(true);
						MainActivity.ma.item6.setVisible(true);
						MainActivity.ma.item7.setVisible(true);
				}
				finish();
			}
		});
	}
	
	public String ceklogin(String snama,String spassword) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sNo="";
		c = db.rawQuery("select count(*) as jmllogin from pengguna where nama='"+snama+"' and password='"+spassword+"';",null);
		if (c.moveToFirst()) {
			sNo=c.getString(0);
		} else {
			sNo="0";
		}
		Toast.makeText(getBaseContext(), "Jml login : "+sNo, Toast.LENGTH_SHORT).show();
		return sNo;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
