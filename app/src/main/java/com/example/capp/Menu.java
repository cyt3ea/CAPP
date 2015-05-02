//LOGIN PAGE

package com.example.capp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Menu extends Activity {
	static boolean load = false;

	Button btnSignIn,btnSignUp;
	LoginDataBaseAdapter loginDataBaseAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getActionBar().hide();
		setContentView(R.layout.activity_main);
		
		// create the instance of Database
		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		// Get The Reference Of Buttons
		btnSignIn=(Button)findViewById(R.id.buttonSignIn);
		btnSignUp=(Button)findViewById(R.id.buttonSignUP);

		// Set OnClick Listener on SignUp button 
		btnSignUp.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				/// Create Intent for SignUpActivity and Start The Activity
				Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});

		// get the References of views
		final EditText editTextUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
		final EditText editTextPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);

		// Set On ClickListener
		btnSignIn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				// get The User name and Password 
				String userName = editTextUserName.getText().toString();
				String password = editTextPassword.getText().toString();

				// fetch the Password form database for respective user name
				String storedPassword = loginDataBaseAdapter.getSingleEntry(userName);

				// check if the Stored password matches with  Password entered by user
				if(password.equals(storedPassword))
				{
					Toast.makeText(Menu.this, "Login Successful", Toast.LENGTH_LONG).show();
					Intent intentHomePage = new Intent(getApplicationContext(), HomePage.class);
					intentHomePage.putExtra("key", userName);
					startActivity(intentHomePage);
					finish();
				}
				else
					Toast.makeText(Menu.this, "User Name and Password Does Not Match", Toast.LENGTH_LONG).show();
			}
		});

	}

	public static boolean getLoad(){
		return load;
	}
	//
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// Close The Database
		//loginDataBaseAdapter.close();
	}


}
