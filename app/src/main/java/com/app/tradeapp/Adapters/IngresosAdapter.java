package com.app.tradeapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tradeapp.Fragments.TransactionsFragment;
import com.app.tradeapp.Model.Categorias;
import com.app.tradeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class IngresosAdapter extends RecyclerView.Adapter<IngresosAdapter.ViewHolder> {
    
    List<Categorias> lista_categorias;
    int posicionSeleccionada = -1;
    public static String categoriaSeleccionada = "";

    public IngresosAdapter(List<Categorias> lista_categorias) {
        this.lista_categorias = lista_categorias;
    }

    public static ArrayList<Categorias> listaCategorias_ingreso = null;

    public static void obtenerListaIngresos() {

        listaCategorias_ingreso = new ArrayList<Categorias>();
        listaCategorias_ingreso.add(new Categorias(1, R.drawable.ic_salario, "Salario"));
        listaCategorias_ingreso.add(new Categorias(2, R.drawable.ic_deposito, "Interés"));
        listaCategorias_ingreso.add(new Categorias(3, R.drawable.ic_regalo, "Regalo"));
        listaCategorias_ingreso.add(new Categorias(4, R.drawable.ic_otros, "Otros"));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoria.setImageResource(listaCategorias_ingreso.get(position).getCategoria());
        holder.nombre.setText(listaCategorias_ingreso.get(position).getNombre());

        final int i = position;
        holder.categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionSeleccionada = i;
                categoriaSeleccionada = listaCategorias_ingreso.get(position).getNombre();
                notifyDataSetChanged();
            }
        });

        if (posicionSeleccionada==position) {
            holder.categoria.setSelected(true);
        } else {
            holder.categoria.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return lista_categorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton categoria;
        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoria = itemView.findViewById(R.id.categoria);
            nombre = itemView.findViewById(R.id.textCategoria);
        }
    }
}

