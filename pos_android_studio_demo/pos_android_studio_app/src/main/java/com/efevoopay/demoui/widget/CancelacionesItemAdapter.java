
package com.cohetepay.demoui.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cohetepay.demoui.R;
import com.cohetepay.demoui.interfaces.TransactionsViewInterface;
import com.cohetepay.demoui.utils.Transaction;

import java.util.ArrayList;
import java.util.Locale;

public class CancelacionesItemAdapter extends RecyclerView.Adapter<CancelacionesItemAdapter.MyViewHolder> {
    private final TransactionsViewInterface transactionsViewInterface;
    Context context;
    ArrayList<Transaction> _transactions = new ArrayList<>();

    public CancelacionesItemAdapter(Context ct,ArrayList<Transaction> transactions, TransactionsViewInterface transactionsViewInterface){
        context =ct;
        _transactions = transactions;
        this.transactionsViewInterface = transactionsViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wmx_transactionitem2, viewGroup, false);

        return new MyViewHolder(view, transactionsViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_auth.setText(_transactions.get(i).get_auth());
        myViewHolder.tv_date2.setText(_transactions.get(i).get_date());
        myViewHolder.tv_amount2.setText(_transactions.get(i).get_total());
        myViewHolder.tv_time.setText(_transactions.get(i).get_time());
        myViewHolder.tv_card.setText("**** "+_transactions.get(i).get_card());

        myViewHolder.iv_status.setVisibility(View.GONE);
        myViewHolder.tv_amount2.setTextColor(0xFF000000);

        if(_transactions.get(i).get_redtarj().equals("MC")){
            myViewHolder.iv_process.setImageResource(R.drawable.masterdcard);
        }else if(_transactions.get(i).get_redtarj().equals("Visa")){
            myViewHolder.iv_process.setImageResource(R.drawable.visa);
        }else if(_transactions.get(i).get_redtarj().toUpperCase(Locale.ROOT).equals("AMEX")){
            myViewHolder.iv_process.setImageResource(R.drawable.amex);
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

        public MyViewHolder(@NonNull View itemView, TransactionsViewInterface transactionsViewInterface) {
            super(itemView);
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
