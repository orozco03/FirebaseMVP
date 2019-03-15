package www.mensajerosurbanos.com.co.firebasemvp.Vista.ResgisterView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import www.mensajerosurbanos.com.co.firebasemvp.Presentador.RegisterPresenter.PresentadorRegistro;
import www.mensajerosurbanos.com.co.firebasemvp.R;

public class VistaRegistro extends AppCompatActivity implements View.OnClickListener{

    private EditText nombreEditText, usuarioEditText, emailEditText, passREditText, confirmarPassREditText;
    private PresentadorRegistro presentadorRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_registro);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        presentadorRegistro = new PresentadorRegistro(this, auth, databaseReference);

        nombreEditText = findViewById(R.id.nombreEditText);
        usuarioEditText = findViewById(R.id.ususarioEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passREditText = findViewById(R.id.passREditText);
        confirmarPassREditText = findViewById(R.id.confirmarPassREditText);

        Button btn_register = findViewById(R.id.btn_registrarUsu);
        btn_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_registrarUsu:
                String nombre = nombreEditText.getText().toString().trim();
                String username = usuarioEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String pass = passREditText.getText().toString().trim();
                String confirmarPass = confirmarPassREditText.getText().toString().trim();

                if (pass.equals(confirmarPass)){
                    presentadorRegistro.signUpUser(email,pass,nombre,username);
                }else {
                    Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
