/*
 * MIT License
 *
 * Copyright (c) [2019] [sanjeev yadav]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
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
