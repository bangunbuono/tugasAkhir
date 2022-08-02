package com.example.fixinventori.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Adapter.ChatAdapter;
import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    UserSession session;
    TextView tvChatUser;
    RecyclerView rvChat;
    ChatAdapter chatAdapter;
    EditText etInputChat;
    List<ChatMessageModel> listChat;
    RoundedImageView rivSend, rivBack;
    String managerName, user;
    FirebaseFirestore database;
    String conversionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Objects.requireNonNull(getSupportActionBar()).hide();

        session = new UserSession(this);
        user = session.getString("username");

        listChat = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

        tvChatUser = findViewById(R.id.tvChatUser);
        rvChat = findViewById(R.id.rvChat);
        etInputChat = findViewById(R.id.etInputChat);
        rivSend = findViewById(R.id.rivSend);
        rivBack = findViewById(R.id.rivBack);

        managerName = getIntent().getStringExtra(Constants.KEY_MANAGER);

        getManagerId();

        tvChatUser.setText(managerName);

        rivBack.setOnClickListener(view -> onBackPressed());

        rivSend.setOnClickListener(view -> sendMessage());

        listenMessages();
    }

    //chat

    private void sendMessage(){
        HashMap<String , Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, session.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, session.getString(Constants.KEY_MANAGER_ID));
        message.put(Constants.KEY_MESSAGE, etInputChat.getText().toString().trim());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database
                .collection(Constants.KEY_COLLECTION_CHAT)
                .add(message);
//        if(conversionId==null)
//            updateConversion(etInputChat.getText().toString().trim());
//        else {
//            HashMap<String,Object> conversion = new HashMap<>();
//            conversion.put(Constants.KEY_SENDER_ID, session.getString(Constants.KEY_USER_ID));
//            conversion.put(Constants.KEY_SENDER_NAME, session.getString(Constants.KEY_NAME));
//            conversion.put(Constants.KEY_SENDER_IMAGE, session.getString(Constants.KEY_IMAGE));
//            conversion.put(Constants.KEY_RECEIVER_ID, session.getString(Constants.KEY_MANAGER_ID));
//            conversion.put(Constants.KEY_RECEIVER_NAME, session.getString(Constants.KEY_MANAGER));
//            conversion.put(Constants.KEY_LAST_MESSAGE, etInputChat.getText().toString().trim());
//            conversion.put(Constants.KEY_TIMESTAMP, new Date());
//            addConversion(conversion);
//        }
        etInputChat.setText(null);
    }

    public void getManagerId() {
        database.collection(Constants.KEY_COLLECTION_MANAGERS)
                .whereEqualTo(Constants.KEY_MANAGER, managerName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.getResult()!=null){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String managerId = documentSnapshot.getId();
                        session.putString(Constants.KEY_MANAGER_ID, managerId);
                    }
                });
    }

    private void listenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, session.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, session.getString(Constants.KEY_MANAGER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, session.getString(Constants.KEY_MANAGER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, session.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
//        System.out.println("key sender id = " + session.getString(Constants.KEY_USER_ID));
//        System.out.println("key receiver id = " + session.getString(Constants.KEY_MANAGER_ID));
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if(error != null)
            return;
        if (value!= null && listChat!=null) {
            int count = listChat.size();

            for (DocumentChange change: value.getDocumentChanges()) {
                if(change.getType() == DocumentChange.Type.ADDED){
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.senderId = change.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessageModel.receiverId = change.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessageModel.message = change.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessageModel.dateTime = getDate(change.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessageModel.dateObject = change.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    listChat.add(chatMessageModel);
                }
            }
            listChat.sort(Comparator.comparing(obj -> obj.dateObject));
            chatAdapter = new ChatAdapter(listChat,
                    session.getString(Constants.KEY_USER_ID));
            rvChat.setAdapter(chatAdapter);
            rvChat.setVisibility(View.VISIBLE);

            if (count==0) chatAdapter.notifyDataSetChanged();
            else{
                chatAdapter.notifyItemRangeInserted(listChat.size(), listChat.size());
                rvChat.smoothScrollToPosition(listChat.size()-1);
            }
        }
//        if (conversionId==null) checkForConversion();

    });

    private String getDate(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

//
    //conversation
//

    private void checkForConversion(){
        if(listChat!=null)
        if(listChat.size()>0) {
            checkForConversionRemotely(
                    session.getString(Constants.KEY_USER_ID),
                    session.getString(Constants.KEY_MANAGER_ID));
            checkForConversionRemotely(
                    session.getString(Constants.KEY_MANAGER_ID),
                    session.getString(Constants.KEY_USER_ID));
        }
    }

    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    void updateConversion(String message){
        DocumentReference documentReference = database
                .collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .document(conversionId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message, Constants.KEY_TIMESTAMP, new Date());

    }

    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnComplete);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnComplete = task -> {
        if (task.isSuccessful() && task.getResult()!= null && task.getResult().getDocuments().size()>0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };
}