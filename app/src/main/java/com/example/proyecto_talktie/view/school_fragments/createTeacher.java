package com.example.proyecto_talktie.view.school_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.school.Teacher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class createTeacher extends Fragment {

    private AppCompatButton save;
    private EditText nameT, positionT, emailT;
   private ImageView backArrow;
   NavController navController;
   FirebaseAuth mAuth = FirebaseAuth.getInstance();
   String userId = mAuth.getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backArrow = view.findViewById(R.id.img_back);
        navController = Navigation.findNavController(view);

        nameT = view.findViewById(R.id.etTeacherSchool);
        positionT = view.findViewById(R.id.etPositionTeacher);
        emailT = view.findViewById(R.id.etEmailTeacher);
        save = view.findViewById(R.id.btnSaveTeacher);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goSchoolProfile);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {

                    String name = nameT.getText().toString();
                    String email = emailT.getText().toString();
                    String position = positionT.getText().toString();

                    createTeacher(name, email, position, userId);
                }
            }
        });


    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(nameT.getText().toString())) {
            nameT.setError("Required.");
            valid = false;
        } else {
            nameT.setError(null);
        }

        return valid;
    }

    private void createTeacher(String name, String email, String position, String userId) {

        Teacher teacher = new Teacher(name, userId);

        if (!email.isEmpty()) {
            teacher.setEmail(email);
        }

        if (!position.isEmpty()) {
            teacher.setPosition(position);
        }

        FirebaseFirestore.getInstance().collection("Teacher")
                .add(teacher)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String teacherId = documentReference.getId();
                        teacher.setTeacherId(teacherId);

                       documentReference.set(teacher)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(getContext(), "Teacher created", Toast.LENGTH_SHORT).show();
                                       navController.popBackStack();
                                       Log.d("ACTUALIZACION", "Profesor creado y actualizado con éxito");
                                   }
                               });
                    }
                });
    }

}