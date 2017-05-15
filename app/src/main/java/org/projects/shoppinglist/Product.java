package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kasper on 09-03-2017.
 */

public class Product implements Parcelable{
    String name;
    int quantity;
    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product( );
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product() {
    }

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + " " + quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getName(){
        return this.name;
    }
    public void setName(String nName){
        this.name = nName;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public void setQuantity(int nQuant){
        this.quantity = nQuant;
    }
}
