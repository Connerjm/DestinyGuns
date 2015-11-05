package edu.uw.connerjm.destinyguns;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

public class LoginActivity extends AppCompatActivity implements LoginFragment.MyRegisterListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.lr_container, new LoginFragment()).commit();

    }

    @Override
    public void myStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.lr_container, new RegisterFragment()).commit();
    }

    @Override
    public void myEnd() {
        getSupportFragmentManager().beginTransaction().replace(R.id.lr_container, new LoginFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
