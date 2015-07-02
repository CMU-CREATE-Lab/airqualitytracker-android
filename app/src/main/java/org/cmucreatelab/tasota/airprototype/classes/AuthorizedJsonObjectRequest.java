package org.cmucreatelab.tasota.airprototype.classes;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 7/1/15.
 */
public class AuthorizedJsonObjectRequest extends JsonObjectRequest {

    private final String authToken; // must be instantiated


    public AuthorizedJsonObjectRequest(String authToken, int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.authToken = authToken;
    }


    public AuthorizedJsonObjectRequest(String authToken, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        this.authToken = authToken;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // ASSERT authToken is not null
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+authToken);
        return headers;
    }

}
