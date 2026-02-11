package com.lupay.demoui.utils;

import static com.lupay.demoui.utils.AlgorithmAES.generateIv;
import static com.lupay.demoui.utils.AlgorithmAES.generateKey;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.blumonpay.capx.functions.RSA;
import com.blumonpay.capx.model.RSAData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Locale;

public class ConfigAmex {
    public DBManager dbManager;
    private Context context;
    private String _rsaamex = "",_tkamex = "",_keyamex="";
    public final boolean[] bndamex = {Boolean.FALSE} ;
    public boolean nuevainit = false;
    public int countamex=0;
    public Integer _statusseller=0;
    private VolleyStringCallBack CallBack;

    public ConfigAmex(Context mContext){
        dbManager = new DBManager(mContext);
        dbManager.open();
        context=mContext;
    }
    public void tpvConfigAmex(String ksn_posId,Integer valor, VolleyStringCallBack callBack) {
        try {
            nuevainit = false;
            CallBack = callBack;
            String URL = Utils.TPVCONFIGAMEX + "/api/apiv0/agrs/terminales/tpv";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("snTerminal", ksn_posId);

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(!object.has("mensaje")){
                            //bndamex[0] =Boolean.TRUE;
                            TRACE.d("tpvConfig: " +  TRACE.NEW_LINE + response.toString() );
                            InitActivaAmex(response.toString(),ksn_posId,valor);
                        }else{
                            dbManager.update(ksn_posId, "0","","",Integer.parseInt("0"),"","");
                            bndamex[0] =Boolean.TRUE;
                            callBack.onSuccess();
                        }
                    } catch (JSONException e) {
                        bndamex[0] =Boolean.FALSE;
                        e.printStackTrace();
                        callBack.onError("",true);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message;
                    Boolean intentar;
                    error.printStackTrace();
                    if (error instanceof TimeoutError) {
                        if(!hasRealInternet()){
                            message = "LA CONEXION A INTERNET ESTA DEBIL O SIN CONEXION, ";
                            intentar = false;
                        } else {
                            message = "EL SERVICIO SUPERO EL TIEMPO DE ESPERA, ";
                            intentar = true;
                        }
                    } else if (error instanceof NoConnectionError){
                        message = "NO HAY CONEXION A INTERNET, ";
                        intentar = false;
                    } else if (error instanceof NetworkError){
                        message = "OCURRIO UN PROBLEMA CON LA RED, ";
                        intentar = false;
                    } else if (error instanceof ServerError){
                        message = "EL SERVICIO NO PUDO PROCESAR LA SOLICITUD, ";
                        intentar = true;
                    } else {
                        message = "OCURRIO UN ERROR INESPERADO, ";
                        intentar = true;
                    }
                    TRACE.d("VolleyError: " +  TRACE.NEW_LINE + error.getMessage() );
                    TRACE.d(message);
                    bndamex[0] =Boolean.FALSE;
                    callBack.onError(message, intentar);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestSingleton.getInstance(context).getRequestQueue().add(stringRequest);
        } catch (JSONException e) {
            bndamex[0] =Boolean.FALSE;
            TRACE.d("JSONException: " +  TRACE.NEW_LINE + e.toString() );
            callBack.onError("",true);
        }
    }
    public void InitActivaAmex(String _tpv,String ksn_posId,Integer valor) {
        String URL="";
        nuevainit = false;
        try {

            JSONObject objtpv = new JSONObject(_tpv);
            String statusseller=objtpv.getString("statusseller").toString();
            String datafield43=objtpv.getString("datafield43").toString();
            String datafield60=objtpv.getString("datafield60").toString();
            _statusseller=Integer.parseInt(statusseller);
            TRACE.d("_statusseller:" +  _statusseller);
            if (_statusseller==1){
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("numserie", ksn_posId);
                if(valor==1){
                    URL = Utils.TERMINAL_AMEX + "/amex/tpv/initactiva";
                }else{
                    generakeyamex();
                    URL =  Utils.TERMINAL_AMEX + "/amex/tpv/initllave";
                    jsonBody.put("tpv", Build.MODEL+"Android smart POS");
                    jsonBody.put("device_tk", _tkamex);
                    jsonBody.put("device_rsa", _rsaamex);
                    jsonBody.put("device_key", _keyamex);
                }

                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TRACE.d("initllaveamex" +  TRACE.NEW_LINE + response.toString() );
                        DatosInicializacion(ksn_posId,response.toString(),datafield43,datafield60);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message;
                        Boolean intentar;
                        error.printStackTrace();
                        if (error instanceof TimeoutError) {
                            if(!hasRealInternet()){
                                message = "LA CONEXION A INTERNET ESTA DEBIL O SIN CONEXION, ";
                                intentar = false;
                            } else {
                                message = "EL SERVICIO SUPERO EL TIEMPO DE ESPERA, ";
                                intentar = true;
                            }
                        } else if (error instanceof NoConnectionError){
                            message = "NO HAY CONEXION A INTERNET, ";
                            intentar = false;
                        } else if (error instanceof NetworkError){
                            message = "OCURRIO UN PROBLEMA CON LA RED, ";
                            intentar = false;
                        } else if (error instanceof ServerError){
                            message = "EL SERVICIO NO PUDO PROCESAR LA SOLICITUD, ";
                            intentar = true;
                        } else {
                            message = "OCURRIO UN ERROR INESPERADO, ";
                            intentar = true;
                        }
                        TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.getMessage() );
                        TRACE.d(message);
                        bndamex[0] =Boolean.FALSE;
                        CallBack.onError(message,intentar);
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        String parsed;
                        try {
                            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException var4) {
                            parsed = new String(response.data);
                        }

                        if (response != null) {
                            responseString = String.valueOf(parsed);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                RequestSingleton.getInstance(context).getRequestQueue().add(stringRequest);
            }else{
                dbManager.update(ksn_posId, "0","","",Integer.parseInt("0"),"","");
                bndamex[0] =Boolean.TRUE;
                CallBack.onSuccess();
            }

        } catch (JSONException e) {
            bndamex[0] =Boolean.FALSE;;
            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );
            CallBack.onError("",true);
        }
    }
    private void DatosInicializacion(String ksn_posId,String _json,String datafield43, String datafield60){
        try {
            JSONObject object = new JSONObject(_json);
            if(object.has("id")){
                if(object.getString("codigo").equals("00") && (Integer.parseInt(object.getString("count"))>0 && Integer.parseInt(object.getString("count"))<1000000)){
                    //dbManager.onUpgrade();
                    dbManager.update(ksn_posId, "1",object.getString("tk").toString(),object.getString("ipek").toString(),Integer.parseInt(object.getString("count")),datafield43,datafield60);
                    nuevainit=false;
                    bndamex[0] =Boolean.TRUE;
                    TRACE.d("Activaamex" +  TRACE.NEW_LINE );
                    CallBack.onSuccess();
                }else{
                    nuevainit=true;
                    bndamex[0] =Boolean.FALSE;
                    TRACE.d("Nuevaamex" +  TRACE.NEW_LINE );
                    CallBack.onError("",true);
                }
            }else if(object.has("codigo")){
                if(object.getString("codigo").equals("72")||object.getString("codigo").equals("11")){
                    nuevainit=true;
                    bndamex[0] =Boolean.FALSE;
                    //tpvConfig(ksn_posId,0);
                    TRACE.d("codigoamex:" + object.getString("codigo"));
                    TRACE.d("Nuevaamex" +  TRACE.NEW_LINE );
                    CallBack.onError("CODIGOAMEX:" + object.getString("codigo") + (object.has("name") ? ", " + object.getString("name").toUpperCase(Locale.ROOT) + ", " : ", "),true);
                }
            }

        } catch (JSONException e) {
            bndamex[0] =Boolean.FALSE;
            CallBack.onError("",true);
        }
    }
    public void initactivaamex(String ksn_posId) {
        String URL=Utils.TERMINAL_AMEX + "/amex/tpv/initactiva";
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("numserie", ksn_posId);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("initactivaamex" +  TRACE.NEW_LINE + response.toString());
                    validainicializacion(ksn_posId,response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.getMessage() );
                    bndamex[0] =Boolean.FALSE;
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            RequestSingleton.getInstance(context).getRequestQueue().add(stringRequest);
        } catch (JSONException e) {
            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );
        }
    }
    private void initnuevaamex(String ksn_posId) {
        String URL=Utils.TERMINAL_AMEX + "/amex/tpv/initllave";
        try {
            generakeyamex();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("numserie", ksn_posId);
            jsonBody.put("device_tk", _tkamex);
            jsonBody.put("device_rsa", _rsaamex);
            jsonBody.put("device_key", _keyamex);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("initnuevaamex" +  TRACE.NEW_LINE + response.toString() );
                    bndamex[0] =Boolean.TRUE;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.getMessage() );
                    bndamex[0] =Boolean.FALSE;
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            RequestSingleton.getInstance(context).getRequestQueue().add(stringRequest);
        } catch (JSONException e) {
            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );
        }
    }
    public boolean validainicializacion(String ksn_posId,String _json) {
        try {
            JSONObject object = new JSONObject(_json);
            if(!object.has("id"))
            {
                initnuevaamex(ksn_posId);
                //dbManager.update(ksn_posId,"1","","","");
                nuevainit=false;
            }
        } catch (JSONException e) {
            bndamex[0] =Boolean.FALSE;
        }
        return true;
    }
    private void generakeyamex()
    {
        RSA rsa = new RSA();

        RSAData rsaD = new RSAData();
        rsaD = rsa.generateKeys("3082010902820100CF57041EC2E7399C2BBD6CB0E8EDFC126B7837442541BCE86CC2804F9D90FE06EAE65B07014D789ED17300540D665213054E3E3A2A16D7FE1CFCC1382AF1485C542469D2AB327522444BF1A1EF1D8B79D9E9317B87D3531B364A8FCD24C0C6476E534D0D89070EEE2CBC999F00C5BEF3B935719AB459BBEE4EA86FEBEAC0F02A4F25D4007BA948E7B1E4A0456EB77107C4FCDAC79125EEE5A9D039995B6111F339DB1296A21D9F2048A8213BE29CE36DF0338D1BC04C3D42C0F6965E9694AFB05203D0BC05E6113AA6DA20DF0AB23DEA631144A8891352D866CBA9423B71890A4FD2B2112CE7BB57081581816232CD831932834EF05AA050C6FEBD434E9512ED0203010001");

        _rsaamex=rsaD.getRsa();
        _keyamex=generateKey(128);
        _tkamex=generateIv();
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork == null) return false;
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    private boolean hasRealInternet(){
        if(!isNetworkAvailable()) return false;

        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }
}
