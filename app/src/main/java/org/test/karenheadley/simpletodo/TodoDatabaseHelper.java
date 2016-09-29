package org.test.karenheadley.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TodoDatabase";
    private static final String TABLE_NAME = "todoItem";
    private static final int DATABASE_VERSION = 1;
    private static TodoDatabaseHelper sInstance;


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Use the application context, which will ensure that you
    // don't accidentally leak an Activity's context.
    // See this article for more information: http://bit.ly/6LRzfx
    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context);
        }
        return sInstance;
    }
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "( pos INTEGER PRIMARY KEY, text TEXT, date DATE, priority INTEGER )";
        db.execSQL(CREATE_TODO_ITEMS_TABLE);
    }
    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void addTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("pos", todoItem.pos);
            values.put("text", todoItem.text);
            //values.put("priority", todoItem.priority);
            // values.put("date"...
            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public void updateTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("pos", todoItem.pos);
            values.put("text", todoItem.text);
            //values.put("priority", todoItem.priority);
            // values.put("date"....
            db.update(TABLE_NAME, values, "pos=" + Integer.toString(todoItem.pos), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public void deleteTodoItem(int pos) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, "pos=" + Integer.toString(pos), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<TodoItem> getAllTodos() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<TodoItem> todoItems = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TodoItem todoItem = new TodoItem();
                    todoItem.pos = cursor.getInt(cursor.getColumnIndex("pos"));
                    todoItem.text = cursor.getString(cursor.getColumnIndex("text"));
                    // todoItem.date = cursor.get
                    // todoItem.priority = cursor.getInt(cursor.getColumnIndex("priority"));
                    todoItems.add(todoItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todoItems;
    }
}
