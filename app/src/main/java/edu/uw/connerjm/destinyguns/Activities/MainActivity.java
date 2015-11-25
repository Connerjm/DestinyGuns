package edu.uw.connerjm.destinyguns.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

    String mList;
    String mRarity;
    String mSlot;
    String mType;

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

        //Set up all of the xml layout elements and their listeners.
        setUp();
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

    private void setUp()
    {
        setUpListSpinner();
        setUpRaritySpinner();
        setUpSlotSpinner();
        setUpTypeSpinner();
        setUpButtons();
    }

    private void setUpListSpinner()
    {
        Spinner userListSpinner = (Spinner) findViewById(R.id.user_list_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userListSpinner.setAdapter(adapter);
        userListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    mList = null;
                } else
                {
                    mList = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mList = null;
            }
        });
    }

    private void setUpRaritySpinner()
    {
        Spinner raritySpinner = (Spinner) findViewById(R.id.weapon_rarity_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.rarity_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        raritySpinner.setAdapter(adapter2);
        raritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    mRarity = null;
                } else
                {
                    mRarity = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mRarity = null;
            }
        });
    }

    private void setUpSlotSpinner()
    {
        Spinner slotSpinner = (Spinner) findViewById(R.id.weapon_slot_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.slot_spinner, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotSpinner.setAdapter(adapter3);
        slotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    mSlot = null;
                } else
                {
                    mSlot = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mSlot = null;
            }
        });
    }

    private void setUpTypeSpinner()
    {
        Spinner typeSpinner = (Spinner) findViewById(R.id.weapon_type_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.type_spinner, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter4);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    mType = null;
                } else
                {
                    mType = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mType = null;
            }
        });
    }

    private void setUpButtons()
    {
        Button selectListButton = (Button) findViewById(R.id.choose_list_button);
        selectListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO go to server and get all the weapons in the chosen list.
                if(mList != null)
                {
                    Toast.makeText(v.getContext(), "Going to the " + mList, Toast.LENGTH_LONG).show();
                }
            }
        });

        Button refineWeaponsButton = (Button) findViewById(R.id.refine_list_button);
        refineWeaponsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO go to server and get all the weapons that fulfill the selected parameters.
                if(mRarity != null && mSlot != null && mType != null)
                {
                    Toast.makeText(v.getContext(), "Retrieving all " + mRarity + ", " + mSlot + " " + mType + " Weapons.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
