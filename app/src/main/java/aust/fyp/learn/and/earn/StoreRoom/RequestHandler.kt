package aust.fyp.learn.and.earn.StoreRoom

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class RequestHandler private constructor(var context: Context) {

    companion object {

        private var requestHandlerObject: RequestHandler? = null
        private var mRequestQueue: RequestQueue? = null

        fun getInstance(context: Context): RequestHandler? {
            if (requestHandlerObject == null) {
                requestHandlerObject = RequestHandler(context)
            }
            return requestHandlerObject
        }

    }

    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context)
        }
        return mRequestQueue
    }

    fun addToRequestQueue(req: Request<String>) {
        req.setRetryPolicy(
            DefaultRetryPolicy(
                30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        getRequestQueue()!!.add<String>(req)
    }


}