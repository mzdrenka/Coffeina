package com.bubus.coffeina;

public class Drink {

    private final String name;
    private final String description;
    private final int imageResourceId;

    // drinks to tablica obiektów klasy Drink
    public static final Drink[] drinks = {

            new Drink("Latte", "Czarne espresso z gorącym mlekiem i mleczną pianką.", R.drawable.latte),
            new Drink("Cappuccino", "Czarne espresso z dużą ilością spienionego mleka.", R.drawable.cappuccino),
            new Drink("Espresso", "Czarna kawa ze świeżo mielonych ziaren najwyższej jakości.", R.drawable.filter)

    };

    // Każdy Drink ma nazwę, opis oraz zasób graficzny
    public Drink(String name, String description, int imageResourceId){

        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;

    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String toString() {
        return this.name;
    }
}
