package com.cohetepay.demoui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.cohetepay.demoui.R;
import com.cohetepay.demoui.activities.WMX_Connection_Test;
import com.cohetepay.demoui.activities.WMX_KSN;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NotConnectionDialog extends BottomSheetDialogFragment {
    private BottomSheetDialog dialog;
    private View containerView;
    private Context ctx;
    private BottomSheetBehavior behavior;

    public View getContainerView() {
        return containerView;
    }

    public NotConnectionDialog(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        containerView = inflater.inflate(R.layout.wmx_not_network_modal, container, false);
        return containerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        LinearLayout layout = dialog.findViewById(R.id.lyt_not_connection_container);
        assert layout != null;

        TextView txt_connection_footer = view.findViewById(R.id.txt_connection_footer);
        String text_1 = ctx.getString(R.string.wmx_not_network_connection_footer_1);
        String text_2 = ctx.getString(R.string.wmx_not_network_connection_footer_2);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        SpannableString spannableString = new SpannableString(text_1 + " " + text_2);
        spannableString.setSpan(boldSpan, 0, text_1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_connection_footer.setText(spannableString);

        AppCompatButton btn_connection_cancel = view.findViewById(R.id.btn_not_network_close);
        AppCompatButton btn_connection_test = view.findViewById(R.id.btn_not_network_connection_test);
        btn_connection_cancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });
        btn_connection_test.setOnClickListener((v) -> {
            dialog.dismiss();
            Intent intent = new Intent(this.ctx, WMX_Connection_Test.class);
            intent.putExtra("ksn_posId", WMX_KSN.getPosId());
            intent.putExtra("type", 1);
            startActivity(intent);
        });
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels - 200);
    }
}
