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

        // Configura un listener de clic para el LinearLayout
        logoutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestra el diálogo de confirmación para cerrar sesión
                showLogoutDialog();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        schoolViewModel = new ViewModelProvider(requireActivity()).get(SchoolViewModel.class);
        teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

        navController = Navigation.findNavController(view);
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

        schoolViewModel.getSchoolData(schoolId).observe(getViewLifecycleOwner(), school -> {

            name.setText(school.getName());
            summary.setText(school.getSummary());

           String imageprofileURL = school.getProfileImage();

            Context context = getView().getContext();
            if (imageprofileURL != null && !imageprofileURL.isEmpty()) {
                Uri uriImagep = Uri.parse(imageprofileURL);
                Glide.with(context)
                        .load(uriImagep)
                        .into(imageSchool);
            } else {
                Glide.with(context)
                        .load(R.drawable.profile_image_defaut)
                        .into(imageSchool);
            }
        });


        teacherViewModel.getTeachers().observe(getViewLifecycleOwner(), teachers -> {
            adapter.setTeacherList(teachers);
        });

        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_goCreateTeacher);
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryImageRegister();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                if (editTextAbout == null) {
                    editTextAbout = new EditText(requireActivity());
                }

                editTextAbout.setText(summary.getText());

                //remplazar el texview con el editText
                ViewGroup parent = (ViewGroup) summary.getParent();
                int index = parent.indexOfChild(summary);
                parent.removeView(summary);
                parent.addView(editTextAbout, index);

                editTextAbout.requestFocus();
                editTextAbout.selectAll();

                //actualizar newAbout
                newSummary = summary.getText().toString().trim();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateAbout = editTextAbout.getText().toString().trim();

                if (!updateAbout.isEmpty()) {

                    if (!updateAbout.equals(newSummary)) {
                        newSummary = updateAbout;

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

                    //volver a un textView
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

    private void selectGalleryImageRegister() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

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

    //LOG OUT
    private void showLogoutDialog() {
        // Crea un AlertDialog con el diseño personalizado
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
                        // No hacer nada, simplemente cierra el diálogo
                        dialog.dismiss();
                    }
                });

        // Muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}