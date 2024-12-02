package com.example.zenithevents.HelperClasses;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * A utility class for handling Bitmap operations such as encoding to Base64 and decoding from Base64.
 * <p>
 * Note: The JavaDocs for this class were generated using OpenAI's ChatGPT.
 * </p>
 * This class includes methods to:
 * <ul>
 *     <li>Encode a Bitmap image to a Base64 string.</li>
 *     <li>Decode a Base64 string to a Bitmap image.</li>
 *     <li>Retrieve a Bitmap from a given URI.</li>
 * </ul>
 */
public class BitmapUtils {

    /**
     * Encodes a Bitmap image into a Base64 string representation.
     * This is useful for storing or transmitting image data in a text format.
     *
     * @param bitmap The Bitmap image to be encoded.
     * @return A Base64-encoded string representing the input Bitmap image.
     */
    public static String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Decodes a Base64 string into a Bitmap image.
     * This is useful for converting image data received in Base64 format back into a Bitmap.
     *
     * @param base64Str The Base64-encoded string to be decoded.
     * @return A Bitmap object decoded from the input Base64 string.
     */
    public static Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * Retrieves a Bitmap image from the provided URI.
     * This method loads an image from a content URI, such as from the gallery or file system.
     *
     * @param context The context used to access the content resolver.
     * @param uri     The URI pointing to the image to be loaded.
     * @return A Bitmap object representing the image at the given URI, or null if an error occurs.
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
