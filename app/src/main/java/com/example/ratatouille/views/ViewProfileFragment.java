package com.example.ratatouille.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ratatouille.R;
import com.example.ratatouille.controllers.UserController;
import com.example.ratatouille.models.Users;
import com.example.ratatouille.vars.VariablesUsed;

import org.w3c.dom.Text;

import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE = 1;
    ImageView imageProfile;
    TextView editButton;
    TextView usernameText;
    TextView emailText;
    TextView phoneNumberText;
    TextView addressText;
    TextView yourVouchersText;
    RelativeLayout contactSupport;
    RelativeLayout settings;
    RelativeLayout termsOfUse;
    RelativeLayout privacyPolicy;
    Integer TAKE_IMAGE_CODE = 1001;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageProfile = getView().findViewById(R.id.vp_imageProfile);
        editButton = getView().findViewById(R.id.vp_editButton);
        usernameText = getView().findViewById(R.id.vp_username);
        emailText = getView().findViewById(R.id.vp_email);
        phoneNumberText = getView().findViewById(R.id.vp_phoneNumberText);
        addressText = getView().findViewById(R.id.vp_addressText);
        yourVouchersText = getView().findViewById(R.id.vp_yourVouchersText);
        contactSupport = getView().findViewById(R.id.vp_contactSupport);
        settings = getView().findViewById(R.id.vp_settings);
        termsOfUse = getView().findViewById(R.id.vp_termsOfUse);
        privacyPolicy = getView().findViewById(R.id.vp_privacyPolicy);

        usernameText.setText(VariablesUsed.currentUser.getUsername());
        emailText.setText(VariablesUsed.loggedUser.getEmail());
        phoneNumberText.setText(VariablesUsed.currentUser.getPhone());
        addressText.setText(VariablesUsed.currentUser.getAddress());

        //        ProfilePicture Initializations...
        if(VariablesUsed.loggedUser.getPhotoUrl() != null) {
            Glide.with(getView().getContext())
                    .load(VariablesUsed.loggedUser.getPhotoUrl())
                    .into(imageProfile);
        }

        imageProfile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // mungkin kalau nanti akhir2 bisa diganti jadi select pictures from library..
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(intent, TAKE_IMAGE_CODE);
                }
            }
        });

        contactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              chat rooms
            }
        });

        settings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//              nanti ditambahin
            }
        });

        termsOfUse.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                vpTermsofService.generateTerms();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View promptsView = inflater.inflate(R.layout.dialog_termsofservice,null);

                TextView tosText = promptsView.findViewById(R.id.tos_text);
                tosText.setText(vpTermsofService.getTerms());

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(promptsView);

                alert.setCancelable(false);
                alert.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                TODO: Buat Privacy Policy untuk aplikasi
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageProfile.setImageBitmap(bitmap);
                    UserController.uploadProfilePicture(getContext(), bitmap);
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(getContext(), "Image Selection Has Been Cancelled!", Toast.LENGTH_LONG);
            }
        }
    }
}