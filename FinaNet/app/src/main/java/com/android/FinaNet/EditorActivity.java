package com.android.FinaNet;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.android.FinaNet.data.ItemsContract;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER = 0;

    private static final int GET_PICTURE_REQUEST = 0;
    private static final int GIVE_PERMISSION = 1;
    ImageButton increase;
    ImageButton decrease;
    Uri uri;
    private Uri mCurrentItemUri;
    private EditText mItemEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private ImageView mImageView;
    private Spinner mSupplierSpinner;
    private int mSupplier = ItemsContract.ItemsEntry.SUPPLIER_CISCO;
    private boolean mItemHasChanged = false;
    private String currentPhotoUri;

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener mTouchListener = (view, motionEvent) -> {
        mItemHasChanged = true;
        return false;
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mItemEditText = findViewById(R.id.item_name_edit);

        mQuantityEditText =  findViewById(R.id.quantity_edit);

        mPriceEditText =  findViewById(R.id.price_edit);

        mImageView =  findViewById(R.id.image_view);

        Button getPictureButton = findViewById(R.id.get_picture);

        Button orderItems = findViewById(R.id.order_items);

        increase =  findViewById(R.id.increase);

        decrease =findViewById(R.id.decrease);

        mSupplierSpinner =  findViewById(R.id.spinner_supplier);


        mItemEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mImageView.setOnTouchListener(mTouchListener);
        mSupplierSpinner.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.new_item));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));
            getPictureButton.setText(getString(R.string.change_picture));
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        setupSpinner();

        getPictureButton.setOnClickListener(v -> {
            accessOpenImageSelector();
            mItemHasChanged = true;
        });

        orderItems.setOnClickListener(v -> {
            String nameItem = mItemEditText.getText().toString();
            String quantityOrder = mQuantityEditText.getText().toString();
            String priceItem = mPriceEditText.getText().toString();


            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_EMAIL, ("mailto:"));
            intent1.putExtra(Intent.EXTRA_SUBJECT, (getString(R.string.goods_request)));
            intent1.putExtra(Intent.EXTRA_TEXT, "Request for: " + nameItem + "\nPriced at: " + priceItem + " €" +
                    "\nQuantity to be ordered: " + quantityOrder + "\n\n Thank you!");

            startActivity(intent1);

        });

        decrease.setOnClickListener(view -> {
            substractOneToQuantity();
            mItemHasChanged = true;
        });

        increase.setOnClickListener(view -> {
            addOneToQuantity();
            mItemHasChanged = true;
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> supplierArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_supplier_options, android.R.layout.simple_spinner_item);
        supplierArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSupplierSpinner.setAdapter(supplierArrayAdapter);

        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_Juniper))) {
                        mSupplier = ItemsContract.ItemsEntry.SUPPLIER_JUNIPER;
                    } else if (selection.equals(getString(R.string.supplier_Barracuda))) {
                        mSupplier = ItemsContract.ItemsEntry.SUPPLIER_BARRACUDA;
                    } else if (selection.equals(getString(R.string.supplier_Adtran))) {
                        mSupplier = ItemsContract.ItemsEntry.SUPPLIER_ADTRAN;
                    } else if (selection.equals(getString(R.string.supplier_Adtran))) {
                        mSupplier = ItemsContract.ItemsEntry.SUPPLIER_ADTRAN;
                    } else {
                        mSupplier = ItemsContract.ItemsEntry.SUPPLIER_CISCO;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = ItemsContract.ItemsEntry.SUPPLIER_CISCO;
            }
        });
    }

    public void accessOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GIVE_PERMISSION);
            return;
        }
        openImageSelector();
    }

    private void openImageSelector() {
        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_PICTURE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GIVE_PERMISSION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector();
                // permission was granted
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == GET_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                uri = resultData.getData();
                currentPhotoUri = uri.toString();

                Glide.with(this).load(currentPhotoUri)
                        .placeholder(R.mipmap.ic_launcher)
                        .transition(withCrossFade())
                        .into(mImageView);
                //.fitCenter();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_item);
            menuItem.setVisible(false);

        }
        return true;
    }

    private void saveItem() {
        String itemName = mItemEditText.getText().toString().trim();
        String price = mPriceEditText.getText().toString().trim();
        String quantity = mQuantityEditText.getText().toString();


        if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(price)
                || currentPhotoUri == null) {
            Toast.makeText(this, getString(R.string.incorrect_entry), Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ItemsContract.ItemsEntry.COLUMN_ITEM_NAME, itemName);
        values.put(ItemsContract.ItemsEntry.COLUMN_ITEM_PRICE, price);
        values.put(ItemsContract.ItemsEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(ItemsContract.ItemsEntry.COLUMN_SUPPLIER_NAME, mSupplier);
        values.put(ItemsContract.ItemsEntry.COLUMN_ITEM_PICTURE, currentPhotoUri);

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.failure_insert), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successful_insert), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.failed_update), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successful_update), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> deleteItem());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveItem();
                finish();
                return true;

            case R.id.action_delete_item:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = (dialog, which) -> finish();

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all the attributes for an item, define a projection that contains
        // all columns from the items table
        String[] projection = {
                ItemsContract.ItemsEntry._ID,
                ItemsContract.ItemsEntry.COLUMN_ITEM_NAME,
                ItemsContract.ItemsEntry.COLUMN_SUPPLIER_NAME,
                ItemsContract.ItemsEntry.COLUMN_ITEM_QUANTITY,
                ItemsContract.ItemsEntry.COLUMN_ITEM_PICTURE,
                ItemsContract.ItemsEntry.COLUMN_ITEM_PRICE};

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_ITEM_NAME);
            int supplierColumnIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_SUPPLIER_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_ITEM_QUANTITY);
            int pictureColumnIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_ITEM_PICTURE);

            String name = cursor.getString(nameColumnIndex);
            int supplier = cursor.getInt(supplierColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            mItemEditText.setText(name);
            mPriceEditText.setText(String.valueOf(price));
            mQuantityEditText.setText(String.valueOf(quantity));
            currentPhotoUri = cursor.getString(pictureColumnIndex);

            // Supplier is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options .
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (supplier) {
                case ItemsContract.ItemsEntry.SUPPLIER_JUNIPER:
                    mSupplierSpinner.setSelection(1);
                    break;
                case ItemsContract.ItemsEntry.SUPPLIER_BARRACUDA:
                    mSupplierSpinner.setSelection(2);
                    break;
                case ItemsContract.ItemsEntry.SUPPLIER_ADTRAN:
                    mSupplierSpinner.setSelection(3);
                    break;
                case ItemsContract.ItemsEntry.SUPPLIER_CISCO:
                    mSupplierSpinner.setSelection(0);
                    break;
            }

            Glide.with(this).load(currentPhotoUri)

                    .placeholder(R.mipmap.ic_launcher)
                    .transition(withCrossFade())
                    .into(mImageView);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mItemEditText.setText("");
        mSupplierSpinner.setSelection(0); // Select "Unknown" supplier
        mQuantityEditText.setText("");
        mPriceEditText.setText("");

    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and the negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void substractOneToQuantity() {
        String previousValueString = mQuantityEditText.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
        } else if (previousValueString.equals("0")) {
        } else {
            previousValue = Integer.parseInt(previousValueString);
            mQuantityEditText.setText(String.valueOf(previousValue - 1));
        }
    }

    private void addOneToQuantity() {
        String previousValueString = mQuantityEditText.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        mQuantityEditText.setText(String.valueOf(previousValue + 1));
    }

    /**
     * Perform the deletion of the item in the database.
     */

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            if (rowsDeleted != 0) {
                Toast.makeText(this, getString(R.string.delete_item_successful), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditorActivity.this, MaActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.delete_item_failed), Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
