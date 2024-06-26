package com.example.proyecto_talktie.view.student_fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.school.Recommendation;
import com.example.proyecto_talktie.viewmodel.StudentViewModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragment representing the student's profile screen, where the student's information can be edited and the recommendations made to the student can be visualized.
 */
public class ProfileStudent extends Fragment {
    //Initialization of the student's view-model
    private StudentViewModel studentViewModel;
    private RecyclerView recyclerView;
    private RecommendAdapter adapter;
    private FirebaseAuth mAuth;
    private ImageView editButton;
    private AppCompatButton saveButton;
    private String newAbout = "";
    private EditText editTextAbout;
    FirebaseUser user;
    TextView textAbout, profileEditTxt, studentName, txtRecommendation;
    CircleImageView profileImg;
    StorageReference storageReference;
    Storage storage;
    Uri uri;
    NavController navController;
    String studentId;
    int SELECT_PICTURE = 200;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_student, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Initialize the adapter and define the recyclerView
        adapter = new RecommendAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.applicantsRecyclerView);
        recyclerView.setAdapter(adapter);


        storageReference = FirebaseStorage.getInstance().getReference();

        //LOG OUT
        LinearLayout logoutLinear = view.findViewById(R.id.LogoutLinear);

        //Configures a click listener for the LinearLayout
        logoutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displays the logout confirmation dialog
                showLogoutDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        studentId = user.getUid();
        textAbout = view.findViewById(R.id.txtDescription);
        txtRecommendation = view.findViewById(R.id.textRecommend);
        profileEditTxt = view.findViewById(R.id.edit_profiletxt);
        profileImg = view.findViewById(R.id.profileImgStudent);
        studentName = view.findViewById(R.id.txtStudentName);
        navController = Navigation.findNavController(view);

        editButton = view.findViewById(R.id.aboutEdit);
        saveButton = view.findViewById(R.id.btnSave);

        //Initialize the viewModel
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        /**
         * Obtains student data and updates the interface
         */
        studentViewModel.getStudentData(studentId).observe(getViewLifecycleOwner(), student -> {
            studentName.setText(student.getName());
            textAbout.setText(student.getAbout());

            //If the student's profile picture is null, the default one is set
            String imageProfileURL = student.getProfileImage();
            Context context = getView().getContext();
            if (imageProfileURL != null && !imageProfileURL.isEmpty()) {
                Uri uriImagep = Uri.parse(imageProfileURL);

                Glide.with(context)
                        .load(uriImagep)
                        .into(profileImg);
            } else {
                Glide.with(context)
                        .load(R.drawable.profile_image_defaut)
                        .into(profileImg);
            }
        });

        /**
         * Gets the list of student recommendations with information from teachers
         */
        studentViewModel.getRecommendationLiveData(studentId).observe(getViewLifecycleOwner(), recommendations -> {
            Log.d("Recomendaciones", "Acceder livedata recomendaciones " + studentId);
            if (recommendations != null) {
                txtRecommendation.setVisibility(View.VISIBLE);
            }
            //Update data on exist adapter
            adapter.setRecommendationList(recommendations);
        });

        profileEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryImageRegister();
            }
        });

        //Button to edit the user's about
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                if (editTextAbout == null) {
                    editTextAbout = new EditText(requireActivity());
                }

                editTextAbout.setText(textAbout.getText());

                //Replace texview with editText
                ViewGroup parent = (ViewGroup) textAbout.getParent();
                int index = parent.indexOfChild(textAbout);
                parent.removeView(textAbout);
                parent.addView(editTextAbout, index);

                editTextAbout.requestFocus();
                editTextAbout.selectAll();

                //Update newAbout
                newAbout = textAbout.getText().toString().trim();
            }
        });

        //Button to save the new user's about
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateAbout = editTextAbout.getText().toString().trim();

                if (!updateAbout.isEmpty()) {
                    //Check if the about has changed
                    if (!updateAbout.equals(newAbout)) {
                        newAbout = updateAbout;

                        //Update about changes
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Student").document(studentId)
                                .update("about", newAbout)
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

                    //Back to a textView
                    ViewGroup parent = (ViewGroup) editTextAbout.getParent();
                    int index = parent.indexOfChild(editTextAbout);
                    parent.removeView(editTextAbout);
                    parent.addView(textAbout, index);

                    editButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);

                    textAbout.setText(newAbout);

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
                            .into(profileImg);
                    uploadImage(selectedImageUri);
                }
            }
        }
    }

    private void uploadImage(Uri uri) {
        if (user != null) {
            if (uri != null) {
                StorageReference ref = storageReference.child("userprofileimages/" + studentId + "/" + UUID.randomUUID().toString());

                ref.putFile(uri)
                        .continueWithTask(task ->
                                task.getResult().getStorage().getDownloadUrl())
                        .addOnSuccessListener(url -> linkImagetoUser(url.toString()));
            }
        }
    }

    private void linkImagetoUser(String imageUrl){
        if (user!= null){
            FirebaseFirestore.getInstance().collection("User").document(studentId)
                    .update("profileImage",imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
            FirebaseFirestore.getInstance().collection("Student").document(studentId)
                    .update("profileImage",imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override

                        public void onSuccess(Void unused) {
                        }
                    });
        }


    }

    /**
     * Class representing the adaptor of the recommendations made to a student
     */
    class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendationViewHolder> {
        private List<Recommendation> recommendationList;

        public RecommendAdapter(List<Recommendation> recommendationList) {
            this.recommendationList = recommendationList;
        }

        @NonNull
        @Override
        public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecommendationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommendations, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
            Recommendation recommendation = recommendationList.get(position);

            //change image and teacher name by a search through their id.
            holder.nameTeacher.setText(recommendation.getTeacher().getName());
            holder.textRecommendation.setText(recommendation.getRecommendationText());

            String profileImage = recommendation.getTeacher().getProfileImage();

            //Load the teacher image, in case of being null the default one is set.
            Context context = getView().getContext();
            if (profileImage != null && !profileImage.isEmpty()) {
                Uri uriImagep = Uri.parse(profileImage);

                Glide.with(context)
                        .load(uriImagep)
                        .into(holder.imageTeacher);
            } else {
                Glide.with(context)
                        .load(R.drawable.teacher_default)
                        .into(holder.imageTeacher);
            }

            //Hide teacher information in case it is empty
            boolean emailIsEmpty = isNullOrEmpty(recommendation.getTeacher().getEmail());
            boolean positionIsEmpty = isNullOrEmpty(recommendation.getTeacher().getPosition());

            if (emailIsEmpty || positionIsEmpty) {
                holder.separator.setVisibility(View.GONE);
            } else {
                holder.separator.setVisibility(View.VISIBLE);
            }

            if (!emailIsEmpty) {
                holder.email.setText(recommendation.getTeacher().getEmail());
                holder.email.setVisibility(View.VISIBLE);
            } else {
                holder.email.setVisibility(View.GONE);
            }

            if (!positionIsEmpty) {
                holder.position.setText(recommendation.getTeacher().getPosition());
                holder.position.setVisibility(View.VISIBLE);
            } else {
                holder.position.setVisibility(View.GONE);
            }

        }

        private boolean isNullOrEmpty(String string) {
            return string == null || string.isEmpty();
        }


        @Override
        public int getItemCount() {
            //Number of recommendations to display
            return recommendationList.size();
        }

        /**
         * Method updating the list of recommendations
         * @param recommendations list of recommendations
         */
        public void setRecommendationList(List<Recommendation> recommendations) {
            this.recommendationList = recommendations;
            notifyDataSetChanged();
        }

        /**
         * Class extracting from the view holder for later use in the adapter
         */
        class RecommendationViewHolder extends RecyclerView.ViewHolder {
            ImageView imageTeacher;
            TextView nameTeacher, textRecommendation, position, email, separator;
            RecommendationViewHolder(@NonNull View itemView) {
                super(itemView);

                imageTeacher = itemView.findViewById(R.id.imageTeacherRecommend);
                nameTeacher = itemView.findViewById(R.id.nameTeacherRecommend);
                textRecommendation = itemView.findViewById(R.id.textTeacherRecommend);
                position = itemView.findViewById(R.id.position_teacher);
                email = itemView.findViewById(R.id.email_teacher);
                separator = itemView.findViewById(R.id.separatorT);
            }
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
                        navController.navigate(R.id.login);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada, simplemente cierra el diálogo
                        dialog.dismiss();
                    }
                });

        //Displays the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}