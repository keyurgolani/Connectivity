package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.Utilities;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 1;
    private static final int REQUEST_VERIFICATION = 0;

    @Bind(R.id.et_emailid) EditText etEmailID;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.btn_login) Button btnLogin;
    @Bind(R.id.link_signup) TextView linkSignUp;
    String email;
    String password;

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

        linkSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_SIGNUP || requestCode == REQUEST_VERIFICATION) && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


    //Private Methods

    private void doLogin() {
        password = etPassword.getText().toString();
        email = etEmailID.getText().toString();
        if (!validateCredentialInput()) {
            Toast.makeText(getBaseContext(), "Please enter valid details !", Toast.LENGTH_LONG).show();
            btnLogin.setEnabled(true);
            return;
        }
        btnLogin.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        RequestHandler.HTTPRequest(getApplicationContext(), ProjectProperties.METHOD_SIGNIN, params, new ResponseHandler() {
                    @Override
                    public void handleSuccess(JSONObject response) throws JSONException {
                        switch (response.getInt("status_code")) {
                            case 200:
                                onLoginSuccess(response.getJSONObject("message").getString("unique_id"));
                                break;
                            case 301:
                                redirectToVerify();
                                break;
                            case 400:
                                Toast.makeText(getApplicationContext(), "Please enter valid details.", Toast.LENGTH_SHORT);
                                break;
                            case 401:
                                Toast.makeText(getApplicationContext(), "Invalid Credentials. Please try again.", Toast.LENGTH_SHORT);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Internal Error. Please try again later.", Toast.LENGTH_SHORT);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void handleError(Exception e) {
                        e.printStackTrace();
                        onLoginFailed();
                        progressDialog.dismiss();
                    }
                });
    }

    private void redirectToVerify() {
        Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
        intent.putExtra("email", email);
        startActivityForResult(intent, REQUEST_VERIFICATION);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private void onLoginSuccess(String uniqueID) {
        btnLogin.setEnabled(true);
        PreferenceHandler.putAccessKey(uniqueID);
        Utilities.cacheProfile(getApplicationContext());
        Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_LONG).show();
        finish();
    }

    private void onLoginFailed() {
        etPassword.setText("");
        Toast.makeText(getBaseContext(), "An Error Occured. Please try again later.", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    private boolean validateCredentialInput() {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailID.setError("Enter a valid email address");
            valid = false;
        } else {
            etEmailID.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
}
