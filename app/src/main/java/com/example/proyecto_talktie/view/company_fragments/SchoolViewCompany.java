package com.example.proyecto_talktie.view.company_fragments;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyecto_talktie.MainActivity;
import com.example.proyecto_talktie.R;
import com.example.proyecto_talktie.models.school.School;
import com.example.proyecto_talktie.viewmodel.SchoolSearchViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragment responsible for displaying a list of schools from the company's perspective.
 * It retrieves school data from Firestore and displays it in a RecyclerView.
 * Each item in the list includes the school's name, type, and profile image.
 * Clicking on a school item navigates to the detailed profile of that school.
 */
public class SchoolViewCompany extends Fragment {

    NavController navController;
    private RecyclerView recyclerView;
    SchoolSearchViewModel schoolSearchViewModel;

    MainActivity mainActivity;
    /**
     * Method called when the fragment is first created.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_view_company, container, false);
    }
    /**
     * Method called when the view associated with the fragment has been created.
     * Here, UI elements are configured, FirestoreRecyclerAdapter is initialized with Firestore data,
     * and click events on school items are handled.
     * @param view The inflated view of the fragment.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        schoolSearchViewModel = new ViewModelProvider(requireActivity()).get(SchoolSearchViewModel.class);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.schoolRecyclerView);

        mainActivity = (MainActivity) requireActivity();
        mainActivity.showNavBotComp();
        Query baseQuery = FirebaseFirestore.getInstance().collection("School");

        FirestoreRecyclerOptions<School> options = new FirestoreRecyclerOptions.Builder<School>()
                .setQuery(baseQuery, School.class)
                .setLifecycleOwner(this)
                .build();

        final SchoolViewAdapter adapter = new SchoolViewAdapter(options);
        recyclerView.setAdapter(adapter);
    }
    /**
     * Adapter class for the RecyclerView that displays the list of schools.
     * It binds school data to the ViewHolder and handles click events on school items.
     */
    class SchoolViewAdapter extends FirestoreRecyclerAdapter<School, SchoolViewAdapter.SchoolViewHolder>{
        /**
         * Constructor for the SchoolViewAdapter.
         * @param options The options for the FirestoreRecyclerAdapter.
         */
       public SchoolViewAdapter(@NonNull FirestoreRecyclerOptions<School> options){
           super(options);
       }
        /**
         * Method called to bind school data to the ViewHolder.
         * @param holder The ViewHolder to bind the data to.
         * @param position The position of the item in the RecyclerView.
         * @param model The School object containing the data to bind.
         */
        @Override
        protected void onBindViewHolder(@NonNull SchoolViewAdapter.SchoolViewHolder holder, int position, @NonNull School model) {
            holder.school_name.setText(model.getName());
            holder.school_type.setText(model.getTypeSchool());

            String imageProfile = model.getProfileImage();
            Context context = getView().getContext();
            if (imageProfile != null && !imageProfile.isEmpty()) {
                Uri uriImage = Uri.parse(imageProfile);
                Glide.with(context)
                        .load(uriImage)
                        .into(holder.school_image);
            } else {
                Glide.with(context)
                        .load(R.drawable.school_default_image)
                        .into(holder.school_image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    schoolSearchViewModel.select(model);
                    navController.navigate(R.id.schoolProfileFromCompany);
                }
            });
        }
        /**
         * Method called to create a new ViewHolder.
         */
        @NonNull
        @Override
        public SchoolViewAdapter.SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SchoolViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_school_view, parent, false));
        }
        /**
         * ViewHolder class that holds references to UI elements for each item in the RecyclerView.
         */
        class SchoolViewHolder extends  RecyclerView.ViewHolder{
            TextView school_name, school_type;
            ImageView school_image;
            /**
             * Constructor for the SchoolViewHolder.
             * @param itemView The view corresponding to the ViewHolder.
             */
            public SchoolViewHolder(@NonNull View itemView){
                super(itemView);

                school_name = itemView.findViewById(R.id.school_name);
                school_type = itemView.findViewById(R.id.school_type);
                school_image = itemView.findViewById(R.id.school_image);
            }
        }
    }
}