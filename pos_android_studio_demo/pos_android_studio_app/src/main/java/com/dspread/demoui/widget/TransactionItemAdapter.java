package com.dspread.demoui.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dspread.demoui.R;
import com.dspread.demoui.utils.Transaction;

import java.util.ArrayList;

public class TransactionItemAdapter extends RecyclerView.Adapter<TransactionItemAdapter.MyViewHolder> {
    Context context;
    ArrayList<Transaction> _transactions = new ArrayList<>();

    public TransactionItemAdapter(Context ct,ArrayList<Transaction> transactions){
        context =ct;
        _transactions = transactions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wmx_transactionitem, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_card.setText(_transactions.get(i).get_pan());
        myViewHolder.tv_Amount.setText(_transactions.get(i).get_amount());
        myViewHolder.tv_date.setText(_transactions.get(i).get_date());
        myViewHolder.tv_propina.setText(_transactions.get(i).get_tips());
    }

    @Override
    public int getItemCount() {
        return _transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_card, tv_Amount, tv_date, tv_propina;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_card = itemView.findViewById(R.id.card_txt);
            tv_Amount = itemView.findViewById(R.id.monto_txt);
            tv_date = itemView.findViewById(R.id.date_txt);
            tv_propina = itemView.findViewById(R.id.propina_txt);
        }
    }
}
