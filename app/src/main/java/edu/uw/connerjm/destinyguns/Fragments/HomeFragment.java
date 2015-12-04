package edu.uw.connerjm.destinyguns.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import edu.uw.connerjm.destinyguns.R;

/**
 * Is the main fragment of our application. Here is where the user will choose which weapons they
 * want to see a list off, whether it is one of the three lists a user has access to or by using
 * the drop down spinners to refine a list of weapons that way.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.02
 * @since 29/11/2015
 */
public class HomeFragment extends Fragment
{

//INTERFACES

    public interface homeInterfaceListener
    {
        void switchToListFragment(boolean wasList, String theList, String rarity, String slot, String type);
    }

//VARIABLES

    /** References to our buttons in the xml. */
    Button mSelectListButton;
    Button mRefineWeaponsButton;

    /** The list name or resulting fields from spinners. */
    String mList;
    String mRarity;
    String mSlot;
    String mType;

//CONSTRUCTOR

    public HomeFragment() {/* Required empty public constructor */}

//OVERWRITTEN METHODS

    /**
     * Called every time to create the view.
     *
     * @param inflater inflates the xml layout.
     * @param container is the parent activity.
     * @param savedInstanceState saved information from last time.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Set up all of the xml layout elements and their listeners.
        setUp(v);

        return v;
    }

//HELPER METHODS

    /**
     * Sets into motion the creation of all the bits from the xml layout.
     *
     * @param v is the parent view.
     */
    private void setUp(View v)
    {
        setUpListSpinner(v);
        setUpRaritySpinner(v);
        setUpSlotSpinner(v);
        setUpTypeSpinner(v);
        setUpButtons(v);
    }

    /**
     * Creates the spinner that holds the three potential lists.
     *
     * @param v is the parent view.
     */
    private void setUpListSpinner(View v)
    {
        Spinner userListSpinner = (Spinner) v.findViewById(R.id.user_list_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
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
                    mSelectListButton.setEnabled(false);
                } else
                {
                    mList = (String) parent.getItemAtPosition(position);
                    mSelectListButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mList = null;
                mSelectListButton.setEnabled(false);
            }
        });
    }

    /**
     * Creates the spinner that holds the two different weapon rarities.
     *
     * @param v is the parent view.
     */
    private void setUpRaritySpinner(View v)
    {
        Spinner raritySpinner = (Spinner) v.findViewById(R.id.weapon_rarity_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
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
                }
                else
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

    /**
     * Creates the spinner that holds the three different weapon equip slots.
     *
     * @param v is the parent view.
     */
    private void setUpSlotSpinner(View v)
    {
        Spinner slotSpinner = (Spinner) v.findViewById(R.id.weapon_slot_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(),
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
                }
                else
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

    /**
     * Creates the spinner for the many different weapon types that we have.
     *
     * @param v is the parent view.
     */
    private void setUpTypeSpinner(View v)
    {
        Spinner typeSpinner = (Spinner) v.findViewById(R.id.weapon_type_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity(),
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
                }
                else
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

    /**
     * Creates the two buttons and their listeners, one for selecting a list, the other for
     * refineing using the spinners.
     *
     * @param v is the parent view.
     */
    private void setUpButtons(View v)
    {
        mSelectListButton = (Button) v.findViewById(R.id.choose_list_button);
        mSelectListButton.setEnabled(false);
        mSelectListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((homeInterfaceListener) getActivity()).switchToListFragment(true, mList, null, null, null);
            }
        });

        mRefineWeaponsButton = (Button) v.findViewById(R.id.refine_list_button);
        mRefineWeaponsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String TAG = "spinners testing";

                boolean raritySet = !(mRarity == null);
                boolean slotSet = !(mSlot == null);
                boolean typeSet = !(mType == null);
                if(raritySet && !slotSet && !typeSet)//just rarity
                {
                    Log.d(TAG, "rarity");
                    ((homeInterfaceListener) getActivity())
                            .switchToListFragment(false, null, mRarity, null, null);
                }
                else if(!raritySet && slotSet && !typeSet)//just slot
                {
                    Log.d(TAG, "slot");
                    ((homeInterfaceListener) getActivity())
                            .switchToListFragment(false, null, null, mSlot, null);
                }
                else if(!raritySet && !slotSet && typeSet)//just type
                {
                    Log.d(TAG, "type");
                    ((homeInterfaceListener) getActivity())
                            .switchToListFragment(false, null, null, null, mType);
                }
                else if(raritySet && slotSet && !typeSet)//rarity and slot
                {
                    Log.d(TAG, "rarity and slot");
                    ((homeInterfaceListener) getActivity())
                            .switchToListFragment(false, null, mRarity, mSlot, null);
                }
                else if(raritySet && !slotSet)//rarity and type (the normal all set url.)
                {
                    Log.d(TAG, "rarity  and type");
                    ((homeInterfaceListener) getActivity())
                            .switchToListFragment(false, null, mRarity, mSlot, mType);
                }
                else if(!raritySet && slotSet)//slot and type
                {
                    Log.d(TAG, "slot and type");
                    if(isValidRequest())
                    {
                        ((homeInterfaceListener) getActivity())
                                .switchToListFragment(false, null, null, mSlot, mType);
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), getString(R.string.not_valid),
                                Toast.LENGTH_LONG).show();
                    }
                }
                else if(!raritySet)//none
                {
                    Log.d(TAG, "none");
                    ((homeInterfaceListener) getActivity())
                            .switchToListFragment(false, null, null, null, null);
                }
                else//all are set
                {
                    Log.d(TAG, "all");
                    if(isValidRequest())
                    {
                        ((homeInterfaceListener) getActivity())
                                .switchToListFragment(false, null, mRarity, mSlot, mType);
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), getString(R.string.not_valid),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Checks the validity of the slot and type combo as types have specific slots.
     *
     * @return whether the combination is correct or not.
     */
    private boolean isValidRequest()
    {
        boolean isValid = false;
        switch(mSlot)
        {
            case "Primary":
                switch (mType)
                {
                    case "Auto Rifle":
                        isValid = true;
                        break;
                    case "Hand Cannon":
                        isValid = true;
                        break;
                    case "Scout Rifle":
                        isValid = true;
                        break;
                    case "Pulse Rifle":
                        isValid = true;
                        break;
                }
                break;
            case "Special":
                switch (mType)
                {
                    case "Fusion Rifle":
                        isValid = true;
                        break;
                    case "Sniper Rifle":
                        isValid = true;
                        break;
                    case "Shotgun":
                        isValid = true;
                        break;
                    case "Sidearm":
                        isValid = true;
                        break;
                }
                break;
            case "Heavy":
                switch (mType)
                {
                    case "Machine Gun":
                        isValid = true;
                        break;
                    case "Rocket Launcher":
                        isValid = true;
                        break;
                    case "Sword":
                        isValid = true;
                        break;
                }
                break;
        }
        return isValid;
    }
}
