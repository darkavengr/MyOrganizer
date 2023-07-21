package com.example.myorganizer;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

//
// Database class
//
class Database {
    SQLiteDatabase database;
    String DBName;

    //
    // Database constructor
    //
    // In: DatabaseName         Name of the database to use
    //
    // Returns: nothing
    //
    Database(String DatabaseName) {

        database = openOrCreateDatabase(DatabaseName,null);
        DBName = DatabaseName;          // save database name
    }

    //
    // Create table
    //
    // In:      Table      Table name
   //           params      Column name and types
    //
    // Returns: Nothing
    //
    void CreateTable(String Table,String...params) {
     String SQLStatement="CREATE TABLE IF NOT EXISTS "+Table+"(";             // Create table statement
     int NameOrType=0;

     // Add names and types

     for(String s:params) {
        SQLStatement += s;

        if(NameOrType++ == 1) {         // End of name and type pair
                 SQLStatement += ",";
                 NameOrType=0;

        }
        else {
                SQLStatement += " ";
        }

       }

        SQLStatement = SQLStatement.substring(0,SQLStatement.length()-1);        // Remove , from end

        SQLStatement += ");";

        database.execSQL("DROP TABLE "+Table);        // Add table
        database.execSQL(SQLStatement);        // Add table
    }

    //
    // Delete table
    //
    // In:  Table           Database table to delete
    //
    // Returns: Nothing
    //
    void DropTable(String Table) {
        database.execSQL("DROP TABLE "+Table);
    }

    //
    // Add a task to the database
    //
    // In:  Table           Database table to insert into
    //      columnValues    Values to insert
    //
    // Returns: Nothing
    //
    void Add(String table, Object...columnValues) {
        String SQLStatement="INSERT INTO "+table + " VALUES(";
        int count=0;

        for(Object s:columnValues) {
            SQLStatement += s + ",";
        }

        SQLStatement = SQLStatement.substring(0,SQLStatement.length()-1);        // Remove , from end
        SQLStatement += ");";

        database.execSQL(SQLStatement);
    }

    //
    // Update a task in the database
    //
    // In:  Table           Database table to insert into
    //      WhereClause
    //      columnValues    Values to insert
    //
    // Returns: Nothing
    //
    void Update(String table, String WhereClause,Object...columnValues) {
        String SQLStatement="UPDATE "+table + " SET ";
        int count=0;

        int NameOrValue=0;

        // Add names and types

        for(Object s:columnValues) {

            if(NameOrValue == 0) {
                SQLStatement += s + "=";
            }
            if(NameOrValue == 1) {
                SQLStatement += s;
            }
            if(NameOrValue == 2) {
                SQLStatement += ",";
            }

            NameOrValue++;

        }

        if(SQLStatement.charAt((SQLStatement.length()-1)) == ',') {
            SQLStatement = SQLStatement.substring(0, SQLStatement.length() - 1);        // Remove , from end
        }

        if(WhereClause != "") SQLStatement += " WHERE "+WhereClause;

        SQLStatement += ';';

        Log.d("UPDATE",SQLStatement);

        database.execSQL(SQLStatement);        // Add table
    }
    //
    // Query database
    //
    //
    // In:  Table           Database table to insert into
    //      Columns         Columns to project
    //      selection       Columns to select
    //      sectionArgs     Arguments to select columns
    //      groupBy         Group by columns (optional)
    //      having          Having clause
    //      orderBy         Order by clause
    //      limit           Limits on the number of columns returned
    //      results         List of lists to hold results
    //
    // Returns: Nothing
    //
    ArrayList<ArrayList<Object>> Select(String Table,String Columns[],String selection,String selectionArgs[],String groupBy,String having,String orderBy,String limit) {
        int count;
        int type;

        int COLUMN_NULL = 0;
        int COLUMN_INTEGER = 1;
        int COLUMN_FLOAT = 2;
        int COLUMN_STRING = 3;
        int COLUMN_BLOB = 4;

        ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> temp = new ArrayList<Object>();

        // Query database
        Cursor dbcursor = database.query(Table, Columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        // Loop through results and add to list

        count = 0;
        while (dbcursor.moveToNext()) {

            for(count=0;count<dbcursor.getColumnCount();count++) {
                type = dbcursor.getType(count);

                if (type == COLUMN_NULL) {
                    temp.add(dbcursor.getString(count));
                } else if (type == COLUMN_INTEGER) {
                    temp.add(dbcursor.getInt(count));
                } else if (type == COLUMN_FLOAT) {
                    temp.add(dbcursor.getFloat(count));
                }
                if (type == COLUMN_STRING) {
                    temp.add(dbcursor.getString(count));
                }

            }

            results.add(temp);          // add to list of lists

            // Must use new instead of clear. Javas u es shallow copies and pointers
            // the results array is cleared if the temp array is cleared if clear() is used
            temp=new ArrayList<Object>();
            }

        dbcursor.close();

        return(results);
    }


    //
    // Delete from database
    //
    // In:    Table             Table name
    //        whereString       select columns to use in deletion
    //        whereArgs         arguments used with columns to use in deletion
    //
    // Returns: 0 if task deleted successfully, -1 otherwise
    //
    void Delete(String Table,String whereString,String whereArgs[]) {
        database.delete(Table,whereString,whereArgs);
    }
}


