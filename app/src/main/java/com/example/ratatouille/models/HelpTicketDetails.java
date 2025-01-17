package com.example.ratatouille.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ratatouille.db.DatabaseHelper;
import com.example.ratatouille.db.DatabaseVars;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

// assumed that Details CANNOT BE UPDATED!

public class HelpTicketDetails {
    private String messageID;
    private String ticketID;
    private String employeeID;
    private String message;

    private static HelpTicketDetails selectedValues = null;

    public HelpTicketDetails() {
        //let this to be blank, for data retrival..
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public HelpTicketDetails(String messageID, String ticketID, String employeeID, String message) {
        this.messageID = messageID;
        this.ticketID = ticketID;
        this.employeeID = employeeID;
        this.message = message;
    }

    public void save() {
        DatabaseReference dbRef = DatabaseHelper.getDb().getReference(DatabaseVars.HelpTicketDetailsTable.HELPTICKETDETAILS_TABLE);

        dbRef.child(messageID).setValue(this);
    }

    public static void delete(String messageID){
        DatabaseReference dbRef = DatabaseHelper.getDb().getReference(DatabaseVars.HelpTicketDetailsTable.HELPTICKETDETAILS_TABLE);
        dbRef.child(messageID).removeValue();
    }

    public static HelpTicketDetails get(String messageID){
        DatabaseReference dbRef = DatabaseHelper.getDb().getReference(DatabaseVars.HelpTicketDetailsTable.HELPTICKETDETAILS_TABLE);

        selectedValues = null;

        dbRef.child(messageID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedValues = snapshot.getValue(HelpTicketDetails.class);
                Log.w(TAG, "onSuccess: HelpTicketDetails successfully retrieved!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onFailure: All HelpTicketDetails failed to be retrieved!");
            }
        });

        return selectedValues;
    }

    public static ArrayList<HelpTicketDetails> getAll(String ticketID){
        DatabaseReference dbRef = DatabaseHelper.getDb().getReference(DatabaseVars.HelpTicketDetailsTable.HELPTICKETDETAILS_TABLE);

        ArrayList<HelpTicketDetails> detailList = new ArrayList<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot eachData : snapshot.getChildren()){
                    if(eachData.child("ticketID").equals(ticketID)) {
                        HelpTicketDetails curDetail = eachData.getValue(HelpTicketDetails.class);
                        detailList.add(curDetail);
                    }
                }
                Log.w(TAG, "onSuccess: All HelpTicketDetails successfully retrieved!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onFailure: All HelpTicketDetails failed to be retrieved!");
            }
        });

        return detailList;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getMessage() {
        return message;
    }

}
