package edu.uw.connerjm.destinyguns;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * The activity that will hold the Login and Register Fragment.
 * Also used to transition between the two through an interface method.
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.MyRegisterListener
{

    /**
     * Sets the login fragment as the default fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.lr_container, new LoginFragment()).commit();

    }

    /**
     * Displays the register fragment based on if the user has clicked the register button
     * on the login fragment.
     */
    @Override
    public void myStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.lr_container, new RegisterFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
