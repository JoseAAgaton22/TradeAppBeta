package com.app.tradeapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tradeapp.Model.Subvalor;
import com.app.tradeapp.Model.User;
import com.app.tradeapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubvalorAdapter extends RecyclerView.Adapter<SubvalorAdapter.ViewHolder>{

    private Context mContext;
    private List<Subvalor> mSubvalores;

    public SubvalorAdapter(Context mContext, List<Subvalor> mSubvalores) {
        this.mContext = mContext;
        this.mSubvalores = mSubvalores;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.subvalor_item, parent, false);

        return new SubvalorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubvalorAdapter.ViewHolder holder, int position) {
        final Subvalor subvalor = mSubvalores.get(position);

        holder.nombre.setText(subvalor.getSubvalor_nombre());
        holder.valor.setText(String.valueOf(subvalor.getSubvalor_valor()));

        holder.borrar_subvalor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubvalores.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mSubvalores.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubvalores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nombre;
        public TextView valor;
        public ImageView borrar_subvalor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.subvalor_nombre);
            valor = itemView.findViewById(R.id.subvalor_valor);
            borrar_subvalor = itemView.findViewById(R.id.borrar_subvalor);

        }
    }
}
