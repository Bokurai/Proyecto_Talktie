package com.example.proyecto_talktie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class sendRequest2 extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText letterEditText;
    private EditText experienceEditText;
    private ImageButton uploadCV;
    private Button sendButton;
    private NavController navController;
    private String documentUrl;
    private String offerId;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private static final int SELECT_DOCUMENT = 1;
    private FirebaseUser user;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_request2, container, false);
        LinearLayout linearLayout = rootView.findViewById(R.id.letter);
        letterEditText = linearLayout.findViewById(R.id.txtWriteyourlett);

        experienceEditText = rootView.findViewById(R.id.txtexperience);
        experienceEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el texto actual coincide con el texto de ejemplo
                if (experienceEditText.getText().toString().equals(getString(R.string.msg_write_your_lett))) {
                    // Limpiar el texto del EditText
                    experienceEditText.setText("");
                }
            }
        });
        uploadCV = rootView.findViewById(R.id.uploadCV);
        sendButton = rootView.findViewById(R.id.btnSendNow);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Bundle args = getArguments();
        if (args != null) {
            offerId = args.getString("offerId");
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getContext(), "No se pudo obtener el ID de la oferta", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Agregar un OnClickListener al botón de subir CV
        uploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryDocument();
            }
        });

        return rootView;
    }

    private void selectGalleryDocument() {
        Intent intent = new Intent();
        intent.setType("*/*"); // Tipo de archivo
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), SELECT_DOCUMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == SELECT_DOCUMENT && data != null && data.getData() != null) {
            Uri uri = data.getData();
            uploadDocument(uri);
        }
    }

    private void uploadDocument(Uri documentUri) {
        if (documentUri != null) {
            // Generar una referencia única para el documento en Firebase Storage
            String filename = offerId + "_" + System.currentTimeMillis();
            StorageReference documentRef = storageReference.child("documents/" + filename);
            documentRef.putFile(documentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            documentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    documentUrl = uri.toString(); // Actualizar la variable de instancia documentUrl
                                    addDocumentToOffer(offerId, documentUrl);
                                    Log.d("tag", "esta es la url" + documentUrl);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error al obtener la URL del documento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al subir el documento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void addDocumentToOffer(String offerId, String documentUrl) {
        DocumentReference offerRef = db.collection("Offer").document(offerId);

        // Actualizar el campo de documentoUrl de la oferta con la URL del documento
        offerRef.update("documentUrl", documentUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Documento subido exitosamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al subir el documento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addApplicantToOffer(String offerId, String letter, String experience) {
        CollectionReference studentCollectionRef = db.collection("Student");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        studentCollectionRef.whereEqualTo("studentId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Obtener el primer documento (asumiendo que solo hay uno)
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String studentId = documentSnapshot.getId();
                            DocumentReference offerRef = db.collection("Offer").document(offerId);

                            Map<String, Object> applicantData = new HashMap<>();
                            applicantData.put("letter", letter);
                            applicantData.put("experience", experience);
                            applicantData.put("documentUrl", documentUrl);

                            offerRef.collection("Applicants").document(studentId)
                                    .set(applicantData)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Error al añadir datos del solicitante: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "No se encontró ningún documento de Student para este usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al obtener datos de Student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}