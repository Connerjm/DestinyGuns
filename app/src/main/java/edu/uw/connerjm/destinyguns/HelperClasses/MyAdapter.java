package edu.uw.connerjm.destinyguns.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uw.connerjm.destinyguns.R;

/**
 * A custom adapter so that we can have and icon for the image icon and damage icon.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 05/12/2015
 */
public class MyAdapter extends ArrayAdapter<WeaponInfo>
{

    private final Context mContext;
    private final ArrayList<WeaponInfo> mWeaponsArrayList;

    public MyAdapter(Context context, ArrayList<WeaponInfo> weaponsArrayList)
    {
        super(context, R.layout.list_item, weaponsArrayList);

        mContext = context;
        mWeaponsArrayList = weaponsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        ImageView icon = (ImageView) rowView.findViewById(R.id.list_item_image);
        TextView title = (TextView) rowView.findViewById(R.id.list_item_title);
        ImageView typeicon = (ImageView) rowView.findViewById(R.id.list_type_icon);

        icon.setImageBitmap(mWeaponsArrayList.get(position).mIcon);
        title.setText(mWeaponsArrayList.get(position).mName);
        typeicon.setImageBitmap(mWeaponsArrayList.get(position).mDamageIcon);

        return rowView;
     }
}
