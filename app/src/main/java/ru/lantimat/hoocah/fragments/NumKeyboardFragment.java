package ru.lantimat.hoocah.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.lantimat.hoocah.R;


public class NumKeyboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btnDot;
    Button btn0;
    Button btnDel;

    public ButtonClickListener listener;

    public interface ButtonClickListener {
        public void onTextChange(CharSequence newText);
    }

    public static NumKeyboardFragment newInstance(String param1) {
        NumKeyboardFragment fragment = new NumKeyboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public NumKeyboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_num_keyboard, container,false);
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn3 = (Button) view.findViewById(R.id.btn3);
        btn4 = (Button) view.findViewById(R.id.btn4);
        btn5 = (Button) view.findViewById(R.id.btn5);
        btn6 = (Button) view.findViewById(R.id.btn6);
        btn7 = (Button) view.findViewById(R.id.btn7);
        btn8 = (Button) view.findViewById(R.id.btn8);
        btn9 = (Button) view.findViewById(R.id.btn9);
        btnDot = (Button) view.findViewById(R.id.btnDot);
        btn0 = (Button) view.findViewById(R.id.btn0);
        btnDel = (Button) view.findViewById(R.id.btnDel);

        // создание обработчика
        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    switch (v.getId()) {
                        case R.id.btn0:
                            listener.onTextChange("0");
                            break;
                        case R.id.btn1:
                            listener.onTextChange("1");
                            break;
                        case R.id.btn2:
                            listener.onTextChange("2");
                            break;
                        case R.id.btn3:
                            listener.onTextChange("3");
                            break;
                        case R.id.btn4:
                            listener.onTextChange("4");
                            break;
                        case R.id.btn5:
                            listener.onTextChange("5");
                            break;
                        case R.id.btn6:
                            listener.onTextChange("6");
                            break;
                        case R.id.btn7:
                            listener.onTextChange("7");
                            break;
                        case R.id.btn8:
                            listener.onTextChange("8");
                            break;
                        case R.id.btn9:
                            listener.onTextChange("9");
                            break;
                        case R.id.btnDot:
                            listener.onTextChange(".");
                            break;
                        case R.id.btnDel:
                            listener.onTextChange("Del");
                            break;

                }

            }
        };

        btn1.setOnClickListener(oclBtn);
        btn2.setOnClickListener(oclBtn);
        btn3.setOnClickListener(oclBtn);
        btn4.setOnClickListener(oclBtn);
        btn5.setOnClickListener(oclBtn);
        btn6.setOnClickListener(oclBtn);
        btn7.setOnClickListener(oclBtn);
        btn8.setOnClickListener(oclBtn);
        btn9.setOnClickListener(oclBtn);
        btnDot.setOnClickListener(oclBtn);
        btn0.setOnClickListener(oclBtn);
        btnDel.setOnClickListener(oclBtn);


        return view;
    }

    public void setButtonClickListener(ButtonClickListener listener) {
        this.listener = listener;
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
