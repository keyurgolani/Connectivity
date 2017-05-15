package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by keyurgolani on 5/10/17.
 */

public class RequestHandler {

    public static void HTTPRequest(Context context, final String method, final HashMap<String, String> params, final ResponseHandler rh) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest strRequest = new StringRequest(Request.Method.POST, ProjectProperties.getURL(method),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            rh.handleSuccess(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            rh.handleError(error);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(strRequest);
    }
}
