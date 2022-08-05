package com.example.fixinventori.Chat.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Adapter.ChatAdapter;
import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.Chat.network.ApiClient;
import com.example.fixinventori.Chat.network.ApiServices;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    UserSession session;
    TextView tvChatUser, tvAvailability;
    RecyclerView rvChat;
    ChatAdapter chatAdapter;
    EditText etInputChat;
    List<ChatMessageModel> listChat;
    RoundedImageView rivSend, rivBack;
    String managerName, user, managerToken;
    FirebaseFirestore database;
    String conversionId = null;
    boolean isReceiverAvalaible = false;
    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Objects.requireNonNull(getSupportActionBar()).hide();

        session = new UserSession(this);
        user = session.getString("username");
        database = FirebaseFirestore.getInstance();

        managerName = getIntent().getStringExtra(Constants.KEY_MANAGER);
        getReceiverId();

        listChat = new ArrayList<>();

        tvChatUser = findViewById(R.id.tvChatUser);
        rvChat = findViewById(R.id.rvChat);
        etInputChat = findViewById(R.id.etInputChat);
        rivSend = findViewById(R.id.rivSend);
        rivBack = findViewById(R.id.rivBack);
        tvAvailability = findViewById(R.id.tvAvailability);

        tvChatUser.setText(managerName);

        rivBack.setOnClickListener(view -> onBackPressed());

        rivSend.setOnClickListener(view -> {
            if(!etInputChat.getText().toString().isEmpty()) {
                sendMessage();
            }
        });

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
        if(conversionId!=null)
            updateConversion(etInputChat.getText().toString().trim());
        else {
            HashMap<String,Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, session.getString(Constants.KEY_USER_ID));
            conversion.put(Constants.KEY_SENDER_NAME, user);
//            conversion.put(Constants.KEY_SENDER_IMAGE, session.getString(Constants.KEY_IMAGE));
            conversion.put(Constants.KEY_RECEIVER_ID, session.getString(Constants.KEY_MANAGER_ID));
            conversion.put(Constants.KEY_RECEIVER_NAME, managerName);
            conversion.put(Constants.KEY_LAST_MESSAGE, etInputChat.getText().toString().trim());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
        if(!isReceiverAvalaible){
            try {
                JSONArray tokens = new JSONArray();
                tokens.put(managerToken);

                JSONObject data = new JSONObject();
                data.put(Constants.KEY_USER_ID, session.getString(Constants.KEY_USER_ID));
                data.put(Constants.KEY_NAME, session.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN, session.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE, etInputChat.getText().toString().trim());

                System.out.println("token user" +session.getString(Constants.KEY_FCM_TOKEN));

                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MESSAGE_DATA, data);
                body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

                sendNotification(body.toString());

            }catch (Exception e){
                Toast.makeText(this, "0"+e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println( "cek 2"+e.getMessage());
            }
        }
        System.out.println("manager token " + managerToken);

        etInputChat.setText(null);
    }

    public void getReceiverId() {
        System.out.println("complete");
        database.collection(Constants.KEY_COLLECTION_MANAGERS)
                .whereEqualTo(Constants.KEY_MANAGER, managerName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.getResult()!=null){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String userId = documentSnapshot.getId();
                        session.putString(Constants.KEY_MANAGER_ID, userId);
                        System.out.println(userId);
                        status = true;
                    }
                }).addOnFailureListener(e -> System.out.println("gagal" + e.getMessage()));
    }

//    public void getManagerId() {
//        System.out.println("get manager id");
//        database.collection("managers")
//                .whereEqualTo(Constants.KEY_MANAGER, managerName)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isComplete() && task.getResult()!=null){
//                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                        String managerId = documentSnapshot.getId();
//                        System.out.println("direct id"+documentSnapshot.getId());
//                        String FCMtoken = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
//                        session.putString(Constants.KEY_MANAGER_ID, managerId);
//                        session.putString(Constants.KEY_FCM_TOKEN_MANAGER, FCMtoken);
//                        System.out.println("getmanager "+managerId);
//
//                    }
//                }).addOnFailureListener(e -> System.out.println("gagal dapat managerId"));
//    }

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
        if (conversionId==null) checkForConversion();

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
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
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

    private void checkAvailableReceivers(){
        System.out.println("check avalaibale managerid " + session.getString(Constants.KEY_MANAGER_ID));
        database.collection(Constants.KEY_COLLECTION_MANAGERS)
                .document(session.getString(Constants.KEY_MANAGER_ID))
                .addSnapshotListener(ChatActivity.this, ((value, error) -> {
                    if(error!=null) {
                        System.out.println("error available user "+error.getMessage());
                        return;
                    }
                    else if(value!=null){
                        if (value.getLong(Constants.KEY_AVAILABILITY)!=null){
                            int availability = Objects.requireNonNull(
                                            value.getLong(Constants.KEY_AVAILABILITY))
                                    .intValue();
                            isReceiverAvalaible = availability == 1;
                        }
                        managerToken = value.getString(Constants.KEY_FCM_TOKEN);
                        System.out.println("ini "+managerToken);
//                        if(user.image == null){
//                            user.image =  value.getString(Constants.KEY_IMAGE);
//                            chatAdapter.setReceiverProfilImage(getBitmapFromEncodedString(user.image));
//                            chatAdapter.notifyItemRangeChanged(0, listChat.size());
//                        }
                    }
                    if (isReceiverAvalaible){
                        tvAvailability.setText(String.format("%s", "Online"));
                        tvAvailability.setVisibility(View.VISIBLE);
                        tvChatUser.setPadding(0, 6,0,0);
                    }else {
                        tvAvailability.setVisibility(View.GONE);
                        tvChatUser.setPadding(0,12,0,0);
                    }
                }));
    }

    //notification

    private void sendNotification(String messageBody){
        ApiClient.getClient().create(ApiServices.class)
                .sendMessage(Constants.getRemoteMsgHeaders(), messageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if(response.isSuccessful()){
                            try {
                                if(response.body()!=null){
                                    JSONObject responseJSON = new JSONObject(response.body());
                                    JSONArray results = responseJSON.getJSONArray("results");
                                    if(responseJSON.getInt("failure")==1){
                                        JSONObject error = (JSONObject) results.get(0);
                                        Toast.makeText(ChatActivity.this, "cek "+error.getString("error"),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }catch (JSONException e){
                                Toast.makeText(ChatActivity.this, "1"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(ChatActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            System.out.println("respons "+response.code());
                            Toast.makeText(ChatActivity.this,
                                    "Error" + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(ChatActivity.this, "2"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(status)
        checkAvailableReceivers();
    }
}