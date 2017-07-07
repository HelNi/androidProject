package com.example.user.worktime;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by User on 06.07.2017.
 */

public class AboutFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.about_fragment, container, false);

        //Make the link to GitHub clickable.
        TextView gitHub_TextView = (TextView) inflate.findViewById(R.id.github_link);
        gitHub_TextView.setMovementMethod(LinkMovementMethod.getInstance());

        //Make the link to the imprint-page clickable.
        TextView imprint_TextView = (TextView) inflate.findViewById(R.id.imprint);
        imprint_TextView.setMovementMethod(LinkMovementMethod.getInstance());

        return inflate;
    }
}
