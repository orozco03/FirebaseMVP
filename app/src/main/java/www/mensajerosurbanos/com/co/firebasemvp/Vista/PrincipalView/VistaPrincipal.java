package www.mensajerosurbanos.com.co.firebasemvp.Vista.PrincipalView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;

import www.mensajerosurbanos.com.co.firebasemvp.Presentador.PrincipalPresenter.PresenterPrincipal;
import www.mensajerosurbanos.com.co.firebasemvp.R;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class VistaPrincipal extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private PresenterPrincipal presenterPrincipal;
    private StorageReference storageReference;

    private Dialog dialog;
    private ProgressDialog progressDialog;
    private EditText nombreProducto, precioProducto;
    private final int PICK_PHTOTO = 1;

    private ImageView imagProducto;
    private  Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        presenterPrincipal = new PresenterPrincipal(this, databaseReference,auth,storageReference);
        presenterPrincipal.welcomeMessage();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        initRecycler();
    }

    public void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,VERTICAL));
        presenterPrincipal.cargarRecyclerView(recyclerView);
    }

    public void cargarProducto(){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_row);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        nombreProducto = dialog.findViewById(R.id.nombreProduEdidText);
        precioProducto = dialog.findViewById(R.id.precioroduEdidText);

        Button btn_añadirProd = dialog.findViewById(R.id.btn_añadirProdu);
        btn_añadirProd.setOnClickListener(this);
        ImageView ImagenProducto = dialog.findViewById(R.id.img_producto);
        ImagenProducto.setOnClickListener(this);
        imagProducto = dialog.findViewById(R.id.img_producto);

        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                cargarProducto();
                break;

            case R.id.btn_añadirProdu:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Añadiendo producto...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String nombreProd = nombreProducto.getText().toString().trim();
                float precioProd = Float.parseFloat(precioProducto.getText().toString().trim());
                presenterPrincipal.cargarProductoFirebase(nombreProd,precioProd,dialog,progressDialog,filePath);
                break;

            case R.id.img_producto:
                abrirFotoGaleria();
                break;
        }
    }

    public void abrirFotoGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PICK_PHTOTO);
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHTOTO && resultCode == RESULT_OK && data !=  null && data.getData() != null){
            filePath = data.getData();

            try {
                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagProducto.setImageBitmap(bitmapImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


