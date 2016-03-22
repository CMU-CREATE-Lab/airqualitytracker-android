package org.cmucreatelab.tasota.airprototype.activities.options_menu.login;

import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.UIElements;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

/**
 * Created by mike on 3/22/16.
 */
public class LoginUIElements extends UIElements<LoginActivity> {

    protected EditText editTextLoginUsername,editTextLoginPassword;
    protected TextView textViewLogoutUsername;


    public LoginUIElements(LoginActivity activity) { super(activity); }


    public void populate() {
        activity.loggedIn = GlobalHandler.getInstance(activity.getApplicationContext()).esdrLoginHandler.isUserLoggedIn();
        display();

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    protected void display() {
        if (!activity.loggedIn) {
            activity.setContentView(R.layout.__options_menu____login__login_activity);
            editTextLoginUsername = (EditText) activity.findViewById(R.id.editTextLoginUsername);
            editTextLoginPassword = (EditText) activity.findViewById(R.id.editTextLoginPassword);
            activity.findViewById(R.id.buttonLogin).setOnClickListener(activity.clickListener);
        } else {
            activity.setContentView(R.layout.__options_menu____login__logout_activity);
            textViewLogoutUsername = (TextView) activity.findViewById(R.id.textViewLogoutUsername);
            if (activity.username.equals("")) {
                GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
                if (globalHandler.esdrLoginHandler.isUserLoggedIn()) {
                    textViewLogoutUsername.setText(globalHandler.esdrAccount.getUsername());
                }
            } else {
                textViewLogoutUsername.setText(activity.username);
            }
            activity.findViewById(R.id.buttonLogout).setOnClickListener(activity.clickListener);
        }
    }

}
