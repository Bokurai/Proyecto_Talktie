package com.example.proyecto_talktie.view.company_fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.MainActivity;
import com.example.proyecto_talktie.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragment representing a company profile. Allows the company to view and edit its information,
 * such as the company name, description, profile picture, etc. In addition, it provides the
 * functionality for the company to log out and navigate to the company's login screen.
 */
public class ProfileCompanySelf extends Fragment {
    MainActivity mainActivity;
    NavController navController;
    private FirebaseAuth mAuth;
    private ImageView editButtonC;
    private AppCompatButton saveButtonC;
    private String newAboutC = "";
    private EditText editTextAbout;
    FirebaseUser user;
    TextView textAboutC, profileEditTxtC, companyName;
    CircleImageView profileImgC;
    StorageReference storageReference;
    String companyId;
    int SELECT_PICTURE = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /**
     * Manages the company's self-profile, allowing them to view and edit their information,
     * including the company name, summary, and profile image.
     * @return the inflated view for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_company_self, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        LinearLayout logoutLinear = view.findViewById(R.id.LogoutLinearCompany);

        logoutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        return view;
    }
    /**
     * Sets up the view components and handles user interaction for managing the company's self-profile.
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        companyId = user.getUid();
        textAboutC = view.findViewById(R.id.txtDescriptionCompProfile);
        profileEditTxtC = view.findViewById(R.id.edit_profiletxtcompany);
        profileImgC = view.findViewById(R.id.profileImgCompanyP);
        companyName = view.findViewById(R.id.txtCompanyNameP);
        navController = Navigation.findNavController(requireView());

        editButtonC = view.findViewById(R.id.aboutEditCompany);
        saveButtonC = view.findViewById(R.id.btnSaveCompany);

        loadUserInfo();
        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotComp();

        profileEditTxtC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryImageRegister();
            }
        });

        editButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButtonC.setVisibility(View.GONE);
                saveButtonC.setVisibility(View.VISIBLE);

                if (editTextAbout == null) {
                    editTextAbout = new EditText(requireActivity());
                }

                editTextAbout.setText(textAboutC.getText());

                //remplazar el texview con el editText
                ViewGroup parent = (ViewGroup) textAboutC.getParent();
                int index = parent.indexOfChild(textAboutC);
                parent.removeView(textAboutC);
                parent.addView(editTextAbout, index);

                editTextAbout.requestFocus();
                editTextAbout.selectAll();

                //actualizar newAbout
                newAboutC = textAboutC.getText().toString().trim();
            }
        });

        saveButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateAbout = editTextAbout.getText().toString().trim();

                if (!updateAbout.isEmpty()) {

                    if (!updateAbout.equals(newAboutC)) {
                        newAboutC = updateAbout;

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Company").document(companyId)
                                .update("summary", newAboutC)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Log.d("UPDATE", "About actualizado");

                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("UPDATE", "No ha sido posible actualizar el about", e);
                                });
                    }

                    //volver a un textView
                    ViewGroup parent = (ViewGroup) editTextAbout.getParent();
                    int index = parent.indexOfChild(editTextAbout);
                    parent.removeView(editTextAbout);
                    parent.addView(textAboutC, index);

                    editButtonC.setVisibility(View.VISIBLE);
                    saveButtonC.setVisibility(View.GONE);

                    textAboutC.setText(newAboutC);

                }
            }
        });
    }
    /**
     * Loads the user's information, including company name, summary, and profile image.
     */
    public void loadUserInfo() {
        if (user != null && getView() != null) {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Company").document(companyId);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (getView() != null) {
                        companyName.setText(documentSnapshot.getString("name"));
                        textAboutC.setText(documentSnapshot.getString("summary"));
                        String imageprofileURL = documentSnapshot.getString("profileImage");
                        Context context = getView().getContext();
                        if (imageprofileURL != null && !imageprofileURL.isEmpty()) {
                            Uri uriImagep = Uri.parse(imageprofileURL);

                            Glide.with(context)
                                    .load(uriImagep)
                                    .into(profileImgC);
                        } else {
                            Glide.with(context)
                                    .load(R.drawable.profile_image_defaut)
                                    .into(profileImgC);
                        }
                    }
                }
            });
        }
    }
    /**
     * Opens the gallery for selecting a new profile image.
     */
    private void selectGalleryImageRegister() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

    }
    /**
     * Handles the result from selecting an image from the gallery.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    Glide.with(this).load(selectedImageUri)
                            .into(profileImgC);
                    uploadImage(selectedImageUri);
                }
            }
        }
    }
    /**
     * Uploads the selected image to Firebase Storage and updates the profile image URL in Firestore.
     * @param uri The URI of the selected image.
     */
    private void uploadImage(Uri uri) {
        if (user != null) {
            if (uri != null) {
                StorageReference ref = storageReference.child("userprofileimages/" + companyId + "/" + UUID.randomUUID().toString());

                ref.putFile(uri)
                        .continueWithTask(task ->
                                task.getResult().getStorage().getDownloadUrl())
                        .addOnSuccessListener(url -> linkImagetoUser(url.toString()));
            }
        }
    }
    /**
     * Updates the profile image URL in Firestore.
     * @param imageUrl The URL of the uploaded profile image.
     */
    private void linkImagetoUser(String imageUrl) {
        if (user != null) {
            FirebaseFirestore.getInstance().collection("User").document(companyId)
                    .update("profileImage", imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
            FirebaseFirestore.getInstance().collection("Company").document(companyId)
                    .update("profileImage", imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override

                        public void onSuccess(Void unused) {
                        }
                    });
        }
    }
    /**
     * Displays a dialog to confirm the logout action. If the user confirms, it signs out the current user
     * and navigates to the company login screen.
     */
        private void showLogoutDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(R.layout.dialog_logout)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            navController.navigate(R.id.companyLogin);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
