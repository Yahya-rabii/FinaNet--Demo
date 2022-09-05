package com.android.FinaNet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.FinaNet.adapters.MenuListAdapter;
import com.android.FinaNet.model.FinaModel;
import com.android.FinaNet.model.Menu;
import java.util.ArrayList;
import java.util.List;

public class brandsMenuActivity extends AppCompatActivity implements MenuListAdapter.MenuListClickListener {
    private List<Menu> menuList = null;
    private List<Menu> itemsInCartList;
    private int totalItemInCart = 0;
    private TextView buttonCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fina_menu);

        FinaModel finaModel = getIntent().getParcelableExtra("FinaModel");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(finaModel.getName());
        actionBar.setSubtitle(finaModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);


        menuList = finaModel.getMenus();
        initRecyclerView();


         buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(v -> {
            if(itemsInCartList != null && itemsInCartList.size() <= 0) {
                Toast.makeText(brandsMenuActivity.this, "Please add some items in cart.", Toast.LENGTH_SHORT).show();
                return;
            }
            finaModel.setMenus(itemsInCartList);
            Intent i = new Intent(brandsMenuActivity.this, PlaceYourOrderActivity.class);
            i.putExtra("FinaModel", finaModel);
            startActivityForResult(i, 1000);
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        MenuListAdapter menuListAdapter = new MenuListAdapter(menuList, this);
        recyclerView.setAdapter(menuListAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onAddToCartClick(Menu menu) {
        if(itemsInCartList == null) {
            itemsInCartList = new ArrayList<>();
        }
        itemsInCartList.add(menu);
        totalItemInCart = 0;

        for(Menu m : itemsInCartList) {
            totalItemInCart = totalItemInCart + m.getTotalInCart();
        }
        buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            int index = itemsInCartList.indexOf(menu);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, menu);

            totalItemInCart = 0;

            for(Menu m : itemsInCartList) totalItemInCart = totalItemInCart + m.getTotalInCart();
            buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRemoveFromCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            itemsInCartList.remove(menu);
            totalItemInCart = 0;

            for(Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }//do nothing
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            //
            finish();
        }
    }
}