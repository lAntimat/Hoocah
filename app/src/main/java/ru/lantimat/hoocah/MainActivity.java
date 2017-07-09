package ru.lantimat.hoocah;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Calendar;

import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.adapters.ReservationRecyclerAdapter;
import ru.lantimat.hoocah.auth.LoginActivity;
import ru.lantimat.hoocah.fragments.OpenOrdersFragment;
import ru.lantimat.hoocah.models.ActiveOrder;
import ru.lantimat.hoocah.models.GoodsModel;
import ru.lantimat.hoocah.models.ReservatonModel;
import ru.lantimat.hoocah.models.TableModel;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";
    ArrayList<TableModel> arTables = new ArrayList<>();
    ArrayList<ReservatonModel> arReserv = new ArrayList<>();
    ArrayList<Button> arButtons = new ArrayList<>();
    DatabaseReference databaseReference;
    Toolbar toolbar;
    Drawer result;
    AccountHeader headerResult;
    FirebaseUser user;
    Context context;
    Calendar dateAndTime = Calendar.getInstance();
    DateTime dateTime;
    RecyclerView recyclerView;
    ReservationRecyclerAdapter reservationRecyclerAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = getApplicationContext();
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        //authCheck();
        initDrawer();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        reservationRecyclerAdapter = new ReservationRecyclerAdapter(arReserv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reservationRecyclerAdapter);

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });


        /*for(int i = 1; i < 11; i++) {
            TableModel tableModel = new TableModel(i, -1, true, false, -1);
            //arTables.add(tableModel);
            databaseReference.child(Constants.TABLES).child(String.valueOf(i)).setValue(tableModel);

        }*/

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        Fragment fragment;

        fragment = OpenOrdersFragment.newInstance("");

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.open_order_frame, fragment).commit();


        setupButtons();

        tablesReferenceListener();

    }

    @Override
    protected void onStart() {
        //FirebaseDatabase.getInstance().goOnline();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //FirebaseDatabase.getInstance().goOffline();
        super.onStop();
    }

    private void setupButtons() {
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
                showPopUpMenu(btn1, "1");
                return true;
            }
        });

        final Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "2");
                startActivity(intent1);
            }
        });

        btn2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn2, "2");
                return true;
            }
        });

        final Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "3");
                startActivity(intent1);
            }
        });

        btn3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn3, "3");
                return true;
            }
        });

        final Button btn4 = (Button) findViewById(R.id.button4);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "4");
                startActivity(intent1);
            }
        });

        btn4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn4, "4");
                return true;
            }
        });

        final Button btn5 = (Button) findViewById(R.id.button5);

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "5");
                startActivity(intent1);
            }
        });

        btn5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn5, "5");
                return true;
            }
        });

        final Button btn6 = (Button) findViewById(R.id.button6);

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "6");
                startActivity(intent1);
            }
        });

        btn6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn6, "6");
                return true;
            }
        });

        final Button btn7 = (Button) findViewById(R.id.button7);

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "7");
                startActivity(intent1);
            }
        });

        btn7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn7, "7");
                return true;
            }
        });

        final Button btn8 = (Button) findViewById(R.id.button8);

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "8");
                startActivity(intent1);
            }
        });

        btn8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn8, "8");
                return true;
            }
        });

        final Button btn9 = (Button) findViewById(R.id.btnComment);

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "9");
                startActivity(intent1);
            }
        });

        btn9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn9, "9");
                return true;
            }
        });

        final Button btn10 = (Button) findViewById(R.id.button10);

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OrderActivity.class);
                intent1.putExtra("id", "10");
                startActivity(intent1);
            }
        });


        btn10.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(btn10, "10");
                return true;
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
    }

    public void initDrawer() {

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.cactus)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withOnAccountHeaderItemLongClickListener(new AccountHeader.OnAccountHeaderItemLongClickListener() {
                    @Override
                    public boolean onProfileLongClick(View view, IProfile profile, boolean current) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .items(R.array.dialog_account_items)
                                .positiveText("Закрыть")
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        switch (position){
                                            case 0:
                                                Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
                                                startActivity(intent);
                                                break;
                                            case 1:
                                                FirebaseAuth.getInstance().signOut();
                                                break;
                                        }
                                    }
                                })
                                .show();
                        return true;
                    }
                })
                .build();





        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Столики");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("История");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(1).withName("Редактировать");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(1).withName("Статистика");
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);

