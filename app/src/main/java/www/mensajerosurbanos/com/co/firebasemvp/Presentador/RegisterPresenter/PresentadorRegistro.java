package www.mensajerosurbanos.com.co.firebasemvp.Presentador.RegisterPresenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import www.mensajerosurbanos.com.co.firebasemvp.Vista.PrincipalView.VistaPrincipal;

public class PresentadorRegistro {

    private Context context;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String TAG = "PresentadorRegistro";

    public PresentadorRegistro(Context context, FirebaseAuth auth, DatabaseReference databaseReference) {
        this.context = context;
        this.auth = auth;
        this.databaseReference = databaseReference;
    }

    public void signUpUser(final String email, final String pass, final String nombreCompleto, final String username){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registrando Usuario");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"Exito");
                            progressDialog.dismiss();
                            Map<String,Object> crearUsuario = new HashMap<>();
                            crearUsuario.put("nombreCompleto", nombreCompleto);
                            crearUsuario.put("username", username);
                            crearUsuario.put("email", email);

                            databaseReference.child("Usuario").child(task.getResult().getUser().getUid()).updateChildren(crearUsuario);

                            Intent intent = new Intent(context, VistaPrincipal.class);
                            context.startActivity(intent);
                        }else {
                            progressDialog.dismiss();
                            Log.w(TAG,"Error", task.getException());
                            Toast.makeText(context, "Fallo",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
