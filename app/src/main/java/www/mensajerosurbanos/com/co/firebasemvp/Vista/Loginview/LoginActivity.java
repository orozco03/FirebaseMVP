package www.mensajerosurbanos.com.co.firebasemvp.Vista.Loginview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import www.mensajerosurbanos.com.co.firebasemvp.Presentador.LoginPresenter.PresentadorLogin;
import www.mensajerosurbanos.com.co.firebasemvp.R;
import www.mensajerosurbanos.com.co.firebasemvp.Vista.ResgisterView.VistaRegistro;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText, passEdirText;
    private PresentadorLogin presentadorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        presentadorLogin = new PresentadorLogin(this, auth,databaseReference);

        nameEditText = findViewById(R.id.nameEditText);
        passEdirText = findViewById(R.id.passEditText);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        Button btn_register = findViewById(R.id.btn_registrar);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:

                String email = nameEditText.getText().toString().trim();
                String pas = passEdirText.getText().toString().trim();
                presentadorLogin.signInUser(email,pas);
                break;

            case R.id.btn_registrar:
                Intent intent = new Intent(this, VistaRegistro.class);
                startActivity(intent);
                break;
        }
    }
}
