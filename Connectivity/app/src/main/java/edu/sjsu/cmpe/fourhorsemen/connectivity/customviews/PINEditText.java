package edu.sjsu.cmpe.fourhorsemen.connectivity.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.Toast;


/**
 * Created by gauravchodwadia on 5/7/17.
 */

public class PINEditText extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = "PINEditText";

    public PINEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PINEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PINEditText(Context context) {
        super(context);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new PINInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class PINInputConnection extends InputConnectionWrapper {

        public PINInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                Toast.makeText(getContext(), "Helloo", Toast.LENGTH_LONG);                // Un-comment if you wish to cancel the backspace:
                // return false;
            }
            return super.sendKeyEvent(event);
        }

    }
}
