package com.android.FinaNet;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.FinaNet.interfaces.IContactDAO;
import com.android.FinaNet.models.Contacts;

@Database(entities = {Contacts.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IContactDAO getContactDAO();
}
