package edu.uw.connerjm.destinyguns.HelperClasses;

/**
 * Holds the information for any given weapon.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 29/11/2015
 */
public class WeaponInfo
{

//VARIABLES

    public String mName;
    public String mDamageType;

//CONSTRUCTOR

    /**
     * Creates a weapon info object for use in the refines list of weapons.
     *
     * @param name is the name of the weapon.
     * @param damageType is the type of damage that is dealt.
     */
    public WeaponInfo(String name, String damageType)
    {
        mName = name;
        mDamageType = damageType;
    }

//OVERWRITTEN METHODS

    /**
     * Returns the name of this weapon which is what is shown in the list.
     *
     * @return the weapon name.
     */
    @Override
    public String toString()
    {
        return mName;
    }
}
