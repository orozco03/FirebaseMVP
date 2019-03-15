package www.mensajerosurbanos.com.co.firebasemvp.Presentador.LoginPresenter;

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

import www.mensajerosurbanos.com.co.firebasemvp.Vista.PrincipalView.VistaPrincipal;

public class PresentadorLogin {

    private Context context;
    private String TAG = "PresentadorLogin";
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    public PresentadorLogin(Context context, FirebaseAuth auth, DatabaseReference databaseReference) {
        this.context = context;
        this.auth = auth;
        this.databaseReference = databaseReference;
    }

    public void signInUser(String email, String pass){

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Ingresando...");
        dialog.setCancelable(false);
        dialog.show();

        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"Bien");
                            dialog.dismiss();
                            //ENVIA LOS DATOS A FIREBASE EN TIEMPO REAL Y DESPUES SE ENVIA AL SIGUIENTE ACTIVITY
                            //databaseReference.child("Usuarios").child(task.getResult().getUser().getUid()).child("titulo").setValue("FirebaseMVP");
                            Intent intent = new Intent(context, VistaPrincipal.class);
                            context.startActivity(intent);
                        }else {
                            dialog.dismiss();
                            Log.w(TAG,"Mal",task.getException());
                            Toast.makeText(context,"Fallo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
