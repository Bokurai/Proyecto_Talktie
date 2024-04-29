package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {

    MainActivity mainActivity;
    NavController navController;
    LinearLayout search_bar;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private companyOfferAdapter adapter;
    private FirebaseAuth mAuth;
    private  String stuentId;
    private Handler handler;
    private Runnable runnable;
    private final int ACTUALIZACION_INTER = 5000;
    private boolean primeraEjecucion = true;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                homeViewModel.getOffersComapanies(stuentId).observe(getViewLifecycleOwner(), offer -> {
                    adapter.setOfferObjectList(offer);
                });
                handler.postDelayed(this, ACTUALIZACION_INTER);
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        /**if (primeraEjecucion) {
            homeViewModel.getOffersComapanies(stuentId).observe(getViewLifecycleOwner(), offer -> {
                adapter.setOfferObjectList(offer);
            });

            primeraEjecucion = false;
        }**/
        handler.postDelayed(runnable, ACTUALIZACION_INTER);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new companyOfferAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.recyclerview_company_offers);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        stuentId = currentUser.getUid();

        navController = Navigation.findNavController(view);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBot();

        search_bar = view.findViewById(R.id.search_bar);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        Log.d("Offers", "Llego aquí " + stuentId);

       /** homeViewModel.getOffersComapanies(stuentId).observe(getViewLifecycleOwner(), offer -> {
            adapter.setOfferObjectList(offer);
        });**/

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goStudentSearchView);
            }
        });


    }

    class companyOfferAdapter extends RecyclerView.Adapter<companyOfferAdapter.companyOfferViewHolder> {
        private List<OfferObject> offerObjectList;

        public companyOfferAdapter(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
        }

        @NonNull
        @Override
        public companyOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new companyOfferViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_company_offer, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull companyOfferViewHolder holder, int position) {
            OfferObject offer = offerObjectList.get(position);

            //poner el nombre de la empresa y la oferta
            holder.nameOffer.setText(offer.getName());
            holder.nameCompany.setText(offer.getCompanyId());

        }

        @Override
        public int getItemCount() {
            return offerObjectList.size();
        }

        public void setOfferObjectList(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
            notifyDataSetChanged();
        }

        class companyOfferViewHolder extends RecyclerView.ViewHolder {
            //Falta image view
            TextView nameCompany, nameOffer;

            public companyOfferViewHolder(@NonNull View itemView) {
                super(itemView);

                nameCompany = itemView.findViewById(R.id.companyNameOffer);
                nameOffer = itemView.findViewById(R.id.offerNameCompany);
            }
        }
    }
}