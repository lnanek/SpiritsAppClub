package club.spiritsapp.network;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import club.spiritsapp.model.Vineyard;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class VineyardsRequest extends Request<List<Vineyard>> {
	private final Gson gson = new Gson();
	private final Map<String, String> headers;
	private final Listener<List<Vineyard>> listener;
	private String httpPostBody;

	Type listType = new TypeToken<List<Vineyard>>() {
	}.getType();

	/**
	 * Make a GET request and return a parsed object from JSON.
	 *
	 * @param url
	 *            URL of the request to make
	 * @param headers
	 *            Map of request headers
	 */
	public VineyardsRequest(final int method, String url,
			Map<String, String> headers, Listener<List<Vineyard>> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.headers = headers;
		this.listener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected void deliverResponse(List<Vineyard> response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<List<Vineyard>> parseNetworkResponse(
			NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

			List<Vineyard> list = gson.fromJson(json, listType);

			return (Response.success(list,
					HttpHeaderParser.parseCacheHeaders(response)));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
	public void setBody(final String body) {
		httpPostBody = body;
	}

	// this is the relevant method
	@Override
	public byte[] getBody() throws AuthFailureError {
		if (null == httpPostBody) {
			return super.getBody();
		}

		return httpPostBody.getBytes();
	}

	public String getBodyContentType() {

		if (null == httpPostBody) {
			return super.getBodyContentType();
		}

		return "application/javascript";
	}

}