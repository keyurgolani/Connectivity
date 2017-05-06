package edu.sjsu.cmpe.fourhorsemen.connectivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText tfEmailID;
    @Bind(R.id.input_password) EditText tfPassword;
    @Bind(R.id.btn_login) Button btnLogin;
    @Bind(R.id.link_signup) TextView linkSignUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

//        linkSignUp.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
    }

    public void doLogin() {
        Log.d(TAG, "Login");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            btnLogin.setEnabled(true);
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = tfEmailID.getText().toString();
        String password = tfPassword.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }



    public boolean validate() {
        boolean valid = true;

        String email = tfEmailID.getText().toString();
        String password = tfPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tfEmailID.setError("Enter a valid email address");
            valid = false;
        } else {
            tfEmailID.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            tfPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            tfPassword.setError(null);
        }

        return valid;
    }
}
