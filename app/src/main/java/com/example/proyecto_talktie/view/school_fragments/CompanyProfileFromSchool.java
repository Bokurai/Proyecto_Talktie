package com.example.proyecto_talktie.view.school_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto_talktie.MainActivity;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.Business;
import com.example.proyecto_talktie.viewmodel.CompanySearchViewModel;


public class CompanyProfileFromSchool extends Fragment {

    private TextView nameCompany, location, description, type, phone, email, website, headquaters;

    private AppCompatButton message;
    MainActivity mainActivity;
    private ImageView backButton;
    private CompanySearchViewModel companySearchViewModel;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_profile_from_school, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companySearchViewModel = new ViewModelProvider(requireActivity()).get(CompanySearchViewModel.class);
        navController = Navigation.findNavController(view);
        mainActivity = (MainActivity) requireActivity();
        mainActivity.hideNavBotSchool();


        nameCompany = view.findViewById(R.id.txtNameCompanyPS);
        location = view.findViewById(R.id.txtLocationCompanyPS);
        description = view.findViewById(R.id.txtCompanyDescriptionPS);
        type = view.findViewById(R.id.typeCompanyTextViewPS);
        phone = view.findViewById(R.id.phoneTextViewCompanyPS);
        email = view.findViewById(R.id.emailTextViewPS);
        website = view.findViewById(R.id.websiteTextViewPS);
        message = view.findViewById(R.id.btnMessageCompany);
        backButton = view.findViewById(R.id.img_return_to_companyview);
        headquaters = view.findViewById(R.id.txtHeadquartersCompany);

        companySearchViewModel.selected().observe(getViewLifecycleOwner(), new Observer<Business>() {
            @Override
            public void onChanged(Business business) {
                nameCompany.setText(business.getName());
                location.setText(business.getAddress());
                description.setText(business.getSummary());
                type.setText(business.getTypeCompany());
                phone.setText(business.getPhone());
                email.setText(business.getEmail());
                website.setText(business.getWebsite());
                headquaters.setText(business.getHeadquarters());
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goSchoolMessages);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
    }
}