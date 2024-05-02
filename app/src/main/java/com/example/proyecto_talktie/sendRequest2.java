package com.example.proyecto_talktie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class sendRequest2 extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText letterEditText;
    private EditText experienceEditText;
    private ImageButton cvButton;
    private Button sendButton;
    private NavController navController;


    private String offerId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_request2, container, false);
        // Inflar el layout para este fragmento
        LinearLayout linearLayout = rootView.findViewById(R.id.letter);
        letterEditText = linearLayout.findViewById(R.id.txtWriteyourlett);

        experienceEditText = rootView.findViewById(R.id.txtexperience);
        cvButton = rootView.findViewById(R.id.uploadCV);
        sendButton = rootView.findViewById(R.id.btnSendNow);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);


        // Obtener el ID de la oferta de los argumentos del fragmento (puedes pasar este ID al abrir el fragmento)
        Bundle args = getArguments();
        if (args != null) {
            offerId = args.getString("offerId");
        }

        // Agregar un OnClickListener al botón de enviar
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de la aplicación
                String letter = letterEditText.getText().toString();
                String experience = experienceEditText.getText().toString();
                // Verificar si el ID de la oferta está disponible
                if (offerId != null) {
                    // Añadir los datos del usuario a la subcolección "applicants" dentro del documento de la oferta correspondiente
                    addApplicantToOffer(offerId, letter, experience);

                    // Mostrar mensaje de éxito como un popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Correctly applied to the offer. We will contact you within 23-48 hours.\n\nGood luck! :)")

                            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    navController.navigate(R.id.action_goOffer);
                                }
                            });
                    // Crear y mostrar el dialogo
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getContext(), "No se pudo obtener el ID de la oferta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void addApplicantToOffer(String offerId, String letter, String experience) {
        // Obtener la referencia al documento de la oferta
        DocumentReference offerRef = db.collection("Offer").document(offerId);

        // Crear un objeto Map para representar los datos del solicitante
        Map<String, Object> applicantData = new HashMap<>();
        applicantData.put("letter", letter);
        applicantData.put("experience", experience);

        // Añadir los datos del solicitante a la subcolección "applicants" dentro del documento de la oferta
        offerRef.collection("Applicants")
                .add(applicantData)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al añadir datos del solicitante: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}