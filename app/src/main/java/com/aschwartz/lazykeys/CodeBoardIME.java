package com.aschwartz.lazykeys;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Switch;

import com.aschwartz.lazykeys.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Ruby(aka gazlaws) on 13/02/2016.
 */


public class CodeBoardIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView kv;
    private Keyboard keyboard;
    EditorInfo sEditorInfo;

    private boolean caps = false;

    @Override
    public void onKey(int primaryCode, int[] KeyCodes) {

        InputConnection ic = getCurrentInputConnection();
        CharSequence txt = getCurrentInputConnection().getTextBeforeCursor(1000, 0);


        int len = txt.length();

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                    if(ic.getSelectedText(0)== null){
                        ic.deleteSurroundingText(1, 0);
                    }
                    else getCurrentInputConnection().commitText("",1);


                break;
            case 27:
                //Escape
                long now = System.currentTimeMillis();
                int meta = 0;
                if (ic != null) ic.sendKeyEvent(new KeyEvent(
                        now, now, KeyEvent.ACTION_DOWN, primaryCode, 0, meta));
                break;

            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case -4:
                switch (sEditorInfo.imeOptions & (EditorInfo.IME_MASK_ACTION|EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
                    case EditorInfo.IME_ACTION_GO:
                        ic.performEditorAction(EditorInfo.IME_ACTION_GO);
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        ic.performEditorAction(EditorInfo.IME_ACTION_NEXT);
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        ic.performEditorAction(EditorInfo.IME_ACTION_SEARCH);
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        ic.performEditorAction(EditorInfo.IME_ACTION_SEND);
                        break;
                    default:
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                }
                break;


            case 53737:
                getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
                break;

            case 53738:
                getCurrentInputConnection().performContextMenuAction(android.R.id.cut);
                break;

            case 53739:
                getCurrentInputConnection().performContextMenuAction(android.R.id.copy);
                break;
            case 53740:
                getCurrentInputConnection().performContextMenuAction(android.R.id.paste);
                break;

            case 9:
                ic.commitText("\u0009", 1);
                break;

            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                    ic.commitText(String.valueOf(code), 1);
//              else if (code == 12344) {
//                    ic.commitText("printf(", 1);
//                } else if (code == 12345) {
//                    ic.commitText("scanf(", 1);
//                } else if (code == 10101) {
//                    ic.commitText("for();", 1);
                } else ic.commitText(String.valueOf(code), 1);

        }

    }

//    private void sendEscape() {
//        if (isConnectbot()) {
//            sendKeyChar((char) 27);
//        } else {
//            sendModifiedKeyDownUp(111 /*KeyEvent.KEYCODE_ESCAPE */);
//        }
//    }


    @Override
    public void onPress(int primaryCode) {
    }


    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {

    }

    @Override
    public View onCreateInputView() {

        SharedPreferences pre = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);

        switch (pre.getInt("SAVED_RADIO_BUTTON_INDEX",0)){
            case 0:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
                break;
            case 1:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard1, null);
                break;
            case 2:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);
                break;
            case 3:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard3, null);
                break;
            case 4:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard4, null);
                break;
            case 5:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard5, null);
                break;

            default:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
                break;


        }

        if(pre.getInt("PREVIEW",1)==1){
            kv.setPreviewEnabled(true);
        } else  kv.setPreviewEnabled(false);

        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);

        setInputView(onCreateInputView());
        sEditorInfo = attribute;
    }
}

