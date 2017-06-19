package ru.lantimat.hoocah;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Method;

import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.models.ActiveOrder;


public class EditPayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    DatabaseReference mDatabaseReference;
    TextView tvTotalPrice;
    float totalPrice;
    MaterialEditText editTextCash;
    MaterialEditText editTextCard;

    public static EditPayFragment newInstance(String param1) {
        EditPayFragment fragment = new EditPayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public EditPayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.ACTIVE_ITEM);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mParam1!=null) {
                    ActiveOrder activeOrder = dataSnapshot.child(mParam1).getValue(ActiveOrder.class);
                    if (activeOrder != null) {
                        tvTotalPrice.setText(String.valueOf(activeOrder.getTotalPrice()));
                        totalPrice = activeOrder.getTotalPrice();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_pay, container,false);
        editTextCash = (MaterialEditText) view.findViewById(R.id.etCash);
        editTextCard = (MaterialEditText) view.findViewById(R.id.etCard);
        tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editTextCash.setShowSoftInputOnFocus(false);
            editTextCard.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus"
                        , new Class[]{boolean.class});
                method.setAccessible(true);
                method.invoke(editTextCash, false);
            } catch (Exception e) {
                // ignore
            }
        }


        editTextCash.requestFocus();
        editTextCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && s.length()>0) {
                    String str = s.toString();
                    int pay = Integer.parseInt(str);
                    float left;
                    left = pay - totalPrice;
                    editTextCash.setHelperTextColor(Color.BLACK);
                    if(pay>0) editTextCash.setHelperTextAlwaysShown(true);
                    else editTextCash.setHelperTextAlwaysShown(false);
                    editTextCash.setHelperText("Сдача " + String.valueOf(left));

                }
            }
        });

        editTextCash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextCash.setSelection(editTextCash.length());
                editTextCash.requestFocus();
                return true;
            }
        });

        return view;
    }

    public void updateTextValue(CharSequence newText) {
        if(editTextCash.hasFocus()) {
            if (editTextCash != null) {
                if (newText.equals("Del")) {
                    int length = editTextCash.getText().length();
                    if (length > 0) {
                        editTextCash.getText().delete(length - 1, length);
                    }
                } else {
                    editTextCash.setText(editTextCash.getText() + newText.toString());
                    editTextCash.setSelection(editTextCash.length());
                }
            }
        } else if(editTextCard.hasFocus()) {
            if (editTextCard != null) {
                if (newText.equals("Del")) {
                    int length = editTextCard.getText().length();
                    if (length > 0) {
                        editTextCard.getText().delete(length - 1, length);
                    }
                } else {
                    editTextCard.setText(editTextCard.getText() + newText.toString());
                    editTextCard.setSelection(editTextCard.length());
                }
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
