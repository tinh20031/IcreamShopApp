package com.example.iceamapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cart implements Parcelable {
    @SerializedName("cartId")
    private int cartId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("iceCreamId")
    private int iceCreamId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("iceCreamName")
    private String iceCreamName;

    @SerializedName("image")
    private String image;

    @SerializedName("price")
    private float price;

    public Cart(int userId, int iceCreamId, int quantity) {
        this.userId = userId;
        this.iceCreamId = iceCreamId;
        this.quantity = quantity;
    }

    protected Cart(Parcel in) {
        cartId = in.readInt();
        userId = in.readInt();
        iceCreamId = in.readInt();
        quantity = in.readInt();
        createdAt = in.readString();
        iceCreamName = in.readString();
        image = in.readString();
        price = in.readFloat();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cartId);
        dest.writeInt(userId);
        dest.writeInt(iceCreamId);
        dest.writeInt(quantity);
        dest.writeString(createdAt);
        dest.writeString(iceCreamName);
        dest.writeString(image);
        dest.writeFloat(price);
    }

    // Getters
    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public int getIceCreamId() { return iceCreamId; }
    public int getQuantity() { return quantity; }
    public String getCreatedAt() { return createdAt; }
    public String getIceCreamName() { return iceCreamName; }
    public String getImage() { return image; }
    public float getPrice() { return price; }
}