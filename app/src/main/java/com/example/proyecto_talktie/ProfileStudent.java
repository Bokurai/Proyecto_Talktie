package com.example.proyecto_talktie;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileStudent extends Fragment {
    //Initialization of the student's view-model
    private StudentViewModel studentViewModel;
    private RecyclerView recyclerView;
    private RecommendAdapter adapter;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    TextView textAbout, profileEditTxt, studentName, txtRecommendation;
    CircleImageView profileImg;
    StorageReference storageReference;
    NavController navController;
    Storage storage;
    Uri uri;
    String studentId;
    int SELECT_PICTURE = 200;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_student, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Create and set the recyclerview
        adapter = new RecommendAdapter(new ArrayList<>());

        recyclerView = view.findViewById(R.id.recommendRecyclerView);
        recyclerView.setAdapter(adapter);
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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        studentId = user.getUid();
        textAbout = view.findViewById(R.id.txtDescription);
        txtRecommendation = view.findViewById(R.id.textRecommend);
        profileEditTxt = view.findViewById(R.id.edit_profiletxt);
        navController = Navigation.findNavController(view);
        profileImg = view.findViewById(R.id.profileImgStudent);
        studentName = view.findViewById(R.id.txtStudentName);

        loadUserInfo();
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        /**
         * I get the livedata about from the view-model to show it to the user
         */
        studentViewModel.getAbout(studentId).observe(getViewLifecycleOwner(), aboutMe -> {
            Log.d("Recomendaciones", "Obtiene about " + studentId);
            textAbout.setText(aboutMe);
        });

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

    }

    public void loadUserInfo() {
        if (user != null && getView() != null) {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Student").document(studentId);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (getView() != null) {
                        studentName.setText(documentSnapshot.getString("name"));
                        String imageprofileURL = documentSnapshot.getString("profileImage");
                        Context context = getView().getContext();
                        if (imageprofileURL != null && !imageprofileURL.isEmpty()) {
                            Uri uriImagep = Uri.parse(imageprofileURL);

                            Glide.with(context)
                                    .load(uriImagep)
                                    .into(profileImg);
                        } else {
                            Glide.with(context)
                                    .load(R.drawable.profile_image_defaut)
                                    .into(profileImg);
                        }
                    }
                }
            });
        }
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
            holder.nameTeacher.setText(recommendation.getTeacherName());
            holder.imageTeacher.setImageResource(R.drawable.img_pngtreeavatar);

            holder.textRecommendation.setText(recommendation.getRecommendationText());

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
            TextView nameTeacher, textRecommendation;
            RecommendationViewHolder(@NonNull View itemView) {
                super(itemView);

                imageTeacher = itemView.findViewById(R.id.imageTeacherRecommend);
                nameTeacher = itemView.findViewById(R.id.nameTeacherRecommend);
                textRecommendation = itemView.findViewById(R.id.textTeacherRecommend);
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
                        // Aquí puedes poner la lógica para cerrar sesión
                        // Por ejemplo, limpiar datos de sesión, cerrar sesión en el servidor, etc.
                        // Luego, cierra la aplicación o realiza otras acciones necesarias
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

        // Muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}