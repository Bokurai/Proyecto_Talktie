package com.example.proyecto_talktie.view.student_fragments;

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

import com.example.proyecto_talktie.R;
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
import java.util.List;
import java.util.Map;
/**
 * Fragment where a student can send a request for an offer by uploading a document, writing a letter,
 * and providing their experience. This fragment also handles the upload of the document to Firebase Storage
 * and the addition of applicant data to the offer in Firestore.
 */

public class sendRequest2 extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private EditText letterEditText;
    private EditText experienceEditText;
    private TextView uploadCV;
    private Button sendButton;
    private NavController navController;
    private String documentUrl;
    private String offerId;
    private static final int SELECT_DOCUMENT = 1;
    private FirebaseUser user;
    private int numApplicants = 0;

    /**
     * Inflates the layout for this fragment and initializes its views.
     * It sets up click listeners for the experience EditText and the uploadCV TextView.
     * It also retrieves the offerId from the fragment arguments and sets up the sendButton click listener.
     * Additionally, it initializes the number of applicants for the offer if the offerId is not null.
     */
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

        uploadCV = rootView.findViewById(R.id.txtUpload);
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
                if (offerId != null) {
                    addApplicantToOffer(offerId, letter, experience);
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
        //Add OnClickListener to the button for upload the resume
        uploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryDocument();
            }
        });
        if (offerId != null) {
            initNumApplicants(offerId);
        }

        return rootView;
    }
    /**
     * Opens the file picker to select a document from the gallery.
     */
    private void selectGalleryDocument() {
        Intent intent = new Intent();
        intent.setType("*/*"); // Tipo de archivo
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), SELECT_DOCUMENT);
    }
    /**
     * Handles the result of the file picker activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == SELECT_DOCUMENT && data != null && data.getData() != null) {
            Uri uri = data.getData();
            uploadDocument(uri);
        }
    }
    /**
     * Uploads a document to Firebase Storage.
     * If the document URI is not null, it generates a unique reference for the document in Firebase Storage,
     * uploads the document, and updates the documentUrl variable upon successful upload.
     * It also adds the document to the offer in the Firestore database.
     *
     * @param documentUri The URI of the document to be uploaded.
     */
    private void uploadDocument(Uri documentUri) {
        if (documentUri != null) {
            // Generate unique reference for documents in Firebase Storage
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
    /**
     * Adds the document URL to the offer document in Firestore.
     */
    private void addDocumentToOffer(String offerId, String documentUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference offerRef = db.collection("Offer").document(offerId).collection("Applicants").document(userId);

        Map<String, Object> docData = new HashMap<>();
        docData.put("documentUrl", documentUrl);

        // Update the documentUrl field of the offer with the document's URL
        offerRef.set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Document uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error uploading document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Adds an applicant to a specific offer in the Firestore database.
     * This method retrieves the current user's ID, retrieves the corresponding document from the "Student" collection,
     * and then adds the applicant's data (letter, experience, documentUrl) to the "Applicants" subcollection
     * of the specified offer.
     *
     * @param offerId    The ID of the offer to which the applicant is being added.
     * @param letter     The letter submitted by the applicant.
     * @param experience The experience information provided by the applicant.
     */
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
    /**
     * Initializes the number of applicants for a specific offer by querying the Firestore database.
     * This method retrieves the documents from the "Applicants" subcollection of the specified offer
     * and updates the numApplicants variable accordingly.
     * @param offerId The ID of the offer for which the number of applicants is to be initialized.
     */
    private void initNumApplicants(String offerId) {
        db.collection("Offer").document(offerId).collection("Applicants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> applicantsDocuments = queryDocumentSnapshots.getDocuments();
                    numApplicants = applicantsDocuments.size();
                })
                .addOnFailureListener(e -> {
                    Log.e("sendRequest2", "Error al obtener la cantidad de aplicantes: " + e.getMessage());
                });
    }
}