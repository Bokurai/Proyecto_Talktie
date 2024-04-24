package com.example.proyecto_talktie;

import android.annotation.SuppressLint;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
        offerViewModel = new ViewModelProvider(this).get(OfferViewModel.class);

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

                        if(searchViewModel.isFollowing()) {
                            searchViewModel.setFollowing(false);
                            follow.setText("Follow");

                            business.followers.remove(currentUser.getUid());
                        } else {
                            searchViewModel.setFollowing(true);
                            follow.setText("Following");

                            business.followers.add(currentUser.getUid());
                        }
                    }
                });


            }
        });



    }

    class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {
        private List<OfferObject> offerObjectList;

        public OffersAdapter(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OffersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offers,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {

            OfferObject offerObject = offerObjectList.get(position);

            holder.name.setText(offerObject.getName());
            holder.companyName.setText(offerObject.getCompanyId());

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
            TextView name, companyName, tag1, tag2, tag3;

            public OffersViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.offerName);
                companyName = itemView.findViewById(R.id.companyName);
                tag1 = itemView.findViewById(R.id.btnTagOne);
                tag2 = itemView.findViewById(R.id.btnTagTwo);
                tag3 = itemView.findViewById(R.id.btnTagThree);
            }
        }
    }

    public void updateFollowers(Business business) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();




    }
}