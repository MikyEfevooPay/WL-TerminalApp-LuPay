package com.lupay.demoui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.lupay.demoui.config.PenConfig;
import com.lupay.demoui.utils.BitmapUtil;
import com.lupay.demoui.utils.DisplayUtil;
import com.lupay.demoui.utils.RequestSingleton;
import com.lupay.demoui.utils.StatusBarCompat;
import com.lupay.demoui.utils.SystemUtil;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Utils;
import com.lupay.demoui.view.CircleView;
import com.lupay.demoui.view.PaintSettingWindow;
import com.lupay.demoui.view.PaintView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.lupay.demoui.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Blank handwriting drawing board
 *
 * @author king
 * @since 2018/07/10 14:20
 */
public class PaintActivity extends BaseActivity implements View.OnClickListener, PaintView.StepCallback {

    /**
     * Canvas maximum width
     */
    public static final int CANVAS_MAX_WIDTH = 3000;
    /**
     * Canvas maximum height
     */
    public static final int CANVAS_MAX_HEIGHT = 3000;

    private ImageView mHandView;
    private ImageView mUndoView;
    private ImageView mRedoView;
    private ImageView mPenView;
    private Button mClearView;
    private Button mOkView;
    private CircleView mSettingView;

    private PaintView mPaintView;

    private ProgressDialog mSaveProgressDlg;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;

    private String mSavePath;
    private boolean hasSize = false;

    private float mWidth;
    private float mHeight;
    private float widthRate = 1.0f;
    private float heightRate = 1.0f;
    private int bgColor,v_trans_id;
    private boolean isCrop;
    private String format;
    private String type_transaction,v_total,v_time,v_card,v_redtarjeta,v_tipotarjeta,v_AID,v_ARQC,v_noauth,v_approve,ksn_posId,v_emisor,v_nip,v_entrada,v_months,v_months_total,v_tip,v_subtotal;
    private Intent intent;
    private PaintSettingWindow settingWindow;
    @Override
    protected int getLayoutId() {
        return R.layout.sign_activity_paint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.wmx_title_welcome));
        View mCancelView = findViewById(R.id.tv_cancel);
        mOkView = findViewById(R.id.tv_ok);

        mPaintView = findViewById(R.id.paint_view);
        mHandView = findViewById(R.id.btn_hand);
        mUndoView = findViewById(R.id.btn_undo);
        mRedoView = findViewById(R.id.btn_redo);
        mPenView = findViewById(R.id.btn_pen);
        mClearView = findViewById(R.id.btn_clear);
        mSettingView = findViewById(R.id.btn_setting);
        mUndoView.setOnClickListener(this);
        mRedoView.setOnClickListener(this);
        mPenView.setOnClickListener(this);
        mClearView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
        mHandView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);
        mOkView.setOnClickListener(this);

        mPenView.setSelected(true);
        mUndoView.setEnabled(false);
        mRedoView.setEnabled(false);
        mClearView.setEnabled(!mPaintView.isEmpty());

        mUndoView.setVisibility(View.GONE);
        mRedoView.setVisibility(View.GONE);
        mPenView.setVisibility(View.GONE);
        mSettingView.setVisibility(View.GONE);
        mCancelView.setVisibility(View.GONE);

        mPaintView.setBackgroundColor(Color.WHITE);
        mPaintView.setStepCallback(this);

        PenConfig.PAINT_SIZE_LEVEL = PenConfig.getPaintTextLevel(this);
        PenConfig.PAINT_COLOR = PenConfig.getPaintColor(this);

        mSettingView.setPaintColor(PenConfig.PAINT_COLOR);
        mSettingView.setRadiusLevel(PenConfig.PAINT_SIZE_LEVEL);

        setThemeColor(PenConfig.THEME_COLOR);
        //BitmapUtil.setImage(mClearView, R.drawable.sign_ic_clear, PenConfig.THEME_COLOR);
        BitmapUtil.setImage(mPenView, R.drawable.sign_ic_pen, PenConfig.THEME_COLOR);
        BitmapUtil.setImage(mRedoView, R.drawable.sign_ic_redo, mPaintView.canRedo() ? PenConfig.THEME_COLOR : Color.LTGRAY);
        BitmapUtil.setImage(mUndoView, R.drawable.sign_ic_undo, mPaintView.canUndo() ? PenConfig.THEME_COLOR : Color.LTGRAY);
        //BitmapUtil.setImage(mClearView, R.drawable.sign_ic_clear, !mPaintView.isEmpty() ? PenConfig.THEME_COLOR : Color.LTGRAY);
