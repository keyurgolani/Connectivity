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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.Utilities;

/**
 * Created by gauravchodwadia on 5/6/17.
 */

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static final int REQUEST_VERIFICATION = 0;
    @Bind(R.id.et_emailid)
    EditText etEmailId;
    @Bind(R.id.et_full_name)
    EditText etFullName;
    @Bind(R.id.et_screen_name)
    EditText etScreenName;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_signup)
    Button btnSignUp;
    @Bind(R.id.link_login)
    TextView linkLogin;
    String fullName;
    String screenName;
    String emailId;
    String password;

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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFICATION && resultCode == RESULT_OK) {
            // By default we just finish the Activity and log them in automatically
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the LoginActiviy
        moveTaskToBack(true);
    }

    private void doSignUp() {
        Log.d(TAG, "Method Entry: Inside doSignUp Method");

        fullName = etFullName.getText().toString();
        screenName = etScreenName.getText().toString();
        emailId = etEmailId.getText().toString();
        password = etPassword.getText().toString();

        if (!validateSignUpDetails()) {
            onDetailValidationFailed();
            return;
        }

        btnSignUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", emailId);
        params.put("password", password);
        params.put("fullname", fullName);
        params.put("screenname", screenName);
        RequestHandler.HTTPRequest(getApplicationContext(), ProjectProperties.METHOD_REGISTER, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) {
                progressDialog.dismiss();
                onSignupSuccess();
            }

            @Override
            public void handleError(Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                onSignupFailed();
            }
        });
    }


    private void onSignupSuccess() {
        btnSignUp.setEnabled(true);

        // Start the Signup activity
        Utilities.cacheProfile(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
        intent.putExtra("email", emailId);
        startActivityForResult(intent, REQUEST_VERIFICATION);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private void onDetailValidationFailed() {
        Toast.makeText(getBaseContext(), "Please check the details that you entered !", Toast.LENGTH_LONG).show();
        btnSignUp.setEnabled(true);
    }

    private void onSignupFailed() {
        // TODO: Add a message saying you're offline.
        Toast.makeText(getBaseContext(), "Something went wrong! Please try again.", Toast.LENGTH_LONG).show();
        btnSignUp.setEnabled(true);
    }

    //UI validation for signup details
    private boolean validateSignUpDetails() {

        boolean valid = true;

        if (fullName.isEmpty() || fullName.length() < 3) {
            etFullName.setError("at least 3 characters");
            valid = false;
        } else {
            etFullName.setError(null);
        }

        if(fullName == null || Character.isDigit(fullName.charAt(0))) {
            etFullName.setError("starts with alphabet only");
            valid = false;
        } else {
            etFullName.setError(null);
        }

        if (screenName == null || screenName.isEmpty() || screenName.length() < 3) {
            etScreenName.setError("at least 3 characters");
            valid = false;
        } else {
            etScreenName.setError(null);
        }

        if(screenName.isEmpty() || Character.isDigit(screenName.charAt(0))) {
            etScreenName.setError("starts with alphabet only");
            valid = false;
        } else {
            etScreenName.setError(null);
        }

        if (emailId.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            etEmailId.setError("enter a valid email address");
            valid = false;
        } else {
            etEmailId.setError(null);
        }


        if (password.isEmpty() || password.length() < 6 || password.length() > 10 || password.matches("^.*[^a-zA-Z0-9 ].*$")) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }


}
