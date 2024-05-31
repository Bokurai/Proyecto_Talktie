package com.example.proyecto_talktie.view.school_fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.view.adapters.TeachersAdapter;
import com.example.proyecto_talktie.viewmodel.SchoolViewModel;
import com.example.proyecto_talktie.viewmodel.TeacherViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Fragment representing the profile page of a school, containing information about the school,
 * teachers and the button that navigates to the creation of new teachers.
 */
public class schoolProfile extends Fragment {
    private FloatingActionButton fab;
    private SchoolViewModel schoolViewModel;
    private TeacherViewModel teacherViewModel;
    private RecyclerView recyclerView;
    private ImageView imageSchool, editButton;
    private EditText editTextAbout;
    private FirebaseAuth mAuth;
    private String newSummary = "";
    private TeachersAdapter adapter;
    private AppCompatButton saveButton;
    private TextView name, summary, editImage;
    StorageReference storageReference;
    NavController navController;
    String schoolId;
    FirebaseUser user;
    int SELECT_PICTURE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        //LOG OUT
        LinearLayout logoutLinear = view.findViewById(R.id.LogoutLinear);

        //Set up a click listener for the LinearLayout
        logoutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Shows the confirmation dialog to log out
                showLogoutDialog();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize the ViewModel
        schoolViewModel = new ViewModelProvider(requireActivity()).get(SchoolViewModel.class);
        teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

        navController = Navigation.findNavController(view);
        //Initialize the adapter
        adapter = new TeachersAdapter(new ArrayList<>(), getContext(), navController, teacherViewModel);

        mAuth = FirebaseAuth.getInstance();
        schoolId = user.getUid();

        editButton = view.findViewById(R.id.aboutEditS);
        saveButton = view.findViewById(R.id.btnSaveS);
        fab = view.findViewById(R.id.newTeacher);

        imageSchool = view.findViewById(R.id.profileImgSchool);
        name = view.findViewById(R.id.txtSchoolName);
        summary = view.findViewById(R.id.txtDescriptionSchool);
        editImage = view.findViewById(R.id.edit_profiletxtS);
        recyclerView = view.findViewById(R.id.teachersRecyclerView);

        /**
         * Observe the information obtained from the school
         */
        schoolViewModel.getSchoolData(schoolId).observe(getViewLifecycleOwner(), school -> {

            //Update the interface with school data
            name.setText(school.getName());
            summary.setText(school.getSummary());

           String imageProfileURL = school.getProfileImage();

            //If the image is null, the default will be set
            Context context = getView().getContext();
            if (imageProfileURL != null && !imageProfileURL.isEmpty()) {
                Uri uriImagep = Uri.parse(imageProfileURL);
                Glide.with(context)
                        .load(uriImagep)
                        .into(imageSchool);
            } else {
                Glide.with(context)
                        .load(R.drawable.profile_image_defaut)
                        .into(imageSchool);
            }
        });

        /**
         * Observe the teachers belonging to the school
         */
        teacherViewModel.getTeachers().observe(getViewLifecycleOwner(), teachers -> {
            //Update the adapter
            adapter.setTeacherList(teachers);
        });

        recyclerView.setAdapter(adapter);

        //Button to create new teachers
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_goCreateTeacher);
            }
        });

        //Button to edit profile image
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryImageRegister();
            }
        });

        //Button to edit the summary
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                if (editTextAbout == null) {
                    editTextAbout = new EditText(requireActivity());
                }

                editTextAbout.setText(summary.getText());

                //Replace the texview with the editText
                ViewGroup parent = (ViewGroup) summary.getParent();
                int index = parent.indexOfChild(summary);
                parent.removeView(summary);
                parent.addView(editTextAbout, index);

                editTextAbout.requestFocus();
                editTextAbout.selectAll();

                //Replace the texview with the editText
                newSummary = summary.getText().toString().trim();
            }
        });

        //Button to save the new summary
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateAbout = editTextAbout.getText().toString().trim();

                if (!updateAbout.isEmpty()) {

                    //Check if the summary has changed
                    if (!updateAbout.equals(newSummary)) {
                        newSummary = updateAbout;

                        //Update to the new summary
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("School").document(schoolId)
                                .update("summary", newSummary)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Log.d("UPDATE", "Summary actualizado");

                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("UPDATE", "No ha sido posible actualizar el summary", e);
                                });
                    }

                    //Return to a textView
                    ViewGroup parent = (ViewGroup) editTextAbout.getParent();
                    int index = parent.indexOfChild(editTextAbout);
                    parent.removeView(editTextAbout);
                    parent.addView(summary, index);

                    editButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);

                    summary.setText(newSummary);

                }
            }
        });

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
                            .into(imageSchool);
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
                StorageReference ref = storageReference.child("userprofileimages/" + schoolId + "/" + UUID.randomUUID().toString());

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
    private void linkImagetoUser(String imageUrl){
        if (user!= null){
            FirebaseFirestore.getInstance().collection("User").document(schoolId)
                    .update("profileImage",imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
            FirebaseFirestore.getInstance().collection("School").document(schoolId)
                    .update("profileImage",imageUrl)
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
                        navController.navigate(R.id.schoolLogin);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        //Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}