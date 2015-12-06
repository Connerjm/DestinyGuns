package edu.uw.connerjm.destinyguns.HelperClasses;

import android.graphics.Bitmap;

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

    public Bitmap mIcon;
    public String mName;
    public Bitmap mDamageIcon;

//CONSTRUCTOR

    /**
     * Creates a weapon info object for use in the refines list of weapons.
     *
     * @param icon is the bitmap of the icon for this weapon.
     * @param name is the name of the weapon.
     * @param damageIcon is the type of damage that is dealt.
     */
    public WeaponInfo(Bitmap icon, String name, Bitmap damageIcon)
    {
        mIcon = icon;
        mName = name;
        mDamageIcon = damageIcon;
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
