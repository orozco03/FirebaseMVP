package www.mensajerosurbanos.com.co.firebasemvp.Presentador.PrincipalPresenter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import www.mensajerosurbanos.com.co.firebasemvp.Adaptadores.RecyclerProductoAdapter;
import www.mensajerosurbanos.com.co.firebasemvp.Modelo.ProductoModel;
import www.mensajerosurbanos.com.co.firebasemvp.Modelo.UserModel;
import www.mensajerosurbanos.com.co.firebasemvp.R;

public class PresenterPrincipal {

    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private RecyclerProductoAdapter adapter;

    private StorageReference storageReference;

    public PresenterPrincipal(Context context, DatabaseReference databaseReference, FirebaseAuth auth, StorageReference storageReference) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.auth = auth;
        this.storageReference = storageReference;
    }

    public void welcomeMessage(){
        databaseReference.child("Usuario").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                Toast.makeText( context, "Bienvenido " + userModel.getNombreCompleto() ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"La BD Fallo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cargarRecyclerView(final RecyclerView recyclerView){

        databaseReference.child("Usuario").child(auth.getCurrentUser().getUid()).child("Productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProductoModel productoModel = snapshot.getValue(ProductoModel.class);

                    //Obtener datos de firebase
                    String imagenProd = productoModel.getImage();
                    String nombreProducto = productoModel.getNombreProducto();
                    float precioProducto = productoModel.getPrecioProducto();

                    //set datos del array
                    productoModel.setNombreProducto(nombreProducto);
                    productoModel.setPrecioProducto(precioProducto);
                    productoModel.setImage(imagenProd);

                    arrayListProductos.add(productoModel);
                }
                adapter = new RecyclerProductoAdapter(context, R.layout.producto_row,arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void cargarProductoFirebase(final String nombreProd, final float precioProd, final Dialog dialog, final ProgressDialog progressDialog, Uri filePath ){

        if (filePath != null){

            final StorageReference fotoRef = storageReference.child("Fotos").child(auth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
            fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (task.isSuccessful()){
                        throw  new Exception();
                    }
                    return fotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){

                        Uri downloadLink = task.getResult();
                        Map<String, Object> producto = new HashMap<>();
                        producto.put("nombreProducto", nombreProd);
                        producto.put("precioProducto", precioProd);
                        producto.put("image",downloadLink.toString());

                        databaseReference.child("Usuario").child(auth.getCurrentUser().getUid()).child("Productos").push().updateChildren(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                dialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(context,"Se añadio el producto correctamente",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressDialog.dismiss();
                                Toast.makeText(context, "Error al añadir el producto" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
