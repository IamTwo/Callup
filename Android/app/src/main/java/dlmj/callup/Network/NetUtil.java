package dlmj.callup.Network;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Two on 15/8/3.
 */
public class NetUtil {
    private NetUtil(){
        throw new AssertionError();
    }

    public static void sendJsonObjectRequest(Context context, String requestUrl,
                                             JSONObject requestParameter,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest(requestUrl, requestParameter,
                listener, errorListener);
        NetContext netContext = NetContext.getInstance(context);
        netContext.getJsonRequestQueue().add(request);
    }

    public static void sendJsonArrayRequest(Context context, String requestUrl,
                                            Response.Listener<JSONArray> listener,
                                            Response.ErrorListener errorListener){
        JsonArrayRequest request = new JsonArrayRequest(requestUrl, listener, errorListener);
        NetContext netContext = NetContext.getInstance(context);
        netContext.getJsonRequestQueue().add(request);
    }
}
