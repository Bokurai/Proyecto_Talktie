package com.example.proyecto_talktie;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                navController.navigate(R.id.action_goStudentSearchView);
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

            holder.name.setText(offerObject.getName());

            if (offerObject.getTags().size() > 0) {
                holder.tag1.setText(offerObject.getTags().get(0));
            } else {
                holder.tag1.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
            }

            if (offerObject.getTags().size() > 1) {
                holder.tag2.setText(offerObject.getTags().get(1));
            } else {
                holder.tag2.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
            }

            if (offerObject.getTags().size() > 2) {
                holder.tag3.setText(offerObject.getTags().get(2));
            } else {
                holder.tag3.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
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

            //Faltan campos
            TextView name, tag1, tag2, tag3;

            public OffersViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.offerNamePC);
                tag1 = itemView.findViewById(R.id.btnTagOnePC);
                tag2 = itemView.findViewById(R.id.btnTagTwoPC);
                tag3 = itemView.findViewById(R.id.btnTagThreePC);
            }
        }
    }

}