//create the drawer and remember the `Drawer` result object
                 result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Intent intent = null;
                        switch (position) {
                            case 1:
                                break;
                            case 3:
                                intent = new Intent(getApplication(), CloseOrderActivity.class);
                                break;
                            case 4:
                                intent = new Intent(getApplication(), EditActivity.class);
                                break;
                            case 5:
                                intent = new Intent(getApplication(), StatisticActivity.class);

                                break;
                        }
                        if(intent!=null) {
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();
    }

    public void authCheck() {

        /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Mister Cactus")
                .setPhotoUri(Uri.parse("https://s-media-cache-ak0.pinimg.com/originals/63/a5/e8/63a5e8ee8cdcfab2f952bcd46a73e5c4.jpg"))
                .build();

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        user1.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });*/

        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user != null) {

            ProfileDrawerItem profile = new ProfileDrawerItem().withEmail(user.getEmail());
            if(user.getDisplayName()!=null) profile.withName(user.getDisplayName());
            if (user.getPhotoUrl()!=null) profile.withIcon(user.getPhotoUrl());

            // User is signed in
            // Create the AccountHeader
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.cactus)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .addProfiles(profile)
                    .withOnAccountHeaderItemLongClickListener(new AccountHeader.OnAccountHeaderItemLongClickListener() {
                        @Override
                        public boolean onProfileLongClick(View view, IProfile profile, boolean current) {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .items(R.array.dialog_account_items)
                                    .positiveText("Закрыть")
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                            switch (position){
                                                case 0:
                                                    Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
                                                    startActivity(intent);
                                                    break;
                                                case 1:
                                                    FirebaseAuth.getInstance().signOut();
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                            return true;
                        }
                    })
                    .build();
        } else {
            // Create the AccountHeader
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.cactus)
                    .build();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //finish();
            // No user is signed in
        }
    }

    public void showPopUpMenu(Button button, final String id) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, button);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.main_activity_pop_up_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reserv:
                        boolean isReserv = false;
                        for(int i = 0; i < arReserv.size(); i++) {
                            if(arReserv.get(i).getTableId().equals(id)) {
                                Toast.makeText(getApplicationContext(), "Столик уже забронирован", Toast.LENGTH_SHORT).show();
                                isReserv = true;
                            }
                        }
                        if(!isReserv) showAddReservationDialog(id);
                        break;
                    case R.id.cancel:
                        databaseReference.child(Constants.TABLES).child(id).child("reservation").setValue(false);
                        databaseReference.child(Constants.RESERVATION).child(id).removeValue();
                        break;
                }
                /*Toast.makeText(
                        MainActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();*/

                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    public void showAddReservationDialog(final String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_reservation, null);
        dialogBuilder.setView(dialogView);
        final int[] myHour = {10};
        final int[] myMinute = {10};

        final EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        final EditText edTime = (EditText) dialogView.findViewById(R.id.edTime);

        edTime.setFocusable(false);
        edTime.setClickable(true);

        // установка обработчика выбора времени
        final TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateAndTime.set(Calendar.MINUTE, minute);
                dateTime = DateTime.now()
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(minute);
                edTime.setText(dateTime.toString(DateTimeFormat.shortTime()));
            }
        };

        edTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(t);
            }
        });


        dialogBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(TextUtils.isEmpty(edName.getText())) {
                    Toast.makeText(MainActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(edTime.getText())) {
                    Toast.makeText(MainActivity.this, "Введите url", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.child(Constants.TABLES).child(id).child("reservation").setValue(true);
                databaseReference.child(Constants.TABLES).child(id).child("reservationTime").setValue(dateTime.getMillis()/1000);
                databaseReference.child(Constants.RESERVATION).child(id).setValue(new ReservatonModel(id, true, edName.getText().toString(), dateTime.getMillis()/1000));

            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(TimePickerDialog.OnTimeSetListener t) {
        new TimePickerDialog(MainActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    public void tablesReferenceListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arTables.clear();
                arReserv.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.child(Constants.TABLES).getChildren()) {
                    arTables.add(postSnapshot.getValue(TableModel.class));
                }
                for (DataSnapshot postSnapshot: dataSnapshot.child(Constants.RESERVATION).getChildren()) {
                    arReserv.add(postSnapshot.getValue(ReservatonModel.class));
                }
                reservationRecyclerAdapter.notifyDataSetChanged();
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
            //if (tableModel.isReservation()) button.setBackgroundResource(R.drawable.button_yellow);
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
