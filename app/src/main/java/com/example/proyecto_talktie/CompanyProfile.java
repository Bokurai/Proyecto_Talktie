package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class CompanyProfile extends Fragment {

    TextView nameCompany, location, description, type, phone, email, website;
    ImageView imageReturn;
    RecyclerView recyclerView;
    StudentSearchViewModel searchViewModel;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewModel = new ViewModelProvider(requireActivity()).get(StudentSearchViewModel.class);
        navController = Navigation.findNavController(view);

        nameCompany = view.findViewById(R.id.txtNameCompany);
        location = view.findViewById(R.id.txtLocationCompany);
        description = view.findViewById(R.id.txtCompanyDescription);
        type = view.findViewById(R.id.typeCompanyTextView);
        phone = view.findViewById(R.id.phoneTextView);
        email = view.findViewById(R.id.emailTextView);
        website = view.findViewById(R.id.websiteTextView);

        imageReturn = view.findViewById(R.id.img_return);

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

            }
        });



    }
}