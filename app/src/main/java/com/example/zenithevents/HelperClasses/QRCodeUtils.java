package com.example.zenithevents.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * A utility class that provides various methods for handling QR codes and related operations.
 * <p>Note: The JavaDocs for this class were generated using OpenAI's ChatGPT.
 * <p>
 * This class includes methods to:
 * <ul>
 *     <li>Generate a QR code for a given event ID.</li>
 *     <li>Convert a BitMatrix (from QR code generation) into a Bitmap.</li>
 *     <li>Hash QR code content using the SHA-256 hashing algorithm.</li>
 *     <li>Generate random alphanumeric strings of a specified length.</li>
 *     <li>Encode a Bitmap image into a Base64 string for storage or transmission.</li>
 *     <li>Decode a Base64 string back into a Bitmap image.</li>
 * </ul>
 * <p>
 * The methods in this class can be useful in scenarios where QR codes are needed for event registration, authentication,
 * or sharing event-related data via QR codes.
 */
public class QRCodeUtils {

    /**
     * Generates a QR code as a Bitmap for a given event ID.
     *
     * @param eventId The event ID to be encoded in the QR code.
     * @return A Bitmap representing the generated QR code, or null if an error occurs.
     */
    public static Bitmap generateQRCode(String eventId) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(eventId, BarcodeFormat.QR_CODE, 200, 200);
            return toBitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a BitMatrix to a Bitmap.
     *
     * @param bitMatrix The BitMatrix representation of the QR code.
     * @return A Bitmap object representing the QR code.
     */
    private static Bitmap toBitmap(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }

    /**
     * Hashes the content of a QR code using the SHA-256 algorithm.
     *
     * @param qrCodeContent The content to be hashed (e.g., QR code data).
     * @return The SHA-256 hash of the QR code content as a hexadecimal string,
     *         or null if an error occurs.
     */
    public static String hashQRCodeData(String qrCodeContent) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(qrCodeContent.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a random alphanumeric string of a specified length.
     *
     * @param length The desired length of the random string.
     * @return A randomly generated alphanumeric string of the specified length.
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

    /**
     * Encodes a Bitmap as a Base64 string.
     *
     * @param bitmap The Bitmap to be encoded.
     * @return A Base64 encoded string representing the Bitmap.
     */
    public static String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Decodes a Base64 string into a Bitmap.
     *
     * @param base64Str The Base64 encoded string to decode.
     * @return A Bitmap decoded from the Base64 string.
     */
    public static Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}