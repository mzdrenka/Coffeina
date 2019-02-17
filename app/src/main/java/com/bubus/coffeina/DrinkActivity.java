package com.bubus.coffeina;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.Instant;


public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKNO ="drinkNo";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        // Pobieramy identyfikator napoju z intencji
        int drinkNo = (Integer)getIntent().getExtras().get(EXTRA_DRINKNO);
        // Tworzymy kursor.
        try {
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
            SQLiteDatabase db = coffeinaDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("DRINK",
                    new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                     new String[] {Integer.toString(drinkNo)},
                    null, null,null);
            // Przechodzimy do pierwszego rekordu w kursorze
            if (cursor.moveToFirst()) {
                // Pobieramy z kursora szczegółowe informacje o napoju
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);
                // Wyświetlamy nazwę napoju
                TextView name = (TextView)findViewById(R.id.name);
                name.setText(nameText);
                // Wyświetlamy opis napoju
                TextView description = (TextView)findViewById(R.id.description);
                description.setText(descriptionText);

                // Wyświetlamy zdjęcie napoju
                ImageView photo = (ImageView)findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                // Pobranie czy ulubiony
                CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }

            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this,  e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    // Aktualizujemy bazę danych po kliknięciu pola wyboru
    public void onFavoriteClicked(View view){

        int drinkNo = (Integer)getIntent().getExtras().get(EXTRA_DRINKNO);
        new UpdateDrinkTask().execute(drinkNo);
    }

    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        protected Boolean doInBackground(Integer... drinks){
            int drinkNo = drinks[0];
            SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(DrinkActivity.this);
            try{
                SQLiteDatabase db = coffeinaDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues,
                        "_id = ?", new String[] {Integer.toString(drinkNo)});
                db.close();
                return true;
            }catch (SQLiteException e){
                return false;
            }
        }

        protected void onPostExecute(Boolean success){
            if(!success){
                Toast toast = Toast.makeText(DrinkActivity.this,"Baza danych jest niedostępna", Toast.LENGTH_SHORT);
                toast.show();
            }else if(success){
                Toast toast = Toast.makeText(DrinkActivity.this,"Baza danych zaaktualizowana", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
