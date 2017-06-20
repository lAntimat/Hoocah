package ru.lantimat.hoocah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.models.TableModel;

public class MainActivity extends AppCompatActivity {

    ArrayList<TableModel> arTables = new ArrayList<>();
    ArrayList<Button> arButtons = new ArrayList<>();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        databaseReference = FirebaseDatabase.getInstance().getReference();


        /*for(int i = 1; i < 11; i++) {
            TableModel tableModel = new TableModel(i, -1, true, false, -1);
            //arTables.add(tableModel);
            databaseReference.child(String.valueOf(i)).setValue(tableModel);

        }*/

        Fragment fragment;

        fragment = OpenOrdersFragment.newInstance("");

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.open_order_frame, fragment).commit();


        final Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "1");
                startActivity(intent1);
            }
        });

        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn1);
                return true;
            }
        });

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "2");
                startActivity(intent1);
            }
        });

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "3");
                startActivity(intent1);
            }
        });
        Button btn4 = (Button) findViewById(R.id.button4);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "4");
                startActivity(intent1);
            }
        });

        Button btn5 = (Button) findViewById(R.id.button5);

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "5");
                startActivity(intent1);
            }
        });

        Button btn6 = (Button) findViewById(R.id.button6);

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "6");
                startActivity(intent1);
            }
        });

        Button btn7 = (Button) findViewById(R.id.button7);

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "7");
                startActivity(intent1);
            }
        });

        Button btn8 = (Button) findViewById(R.id.button8);

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "8");
                startActivity(intent1);
            }
        });

        Button btn9 = (Button) findViewById(R.id.btnComment);

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "9");
                startActivity(intent1);
            }
        });

        Button btn10 = (Button) findViewById(R.id.button10);

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "10");
                startActivity(intent1);
            }
        });


        arButtons.add(btn1);
        arButtons.add(btn2);
        arButtons.add(btn3);
        arButtons.add(btn4);
        arButtons.add(btn5);
        arButtons.add(btn6);
        arButtons.add(btn7);
        arButtons.add(btn8);
        arButtons.add(btn9);
        arButtons.add(btn10);

        tablesReferenceListener();
    }

    public void showPopUpMenu(Button button) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, button);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.main_activity_pop_up_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        MainActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    public void tablesReferenceListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arTables.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.child(Constants.TABLES).getChildren()) {
                    arTables.add(postSnapshot.getValue(TableModel.class));
                }
                tablesUpdate(arButtons, arTables);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void tablesUpdate(ArrayList<Button> arButtons, ArrayList<TableModel> arTables) {
        if(arButtons!=null && arTables!=null) {
        for (int i = 0; i < arButtons.size(); i++) {
            Button button = arButtons.get(i);
            TableModel tableModel = arTables.get(i);
            if (tableModel.isFree()) button.setBackgroundResource(R.drawable.button_click_green);
            else if(!tableModel.isFree()) button.setBackgroundResource(R.drawable.button_red);
            if (tableModel.isReservation()) button.setBackgroundResource(R.drawable.button_yellow);
        }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
