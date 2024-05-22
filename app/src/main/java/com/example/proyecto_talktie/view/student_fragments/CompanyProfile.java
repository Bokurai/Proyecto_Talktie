package com.example.proyecto_talktie.view.student_fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.Business;
import com.example.proyecto_talktie.models.company.OfferObject;
import com.example.proyecto_talktie.viewmodel.OfferViewModel;
import com.example.proyecto_talktie.viewmodel.StudentSearchViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CompanyProfile extends Fragment {

    private TextView nameCompany, location, description, type, phone, email, website, headquarters, txtVacancies;
    private ImageView imageReturn;
    private AppCompatButton follow;
    private RecyclerView recyclerView;
    private StudentSearchViewModel searchViewModel;
    private NavController navController;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private OffersAdapter adapter;
    private OfferViewModel offerViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_profile, container, false);

        adapter = new OffersAdapter(new ArrayList<>());

        recyclerView = view.findViewById(R.id.vacanciesRecyclerView);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewModel = new ViewModelProvider(requireActivity()).get(StudentSearchViewModel.class);
        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        navController = Navigation.findNavController(view);

        nameCompany = view.findViewById(R.id.txtNameCompany);
        location = view.findViewById(R.id.txtLocationCompany);
        description = view.findViewById(R.id.txtCompanyDescription);
        type = view.findViewById(R.id.typeCompanyTextView);
        phone = view.findViewById(R.id.phoneTextView);
        email = view.findViewById(R.id.emailTextView);
        website = view.findViewById(R.id.websiteTextView);
        headquarters = view.findViewById(R.id.txtHeadquartersCompany);
        txtVacancies = view.findViewById(R.id.txtVacancies);

        imageReturn = view.findViewById(R.id.img_return);
        follow = view.findViewById(R.id.btnFollowCompany);


        imageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        searchViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Business>() {
            @Override
            public void onChanged(Business business) {

                //Obtener id del usuario
                String userId = currentUser.getUid();

                //Verificar si el usuario sigue a la empresa
                if (business.followers != null && business.followers.containsKey(userId)) {
                    updateFollowButton(true);
                } else {
                    updateFollowButton(false);
                }

                //Actualizar la interfaz con la info de cada empresa
                nameCompany.setText(business.getName());
                location.setText(business.getAddress());
                description.setText(business.getSummary());
                type.setText(business.getTypeCompany());
                phone.setText(business.getPhone());
                email.setText(business.getEmail());
                website.setText(business.getWebsite());
                headquarters.setText(business.getHeadquarters());

                if (business.getOffers() == null) {
                    txtVacancies.setVisibility(View.GONE);
                }

                //Log.d("TAG", "Id de la compañía: " + business.getId());
                offerViewModel.getOffersCompany(business.getCompanyId()).observe(getViewLifecycleOwner(), offerObjects -> {
                    adapter.setOfferObjectList(offerObjects);
                });



                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      boolean isFollowing = business.followers != null && business.followers.containsKey(userId);

                      if (isFollowing) {
                          business.followers.remove(userId);
                          removeFollowed(userId, business.getCompanyId());
                      } else {
                          business.followers.put(userId, true);
                          addFollowed(userId, business.getCompanyId());
                      }

                        //Actualización del botón
                        updateFollowButton(!isFollowing);

                        //guardar cambios
                        FirebaseFirestore.getInstance().collection("Company")
                                .document(business.getCompanyId())
                                .update("followers", business.followers)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG","Actualización con éxito");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG","Error de actualización");
                                    }
                                });
                    }
                });
            }
        });
    }

    public void removeFollowed(String userId, String companyId) {

        FirebaseFirestore.getInstance().collection("Student")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            //Lista follower
                            List<String> followed = (List<String>) documentSnapshot.get("followed");

                            followed.remove(companyId);

                            //Actualizar
                            FirebaseFirestore.getInstance().collection("Student")
                                    .document(userId)
                                    .update("followed", followed)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("TAG", "Se eliminó la empresa de la lista");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "Error al eliminar la empresa");
                                        }
                                    });
                        }
                    }
                });
    }

    public void addFollowed(String userId, String companyId) {

        FirebaseFirestore.getInstance().collection("Student")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            List<String> followed = (List<String>) documentSnapshot.get("followed");

                            if (followed == null) {
                                followed = new ArrayList<>(); // Crear una nueva lista
                            }

                            if (!followed.contains(companyId)) {
                                followed.add(companyId);

                                //Actualizar
                                FirebaseFirestore.getInstance().collection("Student")
                                        .document(userId)
                                        .update("followed", followed)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("TAG", "Se agrego la empresa a la lista");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG", "Error al agregar la empresa a la lista");
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    public void updateFollowButton(boolean isFollowing) {
        if (isFollowing) {
            follow.setText("Following");
            follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0);
            follow.setBackgroundTintList(getResources().getColorStateList(R.color.light_green_F));
        } else {
            follow.setText("Follow");
            follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_grid, 0, 0, 0);
            follow.setBackgroundTintList(getResources().getColorStateList(R.color.light_green_900));
        }
    }

    class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {
        private List<OfferObject> offerObjectList;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        public OffersAdapter(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OffersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offer_company_profile,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {

            OfferObject offerObject = offerObjectList.get(position);

            int colorLightGreen900 = ContextCompat.getColor(holder.itemView.getContext(), R.color.light_green_900);
            int coloramarillo = ContextCompat.getColor(holder.itemView.getContext(), R.color.amber_700);


            holder.name.setText(offerObject.getName());


            CollectionReference applicantsRef = db.collection("Offer").document(offerObject.getOfferId()).collection("Applicants");
            applicantsRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    holder.stateOffer.setTextColor(Color.WHITE);
                                    holder.stateOffer.setBackgroundColor(colorLightGreen900);
                                    holder.stateOffer.setText("Applied");
                                } else {
                                    holder.stateOffer.setTextColor(Color.WHITE);
                                    holder.stateOffer.setBackgroundColor(coloramarillo);
                                    holder.stateOffer.setText("Apply");
                                }
                            } else {
                                Log.e("OffersAdapter", "Error al verificar si el usuario está en la subcolección de aplicantes: ", task.getException());
                            }
                        }
                    });

            applicantsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int numApplicants = queryDocumentSnapshots.size();
                    holder.numApplicants.setText(String.valueOf(numApplicants));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("OffersAdapter", "Error al obtener la cantidad de aplicantes: " + e.getMessage());
                }
            });



            if(offerObject.getTags() != null) {
                if (!offerObject.getTags().get(0).isEmpty()) {
                    holder.tag1.setText(offerObject.getTags().get(0));
                } else {
                    holder.tag1.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (!offerObject.getTags().get(1).isEmpty()) {
                    holder.tag2.setText(offerObject.getTags().get(1));
                } else {
                    holder.tag2.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (!offerObject.getTags().get(2).isEmpty()) {
                    holder.tag3.setText(offerObject.getTags().get(2));
                } else {
                    holder.tag3.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }
            } else {
                holder.tag1.setVisibility(View.GONE);
                holder.tag2.setVisibility(View.GONE);
                holder.tag3.setVisibility(View.GONE);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offerViewModel.seleccionar(offerObject);
                    Log.d("TAG", "Oferta seleccionada " + offerObject.getName());
                    navController.navigate(R.id.action_goOfferDetailsFragment);
                }
            });

        }

        @Override
        public int getItemCount() {
            return offerObjectList.size();
        }

        /**
         *  Method updating the list of offers
         * @param offerObjectList list of offers
         */
        public void setOfferObjectList(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
            notifyDataSetChanged();
            Log.d("OffertsAdapter", "Cantidad de ofertas recibidas: " + offerObjectList.size());
        }

        class OffersViewHolder extends RecyclerView.ViewHolder {

            TextView name, tag1, tag2, tag3, numApplicants, stateOffer;

            public OffersViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.offerNamePC);
                tag1 = itemView.findViewById(R.id.btnTagOnePC);
                tag2 = itemView.findViewById(R.id.btnTagTwoPC);
                tag3 = itemView.findViewById(R.id.btnTagThreePC);
                numApplicants = itemView.findViewById(R.id.numApplicants);
                stateOffer = itemView.findViewById(R.id.stateOffer);
            }
        }
    }

}