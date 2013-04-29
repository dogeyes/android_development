package com.github.dogeyes.todolist;

import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.DialogInterface;
import android.util.Log;
import android.app.Activity;

public class ConfirmFragment extends DialogFragment {
	private static final String TAG = "Confirm";
	
    public interface ConfirmDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onConfirmDialogRelease();
    }
	
    ConfirmDialogListener mListener;
    
	public ConfirmFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		mListener.onConfirmDialogRelease();
	}
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ConfirmDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.confirm_title)
        .setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Log.d(TAG, "confirm yes");
                mListener.onDialogPositiveClick(ConfirmFragment.this);
            }
        })
        .setNegativeButton(R.string.confirm_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	//Log.d(TAG, "confirm no");
            }
        });
		return builder.create();
	}

}
