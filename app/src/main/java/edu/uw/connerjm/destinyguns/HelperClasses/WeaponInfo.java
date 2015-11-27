package edu.uw.connerjm.destinyguns.HelperClasses;

/**
 * Holds the information for any given weapon.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 27/11/2015
 */
public class WeaponInfo
{

    public String mName;
    public String mDamageType;

    public WeaponInfo(String name, String damageType)
    {
        mName = name;
        mDamageType = damageType;
    }

    @Override
    public String toString()
    {
        return mName;
    }
}
