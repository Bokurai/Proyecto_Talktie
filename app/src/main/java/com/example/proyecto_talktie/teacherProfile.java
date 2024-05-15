package com.example.proyecto_talktie;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;


public class teacherProfile extends Fragment {
    NavController navController;
    private ImageView backArrow, imageTeacher, editPostion, editEmail;
    private TextView txtName, txtPosition, txtEmail, editImage;
    private EditText editTextPosition, editTextEmail;
    private AppCompatButton savePosition, saveEmail;
    private String newPosition = "";
    private  String newEmail = "";
    RecyclerView recyclerView;
    StorageReference storageReference;
    String teacherId;
    int SELECT_PICTURE = 200;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        TeacherViewModel teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

        editPostion = view.findViewById(R.id.positionEditS);
        editEmail = view.findViewById(R.id.emailEditT);

        savePosition = view.findViewById(R.id.btnSavePosition);
        saveEmail = view.findViewById(R.id.btnSaveEmail);


        backArrow = view.findViewById(R.id.img_return);
        imageTeacher = view.findViewById(R.id.profileImgTeacher);
        txtName = view.findViewById(R.id.txtTeacherName);
        txtPosition = view.findViewById(R.id.txtPositionTeacher);
        txtEmail = view.findViewById(R.id.txtEmailTeacher);
        editImage = view.findViewById(R.id.edit_profiletxtT);
        recyclerView = view.findViewById(R.id.recommendationTRecyclerView);

        teacherViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {

                teacherId = teacher.getTeacherId();

                txtName.setText(teacher.getName());
                txtPosition.setText(teacher.getPosition());
                txtEmail.setText(teacher.getEmail());

                String imageProfile = teacher.getProfileImage();
                Log.d("TACG", "la imagen del profe es: " + imageProfile);

                Context context = getView().getContext();
                if (imageProfile != null && !imageProfile.isEmpty()) {
                    Uri uriImage = Uri.parse(imageProfile);
                    Glide.with(context)
                            .load(uriImage)
                            .into(imageTeacher);
                } else {
                    Glide.with(context)
                            .load(R.drawable.teacher_default)
                            .into(imageTeacher);
                }


                editPostion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editPostion.setVisibility(View.GONE);
                        savePosition.setVisibility(View.VISIBLE);

                        if (editTextPosition == null) {
                            editTextPosition = new EditText(requireActivity());
                        }

                        editTextPosition.setText(teacher.getPosition());

                        //remplazar el texview con el editText
                        ViewGroup parent = (ViewGroup) txtPosition.getParent();
                        int index = parent.indexOfChild(txtPosition);
                        parent.removeView(txtPosition);
                        parent.addView(editTextPosition, index);

                        editTextPosition.requestFocus();
                        editTextPosition.selectAll();

                        //actualizar newAbout
                        newPosition = txtPosition.getText().toString().trim();

                    }
                });

                savePosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updatePosition = editTextPosition.getText().toString().trim();

                        if (!updatePosition.isEmpty()) {
                            if (!updatePosition.equals(newPosition)) {
                                newPosition = updatePosition;

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Teacher").document(teacherId)
                                        .update("position", newPosition)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("UPDATE", "position actualizado");
                                            }
                                        });
                            }

                            //volver a un textView
                            ViewGroup parent = (ViewGroup) editTextPosition.getParent();
                            int index = parent.indexOfChild(editTextPosition);
                            parent.removeView(editTextPosition);
                            parent.addView(txtPosition, index);

                            editPostion.setVisibility(View.VISIBLE);
                            savePosition.setVisibility(View.GONE);

                            txtPosition.setText(newPosition);
                        }
                    }
                });

                editEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editEmail.setVisibility(View.GONE);
                        saveEmail.setVisibility(View.VISIBLE);

                        if (editTextEmail == null) {
                            editTextEmail = new EditText(requireActivity());
                            editTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }

                        editTextEmail.setText(teacher.getEmail());

                        //remplazar el texview con el editText
                        ViewGroup parent = (ViewGroup) txtEmail.getParent();
                        int index = parent.indexOfChild(txtEmail);
                        parent.removeView(txtEmail);
                        parent.addView(editTextEmail, index);

                        editTextEmail.requestFocus();
                        editTextEmail.selectAll();

                        //actualizar newAbout
                        newEmail = txtEmail.getText().toString().trim();

                    }
                });

                saveEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updateEmail = editTextEmail.getText().toString().trim();

                        if (!updateEmail.isEmpty()) {
                            if (!updateEmail.equals(newEmail)) {
                                newEmail = updateEmail;

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Teacher").document(teacherId)
                                        .update("email", newEmail)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("UPDATE", "email actualizado");
                                            }
                                        });
                            }

                            //volver a un textView
                            ViewGroup parent = (ViewGroup) editTextEmail.getParent();
                            int index = parent.indexOfChild(editTextEmail);
                            parent.removeView(editTextEmail);
                            parent.addView(txtEmail, index);

                            editEmail.setVisibility(View.VISIBLE);
                            saveEmail.setVisibility(View.GONE);

                            txtEmail.setText(newEmail);
                        }
                    }
                });

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryImageRegister();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goSchoolProfile);
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
                            .into(imageTeacher);
                    uploadImage(selectedImageUri);
                }
            }
        }
    }

    private void uploadImage(Uri uri) {
        if (teacherId != null) {
            if (uri != null) {
                StorageReference ref = storageReference.child("teachersprofileimages/" + teacherId + "/" + UUID.randomUUID().toString());

                ref.putFile(uri)
                        .continueWithTask(task ->
                                task.getResult().getStorage().getDownloadUrl())
                        .addOnSuccessListener(url -> linkImagetoUser(url.toString()));
            }
        }
    }

    private void linkImagetoUser(String imageUrl){
        if (teacherId!= null){
            FirebaseFirestore.getInstance().collection("User").document(teacherId)
                    .update("profileImage",imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
            FirebaseFirestore.getInstance().collection("Teacher").document(teacherId)
                    .update("profileImage",imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override

                        public void onSuccess(Void unused) {
                        }
                    });
        }

    }


}