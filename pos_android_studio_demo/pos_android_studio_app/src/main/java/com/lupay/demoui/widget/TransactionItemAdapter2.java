package com.lupay.demoui.widget;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.TransactionsViewInterface;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Transaction;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class TransactionItemAdapter2 extends RecyclerView.Adapter<TransactionItemAdapter2.MyViewHolder> {
    private final TransactionsViewInterface transactionsViewInterface;
    protected String currARQC;
    Context context;
    ArrayList<Transaction> _transactions = new ArrayList<>();

    private CompletableFuture<Boolean> hasTransactionFoundPromise;

    private boolean hasFound;

    public TransactionItemAdapter2(Context ct, ArrayList<Transaction> transactions, TransactionsViewInterface transactionsViewInterface, String currARQC, CompletableFuture<Boolean> hasTransactionFoundPromise){
        context =ct;
        _transactions = transactions;
        this.transactionsViewInterface = transactionsViewInterface;
        this.currARQC = currARQC;
        this.hasTransactionFoundPromise = hasTransactionFoundPromise;
        this.hasFound = false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wmx_transactionitem2, viewGroup, false);

        return new MyViewHolder(view, transactionsViewInterface);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_auth.setText(_transactions.get(i).get_approve());
        myViewHolder.tv_date2.setText(_transactions.get(i).get_date());
        myViewHolder.tv_amount2.setText(_transactions.get(i).get_total());
        myViewHolder.tv_time.setText(_transactions.get(i).get_time());
        myViewHolder.tv_card.setText("**** "+_transactions.get(i).get_card());

        if(!TextUtils.isEmpty(currARQC) && _transactions.get(i).get_arqc().equals(currARQC) && !hasFound) {
            TRACE.d("Found ARQC: " + currARQC);
            hasFound = true;
            hasTransactionFoundPromise.complete(true);
            TransitionDrawable background =  (TransitionDrawable) myViewHolder.mainView.getBackground();
            new Handler().postDelayed(() -> {
                background.reverseTransition(300);
            }, 500);
        } else if ((i + 1) == _transactions.size()) {
            hasTransactionFoundPromise.complete(false);
        }

        if(_transactions.get(i).get_redtarj().toUpperCase(Locale.ROOT).equals("MC")){
            myViewHolder.iv_process.setImageResource(R.drawable.masterdcard);
        }else if(_transactions.get(i).get_redtarj().toUpperCase(Locale.ROOT).equals("AMEX")){
            myViewHolder.iv_process.setImageResource(R.drawable.amex);
        }else if(_transactions.get(i).get_redtarj().toUpperCase(Locale.ROOT).equals("VISA")){
            myViewHolder.iv_process.setImageResource(R.drawable.visa);
        }else{
            myViewHolder.iv_process.setImageResource(R.drawable.internacional);
        }
        if(_transactions.get(i).get_tipotxn().equals("CAN") || (_transactions.get(i).get_tipotxn().equals("REV") && _transactions.get(i).get_redtarj().toUpperCase(Locale.ROOT).equals("AMEX"))){
            myViewHolder.iv_status.setImageResource(R.drawable.efevoo_i_grupo_41699);
            myViewHolder.tv_amount2.setTextColor(ContextCompat.getColor(context,R.color.wmx_cancelation_text));
        }else{
            myViewHolder.iv_status.setImageResource(R.drawable.efevoo_i_check_exito);
            myViewHolder.tv_amount2.setTextColor(ContextCompat.getColor(context,R.color.wmx_success_text));
        }

    }

    @Override
    public int getItemCount() {
        return _transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //_auth, _date2, _amount2,_time, _card, _process, _status;
        TextView tv_auth, tv_date2, tv_amount2, tv_time, tv_card;
        ImageView iv_process, iv_status;

        LinearLayout mainView;

        public MyViewHolder(@NonNull View itemView, TransactionsViewInterface transactionsViewInterface) {
            super(itemView);
            mainView = itemView.findViewById(R.id.lyt_transaction_item);
            tv_auth=itemView.findViewById(R.id.wmx_trans_aut);
            tv_date2=itemView.findViewById(R.id.wmx_historial_cantidad);
            tv_amount2=itemView.findViewById(R.id.wmx_trans_amount);
            tv_time=itemView.findViewById(R.id.wmx_trans_time);
            tv_card=itemView.findViewById(R.id.wmx_trans_card);
            iv_process=itemView.findViewById(R.id.wmx_trans_process);
            iv_status=itemView.findViewById(R.id.wmx_trans_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(transactionsViewInterface != null){
                        int pos =getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            transactionsViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
