package com.example.capp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUPActivity extends Activity
{

	EditText editTextUserName, editTextPassword, editTextConfirmPassword;
	Button btnCreateAccount;

	LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.signup);
		// get Instance  of Database Adapter
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		// Get References of Views
		editTextUserName=(EditText)findViewById(R.id.editTextUserName);
		editTextPassword=(EditText)findViewById(R.id.editTextPassword);
		editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);

		btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);


		btnCreateAccount.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				String userName=editTextUserName.getText().toString();
				String password=editTextPassword.getText().toString();
				String confirmPassword=editTextConfirmPassword.getText().toString();

				// check if any of the fields are vacant
				if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Missing a Field", Toast.LENGTH_LONG).show();
					return;
				}
				// check if both password matches
				if(!password.equals(confirmPassword))
				{
					Toast.makeText(getApplicationContext(), "Passwords Does Not Match", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					// Save the Data in Database
					if(loginDataBaseAdapter.insertEntry(userName, password))
					{
						Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
						finish();
					}
				}
			}
		});
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		loginDataBaseAdapter.close();
	}

}

