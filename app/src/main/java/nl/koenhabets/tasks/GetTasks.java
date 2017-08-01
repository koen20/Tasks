package nl.koenhabets.tasks;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class GetTasks extends Request<String> {
    private static String url = "http://192.168.2.174:9985/tasks";

    private Response.Listener<String> responListener;

    public GetTasks(String token,
                    Response.Listener<String> responseListener,
                    Response.ErrorListener errorListener) {

        super(Method.GET, url + "?token=" + token, errorListener);

        this.responListener = responseListener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String data;
        try {
            data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            data = new String(response.data);
        }
        return Response.success(data, null);
    }

    @Override
    protected void deliverResponse(String response) {
        responListener.onResponse(response);
    }
}
