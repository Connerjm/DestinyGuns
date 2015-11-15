package edu.uw.connerjm.destinyguns.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uw.connerjm.destinyguns.Fragments.LoginFragment;
import edu.uw.connerjm.destinyguns.R;
import edu.uw.connerjm.destinyguns.Fragments.RegisterFragment;

/**
 * The activity that will hold the Login and Register Fragment.
 * Also used to transition between the two through an interface method.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 14/11/2015
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.MyRegisterListener
{

//INHERITED METHODS

    /**
     * Sets the login fragment as the default fragment.
     * @param savedInstanceState is the instance of the information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.lr_container, new LoginFragment())
                .addToBackStack(null).commit();
    }

    /**
     * Displays the register fragment based on if the user has clicked the register button
     * on the login fragment.
     */
    @Override
    public void myStartRegister()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.lr_container,
                new RegisterFragment()).addToBackStack(null).commit();
    }

    /**
     * Changes the displayed activity from the login activity to the main activity.
     */
    @Override
    public void myStartMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
