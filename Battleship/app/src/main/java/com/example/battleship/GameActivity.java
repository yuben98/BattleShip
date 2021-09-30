package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity //implements View.OnClickListener
{

    Random rand = new Random();
    TextView square;
    LinearLayout.LayoutParams params;
    Grid userGrid;
    Grid compGrid;
    TextView status;
    ArrayList<Integer> list;

    public class Ship {
        int size;
        int direction;
        int number;

        public Ship(int d, int n) {
            if (n == 1) size = 5;
            else if (n == 2) size = 4;
            else if (n == 5) size = 2;
            else size = 3;
            direction = d;
            number = n;
        }

        public int getDirection(){
            return direction;
        }

        public int getSize(){
            return size;
        }

        public int getNumber(){
            return number;
        }
    }
    // grid that contains ships.
    // 0: unchecked square, 1 through 5: ships, 6: checked empty square, 7: destroyed ship square
    public class Grid {
        int user;
        int[][] grid;
        Ship[] ships;
        TextView[][] imgGrid;
        LinearLayout[] layouts;
        int hits;


        public Grid(int u) {
            user = u;
            grid = new int[8][8];
            ships = new Ship[5];
            imgGrid = new TextView[8][8];
            layouts = new LinearLayout[8];
            hits = 0;
        }

        public void fillLayouts() {
            //if computer:
            if (user == 0) {
                layouts[0] = (LinearLayout) findViewById(R.id.crow1);
                layouts[1] = (LinearLayout) findViewById(R.id.crow2);
                layouts[2] = (LinearLayout) findViewById(R.id.crow3);
                layouts[3] = (LinearLayout) findViewById(R.id.crow4);
                layouts[4] = (LinearLayout) findViewById(R.id.crow5);
                layouts[5] = (LinearLayout) findViewById(R.id.crow6);
                layouts[6] = (LinearLayout) findViewById(R.id.crow7);
                layouts[7] = (LinearLayout) findViewById(R.id.crow8);
            }
            else {
                layouts[0] = (LinearLayout) findViewById(R.id.urow1);
                layouts[1] = (LinearLayout) findViewById(R.id.urow2);
                layouts[2] = (LinearLayout) findViewById(R.id.urow3);
                layouts[3] = (LinearLayout) findViewById(R.id.urow4);
                layouts[4] = (LinearLayout) findViewById(R.id.urow5);
                layouts[5] = (LinearLayout) findViewById(R.id.urow6);
                layouts[6] = (LinearLayout) findViewById(R.id.urow7);
                layouts[7] = (LinearLayout) findViewById(R.id.urow8);
            }
        }

        public boolean checkGrid(int r, int c, int d, int s) {
            int i;

            if (d == 0) {
                if ((c+s) > 8) return false;
                for (i = 0; i < s; i++) if (grid[r][c + i] != 0) return false;
            }
            else {
                if ((r+s) > 8) return false;
                for(i=0; i<s; i++) if (grid[r+i][c] != 0) return false;
            }

            return true;
        }

        public void placeShip(Ship s, int r, int c) {
            int i;
            if (s.getDirection()==0) for (i=0;i<s.getSize();i++) grid[r][c+i] = s.getNumber();
            else for (i=0;i<s.getSize();i++) grid[r+i][c] = s.getNumber();
        }

        public void initializeImgGrid(){
            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    int icoord = i;
                    int jcoord = j;
                    imgGrid[i][j] = new TextView(GameActivity.this);
                    imgGrid[i][j].setLayoutParams(params);
                    imgGrid[i][j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    layouts[i].addView(imgGrid[i][j]);
                    if(user == 0) {
                        imgGrid[i][j].setBackgroundColor(Color.parseColor("#52c4b3"));
                        imgGrid[i][j].setClickable(true);
                        imgGrid[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                playTurn(icoord, jcoord);
                            }
                        });
                    }
                    else {
                        if (grid[i][j] != 0) {
                            imgGrid[i][j].setBackgroundColor(Color.parseColor("#C8D11D"));
                            imgGrid[i][j].setText(Integer.toString(grid[i][j]));
                        }
                        else imgGrid[i][j].setBackgroundColor(Color.parseColor("#52c4b3"));
                    }
                }
            }
        }

        public void initializeGrid(){
            int r, c, d;
            // initialize int grid
            for (int i=0; i < 5; i++) {
                d= rand.nextInt(2);
                ships[i] = new Ship(d, i+1);
                //horizontal:
                if (d == 0){
                    r = rand.nextInt(8);
                    c = rand.nextInt(8-ships[i].getSize());
                }
                //vertical:
                else {
                    r= rand.nextInt(8- ships[i].getSize());
                    c= rand.nextInt(8);
                }
                // if grid is free place ship, else repeat
                if (checkGrid(r, c, d, ships[i].getSize())) placeShip(ships[i], r, c);
                else i--;
            }
        }

        public void initialize() {
            initializeGrid();
            fillLayouts();
            initializeImgGrid();
        }

        public boolean hit(int x, int y) {
            if (grid[x][y] == 0) {
                grid[x][y] = 6;
                imgGrid[x][y].setVisibility(View.INVISIBLE);
                imgGrid[x][y].setClickable(false);
            }
            else {
                grid[x][y] = 7;
                imgGrid[x][y].setBackgroundColor(Color.parseColor("#802525"));
                imgGrid[x][y].setClickable(false);
                hits++;
            }

            if (hits == 17) {
                for(int i =0; i<8; i++) {
                    for (int j=0; j<8;j++) {
                        imgGrid[i][j].setClickable(false);
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        square = (TextView) findViewById(R.id.square);
        params = (LinearLayout.LayoutParams) square.getLayoutParams();
        square.setVisibility(View.GONE);

        status = (TextView) findViewById(R.id.status);

        list = new ArrayList<Integer>();
        for (int i=0; i<64;i++) {
            list.add(i);
        }

        userGrid = new Grid(1);
        userGrid.initialize();
        compGrid = new Grid(0);
        compGrid.initialize();

        status.setText("Tap a cell to start the game.");

    }

    public void playTurn(int i, int j){
        status.setText("Game in Progress..");
        if (compGrid.hit(i, j)) {
            win();
            return;
        }
        int pos = rand.nextInt(list.size());
        int num = list.get(pos);
        list.remove(pos);
        if(userGrid.hit(num/8, num%8)) lose();
    }

    public void win() {
        status.setBackgroundColor(Color.parseColor("#19d42f"));
        status.setTextColor(Color.parseColor("#000000"));
        status.setText("You WIN!!\nTap here to go the main menu.");
        allowBack();
    }

    public void lose() {
        status.setBackgroundColor(Color.parseColor("#b31b1b"));
        status.setTextColor(Color.parseColor("#FFFFFF"));
        status.setText("Managing to lose is honestly more impressive..\nTap here to go to the main menu.");
        allowBack();
    }

    public void allowBack() {
        status.setClickable(true);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}