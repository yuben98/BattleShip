package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, GameActivity.class);
        Button btn = (Button) findViewById(R.id.button);
        TextView txt = (TextView) findViewById(R.id.instructions);
        String str = "You and your opponent both start the game with 5 ships randomly placed on your respective grids.";
        str = str + "\nYour grid will be on the top and will show your ships' locations.";
        str = str + "\nYour opponent's grid will be on the bottom. You will take turns attacking one tile at a time";
        str = str + "until either you or your opponent destroys the other one's ships.";
        txt.setText(str);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}