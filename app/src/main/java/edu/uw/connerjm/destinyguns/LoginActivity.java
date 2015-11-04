package edu.uw.connerjm.destinyguns;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

public class LoginActivity extends AppCompatActivity implements LoginFragment.RegisterListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.lr_container, loginFragment)
                .commit();
    }

    @Override
    public void startMenu() {
       RegisterFragment registerFragment = new RegisterFragment();
        getFragmentManager().beginTransaction().replace(R.id.lr_container, registerFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
