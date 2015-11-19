package edu.uw.connerjm.destinyguns.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uw.connerjm.destinyguns.R;

/**
 * TODO fill with our list stuff.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 14/11/2015
 */
public class MainActivity extends AppCompatActivity
{

//VARIABLES

    SharedPreferences mSharedPreferences;

//INHERITED METHODS

    /**
     * Called when the application is first opened.
     * It Checks to see if the user has logged in before, if they have, it continues to the main
     * activity. Otherwise, it will take them to the login fragment.
     *
     * @param savedInstanceState is the saved information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Required super call.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Use shared preferences file to check if user has logged in previously.
        mSharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS), MODE_PRIVATE);
        if(savedInstanceState != null)
        {
            return;
        }
        boolean loggedIn = mSharedPreferences.getBoolean(getString(R.string.LOGGED_IN), false);
        if(findViewById(R.id.main_activity) != null)
        {
            if(!loggedIn)//They haven't logged in, bring them to the log in fragment.
            {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Creates the menu with our custom options.
     *
     * @param menu is the menu being passed in.
     * @return the result of whether it worked or not.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles the clicking of the different options in our menu.
     *
     * @param item is the option getting clicked.
     * @return whether it worked or not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Logs out the user and returns them to the login screen.
        if(item.getItemId() == R.id.action_logout)
        {
            mSharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.LOGGED_IN), false);
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.action_info)
        {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
