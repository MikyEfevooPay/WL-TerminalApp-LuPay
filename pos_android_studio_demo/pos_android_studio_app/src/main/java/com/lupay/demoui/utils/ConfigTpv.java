package com.lupay.demoui.utils;

import static com.lupay.demoui.utils.AlgorithmAES.generateIv;
import static com.lupay.demoui.utils.AlgorithmAES.generateKey;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.blumonpay.capx.functions.RSA;
import com.blumonpay.capx.model.RSAData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ConfigTpv {
    public DBManager dbManager;
    public Context context;
    public ProgressDialog spinner;
    private String _rsa = "",_tk = "",_pk = "",_key="";
    public final boolean[] bnd= {Boolean.FALSE} ;
    public boolean nuevainit = false;
    public int count=0;
    public String _jsonca="";
    public Integer _statusseller=0;

    public ConfigTpv(Context mContext){
        dbManager = new DBManager(mContext);
        dbManager.open();
        context=mContext;
    }

    public void tpvConfig(String ksn_posId,Integer valor) {
        try {
            nuevainit = false;
            String URL = Utils.TPVCONFIG + "/api/apiv0/agrs/terminales/tpv";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("snTerminal", ksn_posId);
            jsonBody.put("bd", Utils.TERMINAL_WL_Name);

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(!object.has("mensaje")){
                            //bnd[0] =Boolean.TRUE;
                            bnd[0] =initactiva(response.toString(),ksn_posId,valor);
                            TRACE.d("tpvConfig: " +  TRACE.NEW_LINE + response.toString() );
                        }else{
                            bnd[0] =Boolean.FALSE;
                        }
                    } catch (JSONException e) {
                        bnd[0] =Boolean.FALSE;
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    TRACE.d("VolleyError: " +  TRACE.NEW_LINE + error.getMessage() );
                    bnd[0] =Boolean.FALSE;
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
            bnd[0] =Boolean.FALSE;
            TRACE.d("JSONException: " +  TRACE.NEW_LINE + e.toString() );
        }
    }
    private boolean initactiva(String _tpv,String ksn_posId,Integer valor) {
        String URL="";
        try {

            //String URL =  Utils.TPVCONFIG + "/efevoo/tpv/initllave";
            _jsonca=_tpv;
            JSONObject objtpv = new JSONObject(_tpv);
            String p43=objtpv.getString("p43").toString();
            String p48=objtpv.getString("p48").toString();
            String p120=objtpv.getString("p120").toString();
            String address=objtpv.getString("address").toString();
            String comercio=objtpv.getString("comercio").toString();
            String msi=objtpv.getString("msi").toString();
            String msi3=objtpv.getString("msi3").toString();
            String msi6=objtpv.getString("msi6").toString();
            String msi9=objtpv.getString("msi9").toString();
            String msi12=objtpv.getString("msi12").toString();
            String msi18=objtpv.getString("msi18").toString();
            String minimo3=objtpv.getString("minimo3").toString();
            String minimo6=objtpv.getString("minimo6").toString();
            String minimo9=objtpv.getString("minimo9").toString();
            String minimo12=objtpv.getString("minimo12").toString();
            String minimo18=objtpv.getString("minimo18").toString();
            String interfaz=objtpv.getString("interfaz").toString();
            String codigopostal=objtpv.getString("codigopostal").toString();
            String giro=objtpv.getString("giro").toString();
            String redlogica=objtpv.getString("redlogica").toString();
            String afiliacion=objtpv.getString("afiliacion").toString();
            String datafield43=objtpv.getString("datafield43").toString();
            String datafield60=objtpv.getString("datafield60").toString();
            String statusseller=objtpv.getString("statusseller").toString();
            String emailaddress=objtpv.getString("emailaddress").toString();
            String phonenumber=objtpv.getString("phonenumber").toString();

            _statusseller=Integer.parseInt("0");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("device_id", ksn_posId);
            jsonBody.put("interfaz", interfaz);

            if(valor==1){
                URL = Utils.TERMINAL_API + "/efevoo/tpv/initactiva";
            }else{
                generakey(interfaz);
                URL =  Utils.TERMINAL_API + "/efevoo/tpv/initllave";
                jsonBody.put("tpv", Build.MODEL+"Android smart POS");
                jsonBody.put("device_tk", _tk);
                jsonBody.put("device_rsa", _rsa);
                jsonBody.put("device_p43", p43);
                jsonBody.put("device_p48", p48);
                jsonBody.put("device_p120", p120);
                jsonBody.put("device_address", address);
                jsonBody.put("device_key", _key);
            }

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("initllave" +  TRACE.NEW_LINE + response.toString() );
                    bnd[0] =DatosInicializacion(ksn_posId,response.toString(),p43,p48,p120,address,comercio,msi,msi3,msi6,msi9,msi12,msi18,minimo3,minimo6,minimo9,minimo12,minimo18,interfaz,codigopostal,giro,redlogica,afiliacion,"0","","",emailaddress,phonenumber);
                    //if(spinner.isShowing()) spinner.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.getMessage() );
                    bnd[0] =Boolean.FALSE;
                    if(spinner.isShowing()) spinner.dismiss();
                    //WMX_Ajustes.super.showAlert("informative", "¡INTENTA DE NUEVO!");
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
            bnd[0] =Boolean.FALSE;
            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );
        }
        return bnd[0];
    }
    private boolean DatosInicializacion(String ksn_posId,String _json,String _p43,String _p48,String _p120,String _address,String _comercio,String _msi,String msi3,String msi6,String msi9,String msi12,String msi18,String minimo3,String minimo6,String minimo9,String minimo12,String minimo18,String interfaz,String codigopostal,String giro,String redlogica,String afiliacion,String statusseller,String datafield43, String datafield60, String emailaddress, String phonenumber){
        try {
            JSONObject object = new JSONObject(_json);
            if(object.has("id")){
                if(object.getString("codigo").equals("00") && (Integer.parseInt(object.getString("count"))>0 && Integer.parseInt(object.getString("count"))<1000000)){
                    dbManager.onUpgrade();
                    dbManager.insert(ksn_posId,object.getString("ksn").toString(),object.getString("tk").toString(),object.getString("ipek").toString(),_p43,_p48,_p120,_address,_comercio,_msi,Integer.parseInt(object.getString("count")),msi3,msi6,msi9,msi12,msi18,minimo3,minimo6,minimo9,minimo12,minimo18,interfaz,codigopostal,giro,redlogica,afiliacion,statusseller,datafield43,datafield60,"","",Integer.parseInt("0"),emailaddress,phonenumber);
                    nuevainit=false;
                    bnd[0] =Boolean.TRUE;
                    TRACE.d("Activa" +  TRACE.NEW_LINE );
                }else{
                    nuevainit=true;
                    bnd[0] =Boolean.FALSE;
                    TRACE.d("Nueva" +  TRACE.NEW_LINE );
                }
            }else if(object.has("codigo")){
                if(object.getString("codigo").equals("72")||object.getString("codigo").equals("11")){
                    nuevainit=true;
                    bnd[0] =Boolean.FALSE;
                    //tpvConfig(ksn_posId,0);
                    TRACE.d("codigo:" + object.getString("codigo"));
                    TRACE.d("Nueva" +  TRACE.NEW_LINE );
                }
            }

        } catch (JSONException e) {
            bnd[0] =Boolean.FALSE;;
        }
        return bnd[0];
    }
    private boolean rsa()
    {
        try {
            RSA rsa = new RSA();

            RSAData rsaD = new RSAData();
            rsaD = rsa.generateKeys("3082010902820100CF57041EC2E7399C2BBD6CB0E8EDFC126B7837442541BCE86CC2804F9D90FE06EAE65B07014D789ED17300540D665213054E3E3A2A16D7FE1CFCC1382AF1485C542469D2AB327522444BF1A1EF1D8B79D9E9317B87D3531B364A8FCD24C0C6476E534D0D89070EEE2CBC999F00C5BEF3B935719AB459BBEE4EA86FEBEAC0F02A4F25D4007BA948E7B1E4A0456EB77107C4FCDAC79125EEE5A9D039995B6111F339DB1296A21D9F2048A8213BE29CE36DF0338D1BC04C3D42C0F6965E9694AFB05203D0BC05E6113AA6DA20DF0AB23DEA631144A8891352D866CBA9423B71890A4FD2B2112CE7BB57081581816232CD831932834EF05AA050C6FEBD434E9512ED0203010001");

            _rsa=rsaD.getRsa();
            _pk=rsaD.getPublicKey();
            _tk=rsaD.getTk();
            return true;

        }catch (Throwable t){
            TRACE.d("error rsa: " + t);
            return false;
        }
    }
    private void generakey(String interfaz)
    {
        if(interfaz.equals("Agregador"))
        {
            rsa();
        }
        else
        {
            RSA rsa = new RSA();

            RSAData rsaD = new RSAData();
            rsaD = rsa.generateKeys("3082010902820100CF57041EC2E7399C2BBD6CB0E8EDFC126B7837442541BCE86CC2804F9D90FE06EAE65B07014D789ED17300540D665213054E3E3A2A16D7FE1CFCC1382AF1485C542469D2AB327522444BF1A1EF1D8B79D9E9317B87D3531B364A8FCD24C0C6476E534D0D89070EEE2CBC999F00C5BEF3B935719AB459BBEE4EA86FEBEAC0F02A4F25D4007BA948E7B1E4A0456EB77107C4FCDAC79125EEE5A9D039995B6111F339DB1296A21D9F2048A8213BE29CE36DF0338D1BC04C3D42C0F6965E9694AFB05203D0BC05E6113AA6DA20DF0AB23DEA631144A8891352D866CBA9423B71890A4FD2B2112CE7BB57081581816232CD831932834EF05AA050C6FEBD434E9512ED0203010001");

            _rsa=rsaD.getRsa();
            _key=generateKey(128);
            _tk=generateIv();
        }
    }
}
