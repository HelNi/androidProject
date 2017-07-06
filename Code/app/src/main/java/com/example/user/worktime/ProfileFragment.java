package com.example.user.worktime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.worktime.User.User;

/**
 * Created by User on 05.07.2017.
 */

public class ProfileFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        /**
         * The following methods set the text of the profile_fragment.xml's TextViews to the actual data.
         * TODO get the data of the currently logged in user.
         * The following variables are for test-use only. They can be deleted when the actual data is being used.
         */
        String salutation = "Herr";
        String firstName = "Peter";
        String lastName = "Lankton";
        String email = "p.lankton@chumbucket.com";

        //Set the salutation.
        TextView salutationTextView = (TextView) view.findViewById(R.id.salutation_textview);
        String text = getString(R.string.profile_fragment_salutation) + " " + salutation;
        salutationTextView.setText(text);

        //Set the first name.
        TextView firstNameTextView = (TextView) view.findViewById(R.id.first_name_textview);
        text = getString(R.string.profile_fragment_user_first_name) + " " + firstName;
        firstNameTextView.setText(text);

        //Set the last name.
        TextView lastNameTextView = (TextView) view.findViewById(R.id.last_name_textview);
        text = getString(R.string.profile_fragment_user_last_name) + " " + lastName;
        lastNameTextView.setText(text);

        //Set the mail.
        TextView emailTextView = (TextView) view.findViewById(R.id.email_textview);
        text = getString(R.string.profile_fragment_user_email) + " " + email;
        emailTextView.setText(text);

        return view;
    }

}
