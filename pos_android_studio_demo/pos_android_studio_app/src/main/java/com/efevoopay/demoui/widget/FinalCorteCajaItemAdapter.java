package com.efevoopay.demoui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.HistorialCorteCajaViewInterface;
import com.efevoopay.demoui.utils.CorteCaja;

import java.util.ArrayList;

public class FinalCorteCajaItemAdapter extends RecyclerView.Adapter<FinalCorteCajaItemAdapter.MyViewHolder>{
    private final HistorialCorteCajaViewInterface finalcortecajaViewInterface;
    Context context;
    ArrayList<CorteCaja> _finalcortecaja = new ArrayList<>();

    public FinalCorteCajaItemAdapter(Context ct, ArrayList<CorteCaja> finalcortecaja, HistorialCorteCajaViewInterface finalcortecajaViewInterface){
        context =ct;
        _finalcortecaja = finalcortecaja;
        this.finalcortecajaViewInterface = finalcortecajaViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wmx_transactionitem2, viewGroup, false);

        return new MyViewHolder(view, finalcortecajaViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_auth.setText(_finalcortecaja.get(i).get_numtxn());
        myViewHolder.tv_date2.setText(_finalcortecaja.get(i).get_date());
        myViewHolder.tv_amount2.setText(_finalcortecaja.get(i).get_monto());
        myViewHolder.tv_time.setText(_finalcortecaja.get(i).get_hora());
        myViewHolder.tv_card.setText(_finalcortecaja.get(i).get_pan());

        myViewHolder.iv_status.setImageResource(R.drawable.efevoo_i_check_exito);
        myViewHolder.tv_amount2.setTextColor(ContextCompat.getColor(context,R.color.wmx_success_text));

    }
    @Override
    public int getItemCount() {
        return _finalcortecaja.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_auth, tv_date2, tv_amount2, tv_time, tv_card;
        ImageView  iv_status;

        public MyViewHolder(@NonNull View itemView, HistorialCorteCajaViewInterface cortecajaViewInterface) {
            super(itemView);
            tv_auth=itemView.findViewById(R.id.wmx_trans_aut);
            tv_date2=itemView.findViewById(R.id.wmx_historial_cantidad);
            tv_amount2=itemView.findViewById(R.id.wmx_trans_amount);
            tv_time=itemView.findViewById(R.id.wmx_trans_time);
            tv_card=itemView.findViewById(R.id.wmx_trans_card);
            iv_status=itemView.findViewById(R.id.wmx_trans_status);

        }
    }
}
