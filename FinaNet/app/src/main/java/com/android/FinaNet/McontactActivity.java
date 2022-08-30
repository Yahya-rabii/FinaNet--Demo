package com.android.FinaNet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.android.FinaNet.adapter.Ad_Contacts;
import com.android.FinaNet.interfaces.IContactDAO;
import com.android.FinaNet.models.Contacts;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;

public class McontactActivity extends AppCompatActivity implements View.OnClickListener {





    /**
     * type to identity the intent if its for new contact or to edit contact
     */
    private static final String TYPE = "type";

    // the DAO to access database
    IContactDAO contactDAO;
    AppDatabase database;

    // UI references.
    FloatingActionButton addContact;
    RecyclerView recyclerView;
    Ad_Contacts ad_contacts;


    BottomNavigationView bottomNavigationView;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcontact);


        mAuth = FirebaseAuth.getInstance();





        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.contact);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), about.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.suppro:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.contact:
                        return true;

                    case R.id.nospro:
                        startActivity(new Intent(getApplicationContext(), MaActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    default:
                        return false;
                }
            }
        });












        // setup Database and get DAO
        database = Room.databaseBuilder(this, AppDatabase.class, "contactsdb")
                .allowMainThreadQueries()
                .build();
        contactDAO = database.getContactDAO();

        // initialize views
        addContact = findViewById(R.id.add_contact);
        addContact.setOnClickListener(this);
        recyclerView = findViewById(R.id.Recycler_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onClick(View view) {
        if (view == addContact) {
            // add new contact
            Intent intent = new Intent(this, NewContactActivity.class);
            intent.putExtra(TYPE, 0);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // retrieve all data that was written into the database
        List<Contacts> contactsList = contactDAO.getAllContacts();
        Collections.reverse(contactsList);
        // set the data into the recycler View
        ad_contacts = new Ad_Contacts(McontactActivity.this, contactsList);
        recyclerView.setAdapter(ad_contacts);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* If Logout Item is selected. */
        if (item.getItemId() == R.id.logoutItem) {
            /* Show confirmation dialog. */
            new AlertDialog.Builder(this).setTitle("Logout").setMessage("Are you sure to logout?")
                    /* Cancel action. */
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    /* Sign out then redirect to login activity. */
                    .setPositiveButton("Logout", (dialogInterface, i) -> {
                        mAuth.signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }


}



