package com.example.safecity;

        import android.content.Intent;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;

public class AuthComplainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        this.setTitle("Higher Authority");
        setContentView(R.layout.activity_auth_complain);

        CardView authorityPanel = findViewById(R.id.authorityPanel);
        CardView complaintPanel = findViewById(R.id.complaintPanel);

        authorityPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthComplainActivity.this, Authority.class);
                startActivity(intent);
            }
        });

        complaintPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthComplainActivity.this, CityActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.camenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.SignOutCAMenuId)
        {

            //FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        else if (item.getItemId()==R.id.CACityMenuId)
        {
            //Intent i=new Intent(getApplicationContext(),AuthComplainActivity.class);
            //startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }


}

