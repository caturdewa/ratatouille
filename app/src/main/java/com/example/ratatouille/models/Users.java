package com.example.ratatouille.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ratatouille.db.DatabaseHelper;
import com.example.ratatouille.db.DatabaseVars;
import com.example.ratatouille.vars.VariablesUsed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.sql.Timestamp;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Users {

    private String username, name, phone, address, last_login;
    private Integer points;
    private Integer numberOfLogins = 0;

    public Users(){
        //let this to be blank, for data retrival..
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String username, String name, String phone, String address, String last_login, Integer points, Integer numberOfLogins) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.last_login = last_login;
        this.points = points;
        this.numberOfLogins = numberOfLogins;
    }

    public static void register(Users newUser, String email, String password){ // this register method is an exception, due to the multithreading system of Firebase, we need to static this..
        FirebaseAuth dAuth = DatabaseHelper.getDbAuth();

        dAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = dAuth.getCurrentUser();

                    DatabaseVars.UsersTable dbVars = new DatabaseVars.UsersTable();
                    DatabaseReference dbRef = DatabaseHelper.getDb().getReference(dbVars.USERS_TABLE).child(user.getUid());

                    dbRef.setValue(newUser);

                    user.sendEmailVerification();

                    Log.w(TAG, "Data For User : Registration / Update has been completed! Please check your email to Log In!");

                } else {
                    Log.w(TAG, "Data For User : Failed to save user data!");
                }
            }
        });
    }

    public void save(){
        DatabaseReference dbRef = DatabaseHelper.getDb().getReference(DatabaseVars.UsersTable.USERS_TABLE);
        dbRef.child(VariablesUsed.loggedUser.getUid()).setValue(this);
    }

    public Users update(String username, String name, String phone, String address, String last_login, Integer points){
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.last_login = last_login;
        this.points = points;

        save();

        return this;
    }

    public static void delete(String userID){
        DatabaseReference dbRef = DatabaseHelper.getDb().getReference(DatabaseVars.UsersTable.USERS_TABLE);
        dbRef.child(userID).removeValue();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getNumberOfLogins() {
        return numberOfLogins;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setNumberOfLogins(Integer numberOfLogins) {
        this.numberOfLogins = numberOfLogins;
    }
}
