package com.example.fixinventori.BottomNavBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixinventori.Activity.Setting.AddUserConnect;
import com.example.fixinventori.Activity.Setting.UserManagerActivity;
import com.example.fixinventori.Activity.User.ManagerLoginActivity;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ManagerSettingsFragment extends Fragment {
    UserSession session;
    String manager, encodedImage;
    Bitmap bitmap;
    RoundedImageView rivUserProfile;

    public ManagerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        manager = session.getManagerDetail().get("manager");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_settings, container, false);
        TextView tvLogout = view.findViewById(R.id.tvLogOut);
        TextView tvInfo = view.findViewById(R.id.tvInfo);
        TextView tvConnect = view.findViewById(R.id.tvUserConnect);
        TextView tvBantuan = view.findViewById(R.id.tvBantuan);
        TextView tvManagerName = view.findViewById(R.id.tvUserName);
        TextView tvUserManager = view.findViewById(R.id.tvUserManager);
        rivUserProfile = view.findViewById(R.id.rivUserProfile);

        tvManagerName.setText(manager);

        getUserProfile();

        rivUserProfile.setOnClickListener(view1->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        if(UsageAutoApplication.listConnectedUser.size()>0){
            tvConnect.setVisibility(View.GONE);
        }else tvUserManager.setVisibility(View.GONE);

        tvConnect.setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), AddUserConnect.class)));

        tvUserManager.setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), UserManagerActivity.class)));

        tvBantuan.setOnClickListener(view1 -> System.out.println("bantuan"));

        tvInfo.setOnClickListener(view1 -> System.out.println("info"));

        tvLogout.setOnClickListener(view1 -> {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.
                    collection(Constants.KEY_COLLECTION_MANAGERS).
                    document(session.getString(Constants.KEY_MANAGER_ID));
            HashMap<String, Object> update = new HashMap<>();
            update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(update).addOnSuccessListener(unused -> {
                session.logOutSession();
                Intent intent = new Intent(getActivity(), ManagerLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                requireActivity().finish();
            }).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        });
        return view;
    }

    private void getUserProfile() {
        if(session.getString(Constants.KEY_IMAGE)!=null)
            decodeImage(session.getString(Constants.KEY_IMAGE));
        else rivUserProfile.setImageResource(R.drawable.ic_baseline_account_circle_24);

    }

    private void decodeImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        rivUserProfile.setImageBitmap(bitmap);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    if(result.getData()!=null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = requireActivity()
                                    .getContentResolver().openInputStream(imageUri);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            encodedImage = encodeImage(bitmap);
                            uploadImageFirebase();
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }

                    }
                }
            });

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void uploadImageFirebase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference document = database
                .collection(Constants.KEY_COLLECTION_MANAGERS)
                .document(session.getString(Constants.KEY_MANAGER_ID));

        document.update(Constants.KEY_IMAGE, encodedImage)
                .addOnSuccessListener(unused ->
                {
                    Toast.makeText(getActivity(),  "Berhasil ubah foto profil", Toast.LENGTH_SHORT).show();
                    rivUserProfile.setImageBitmap(bitmap);
                    session.putString(Constants.KEY_IMAGE, encodedImage);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}