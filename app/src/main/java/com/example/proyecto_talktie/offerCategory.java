package com.example.proyecto_talktie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class offerCategory extends Fragment {
    private TextView nameCategory;
    private OfferViewModel offerViewModel;
    private RecyclerView recyclerView;
    private NavController navController;
    private OffersCategory adapter;
    private String category = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_category, container, false);

        adapter = new OffersCategory(new ArrayList<>());

        recyclerView = view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        nameCategory = view.findViewById(R.id.nameCategories);

        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        category = offerViewModel.getCategory();

        nameCategory.setText(category);


        offerViewModel.getOfferCategory(category).observe(getViewLifecycleOwner(), offerObjects -> {
            adapter.setOfferObjectList(offerObjects);
        });

    }

    class OffersCategory extends RecyclerView.Adapter<OffersCategory.OfferViewHolder> {
        private List<OfferObject> offerObjectList;

        public OffersCategory(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OfferViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_offers,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
            OfferObject offerObject = offerObjectList.get(position);

            holder.name.setText(offerObject.getName());
            holder.companyName.setText(offerObject.getCompanyName());

            String imageProfile = offerObject.getCompanyImageUrl();
            Context context = getView().getContext();
            if (!imageProfile.equals("null")){
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.companyImage);

            } else {
                Glide.with(context)
                        .load(R.drawable.build_image_default)
                        .into(holder.companyImage);
            }



            if(offerObject.getTags() != null) {
                if (offerObject.getTags().size() > 0) {
                    holder.tag1.setText(offerObject.getTags().get(0));
                } else {
                    holder.tag1.setVisibility(View.GONE); // Si no hay etiqueta, oculta la vista
                }

                if (offerObject.getTags().size() > 1) {
                    holder.tag2.setText(offerObject.getTags().get(1));
                } else {
                    holder.tag2.setVisibility(View.GONE);
                }

                if (offerObject.getTags().size() > 2) {
                    holder.tag3.setText(offerObject.getTags().get(2));
                } else {
                    holder.tag3.setVisibility(View.GONE);
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

        public void setOfferObjectList(List<OfferObject> offerObjectList) {
            this.offerObjectList.clear();
            this.offerObjectList.addAll(offerObjectList);
            notifyDataSetChanged();
        }



        class OfferViewHolder extends RecyclerView.ViewHolder {
            TextView name, companyName, tag1, tag2, tag3;
            ImageView companyImage;

            public OfferViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.offerName);
                companyName = itemView.findViewById(R.id.companyName);
                tag1 = itemView.findViewById(R.id.btnTagOne);
                tag2 = itemView.findViewById(R.id.btnTagTwo);
                tag3 = itemView.findViewById(R.id.btnTagThree);
                companyImage = itemView.findViewById(R.id.imageCompany);
            }
        }
    }
}