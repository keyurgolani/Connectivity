package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import org.json.JSONObject;

/**
 * Created by keyurgolani on 5/10/17.
 */

public interface ResponseHandler {
    void handleSuccess(JSONObject response) throws Exception;

    void handleError(Exception e) throws Exception;
}
