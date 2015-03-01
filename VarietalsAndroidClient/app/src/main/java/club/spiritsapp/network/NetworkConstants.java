package club.spiritsapp.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;

/**
 * Created by lnanek on 2/28/15.
 */
public class NetworkConstants {


    // Currently broken Modulus.io server
    public static final String SERVER = "http://api.varietals.club/";

    //public static final String SERVER = "http://api-varietals.cfapps.io/";

    public static final RequestQueue getQueue(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        return queue;
    }

    public static void add(final Context context, final Request request) {
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        getQueue(context).add(request);
    }

}
