package com.prashant.zomatov2.firebase;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constant {
    public static DatabaseReference USER_DB = FirebaseDatabase.getInstance().getReference().child("users");
    public static DatabaseReference HOTEL_DB = FirebaseDatabase.getInstance().getReference().child("hotels");
    public static DatabaseReference ITEM_DB = FirebaseDatabase.getInstance().getReference().child("items");

}
