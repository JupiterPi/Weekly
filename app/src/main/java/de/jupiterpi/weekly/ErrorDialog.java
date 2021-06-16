package de.jupiterpi.weekly;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import de.jupiterpi.kaye.orders.weekly.R;

public class ErrorDialog {
    public static void show(Context context) {
        show(context, null);
    }

    public static void show(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message == null ? context.getText(R.string.dialog_error_message) : message)
                .setPositiveButton(R.string.dialog_error_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create().show();
    }
}