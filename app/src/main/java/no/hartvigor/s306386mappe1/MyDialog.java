package no.hartvigor.s306386mappe1;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    private DialogClickListener callback;

    public interface DialogClickListener {
        public void onYesClick();

        public void onNoClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (DialogClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");
        }
    }
}
