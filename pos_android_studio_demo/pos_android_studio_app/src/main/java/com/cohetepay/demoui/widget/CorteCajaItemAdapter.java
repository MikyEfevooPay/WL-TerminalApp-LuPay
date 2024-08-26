package com.cohetepay.demoui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cohetepay.demoui.R;
import com.cohetepay.demoui.interfaces.HistorialCorteCajaViewInterface;
import com.cohetepay.demoui.utils.CorteCaja;

import java.util.ArrayList;

public class CorteCajaItemAdapter extends RecyclerView.Adapter<CorteCajaItemAdapter.MyViewHolder>{
    private final HistorialCorteCajaViewInterface historialcortecajaViewInterface;
    Context context;
    ArrayList<CorteCaja> _historialcortecaja = new ArrayList<>();

    public CorteCajaItemAdapter(Context ct, ArrayList<CorteCaja> historialcortecaja, HistorialCorteCajaViewInterface historialcortecajaViewInterface){
        context =ct;
        _historialcortecaja = historialcortecaja;
        this.historialcortecajaViewInterface = historialcortecajaViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wmx_cortecajaitem, viewGroup, false);

        return new MyViewHolder(view, historialcortecajaViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //myViewHolder.tv_idCorte.setText(_historialcortecaja.get(i).get_idCorte());
        myViewHolder.tv_Identificador.setText(_historialcortecaja.get(i).get_Identificador());
        myViewHolder.tv_Cantidad.setText(_historialcortecaja.get(i).get_Total());
        myViewHolder.tv_FechaHora.setText(_historialcortecaja.get(i).get_FechaHora());

//        myViewHolder.iv_status.setVisibility(View.GONE);
//        myViewHolder.tv_amount2.setTextColor(0xFF000000);

//        if(_transactions.get(i).get_redtarj().equals("MC")){
//            myViewHolder.iv_process.setImageResource(R.drawable.masterdcard);
//        }else{
//            myViewHolder.iv_process.setImageResource(R.drawable.visa);
//        }

    }

    @Override
    public int getItemCount() {return _historialcortecaja.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_idCorte, tv_Identificador, tv_Cantidad, tv_FechaHora;

        public MyViewHolder(@NonNull View itemView, HistorialCorteCajaViewInterface cortecajaViewInterface) {
            super(itemView);
            tv_Cantidad=itemView.findViewById(R.id.wmx_historial_cantidad);
            tv_Identificador=itemView.findViewById(R.id.wmx_historial_identificador);
            tv_FechaHora=itemView.findViewById(R.id.wmx_historial_fechahora);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cortecajaViewInterface != null){
                       int pos =getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            cortecajaViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
