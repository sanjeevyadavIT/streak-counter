/*
 * <Count streak by daily working on a task>
 *  Copyright (C) <2019>  <Sanjeev Yadav>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.betatech.alex.streakcounter.util;

import android.content.Context;
import android.text.TextUtils;

import com.betatech.alex.streakcounter.R;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

/**
 * Utility class to help generate AlertDialog
 */
public final class AlertDialogHelper {

    private AlertDialogHelper() {
    }

    /**
     * Displays the AlertDialog with 3 Action buttons
     * <p>
     * you can set cancelable property
     */
    public static void showAlertDialog(Context context, int drawableIcon, String title, String message, String positiveButtonText, String negativeButtonText, String neutralButtonText, boolean isCancelable, AlertDialogInterface callbacks) {
        if (context == null) return;

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogLight);

        if (drawableIcon != -1)
            alertDialogBuilder.setIcon(drawableIcon);

        if (!TextUtils.isEmpty(title))
            alertDialogBuilder.setTitle(title);
        if (!TextUtils.isEmpty(message))
            alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(positiveButtonText)) {
            alertDialogBuilder.setPositiveButton(positiveButtonText, ((dialog, which) -> {
                callbacks.positiveButtonPressed();
                dialog.dismiss();
            }));
        }

        if (!TextUtils.isEmpty(negativeButtonText)) {
            alertDialogBuilder.setNegativeButton(negativeButtonText, ((dialog, which) -> {
                callbacks.negativeButtonPressed();
                dialog.dismiss();
            }));
        }

        if (!TextUtils.isEmpty(neutralButtonText)) {
            alertDialogBuilder.setNeutralButton(neutralButtonText, ((dialog, which) -> {
                callbacks.neutralButtonPressed();
                dialog.dismiss();
            }));
        }

        alertDialogBuilder.setCancelable(isCancelable);

        //show dialog
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        });

        dialog.show();
    }

    /**
     * Displays the AlertDialog with positive & negative buttons
     * <p>
     * you can set cancelable property
     */
    public static void showAlertDialog(Context context, int drawableIcon, String title, String message, String positive, String negative, boolean isCancelable, AlertDialogInterface callbacks) {
        showAlertDialog(context, drawableIcon, title, message, positive, negative, "", isCancelable, callbacks);
    }

    /**
     * Displays the AlertDialog with positive action button only
     * <p>
     * you can set cancelable property
     */
    public static void showAlertDialog(Context context, int drawableIcon, String title, String message, String positive, boolean isCancelable, AlertDialogInterface callback) {
        showAlertDialog(context, drawableIcon, title, message, positive, "", "", isCancelable, callback);
    }


}
