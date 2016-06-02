package com.example.weather;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.ErrorListener;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.support.v7.app.ActionBarActivity;
import android.app.DownloadManager.Request;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

public void click(View view)
{
	
	String url = "http://wthrcdn.etouch.cn/weather_mini"+"?"+"citykey=101010100 ";//"http://serveropdb.duapp.com/test.php";
	RequestQueue mQueue = Volley.newRequestQueue(this);
	/*StringRequest stringRequest = new StringRequest(url,  
            new Response.Listener<String>() {  
                @Override  
                public void onResponse(String response) {  
                    Log.d("TAG", response);
                    Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();
                }  
            }, new Response.ErrorListener() {  
                @Override  
                public void onErrorResponse(VolleyError error) {  
                    Log.e("TAG", error.getMessage(), error);  
                }  
            });
	 */
	/*String url = "http://serveropdb.duapp.com/test.php";
	RequestQueue mQueue = Volley.newRequestQueue(this);
	StringRequest stringRequest = new StringRequest(Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            volleyError.printStackTrace();
        }
    }){
		
		
           protected Map<String, String> getParams() throws AuthFailureError {
            	
               String appKey = "25e29678acc94631ebfc5c329d887162";
                String cityName ="武汉";
                Map<String, String> map = new HashMap<String, String>();
                map.put("cityname", cityName);
                map.put("key", appKey);
                map.put("dtype", "json");
                return  map;
            }

	};*/
	
	JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
            new Response.Listener<JSONObject>() {
		String info ;
                    @Override
                    public void onResponse(final JSONObject response) {
                            // 成功获取数据后将数据显示在屏幕上
                            try {
                            	 info = response.toString();
                                    // info = response.getString("UTF-8");
                            } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                            }
                            Log.d("TAG", info);
                            runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                            if (null != info)
                                                    //mTv_weather_info.setText(info);
                                            Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
                                    }
                            });
                    }

            }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Log.d("TAG", error.getMessage(), error);
                    }
            }) {

    @Override
    protected Response<JSONObject> parseNetworkResponse(
                    NetworkResponse response) {

            try {
                    JSONObject jsonObject = new  JSONObject(
                                    new String(response.data, "UTF-8"));
                    return        Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
            } catch (Exception je) {
                    return Response.error(new ParseError(je));
            }
    }

};
	mQueue.add(jsonObjectRequest);//stringRequest);
	
	
}
private String getJsonStringFromGZIP(HttpResponse response) {  
    String jsonString = null;  
    try {  
        InputStream is = response.getEntity().getContent();  
        BufferedInputStream bis = new BufferedInputStream(is);  
        bis.mark(2);  
        // 取前两个字节  
        byte[] header = new byte[2];  
        int result = bis.read(header);  
        // reset输入流到开始位置  
        bis.reset();  
        // 判断是否是GZIP格式  
        int headerData = getShort(header);  
        if (result != -1 && headerData == 0x1f8b) {  
            is = new GZIPInputStream(bis);  
        } else {  
            is = bis;  
        }  
        InputStreamReader reader = new InputStreamReader(is, "utf-8");  
        char[] data = new char[100];  
        int readSize;  
        StringBuffer sb = new StringBuffer();  
        while ((readSize = reader.read(data)) > 0) {  
            sb.append(data, 0, readSize);  
        }  
        jsonString = sb.toString();  
        bis.close();  
        reader.close();  
    } catch (Exception e) {  
        Log.e("HttpTask", e.toString(), e);  
    }  
    return jsonString;  
}  

private int getShort(byte[] data) {  
    return (int) ((data[0] << 8) | data[1] & 0xFF);  
}  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
