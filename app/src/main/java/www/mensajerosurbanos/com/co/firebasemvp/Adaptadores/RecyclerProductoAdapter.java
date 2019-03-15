package www.mensajerosurbanos.com.co.firebasemvp.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import www.mensajerosurbanos.com.co.firebasemvp.Modelo.ProductoModel;
import www.mensajerosurbanos.com.co.firebasemvp.R;
import www.mensajerosurbanos.com.co.firebasemvp.Vista.ProductoView.VistaProductoDetalle;

public class RecyclerProductoAdapter extends RecyclerView.Adapter<RecyclerProductoAdapter.productoViewHolder>{

    private Context context;
    private int layoutResource;
    private ArrayList<ProductoModel> arrayListProductos;

    public RecyclerProductoAdapter(Context context, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.context = context;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
    }

    @NonNull
    @Override
    public productoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(layoutResource,viewGroup,false);

        return new productoViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final productoViewHolder productoViewHolder, int position) {

        ProductoModel productoModel = arrayListProductos.get(position);
        productoViewHolder.nombreProd_row.setText("Nombre del producto: " + productoModel.getNombreProducto());
        productoViewHolder.precioProd_row.setText("Precio $" + (int)productoModel.getPrecioProducto());

        Glide
                .with(context)
                .load(productoModel.getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        productoViewHolder.progressBar.setVisibility(View.GONE);
                        productoViewHolder.img_producto.setVisibility(View.VISIBLE);
                        productoViewHolder.img_producto.setImageResource(R.drawable.ic_error_black_24dp);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        productoViewHolder.progressBar.setVisibility(View.GONE);
                        productoViewHolder.img_producto.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(productoViewHolder.img_producto);



    }

    @Override
    public int getItemCount() {
        if (arrayListProductos.size() > 0){
            return arrayListProductos.size();
        }
        return 0;
    }

    public class productoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nombreProd_row, precioProd_row;
        ImageView img_producto;
        ProgressBar progressBar;

        public productoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProd_row = itemView.findViewById(R.id.nombreProd_row);
            precioProd_row = itemView.findViewById(R.id.precioProd_row);
            img_producto = itemView.findViewById(R.id.img_producto);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, VistaProductoDetalle.class);
            context.startActivity(intent);
        }
    }
}
