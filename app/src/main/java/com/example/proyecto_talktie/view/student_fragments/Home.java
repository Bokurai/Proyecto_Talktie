package com.example.proyecto_talktie.view.student_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.MainActivity;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.OfferObject;
import com.example.proyecto_talktie.viewmodel.HomeViewModel;
import com.example.proyecto_talktie.viewmodel.OfferViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends Fragment {
    MainActivity mainActivity;
    NavController navController;
    LinearLayout search_bar;
    private HomeViewModel homeViewModel;
    private OfferViewModel offerViewModel;
    private RecyclerView recyclerView;
    private companyOfferAdapter adapter;
    private FirebaseAuth mAuth;
    private  String studentId;
    private CircleImageView profileImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Initialize the adapter and define the recyclerView
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
        studentId = currentUser.getUid();
        navController = Navigation.findNavController(view);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBot();

        profileImg = view.findViewById(R.id.profileHome);

        search_bar = view.findViewById(R.id.search_bar);

        //Initialize the viewModel
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);


        //Obtain the offers from the companies that the user follows and pass them through the adapter.
        homeViewModel.getOffersComapanies(studentId).observe(getViewLifecycleOwner(), offer -> {
            adapter.setOfferObjectList(offer);
        });

        loadImageUser();

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goStudentSearchView);
            }
        });
    }

    /**
     * Method that searches for the student's image in the database, if available, the default image is set.
     */
    public void loadImageUser() {
        if (getView() != null) {
        FirebaseFirestore.getInstance().collection("Student").document(studentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String imageProfile = documentSnapshot.getString("profileImage");
                    Context context = getView().getContext();
                    if (imageProfile != null && !imageProfile.isEmpty()) {
                        Uri uriImage = Uri.parse(imageProfile);

                        Glide.with(context)
                                .load(uriImage)
                                .into(profileImg);
                    } else {
                        Glide.with(context)
                                .load(R.drawable.profile_image_defaut)
                                .into(profileImg);
                    }
                });
        }
    }

    /**
     * Class representing the adapter of the offers followed by the student.
     */
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

            //Put the company name and offer
            holder.nameOffer.setText(offer.getName());
            holder.nameCompany.setText(offer.getCompanyName());

            Date date = offer.getDate();
            //Formatting the date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(date);
            holder.dateOffer.setText(formattedDate);

            //Load the user's image, in case of being null the default one is set.
            String imageProfile = offer.getCompanyImageUrl();
            Context context = getView().getContext();
            if (imageProfile != null && !imageProfile.isEmpty()){
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.imageCompany);
            } else {
                Glide.with(context)
                        .load(R.drawable.build_image_default)
                        .into(holder.imageCompany);
            }

            //Button that navigates to the details of the offer
            holder.bntDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offerViewModel.seleccionar(offer);
                    navController.navigate(R.id.action_goOfferDetailsFragment);
                }
            });


        }

        @Override
        public int getItemCount() {
            return offerObjectList.size();
        }

        /**
         * Method that changes the list of offers and notifies the adapter.
         * @param offerObjectList New list of offers.
         */
        public void setOfferObjectList(List<OfferObject> offerObjectList) {
            this.offerObjectList = offerObjectList;
            notifyDataSetChanged();
        }

        /**
         * Class that represents the elements of the ViewHolder.
         */
        class companyOfferViewHolder extends RecyclerView.ViewHolder {
            TextView nameCompany, nameOffer, dateOffer;
            ImageView imageCompany;
            AppCompatButton bntDetails;

            public companyOfferViewHolder(@NonNull View itemView) {
                super(itemView);

                nameCompany = itemView.findViewById(R.id.companyNameOffer);
                nameOffer = itemView.findViewById(R.id.offerNameCompany);
                dateOffer = itemView.findViewById(R.id.dateOffer);
                imageCompany = itemView.findViewById(R.id.shapeableImageView);
                bntDetails = itemView.findViewById(R.id.btnDatailsOffer);
            }
        }

    }
}