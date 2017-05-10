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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.et_emailid) EditText etEmailID;
    @Bind(R.id.et_password) EditText etPassword;
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


    //Private Methods

    private void doLogin() {
        Log.d(TAG, "doLogin");

        if (!validateCredentialInput()) {
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

        String email = etEmailID.getText().toString();
        String password = etPassword.getText().toString();

        // TODO: Authentication logic - Remove the following runnable.

        //-----------------------------------------------------------------------------

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.0.17:3000/signin";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d("--------------------",response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", etEmailID.getText().toString());
                params.put("password", etPassword.getText().toString());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(strRequest);
        //-----------------------------------------------------------------------------



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        //onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    private void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        etPassword.setText("");
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    private boolean validateCredentialInput() {
        boolean valid = true;

        String email = etEmailID.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailID.setError("Enter a valid email address");
            valid = false;
        } else {
            etEmailID.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
}