//        mSettingView.setOutBorderColor(PenConfig.THEME_COLOR);
        BitmapUtil.setImage(mHandView, R.drawable.sign_ic_hand, PenConfig.THEME_COLOR);
        initData();
    }

    /**
     * Get the default width of the canvas
     *
     * @return
     */
    private int getResizeWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && dm.widthPixels < dm.heightPixels) {
            return (int) (dm.heightPixels * widthRate);
        }
        return (int) (dm.widthPixels * widthRate);
    }

    /**
     * Get the default height of the canvas
     *
     * @return
     */
    private int getResizeHeight() {
        int toolBarHeight = getResources().getDimensionPixelSize(R.dimen.sign_grid_toolbar_height);
        int actionbarHeight = getResources().getDimensionPixelSize(R.dimen.sign_actionbar_height);
        int statusBarHeight = StatusBarCompat.getStatusBarHeight(this);
        int otherHeight = toolBarHeight + actionbarHeight + statusBarHeight;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && dm.widthPixels < dm.heightPixels) {
            return (int) ((dm.widthPixels - otherHeight) * heightRate);
        }
        return (int) ((dm.heightPixels - otherHeight) * heightRate);
    }

    protected void initData() {
        type_transaction = getIntent().getStringExtra("type_transaction");
        v_total = getIntent().getStringExtra("v_total");
        v_time = getIntent().getStringExtra("v_time");
        v_card = getIntent().getStringExtra("v_card");
        v_redtarjeta = getIntent().getStringExtra("v_redtarjeta");
        v_tipotarjeta = getIntent().getStringExtra("v_tipotarjeta");
        v_AID = getIntent().getStringExtra("v_AID");
        v_ARQC = getIntent().getStringExtra("v_ARQC");
        v_noauth = getIntent().getStringExtra("v_noauth");
        v_approve = getIntent().getStringExtra("v_approve");
        ksn_posId = getIntent().getStringExtra("ksn_posId");
        v_emisor = getIntent().getStringExtra("v_emisor");
        v_nip = getIntent().getStringExtra("v_nip");
        v_entrada = getIntent().getStringExtra("v_entrada");
        v_trans_id = getIntent().getIntExtra("v_trans_id", 0);
        if (type_transaction.equals("MSI")) {
            v_months = getIntent().getStringExtra("v_months");
            v_months_total = getIntent().getStringExtra("v_months_total");

        } else {
            v_months = getIntent().getStringExtra("v_months");
            v_tip = getIntent().getStringExtra("v_tip");
            v_subtotal = getIntent().getStringExtra("v_subtotal");
        }


        isCrop = getIntent().getBooleanExtra("crop", false);
        format = getIntent().getStringExtra("format");
        bgColor = getIntent().getIntExtra("background", Color.TRANSPARENT);
        String mInitPath = getIntent().getStringExtra("image");
        float bitmapWidth = getIntent().getFloatExtra("width", 1.0f);
        float bitmapHeight = getIntent().getFloatExtra("height", 1.0f);

        if (bitmapWidth > 0 && bitmapWidth <= 1.0f) {
            widthRate = bitmapWidth;
            mWidth = getResizeWidth();
        } else {
            hasSize = true;
            mWidth = bitmapWidth;
        }
        if (bitmapHeight > 0 && bitmapHeight <= 1.0f) {
            heightRate = bitmapHeight;
            mHeight = getResizeHeight();
        } else {
            hasSize = true;
            mHeight = bitmapHeight;
        }
        if (mWidth > CANVAS_MAX_WIDTH) {
            //Toast.makeText(getApplicationContext(), "the width of the drawing board has exceeded" + CANVAS_MAX_WIDTH, Toast.LENGTH_LONG).show();
            PaintActivity.super.showAlert("informative", "EL ANCHO DEL TABLERO DE DIBUJO HA EXCEDIDO");
            finish();
            return;
        }
        if (mHeight > CANVAS_MAX_WIDTH) {
            //Toast.makeText(getApplicationContext(), "the height of the drawing board has exceeded" + CANVAS_MAX_WIDTH, Toast.LENGTH_LONG).show();
            PaintActivity.super.showAlert("informative", "LA ALTURA DEL TABLERO DE DIBUJO HA EXCEDIDO");
            finish();
            return;
        }
        //Initial drawing board settings
        if (!hasSize && !TextUtils.isEmpty(mInitPath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(mInitPath);
            mWidth = bitmap.getWidth();
            mHeight = bitmap.getHeight();
            hasSize = true;
            if (mWidth > CANVAS_MAX_WIDTH || mHeight > CANVAS_MAX_HEIGHT) {
                bitmap = BitmapUtil.zoomImg(bitmap, CANVAS_MAX_WIDTH, CANVAS_MAX_WIDTH);
                mWidth = bitmap.getWidth();
                mHeight = bitmap.getHeight();
            }
        }
        mPaintView.init((int) mWidth, (int) mHeight, mInitPath);
        if (bgColor != Color.TRANSPARENT) {
            mPaintView.setBackgroundColor(bgColor);
        }
    }

    /**
     * Horizontal and vertical screen switching
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (settingWindow != null) {
            settingWindow.dismiss();
        }

        int resizeWidth = getResizeWidth();
        int resizeHeight = getResizeHeight();
        if (mPaintView != null && !hasSize) {
            mPaintView.resize(mPaintView.getLastBitmap(), resizeWidth, resizeHeight);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_setting) {
            showPaintSettingWindow();

        } else if (i == R.id.btn_hand) {
            //Switch whether writing is allowed or not
            mPaintView.setFingerEnable(!mPaintView.isFingerEnable());
            if (mPaintView.isFingerEnable()) {
                BitmapUtil.setImage(mHandView, R.drawable.sign_ic_hand, PenConfig.THEME_COLOR);
            } else {
                BitmapUtil.setImage(mHandView, R.drawable.sign_ic_drag, PenConfig.THEME_COLOR);
            }

        } else if (i == R.id.btn_clear) {
            mPaintView.reset();

        } else if (i == R.id.btn_undo) {
            mPaintView.undo();

        } else if (i == R.id.btn_redo) {
            mPaintView.redo();

        } else if (i == R.id.btn_pen) {
            if (!mPaintView.isEraser()) {
                mPaintView.setPenType(PaintView.TYPE_ERASER);
                BitmapUtil.setImage(mPenView, R.drawable.sign_ic_eraser, PenConfig.THEME_COLOR);
            } else {
                mPaintView.setPenType(PaintView.TYPE_PEN);
                BitmapUtil.setImage(mPenView, R.drawable.sign_ic_pen, PenConfig.THEME_COLOR);
            }
        } else if (i == R.id.tv_ok) {
            save();

        } else if (i == R.id.tv_cancel) {
            if (!mPaintView.isEmpty()) {
                showQuitTip();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mPaintView != null) {
            mPaintView.release();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }


    /**
     * Pop up brush settings
     */
    private void showPaintSettingWindow() {
        settingWindow = new PaintSettingWindow(this);
        settingWindow.setSettingListener(new PaintSettingWindow.OnSettingListener() {
            @Override
            public void onColorSetting(int color) {
                mPaintView.setPaintColor(color);
                mSettingView.setPaintColor(color);
            }

            @Override
            public void onSizeSetting(int index) {
                mSettingView.setRadiusLevel(index);
                mPaintView.setPaintWidth(PaintSettingWindow.PEN_SIZES[index]);
            }
        });

        View contentView = settingWindow.getContentView();
        //It needs to be measured first, When PopupWindow has not yet popped up, its width and height are 0
        contentView.measure(SystemUtil.makeDropDownMeasureSpec(settingWindow.getWidth()),
                SystemUtil.makeDropDownMeasureSpec(settingWindow.getHeight()));

        int padding = DisplayUtil.dip2px(this, 45);
        settingWindow.popAtTopRight();
        settingWindow.showAsDropDown(mSettingView, -250, -2 * padding - settingWindow.getContentView().getMeasuredHeight());

    }


    private void initSaveProgressDlg() {
        mSaveProgressDlg = new ProgressDialog(this);
        mSaveProgressDlg.setMessage("Saving, please wait..");
        mSaveProgressDlg.setCancelable(false);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAVE_FAILED:
                    mSaveProgressDlg.dismiss();
                    PaintActivity.super.showAlert("informative", "FIRMA NO VALIDA, ERROR AL GUARDAR");
                    //Toast.makeText(getApplicationContext(), "Save failed", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SAVE_SUCCESS:
                    mSaveProgressDlg.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra(PenConfig.SAVE_PATH, mSavePath);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * save
     */
    private void save() {
        if (mPaintView.isEmpty()) {
            //Toast.makeText(getApplicationContext(), "No text written", Toast.LENGTH_SHORT).show();
            PaintActivity.super.showAlert("informative", "FIRMA DIGITALIZADA NO VALIDA");
            return;
        }
        //先检查是否有存储权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getApplicationContext(), "No permission to read or write storage", Toast.LENGTH_SHORT).show();
            PaintActivity.super.showAlert("informative", "SIN PERMISO PARA LEER O ESCRIBIR EN EL ALMACENAMIENTO");
            return;
        }
        if (mSaveProgressDlg == null) {
            initSaveProgressDlg();
        }
        mSaveProgressDlg.show();
        new Thread(() -> {
            try {
                Bitmap result = mPaintView.buildAreaBitmap(isCrop);
                if (PenConfig.FORMAT_JPG.equals(format) && bgColor == Color.TRANSPARENT) {
                    bgColor = Color.WHITE;
                }
                if (bgColor != Color.TRANSPARENT) {
                    result = BitmapUtil.drawBgToBitmap(result, bgColor);
                }
                if (result == null) {
                    mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
                    return;
                }
                mSavePath = BitmapUtil.saveImage(PaintActivity.this, result, 100, format,String.valueOf(v_trans_id));
                if (mSavePath != null) {
                    //Log.d("1","base64:"+BitmapUtil.convert(result,100,format));
                    //Log.d("1","HEX:"+byteArrayToHex(BitmapUtil.convertByte(result,100,format)));
                    //_name=String.valueOf(v_trans_id)+".png";
                    //_imagen=BitmapUtil.convert(result,100,format);
                    Log.d("1","mSavePath:"+mSavePath);
                    callfirmaelectronica(String.valueOf(v_ARQC)+".png",BitmapUtil.convert(result,100,format));
                    Log.d("1","callfirmaelectronica---------");

                } else {
                    mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
                }
            } catch (Exception e) {

            }
        }).start();

    }

    /**
     * Canvas has operations
     */
    @Override
    public void onOperateStatusChanged() {
        mUndoView.setEnabled(mPaintView.canUndo());
        mRedoView.setEnabled(mPaintView.canRedo());
        mClearView.setEnabled(!mPaintView.isEmpty());

        BitmapUtil.setImage(mRedoView, R.drawable.sign_ic_redo, mPaintView.canRedo() ? PenConfig.THEME_COLOR : Color.LTGRAY);
        BitmapUtil.setImage(mUndoView, R.drawable.sign_ic_undo, mPaintView.canUndo() ? PenConfig.THEME_COLOR : Color.LTGRAY);
        //BitmapUtil.setImage(mClearView, R.drawable.sign_ic_clear, !mPaintView.isEmpty() ? PenConfig.THEME_COLOR : Color.LTGRAY);

    }

    @Override
    public void onBackPressed() {
        /*if (!mPaintView.isEmpty()) {
            showQuitTip();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }*/
    }

    /**
     * Pop up exit prompt
     */
    private void showQuitTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("prompt")
                .setMessage("The current text has not been saved. Do you want to exit?")
                .setNegativeButton("cancle", null)
                .setPositiveButton("confirm", (dialog, which) -> {
                    setResult(RESULT_CANCELED);
                    finish();
                });
        builder.show();
    }
    private void ChangeViewToTicket()
    {
        intent = new Intent(this, WMX_final_ticket_transaction.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type_transaction",type_transaction);
        intent.putExtra("v_total", v_total);
        intent.putExtra("v_time", v_time);
        intent.putExtra("v_card", v_card);
        intent.putExtra("v_redtarjeta", v_redtarjeta);
        intent.putExtra("v_tipotarjeta", v_tipotarjeta);
        intent.putExtra("v_AID", v_AID);
        intent.putExtra("v_ARQC", v_ARQC);
        intent.putExtra("v_noauth", v_noauth);
        intent.putExtra("v_approve", v_approve);
        intent.putExtra("ksn_posId", ksn_posId);
        intent.putExtra("v_emisor", v_emisor);
        intent.putExtra("v_nip", v_nip);
        intent.putExtra("v_entrada", v_entrada);
        intent.putExtra("v_trans_id", v_trans_id);
        if (type_transaction.equals("MSI")) {
            intent.putExtra("v_months", v_months);
            intent.putExtra("v_months_total", v_months_total);

        } else {
            intent.putExtra("v_months", v_months);
            intent.putExtra("v_tip", v_tip);
            intent.putExtra("v_subtotal", v_subtotal);
        }
        startActivity(intent);
        finish();
        mHandler.obtainMessage(MSG_SAVE_SUCCESS).sendToTarget();
    }
    public void callfirmaelectronica(String _name,String _imagen) {
        try {
            String URL = Utils.TERMINAL_BATCH + "/api/firma/electronica";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", _name);
            jsonBody.put("imagen", _imagen);
            TRACE.d("length: "+_imagen.length());
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        TRACE.d("response: "+response.toString() );
                        JSONObject object = new JSONObject(response);
                        if(!object.getString("codigo").equals("false")){
                            ChangeViewToTicket();
                        }
                    } catch (JSONException e) {
                        mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("VolleyError: " +  TRACE.NEW_LINE + error.getMessage() );
                    mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
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

            RequestSingleton.getInstance(this).getRequestQueue().add(stringRequest);
        } catch (JSONException e) {
            mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
            TRACE.d("JSONException: " +  TRACE.NEW_LINE + e.toString() );
        }
    }
}
