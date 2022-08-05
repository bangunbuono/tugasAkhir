package com.example.fixinventori.Chat.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {

    DocumentReference documentReference;
    UserSession session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSession(this);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        if(session.getString(Constants.KEY_USER_ID)!=null)
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(session.getString(Constants.KEY_USER_ID));
        else documentReference = database
                .collection(Constants.KEY_COLLECTION_MANAGERS)
                .document(session.getString(Constants.KEY_MANAGER_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
        if(session.getString(Constants.KEY_USER_ID)!=null)
            System.out.println("base adapter" + session.getString(Constants.KEY_USER_ID) );
        else  System.out.println("base adapter" + session.getString(Constants.KEY_MANAGER_ID));
    }
}
