package com.bubus.coffeina;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends AppCompatActivity {
//strona 529
    private SQLiteDatabase db;
    private Cursor favoritesCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        // Tworzymy obiekt nasłuchujący OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(TopLevelActivity.this, DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };

        // Dodajemy obiekt nasłuchujący do widoku listy
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);

        // Zapisujemy na liście list_favorites ulubione napoje użytkownika
        ListView listFavorites = (ListView)findViewById(R.id.list_favorites);
        try{
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            db = coffeinaDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("DRINK", new String[] {"_id", "NAME"}, "FAVORITE = 1", null, null, null, null);
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(
                    TopLevelActivity.this, android.R.layout.simple_expandable_list_item_1, favoritesCursor, new String[] {"NAME"}, new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        }catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
            toast.show();
        }
        // Po kliknięciu ulubionego napoju przechodzimy do aktywności DrinkActivity
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKNO, (int) id);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        try{
            CoffeinaDatabaseHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            db = coffeinaDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("DRINK",new String[] { "_id", "NAME"},"FAVORITE = 1",null, null, null, null);
            // Pobieramy adapter CursorAdapter używany przez widok ListView
            ListView listVavorites = (ListView) findViewById(R.id.list_favorites);
            CursorAdapter adapter = (CursorAdapter) listVavorites.getAdapter();
            // Zmieniamy kursor używany przez adapter CursorAdapter na nowy
            adapter.changeCursor(newCursor);

            favoritesCursor = newCursor;
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
