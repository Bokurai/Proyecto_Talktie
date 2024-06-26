package com.example.proyecto_talktie.view.company_fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.company.OfferObject;
import com.example.proyecto_talktie.models.student.Student;
import com.example.proyecto_talktie.view.adapters.companyApplicantsAdapter;
import com.example.proyecto_talktie.viewmodel.ApplicantsViewModel;
import com.example.proyecto_talktie.viewmodel.CompanyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fragment displaying details of job applicants for a specific job offer.
 */
public class OffersDetailsApplicantsFragment extends Fragment {
    NavController navController;
    TextView offer_name, offer_date,job_category,contract_time,job_description;
    CompanyViewModel companyViewModel;
    ImageView backArrow;
    ApplicantsViewModel applicantsViewModel;
    private companyApplicantsAdapter adapter;
    private RecyclerView recyclerViewApplicants;
    LinearLayout applicants;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_details_applicants, container, false);
    }

    /**
     * Sets up the views and data for the Applicants Details screen.
     * It initializes the necessary views and adapters, observes changes in the selected offer data,
     * and updates the UI accordingly with the offer details and its applicants.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        recyclerViewApplicants = view.findViewById(R.id.applicantsRecyclerView);

        offer_name = view.findViewById(R.id.txtOfferPosition);
        offer_date = view.findViewById(R.id.txtOfferDate);
        job_category = view.findViewById(R.id.txtWorkField);
        contract_time = view.findViewById(R.id.txtContractTime);
        job_description = view.findViewById(R.id.txtJobDescription);
        backArrow = view.findViewById(R.id.backArrow);
        applicants = view.findViewById(R.id.applicants);

        companyViewModel = new ViewModelProvider(requireActivity()).get(CompanyViewModel.class);
        applicantsViewModel = new ViewModelProvider(requireActivity()).get(ApplicantsViewModel.class);
        adapter = new companyApplicantsAdapter(new ArrayList<>(),applicantsViewModel, navController );

        // Observing selected offer data
        companyViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<OfferObject>() {
            @Override
            public void onChanged(OfferObject offerObject) {
                // Obtaining applicants for the current offer
                String offerId = offerObject.getOfferId();
                applicantsViewModel.getApplicantsIds(offerId);

                offer_name.setText(offerObject.getName());
                job_category.setText(offerObject.getJob_category());
                contract_time.setText(offerObject.getContract_time());
                job_description.setText(offerObject.getJob_description());

                SimpleDateFormat format = new SimpleDateFormat("HH:mm  dd/MM/yyyy");
                Date date = offerObject.getDate();
                String formattedDate = format.format(date);
                offer_date.setText(formattedDate);
                applicantsViewModel.setOfferId(offerId);
                applicantsViewModel.getApplicantsData().observe(getViewLifecycleOwner(), new Observer<List<Student>>() {
                    @Override
                    public void onChanged(List<Student> students) {
                        if (students != null && !students.isEmpty()) {
                            applicants.setVisibility(View.VISIBLE);
                            adapter.setStudentList(students);
                            recyclerViewApplicants.setAdapter(adapter);
                        } else {
                            applicants.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

    }
}