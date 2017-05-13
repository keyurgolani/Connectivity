package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;

/**
 * Created by gauravchodwadia on 5/6/17.
 */

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final int REQUEST_VERIFICATION = 0;
    @Bind(R.id.et_emailid) EditText etEmailId;
    @Bind(R.id.et_first_name) EditText etFirstName;
    @Bind(R.id.et_last_name) EditText etLastName;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.btn_signup) Button btnSignUp;
    @Bind(R.id.link_login) TextView linkLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignUp();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the Signup screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFICATION) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    private void doSignUp() {
        Log.d(TAG, "doSignUp");

        if (!validateSignUpDetails()) {
            onSignupFailed();
            return;
        }

        btnSignUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String emailId = etEmailId.getText().toString();
        String password = etPassword.getText().toString();

        // TODO: SignUp Logic - Progress anim to be diosplayed

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }



    private void onSignupSuccess() {

        //TODO: REST Call Done. Implement Full Logic and separate the REST Call code to a module
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.0.92:3000/register";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email",etEmailId.getText().toString());
                params.put("password",etPassword.getText().toString());
                params.put("fname",etFirstName.getText().toString());
                params.put("lname",etLastName.getText().toString());
                params.put("screenname","Screen Name");
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(strRequest);

        //TODO: Successful signup - e.g. sending the verification code/link

        // This will be done from node at signup time






        btnSignUp.setEnabled(true);

        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
        startActivityForResult(intent, REQUEST_VERIFICATION);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        //TODO: Failed SignUp
        Toast.makeText(getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();

        btnSignUp.setEnabled(true);
    }




    //UI validation for signup details
    private boolean validateSignUpDetails() {

        boolean valid = true;

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String emailId = etEmailId.getText().toString();
        String password = etPassword.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            etFirstName.setError("at least 3 characters");
            valid = false;
        } else {
            etFirstName.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            etLastName.setError("at least 3 characters");
            valid = false;
        } else {
            etLastName.setError(null);
        }


        if (emailId.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            etEmailId.setError("enter a valid email address");
            valid = false;
        } else {
            etEmailId.setError(null);
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
