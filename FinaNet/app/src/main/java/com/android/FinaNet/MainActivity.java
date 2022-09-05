package com.android.FinaNet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.FinaNet.adapters.FinaListAdapter;
import com.android.FinaNet.model.FinaModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FinaListAdapter.FinaListClickListener {


    BottomNavigationView bottomNavigationView;

    FirebaseAuth mAuth;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.suppro);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.about:
                    startActivity(new Intent(getApplicationContext(), about.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.suppro:
                    return true;

                case R.id.contact:
                    startActivity(new Intent(getApplicationContext(), McontactActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.nospro:
                    startActivity(new Intent(getApplicationContext(), MaActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                default:
                    return false;
            }
        });




        List<FinaModel> finaModelList = null;
        try {
            finaModelList = getFinaData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initRecyclerView(finaModelList);

        mAuth = FirebaseAuth.getInstance();

    }





    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void initRecyclerView(List<FinaModel> finaModelList) {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FinaListAdapter adapter = new FinaListAdapter(finaModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<FinaModel> getFinaData() throws Exception {
        InputStream is = getResources().openRawResource(R.raw.fina);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        int n;
        while(( n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0,n);
        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        FinaModel[] finaModels =  gson.fromJson(jsonStr, FinaModel[].class);

        return Arrays.asList(finaModels);

    }

    @Override
    public void onItemClick(FinaModel finaModel) {
        Intent intent = new Intent(MainActivity.this, brandsMenuActivity.class);
        intent.putExtra("FinaModel", finaModel);
        startActivity(intent);

    }



}