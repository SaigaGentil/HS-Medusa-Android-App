package com.example.login_register_me1;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    public static enum Method{
        POST,PUT,DELETE,GET;
    }
    private URL url;
    private HttpURLConnection con;
    private OutputStream os;

    public HttpRequest(URL url)throws IOException{
        this.url = url;
        con = (HttpURLConnection)this.url.openConnection();
    }

    public HttpRequest(String url)throws IOException{ this(new URL(url));
        Log.d("parameters",url);
    }
    private void prepareAll(Method method)throws IOException{
        con.setDoInput(true);
        con.setRequestMethod(method.name());
        if (method==Method.POST || method==Method.PUT){
            con.setDoOutput(true);
            os = con.getOutputStream();
        }
    }

    public HttpRequest prepare() throws IOException{
        prepareAll(Method.GET);
        return this;
    }

    public HttpRequest prepare(Method method)throws IOException{
        prepareAll(method);
        return this;
    }

    public HttpRequest withHeaders(String... headers){
        for (int i=0, last=headers.length; i<last;){
            String[]h=headers[i].split("[:]");
            con.setRequestProperty(h[0],h[1]);
        }
        return this;
    }

    public HttpRequest withData(HashMap<String,String> params) throws IOException{
        StringBuilder result=new StringBuilder();
        for (Map.Entry<String,String>entry : params.entrySet()){
            result.append((result.length()>0?"&":"")+entry.getKey()+"="+entry.getValue());
            Log.d("parameters",entry.getKey()+"  ===>  "+entry.getValue());
        }
        //withData(result.toString());
        return this;
    }
}
