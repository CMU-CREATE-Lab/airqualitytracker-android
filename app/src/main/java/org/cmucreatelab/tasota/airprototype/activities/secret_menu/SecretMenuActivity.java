package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.os.Bundle;
import android.view.Menu;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;

public class SecretMenuActivity extends BaseActivity<SecretMenuUIElements> {

    protected SecretMenuButtonClickerListener buttonClickerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__secret_menu__activity);

        buttonClickerListener = new SecretMenuButtonClickerListener(this);
        uiElements = new SecretMenuUIElements(this);
        uiElements.populate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return true;
    }

}
