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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SchoolProfileFromCompany extends Fragment {

    private TextView nameSchool, location, description, type, phone, email, website;
    private AppCompatButton message;
    ImageView returnButton;
    private SchoolSearchViewModel schoolSearchViewModel;
    private NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_profile_from_company, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        schoolSearchViewModel = new ViewModelProvider(requireActivity()).get(SchoolSearchViewModel.class);
        message = view.findViewById(R.id.btnMessageSchool);
        nameSchool = view.findViewById(R.id.txtNameSchool);
        location = view.findViewById(R.id.txtLocationSchool);
        description = view.findViewById(R.id.txtSchoolDescription);
        type = view.findViewById(R.id.typeSchoolTextView);
        phone = view.findViewById(R.id.schoolphoneTextView);
        email = view.findViewById(R.id.schoolemailTextView);
        website = view.findViewById(R.id.schoolwebsiteTextView);
        returnButton = view.findViewById(R.id.img_returnfromSchool);

        schoolSearchViewModel.selected().observe(getViewLifecycleOwner(), new Observer<School>() {
            @Override
            public void onChanged(School school) {
                nameSchool.setText(school.getName());
                location.setText((school.getHeadquarters()));
                description.setText(school.getSummary());
                type.setText(school.getTypeSchool());
                phone.setText(school.getPhone());
                email.setText(school.getEmail());
                website.setText(school.getWebsite());
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_goCompanyMessages);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
    }
}