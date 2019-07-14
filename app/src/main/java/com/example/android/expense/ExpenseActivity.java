package com.example.android.expense;

/**
 * Created by Saksham Negi on 5/7/19
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.expense.Helper.ExpenseData;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText mDescriptionEdt, mAmountEdt;
    private CardView mAddExpense;
    private FirebaseUser mCurrentUser;
    private String mUserID;


    //Listview
    private ListView mListView;
    private FirebaseListAdapter<ExpenseData> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        mDatabase = FirebaseDatabase.getInstance().getReference("UsersExpenses");
        mDescriptionEdt = (EditText) findViewById(R.id.description);
        mAmountEdt = (EditText) findViewById(R.id.amount);
        mAddExpense = (CardView) findViewById(R.id.add_expense);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();

        //Listview work
        mListView = (ListView) findViewById(R.id.list_view);
        displayExpensesFromFirebase();



        mAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = mDescriptionEdt.getText().toString().trim();
                String amountString = mAmountEdt.getText().toString().trim();
                if(description.equalsIgnoreCase("")){
                    mDescriptionEdt.setError("This cannot be blank");
                }
                else if(amountString.equalsIgnoreCase("")){
                    mAmountEdt.setError("This cannot be blank");
                }
                else{
                    //add expense to firebase database
                    int amount = Integer.parseInt(amountString);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd hh:mm yyyy");
                    String time = dateFormat.format(calendar.getTime());
                    ExpenseData expenseData = new ExpenseData(description, amount,time);
                    mDatabase.child(mUserID).child(calendar.getTime().toString()).setValue(expenseData);

                }

            }
        });

        mDatabase.child(mUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"ExpenseActivity added successfully",Toast.LENGTH_SHORT).show();
                mAmountEdt.setText("");
                mDescriptionEdt.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error occurred while adding expense.",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void displayExpensesFromFirebase() {

        mAdapter = new FirebaseListAdapter<ExpenseData>(this, ExpenseData.class,
                R.layout.expense_item, mDatabase.child(mUserID)) {
            @Override
            protected void populateView(View v, ExpenseData model, int position) {
                TextView descriptionTV = (TextView) v.findViewById(R.id.expense_description);
                TextView amountTV = (TextView) v.findViewById(R.id.expense_amount);
                TextView timeTV = (TextView) v.findViewById(R.id.expense_time);

                descriptionTV.setText(model.getDescription());
                amountTV.setText(String.valueOf(model.getAmount()));
                timeTV.setText(model.getTime());

            }
        };
        mListView.setAdapter(mAdapter);

    }
}
