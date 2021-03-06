package com.example.mo.bandit_v1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button finishSignUpButton = (Button) findViewById(R.id.finishSignUpButton);
        finishSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText vornameSignUpEditText = (EditText) findViewById(R.id.vornameSignUpEditText);
                EditText nachnameSignUpEditText = (EditText) findViewById(R.id.nachnameSignUpEditText);
                EditText emailSignUpEditText = (EditText) findViewById(R.id.emailSignUpEditText);
                EditText passwort1SignUpEditText = (EditText) findViewById(R.id.passwort1SignUpEditText);
                EditText passwort2SignUpEditText = (EditText) findViewById(R.id.passwort2SignUpEditText);
                TextView errorSignUpTextView = (TextView) findViewById(R.id.errorSignUpTextView);

                String vorname = vornameSignUpEditText.getText().toString();
                String nachname = nachnameSignUpEditText.getText().toString();
                String email = emailSignUpEditText.getText().toString();
                String passwort1 = passwort1SignUpEditText.getText().toString();
                String passwort2 = passwort2SignUpEditText.getText().toString();

                SignUpData signUpData;

                if (vorname.isEmpty() || nachname.isEmpty() || email.isEmpty() || passwort1.isEmpty() || passwort2.isEmpty())
                    errorSignUpTextView.setText("Fill all boxes!");
                else {
                    if (passwort1.equals(passwort2)) {

                        signUpData = new SignUpData(vorname, nachname, email, passwort1);
                        new SignUpTask().execute(signUpData);

                    } else
                        errorSignUpTextView.setText("Passwords don't match!");
                }
            }
        });
    }

    private class SignUpTask extends AsyncTask<SignUpData, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(SignUpData... params) {
            // send sign up data to server
            CommunicationUtil commUtil = new CommunicationUtilImpl();
            return commUtil.signUp(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            TextView errorSignUpTextView = (TextView) findViewById(R.id.errorSignUpTextView);
            if (result) {
                errorSignUpTextView.setText("SignUp successfull!");
            } else {
                errorSignUpTextView.setText("Failed to SignUp!");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
