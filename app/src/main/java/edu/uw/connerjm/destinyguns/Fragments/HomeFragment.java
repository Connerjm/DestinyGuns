package edu.uw.connerjm.destinyguns.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{

    public interface homeInterfaceListener
    {
        void switchToListFragment(boolean wasList, String theList, String rarity, String slot, String type);
    }

    Button mSelectListButton;
    Button mRefineWeaponsButton;

    String mList;
    String mRarity;
    String mSlot;
    String mType;

    public HomeFragment()
    {/* Required empty public constructor */}

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

    private void setUp(View v)
    {
        setUpListSpinner(v);
        setUpRaritySpinner(v);
        setUpSlotSpinner(v);
        setUpTypeSpinner(v);
        setUpButtons(v);
    }

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
                    //TODO enable other spinners items.
                    mRarity = null;
                    mRefineWeaponsButton.setEnabled(false);
                }
                else
                {
                    //TODO disable other spinners items.
                    mRarity = (String) parent.getItemAtPosition(position);
                    mRefineWeaponsButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mRarity = null;
                mRefineWeaponsButton.setEnabled(false);
            }
        });
    }

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
                    //TODO enable other spinners items.
                    mSlot = null;
                    mRefineWeaponsButton.setEnabled(false);
                }
                else
                {
                    //TODO disable other spinners items.
                    mSlot = (String) parent.getItemAtPosition(position);
                    mRefineWeaponsButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mSlot = null;
                mRefineWeaponsButton.setEnabled(false);
            }
        });
    }

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
                    //TODO enable other spinners items.
                    mType = null;
                    mRefineWeaponsButton.setEnabled(false);
                } else
                {
                    //TODO disable other spinners items.
                    mType = (String) parent.getItemAtPosition(position);
                    mRefineWeaponsButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mType = null;
                mRefineWeaponsButton.setEnabled(false);
            }
        });
    }

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
        mRefineWeaponsButton.setEnabled(false);
        mRefineWeaponsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isValidRequest())
                {
                    ((homeInterfaceListener) getActivity()).switchToListFragment(false, null, mRarity, mSlot, mType);
                }
                else
                {
                    Toast.makeText(v.getContext(), getString(R.string.not_valid),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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
