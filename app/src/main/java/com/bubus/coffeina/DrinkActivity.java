package com.bubus.coffeina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        // Pobieramy identyfikator napoju z intencji
        int drinkNo = (Integer)getIntent().getExtras().get(EXTRA_DRINKNO);
        Drink drink = Drink.drinks[drinkNo];

        // Wyświetlamy zdjęcie napoju
        ImageView photo = (ImageView)findViewById(R.id.photo);
        photo.setImageResource(drink.getImageResourceId());
        photo.setContentDescription(drink.getName());

        // Wyświetlamy nazwę napoju
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(drink.getName());

        // Wyświetlamy opis napoju
        TextView description = (TextView)findViewById(R.id.description);
        description.setText(drink.getDescription());
    }
}
