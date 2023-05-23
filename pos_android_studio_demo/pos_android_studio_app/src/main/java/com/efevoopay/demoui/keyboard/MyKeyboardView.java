package com.efevoopay.demoui.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.QPOSUtil;
import com.efevoopay.demoui.utils.TRACE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * ****************************************************************
 * File Name: MyKeyboardView
 * File Description: Keyboard View
 * ****************************************************************
 */
public class MyKeyboardView extends KeyboardView {
    public static final int KEYBOARDTYPE_Num = 0;//Number  keyboard
    public static final int KEYBOARDTYPE_Num_Pwd = 1;//Number type keyboard(password)
    public static final int KEYBOARDTYPE_ABC = 2;//letter keyboard
    public static final int KEYBOARDTYPE_Symbol = 4;//symbol keyboard
    public static final int KEYBOARDTYPE_Only_Num_Pwd = 5;//only number keyboard
    private TextView PinText;

    private final String strLetter = "abcdefghijklmnopqrstuvwxyz";//letter

    private EditText mEditText;
    private PopupWindow mWindow;
    private Activity mActivity;

    private Keyboard keyboardNum;
    private Keyboard keyboardNumPwd;
    private Keyboard keyboardOnlyNumPwd;
    private Keyboard keyboardABC;
    private Keyboard keyboardSymbol;
    private int mHeightPixels;//screen height

    public boolean isSupper = false;//whether the letter keyboard is capitalized
    public boolean isPwd = false;//whether the numbers on the number keyboard are random
    private int keyBoardType;//keyboard type
    private List<String> dataList = new ArrayList<>();

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHeight(int mHeightPixels){
        this.mHeightPixels = mHeightPixels;
    }

    public void setContext(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void init(EditText editText, PopupWindow window, int keyBoardType,List<String> dataList) {
        this.dataList = dataList;
        this.mEditText = editText;
        this.mWindow = window;
        this.keyBoardType = keyBoardType;
        this.PinText =  (TextView) findViewById(R.id.terminal_text_Pin);

        if (keyBoardType == KEYBOARDTYPE_Num_Pwd || keyBoardType == KEYBOARDTYPE_Only_Num_Pwd) {
            isPwd = true;
        }
        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(mOnKeyboardActionListener);
        setKeyBoardType(keyBoardType);

    }

    public EditText getEditText() {
        return mEditText;
    }

    /**
     * set keyboard type
     */
    public void setKeyBoardType(int keyBoardType) {
        switch (keyBoardType) {
            case KEYBOARDTYPE_Num:
                if (keyboardNum == null) {
                    keyboardNum = new Keyboard(getContext(), R.xml.keyboard_number);
                }
                setKeyboard(keyboardNum);
                break;
            case KEYBOARDTYPE_ABC:
                if (keyboardABC == null)
                    keyboardABC = new Keyboard(getContext(), R.xml.keyboard_abc);
                setKeyboard(keyboardABC);
                break;
            case KEYBOARDTYPE_Num_Pwd:
                if (keyboardNumPwd == null)
                    keyboardNumPwd = new Keyboard(getContext(), R.xml.keyboard_number);
                randomKey(keyboardNumPwd);
                setKeyboard(keyboardNumPwd);
                break;
            case KEYBOARDTYPE_Symbol:
                if (keyboardSymbol == null)
                    keyboardSymbol = new Keyboard(getContext(), R.xml.keyboard_symbol);
                setKeyboard(keyboardSymbol);
                break;
            case KEYBOARDTYPE_Only_Num_Pwd:
                if (keyboardOnlyNumPwd == null)
                    keyboardOnlyNumPwd = new Keyboard(getContext(), R.xml.keyboard_only_number);
                randomKey(keyboardOnlyNumPwd, true);
                setKeyboard(keyboardOnlyNumPwd);
                break;
        }
    }

    private OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void onPress(int primaryCode) {
            TRACE.d("onPress: "+primaryCode);
        }

        @Override
        public void onRelease(int primaryCode) {
            TRACE.d("onReleaseKey ");
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            TRACE.d("onKeyChange");
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * switch keyboard uppercase or lowercase
     */
    private void changeKey() {
        List<Keyboard.Key> keylist = keyboardABC.getKeys();
        if (isSupper) {// switch uppercase to lowercase
            for (Keyboard.Key key : keylist) {
                if (key.label != null && strLetter.contains(key.label.toString().toLowerCase())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {// Switch lowercase to uppercase
            for (Keyboard.Key key : keylist) {
                if (key.label != null && strLetter.contains(key.label.toString().toLowerCase())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
        isSupper = !isSupper;
    }

    public static KeyBoardNumInterface keyBoardNumInterface;

    /**
     * random number keyboard
     * code 48-57 (0-9)
     */
    public void randomKey(Keyboard pLatinKeyboard, boolean ...sort) {
        Integer[] ayRandomKey = new Integer[13];

        for(int i = 0; i < dataList.size() ; i ++){
            ayRandomKey[i]=Integer.valueOf(dataList.get(i), 16);
            TRACE.d("Key: " + ayRandomKey[i]);
        }

        if (sort[0] == true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Arrays.sort(ayRandomKey, Comparator.comparingInt((a) -> a == 0 ? ayRandomKey.length - 3 : a));



        List<Keyboard.Key> pKeyLis = pLatinKeyboard.getKeys();
        int index = 0;
        int sy = 0;
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < pKeyLis.size(); i++) {
            if(i == 0){
                sy = mHeightPixels-pKeyLis.get(i).height*4-pKeyLis.get(i).x*6;//calculate interval value
            }

            int code = pKeyLis.get(i).codes[0] ;
            int y = sy + pKeyLis.get(i).y+ 95;
            int x = pKeyLis.get(i).x;
            int rit = x + pKeyLis.get(i).width;
            int riby = y + pKeyLis.get(i).height;
            String label;


            if (code > 0) {//number value
                pKeyLis.get(i).label = ayRandomKey[index] + "";
                pKeyLis.get(i).codes[0] = 48 + ayRandomKey[index];
                String locationStr = QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(ayRandomKey[index]))+ QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(x))+ QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(y))
                        + QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(rit)) + QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(riby));
                s.append(locationStr);
                index++;
            }else{
                if(code == -3){
                    label = QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(13));
                }else if(code == -4){
                    label = QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(15));
                }else if(code == 0){
                    label = QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(-1));
                }else{
                    label = QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(14));
                }
                String locationStr = label + QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(x))+ QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(y))
                        + QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(rit)) + QPOSUtil.byteArray2Hex(QPOSUtil.intToByteArray(riby));
                s.append(locationStr);

            }


        }
        keyBoardNumInterface.getNumberValue(s.toString());
    }

    public static void setKeyBoardListener(KeyBoardNumInterface mkeyBoardNumInterface){
        keyBoardNumInterface = mkeyBoardNumInterface;
    }

}
