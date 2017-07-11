package com.example.user.worktime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.worktime.Classes.User.User;

import java.text.Format;

/**
 * Created by User on 05.07.2017.
 */

public class ProfileFragment extends Fragment {
    User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        this.user = (User) args.getSerializable("user");
        super.setArguments(args);
    }

    public static ProfileFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable("user", user);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        TextView fullNameView = (TextView) view.findViewById(R.id.profile_full_name);
        TextView emailView = (TextView) view.findViewById(R.id.profile_email);
        TextView usernameView = (TextView) view.findViewById(R.id.profile_username);

        fullNameView.setText(String.format("%s %s %s", user.getSalutation(), user.getFirstName(), user.getLastName()));
        emailView.setText(user.getEmail());
        usernameView.setText(user.getUserName());
        return view;
    }
}
