package com.app.tradeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tradeapp.Model.Categorias;
import com.app.tradeapp.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriasAdapater extends RecyclerView.Adapter<CategoriasAdapater.ViewHolder> {

    List<Categorias> lista_categorias;
    int posicionSeleccionada = -1;
    String transaccion;

    public CategoriasAdapater(List<Categorias> lista_categorias) {
        this.lista_categorias = lista_categorias;
    }

    public static ArrayList<Categorias> listaCategorias_gasto = null;

    public static void obtenerLista() {

        listaCategorias_gasto = new ArrayList<Categorias>();

        listaCategorias_gasto.add(new Categorias(1, R.drawable.ic_factura, "Facturas"));
        listaCategorias_gasto.add(new Categorias(2, R.drawable.ic__alimentacion, "Alimentación"));
        listaCategorias_gasto.add(new Categorias(3, R.drawable.ic__gym, "Deporte"));
        listaCategorias_gasto.add(new Categorias(4, R.drawable.ic_jeringa, "Salud"));
        listaCategorias_gasto.add(new Categorias(5, R.drawable.ic_transporte, "Transporte"));
        listaCategorias_gasto.add(new Categorias(6, R.drawable.ic_entretenimiento, "Esparcimiento"));
        listaCategorias_gasto.add(new Categorias(7, R.drawable.ic_mascotas, "Mascotas"));
        listaCategorias_gasto.add(new Categorias(8, R.drawable.ic_vacaciones, "Vacaciones"));
        listaCategorias_gasto.add(new Categorias(9, R.drawable.ic_ropa, "Ropa"));
        listaCategorias_gasto.add(new Categorias(10, R.drawable.ic_familia, "Hogar"));
        listaCategorias_gasto.add(new Categorias(11, R.drawable.ic_educacion, "Educación"));
        listaCategorias_gasto.add(new Categorias(12, R.drawable.ic_comunicaciones, "Telefonía"));
        listaCategorias_gasto.add(new Categorias(13, R.drawable.ic_creditos, "Créditos"));
        listaCategorias_gasto.add(new Categorias(14, R.drawable.ic_arriendo, "Arriendo"));
        listaCategorias_gasto.add(new Categorias(15, R.drawable.ic_servicios, "Servicios"));
        listaCategorias_gasto.add(new Categorias(16, R.drawable.ic_otros, "Otros"));

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoria.setImageResource(listaCategorias_gasto.get(position).getCategoria());
        holder.nombre.setText(listaCategorias_gasto.get(position).getNombre());

        final int i = position;
        holder.categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionSeleccionada = i;
                notifyDataSetChanged();
            }
        });

        if (posicionSeleccionada==position){
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
