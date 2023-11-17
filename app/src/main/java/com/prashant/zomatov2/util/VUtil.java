package com.prashant.zomatov2.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;

import es.dmoral.toasty.Toasty;

public class VUtil {

    private static long lastToastTime = 0;
    private static final long TOAST_COOLDOWN_MS = 3000; // 2 seconds


    public static void copyText(Context context, EditText editText) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastToastTime < TOAST_COOLDOWN_MS) {
            return;
        }

        String textToCopy = editText.getText().toString();

        if (!textToCopy.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            lastToastTime = currentTime; // Update last toast time
        } else {
            Toast.makeText(context, "Text is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return packageManager.getApplicationLabel(applicationInfo).toString();
    }

    public static void showSuccessToast(Context context, String message) {
        Toasty.success(context, message).show();
    }

    public static void showErrorToast(Context context, String message) {
        Toasty.error(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showWarning(Context context, String message) {
        Toasty.warning(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getRandomDp() {
        String[] dpUrls = {"https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Fcat.png?alt=media&token=c0e7386b-8769-458d-b07b-85c62a10e487", "https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Fdove.png?alt=media&token=632a4376-054c-43d7-8be5-01f6fb8d9bee", "https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Feagle.png?alt=media&token=4f7e5267-6b8b-42ec-9d98-06e620b3792b", "https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Ffox.png?alt=media&token=2089bb38-8b52-4c88-8d71-2d3fbc0f6f72", "https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Fmacaw.png?alt=media&token=dbfec04e-45ff-4541-a631-0a00f0d81344", "https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Fpanda.png?alt=media&token=4925adf9-51ef-4030-a9fd-436c1f04a3d9", "https://firebasestorage.googleapis.com/v0/b/trade-master-edbae.appspot.com/o/avatar%2Fpenguin.png?alt=media&token=f43f0ad1-7d79-409e-88b6-7d04b6abee45"};
        SecureRandom secureRandom = new SecureRandom();
        int randomIndex = secureRandom.nextInt(dpUrls.length);
        return dpUrls[randomIndex];
    }


    public static void openDialer(Context context, String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Create an intent to open the dialer with the phone number
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));

            // Start the dialer activity
            context.startActivity(dialIntent);
        }
    }

    public static void openWhatsAppNumber(Context context, String phoneNumber) {
        try {
            // Create a Uri with the "smsto" scheme and the WhatsApp phone number
            Uri uri = Uri.parse("smsto:" + "91" + phoneNumber);

            // Create an Intent with ACTION_SENDTO to open the WhatsApp chat
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

            // Set the package to "com.whatsapp" to ensure it opens in WhatsApp
            intent.setPackage("com.whatsapp");

            // Start the activity
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur when trying to open WhatsApp
        }
    }
}
