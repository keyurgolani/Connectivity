package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.customviews.PINEditText;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;

/**
 * Created by gauravchodwadia on 5/7/17.
 */

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = VerificationActivity.class.getSimpleName();

    @Bind(R.id.msg_email)
    TextView msgEmail;
    @Bind(R.id.pin_first_edittext)
    EditText etPin0;
    @Bind(R.id.pin_second_edittext)
    EditText etPin1;
    @Bind(R.id.pin_third_edittext)
    EditText etPin2;
    @Bind(R.id.pin_forth_edittext)
    EditText etPin3;
    @Bind(R.id.btn_verify)
    Button btnVerify;
    @Bind(R.id.link_send_code)
    TextView linkResendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        ButterKnife.bind(this);

        // Getting the email ID from invoking Intent
        Intent receiveIntent = getIntent();
        final String email = receiveIntent.getStringExtra("email");
        msgEmail.setText(email.toString());

        setFocusOnPinPos(0);

        etPin0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    setFocusOnPinPos(1);
                }
            }
        });

        etPin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    setFocusOnPinPos(2);
                } else {
                    setFocusOnPinPos(0);
                }
            }
        });

        etPin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    setFocusOnPinPos(3);
                } else {
                    setFocusOnPinPos(1);
                }
            }
        });

        etPin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    etPin3.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    btnVerify.requestFocus();
                } else {
                    setFocusOnPinPos(2);
                }
            }
        });


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifying the code on click
                final String pincode_string = etPin0.getText().toString()
                        + etPin1.getText().toString()
                        + etPin2.getText().toString()
                        + etPin3.getText();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("code", pincode_string);
                Log.d(TAG, email + " -> " + pincode_string);
                RequestHandler.HTTPRequest(getApplicationContext(), "verifyAccount", params, new ResponseHandler() {
                    @Override
                    public void handleSuccess(JSONObject response) throws JSONException {
                        Log.d(TAG, "Verification Result: " + response.toString());
                        if(response.getInt("status_code") == 200) {
                            String uniqueID = response.getJSONObject("message").getString("unique_id");
                            PreferenceHandler.putAccessKey(uniqueID);
                            setResult(RESULT_OK);
                            finish();
                            // TODO: Start the profile activity.
                        } else {
                            Toast.makeText(getApplicationContext(), "Code Not Matched", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void handleError(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong! Please try again later.", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }


    private void setFocusOnPinPos(int pinPos) {

        switch (pinPos) {
            case 0:
                etPin0.requestFocus();
                break;
            case 1:
                etPin1.requestFocus();
                break;
            case 2:
                etPin2.requestFocus();
                break;
            case 3:
                etPin3.requestFocus();
                break;
            default:
                break;
        }
    }

}
