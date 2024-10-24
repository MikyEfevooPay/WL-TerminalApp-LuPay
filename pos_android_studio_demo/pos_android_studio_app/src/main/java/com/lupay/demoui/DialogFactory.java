package com.lupay.demoui;

import android.content.Context;

import com.lupay.demoui.activities.LoadingDialog;

public class DialogFactory {

    private static LoadingDialog loadingDialog;

    private DialogFactory() {

    }

    public static void showLoadingDialog(Context context) {
        if (null == loadingDialog) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.show();
    }

    public static void dismissLoadingDialog() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
