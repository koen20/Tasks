package nl.koenhabets.tasks.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import nl.koenhabets.tasks.R;


public class LoginActivity extends AppCompatActivity {
    private Button buttonBaseLogin;
    private Button buttonBaseRegister;

    private EditText textEmailLogin;
    private EditText textPasswordLogin;
    private Button buttonLoginAccount;

    private EditText textEmailRegister;
    private EditText textPasswordRegister;
    private Button buttonRegisterAccount;

    private View contentLoginAccount;
    private View contentRegisterAccount;
    private View contentBase;

    private FirebaseAuth mAuth;
    private boolean thing = false;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;

    private void initViews() {
        contentLoginAccount = findViewById(R.id.content_login_account);
        contentRegisterAccount = findViewById(R.id.content_register_account);
        contentBase = findViewById(R.id.content_login_base);

        contentLoginAccount.setVisibility(View.INVISIBLE);
        contentRegisterAccount.setVisibility(View.INVISIBLE);

        buttonBaseLogin = findViewById(R.id.buttonLogin);
        buttonBaseRegister = findViewById(R.id.buttonRegister);
        signInButton = findViewById(R.id.button7);

        textEmailLogin = findViewById(R.id.textEmailLogin);
        textPasswordLogin = findViewById(R.id.textPasswordLogin);
        buttonLoginAccount = findViewById(R.id.buttonLoginAccount);

        textEmailRegister = findViewById(R.id.textEmailRegister);
        textPasswordRegister = findViewById(R.id.textPasswordRegister);
        buttonRegisterAccount = findViewById(R.id.buttonRegisterAccount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       initViews();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 571);
            }
        });
        buttonBaseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentLoginAccount.setVisibility(View.VISIBLE);
                contentBase.setVisibility(View.INVISIBLE);
                thing = true;
            }
        });
        buttonBaseRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentRegisterAccount.setVisibility(View.VISIBLE);
                contentBase.setVisibility(View.INVISIBLE);
                thing = true;
            }
        });

        buttonLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(textEmailLogin.getText() + "", textPasswordLogin.getText() + "")
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
                                    Log.w("login", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        buttonRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(textEmailRegister.getText() + "", textPasswordRegister.getText() + "")
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
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
    }

    @Override
    public void onBackPressed() {
        if (thing) {
            contentRegisterAccount.setVisibility(View.INVISIBLE);
            contentLoginAccount.setVisibility(View.INVISIBLE);
            contentBase.setVisibility(View.VISIBLE);
            thing = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 571) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.getId());
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("LoginActivity", "signInWithCredential:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } else {
                    Log.d("LoginActivity", "Google login failed");
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }catch (IllegalArgumentException e){e.printStackTrace();}
        }
    }
}
