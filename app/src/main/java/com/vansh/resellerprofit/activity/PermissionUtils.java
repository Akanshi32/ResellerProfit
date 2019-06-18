package com.vansh.resellerprofit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public final class PermissionUtils {

    /**
     * Empty Constructor
     */
    private PermissionUtils() {
    }

    /**
     * This method checks whether the given permission is already granted or not.
     *
     * @param activity      This is context of the current activity
     * @param permission    This is the permission we need to check
     * @return  boolean     Returns True if already permission granted for this permission else false.
     */
    private static boolean checkPermission(Activity activity, String permission) {
        //Determine whether you have been granted a particular permission.
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This method checks whether the given permission is already granted or not.
     * If not granted then check for whether the user has selected "NEVER ASK AGAIN" option for this permission.
     * If Option selected, then display only a dialog that we need to access these permission else request for new permission prompt.
     *
     * @param activity      This is context of the current activity
     * @param permissionId  This is the an Unique ID for each and every single permission we are passing.
     * @param permissionInfoMsg This is Message we need to display to the user when they selected "NEVER ASK AGAIN" option for the certain permission.
     * @param permission   This is the permission we need to check
     * @return  boolean     Returns True if already permission granted for this permission else false.
     */
    public static boolean checkAndRequestPermission(final Activity activity, final int permissionId, String permissionInfoMsg, final String permission){
        boolean hasPermissionGranted = checkPermission(activity, permission);
        if (!hasPermissionGranted) {
            //Gets whether you should show UI with rationale for requesting a permission. You should do this only if you do not have the permission and the context in which
            // the permission is requested does not clearly communicate to the user what would be the benefit from granting this permission.
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showDialogMessage(activity, permissionInfoMsg,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Requests permissions to be granted to this application. These permissions must be requested in your manifest, they should not be granted to your app.
                                        ActivityCompat.requestPermissions(activity, new String[]{permission},
                                                permissionId);
                                        break;
                                }
                            }
                        });
                return false;
            }
            //Requests permissions to be granted to this application. These permissions must be requested in your manifest, they should not be granted to your app.
            ActivityCompat.requestPermissions(activity,new String[]{permission}, permissionId);
            return false;
        }
        return true;
    }

    /**
     * This method checks whether the given list of all permissions is already granted or not.
     * If not granted then check for whether the user has selected "NEVER ASK AGAIN" option for this permission.
     * If Option selected, then display only a dialog that we need to access these permission else request for new permission prompt.
     *
     * @param activity      This is context of the current activity
     * @param permissionId  This is the an Unique ID for the list of permissions we are passing.
     * @param permissions   This is the list of permissions, we need to check.
     * @param permissionString  This is the list of permissions label, we need to display user before requesting that we need all these permissions.
     * @return  boolean     Returns True if all the permission granted else false.
     */
    public static boolean checkAndRequestMultiplePermission(final Activity activity, final int permissionId, String[] permissions, String[] permissionString) {

        final List<String> permissionsList = new ArrayList<>();
        List<String> permissionsLabelList = new ArrayList<>();
        for (int m = 0; m < permissions.length; m++) {
            if (!checkPermission(activity, permissions[m])) {
                permissionsList.add(permissions[m]);
                permissionsLabelList.add(permissionString[m]);
            }
        }
        if (permissionsList.size() > 0) {
            if (permissionsLabelList.size() > 0) {
                // Need Rationale about why application needs the being-requested permission
                String message = "You need to grant access to " + permissionsLabelList.get(0);
                for (int i = 1; i < permissionsLabelList.size(); i++)
                    message = message + ", " + permissionsLabelList.get(i);
                showDialogMessage(activity, message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Requests permissions to be granted to this application. These permissions must be requested in your manifest, they should not be granted to your app.
                                        ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                                                permissionId);
                                        break;
                                }
                            }
                        });
                return false;
            }
            //Requests permissions to be granted to this application. These permissions must be requested in your manifest, they should not be granted to your app.
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), permissionId);
            return false;
        }
        return true;
    }

    /**
     * This method is used to display a Dialog Message to the user before prompting permission dialog.
     *
     * @param context   This is context of the current activity
     * @param message   This is the message we are displaying in the dialog to the user.
     * @param okListener    This is the dialog OnClickListener for positive button to request for permission.
     */
    private static void showDialogMessage(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}