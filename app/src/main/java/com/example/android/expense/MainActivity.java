package com.example.android.expense;

/**
 * Created by Saksham Negi on 5/7/19
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CardView mLoginCardView;
    private static final int RC_SIGN_IN = 123;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if user is logged in skip to logged in activity
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            startActivity(new Intent(this, ExpenseActivity.class));
            finish();

            Toast.makeText(this, "Welcome " + mUser.getDisplayName(), Toast.LENGTH_LONG).show();
        }


        mLoginCardView = findViewById(R.id.login);


        mLoginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();

            }
        });
    }



    @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == RC_SIGN_IN) {

                    handleSignInResponse(resultCode, data);
                    return;
                }
                Toast.makeText(this, "Unexpected error occurred",Toast.LENGTH_SHORT).show();

                }

    private void handleSignInResponse(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            startActivity(new Intent(this, ExpenseActivity.class)
                    .putExtra("phone", IdpResponse.fromResultIntent(data)));
            finish();
            return;
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText(this,"Error during sign in",Toast.LENGTH_SHORT).show();

        }

    }

    private void userLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Start sign in/sign up activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    public void onBackPressed(){
        finish();
    }

}