package com.android.FinaNet.interfaces;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.android.FinaNet.models.Contacts;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class IContactDAO_Impl implements IContactDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfContacts;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfContacts;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfContacts;

  public IContactDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContacts = new EntityInsertionAdapter<Contacts>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Contacts`(`uid`,`first_name`,`last_name`,`phone_number`,`email`,`address`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contacts value) {
        stmt.bindLong(1, value.uid);
        if (value.firstName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.firstName);
        }
        if (value.lastName == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.lastName);
        }
        if (value.phoneNumber == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.phoneNumber);
        }
        if (value.email == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.email);
        }
        if (value.address == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.address);
        }
      }
    };
    this.__deletionAdapterOfContacts = new EntityDeletionOrUpdateAdapter<Contacts>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Contacts` WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contacts value) {
        stmt.bindLong(1, value.uid);
      }
    };
    this.__updateAdapterOfContacts = new EntityDeletionOrUpdateAdapter<Contacts>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Contacts` SET `uid` = ?,`first_name` = ?,`last_name` = ?,`phone_number` = ?,`email` = ?,`address` = ? WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contacts value) {
        stmt.bindLong(1, value.uid);
        if (value.firstName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.firstName);
        }
        if (value.lastName == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.lastName);
        }
        if (value.phoneNumber == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.phoneNumber);
        }
        if (value.email == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.email);
        }
        if (value.address == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.address);
        }
        stmt.bindLong(7, value.uid);
      }
    };
  }

  @Override
  public void insertContacts(Contacts contacts) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfContacts.insert(contacts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteContacts(Contacts contacts) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfContacts.handle(contacts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateContacts(Contacts contacts) {
    __db.beginTransaction();
    try {
      __updateAdapterOfContacts.handle(contacts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Contacts> getAllContacts() {
    final String _sql = "SELECT * FROM Contacts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUid = _cursor.getColumnIndexOrThrow("uid");
      final int _cursorIndexOfFirstName = _cursor.getColumnIndexOrThrow("first_name");
      final int _cursorIndexOfLastName = _cursor.getColumnIndexOrThrow("last_name");
      final int _cursorIndexOfPhoneNumber = _cursor.getColumnIndexOrThrow("phone_number");
      final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("email");
      final int _cursorIndexOfAddress = _cursor.getColumnIndexOrThrow("address");
      final List<Contacts> _result = new ArrayList<Contacts>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Contacts _item;
        final int _tmpUid;
        _tmpUid = _cursor.getInt(_cursorIndexOfUid);
        final String _tmpFirstName;
        _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        final String _tmpLastName;
        _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        final String _tmpPhoneNumber;
        _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item = new Contacts(_tmpUid,_tmpFirstName,_tmpLastName,_tmpPhoneNumber,_tmpEmail,_tmpAddress);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Contacts getItemById(int uid) {
    final String _sql = "SELECT * FROM Contacts WHERE uid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, uid);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUid = _cursor.getColumnIndexOrThrow("uid");
      final int _cursorIndexOfFirstName = _cursor.getColumnIndexOrThrow("first_name");
      final int _cursorIndexOfLastName = _cursor.getColumnIndexOrThrow("last_name");
      final int _cursorIndexOfPhoneNumber = _cursor.getColumnIndexOrThrow("phone_number");
      final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("email");
      final int _cursorIndexOfAddress = _cursor.getColumnIndexOrThrow("address");
      final Contacts _result;
      if(_cursor.moveToFirst()) {
        final int _tmpUid;
        _tmpUid = _cursor.getInt(_cursorIndexOfUid);
        final String _tmpFirstName;
        _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        final String _tmpLastName;
        _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        final String _tmpPhoneNumber;
        _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _result = new Contacts(_tmpUid,_tmpFirstName,_tmpLastName,_tmpPhoneNumber,_tmpEmail,_tmpAddress);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
