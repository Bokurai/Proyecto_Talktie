package com.example.proyecto_talktie;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Offer extends Fragment {
    private OfferViewModel offerViewModel;
    private RecyclerView recyclerView;
    NavController navController;
    private OffersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        adapter = new OffersAdapter(new ArrayList<>());

        recyclerView = view.findViewById(R.id.offerRecyclerView);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        /**
         *  I get the livedata with the offers from the view-model to show it to the user
         */
        offerViewModel.getOffersLiveData().observe(getViewLifecycleOwner(), offerObjects -> {
            adapter.setOfferObjectList(offerObjects);
        });

        navController = Navigation.findNavController(view);


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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offerViewModel.seleccionar(offerObject);
                    System.out.println("Mostrando oferta 1");
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
            Log.d("OffersAdapter", "Cantidad de ofertas recibidas: " + offerObjectList.size());
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

}