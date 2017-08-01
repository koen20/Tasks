package nl.koenhabets.tasks;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    Button button;
    Button buttonLogin;
    Button buttonLogin2;
    Button buttonRegister2;
    ImageView image;

    EditText editTextEmail;
    EditText editTextPassword;
    private FirebaseAuth mAuth;
    boolean thing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        button = (Button) findViewById(R.id.button);
        buttonLogin = (Button) findViewById(R.id.button2);
        buttonLogin2 = (Button) findViewById(R.id.button3);
        buttonRegister2 = (Button) findViewById(R.id.button4);
        image = (ImageView) findViewById(R.id.imageView3);

        mAuth = FirebaseAuth.getInstance();

        buttonLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLogin.setVisibility(View.VISIBLE);
                editTextEmail.setVisibility(View.VISIBLE);
                editTextPassword.setVisibility(View.VISIBLE);
                buttonRegister2.setVisibility(View.INVISIBLE);
                buttonLogin2.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                thing = true;
            }
        });
        buttonRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.VISIBLE);
                editTextEmail.setVisibility(View.VISIBLE);
                editTextPassword.setVisibility(View.VISIBLE);
                buttonRegister2.setVisibility(View.INVISIBLE);
                buttonLogin2.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                thing = true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(editTextEmail.getText() + "", editTextPassword.getText() + "")
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("login", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(editTextEmail.getText() + "", editTextPassword.getText() + "")
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("login", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(thing){
            button.setVisibility(View.INVISIBLE);
            buttonLogin.setVisibility(View.INVISIBLE);
            editTextEmail.setVisibility(View.INVISIBLE);
            editTextPassword.setVisibility(View.INVISIBLE);
            buttonRegister2.setVisibility(View.VISIBLE);
            buttonLogin2.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            thing = false;
        } else {
            super.onBackPressed();
        }
    }
}
