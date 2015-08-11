package dlmj.callup.Network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Two on 15/8/3.
 */
public class NetContext {
    private Context mContext;

    private RequestQueue mJsonRequestQueue;
    private static NetContext mInstance = null;

    private NetContext(Context context){
        this.mContext = context;
        this.mJsonRequestQueue = Volley.newRequestQueue(context);
    }

    public static NetContext getInstance(Context context){
        if(mInstance == null){
            synchronized(NetContext.class){
                if(mInstance == null){
                    mInstance = new NetContext(context);
                }
            }
        }
        return mInstance;
    }

    public RequestQueue getJsonRequestQueue(){
        return mJsonRequestQueue;
    }
}
