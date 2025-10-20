package com.lupay.demoui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.LayoutRes;

import com.dspread.xpos.QPOSService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Utils {
	//BASE DE DATOS
	public static final String TERMINAL_WL_Name = "wl_lupay";
	//BUCKET CORREO
	public static final String TERMINAL_WL_Bucket = "lupay";
	//PRODUCCION
	public static String TERMINAL_API = "";
	public static final String TERMINAL_BIN = "https://alphawlapi.efevoopaylbda.com/api/apiv0/agrs/terminales/tpv/bines";
	public static final String TPVCONFIG = "https://alphawlapi.efevoopaylbda.com";
	public static final String TERMINAL_AMEX = "https://efevoopayamex-ecommerce.com";
	public static final String TERMINAL_BATCH = "https://efevoopaybatch-ecommerce.com";
	public static final String TPVCONFIGAMEX = "https://testagrswl.efevoo.app";
	// TEST
//	public static final String TERMINAL_API = "https://test-efevoopayloadbalancer-ecommerce.com";
//	public static final String TERMINAL_BIN = "https://alphawlapitest.efevoopaylbda.com/api/apiv0/agrs/terminales/tpv/bines";
//	public static final String TPVCONFIG = "https://alphawlapitest.efevoopaylbda.com";
//	public static final String TERMINAL_AMEX="https://test-efevoopayamex-ecommerce.com";
//	public static final String TERMINAL_BATCH = "https://test-efevoopaybatch-ecommerce.com";
//	public static final String TPVCONFIGAMEX = "https://test-agrs.efevoo.app";

	public static HashMap<String,String> errorMessagesDictionary;
	public static HashMap<QPOSService.Error, String> errorPosDictionary;
	private static char MASK_CHAR = '*';

	public static String tipoUrl(String _interfaz){
		if(_interfaz.equals("Adquiriente")){
			TERMINAL_API="https://adq.efevoopayloadbalancer-ecommerce.com";
		}else{
			TERMINAL_API="https://efevoopayloadbalancer-ecommerce.com";
		}
		return TERMINAL_API;
	}

	public static void setErrorMessages() {
		errorMessagesDictionary = new HashMap();
		errorMessagesDictionary.put("com.android.volley.timeout", "Límite de tiempo excedido");
		errorMessagesDictionary.put("invalid amount", "Límite de tiempo excedido");
		errorMessagesDictionary.put("com.android.volley.noconnectionerror", "Conexión no exitosa, favor de realizar prueba de comunicación");

		errorPosDictionary = new HashMap();
		errorPosDictionary.put(QPOSService.Error.UNKNOWN, "Tarjeta no leída, intente de nuevo.");
		errorPosDictionary.put(QPOSService.Error.CMD_TIMEOUT, "Límite de tiempo excedido");
		errorPosDictionary.put(QPOSService.Error.TIMEOUT, "Límite de tiempo excedido");
		errorPosDictionary.put(QPOSService.Error.APP_SELECT_TIMEOUT, "Límite de tiempo excedido");
	}

	public static String bytes2Hex(byte[] data) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			if ((i & 0x0f) == 0) {
				sb.append("\n");
			}
			sb.append(String.format("%02X ", data[i]));
		}

		return sb.toString();
	}

	/*
	 * v must be less than 100 and great than -1
	 **/
	public static byte int2d2Bcd(int v) {
		return (byte) (((v / 10) << 4) | ((v % 10) & 0x0F));
	}

	/*
	 * v must be less than 16 and greater than -1
	 **/
	public static byte int1d2leftbyte(byte b, int v) {
		return (byte) ((b & 0x0f) | ((v & 0x0f) << 4));
	}

	/*
	 * v must be less than 16 and greater than -1
	 **/
	public static byte int1d2rightbyte(byte b, int v) {
		return (byte) ((b & 0xf0) | (v & 0x0f));
	}

	public static int byteright2int(byte b) {
		return b & 0x0f;
	}

	/*
	 * left aligned, v must be less than 100 and great than -1
	 **/
	public static byte int2d2Bcdl(int v) {
		if (v < 10) {
			v *= 10;
		}
		return (byte) (((v / 10) << 4) | ((v % 10) & 0x0F));
	}

	/* v must be great than 0 */
	public static byte[] int2Bcd(int v) {
		int n = countBytes(v);
		byte[] data = new byte[n];

		for (int i = n - 1; i > -1; i--) {
			data[i] = int2d2Bcd(v % 100);
			v /= 100;
		}

		return data;
	}

	/*
	 * v must be great than 0
	 **/
	public static int int2Bcd(int v, byte[] data) {
		int nb = countBytes(v);
		if (nb > data.length) {
			return -1;
		}

		int n = data.length;
		int i = n - 1;

		for (; i > n - nb - 1; i--) {
			data[i] = int2d2Bcd(v % 100);
			v /= 100;
		}

		for (; i > -1; i--) {
			data[i] = 0;
		}

		return 0;
	}

	/*
	 * v must be great than 0
	 **/
	public static int int2Bcd(int v, byte[] data, int offset, int length) {
		int nb = countBytes(v);
		if (nb > length) {
			return -1;
		}

		int last = offset + length - 1;
		int i = last;

		for (; i > last - nb; i--) {
			data[i] = int2d2Bcd(v % 100);
			v /= 100;
		}

		for (; i >= offset; i--) {
			data[i] = 0;
		}

		return 0;
	}

	public static <T> T isNull(T value, T replace) {
		return value != null ? value : replace;
	}

	public static String isVacio(String[] value, int pos) {
		if (value.length > pos) {
			return value[pos].toString().trim().toUpperCase(Locale.ROOT);
		} else {
			return "";
		}
	}

	public static Bitmap viewToBitmap(View view) {
		int measuredWidth = View.MeasureSpec.makeMeasureSpec(384, View.MeasureSpec.EXACTLY);
		int measuredHeight = View.MeasureSpec.makeMeasureSpec(100000, View.MeasureSpec.UNSPECIFIED);
		view.measure(measuredWidth, measuredHeight);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		int w = view.getWidth();
		int h = view.getHeight();
		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		c.drawColor(-1);
		view.invalidate();
		view.draw(c);
		return bmp;
	}

	/*
	 * v must be great than 0
	 **/
	public static int long2Bcd(long v, byte[] data, int offset, int length) {
		int nb = countBytes(v);
		if (nb > length) {
			return -1;
		}

		int last = offset + length - 1;
		int i = last;

		for (; i > last - nb; i--) {
			data[i] = int2d2Bcd((int) (v % 100));
			v /= 100;
		}

		for (; i >= offset; i--) {
			data[i] = 0;
		}

		return 0;
	}

	public static int numstr2Bcd(byte[] numstr, byte[] data, int offset, int length) {
		int vlen = numstr.length;
		int nb = (vlen + 1) >> 1;
		if (nb > length) {
			return -1;
		}

		byte[] str = numstr;

		int vlast = vlen;
		if ((vlen & 0x01) != 0) {
			vlast--;
		}

		int p = offset;
		int i = 0;
		for (; i < vlast; i += 2) {
			data[p++] = (byte) ((((str[i] - 0x30) & 0x0f) << 4) | ((str[i + 1] - 0x30) & 0x0f));
		}

		if ((vlen & 0x01) != 0) {
			data[p++] = (byte) (((str[i] - 0x30) & 0x0f) << 4);
		}

		return 0;
	}

	/*
	 * left aligned, v must be great than 0
	 **/
	public static byte[] int2Bcdl(int v) {
		int numbers = countNumbers(v);
		if ((numbers & 0x01) == 1) {
			v *= 10;
		}

		byte[] data = int2Bcd(v);

		return data;
	}

	/*
	 * left aligned, v must be great than 0
	 **/
	public static int int2Bcdl(int v, byte[] data) {
		int numbers = countNumbers(v);
		if ((numbers & 0x01) == 1) {
			v *= 10;
		}

		byte[] vd = int2Bcd(v);
		if (vd.length > data.length) {
			return -1;
		}

		int i = 0;
		for (; i < vd.length; i++) {
			data[i] = vd[i];
		}
		for (; i < data.length; i++) {
			data[i] = 0;
		}

		return 0;
	}

	public static int countBytes(long v) {
		int n = 0;
		while (v > 0) {
			n++;
			v /= 100;
		}
		return n;
	}

	public static int countNumbers(long v) {
		int n = 0;
		while (v > 0) {
			n++;
			v /= 10;
		}
		return n;
	}

	public static int bcd2Int(byte b) {
		return ((b >> 4) & (byte) 0x0F) * 10 + (b & 0x0F);
	}

	public static int bcd2Int(byte b1, byte b2) {
		return bcd2Int(b1) * 100 + bcd2Int(b2);
	}

	public static int bcd2Int(byte[] data, int offset, int length) {
		if (length > 4) {
			/* numbers of int value should not be great than 8 */
			return -1;
		}

		int p = offset;
		int last = offset + length;
		int v = 0;
		while (p < last) {
			if (p + 1 < last) {
				/* at lest 2 bytes remained */
				v = v * 10000 + bcd2Int(data[p], data[p + 1]);
				p += 2;
			} else {
				/* only one byte remained */
				v = v * 100 + bcd2Int(data[p]);
				p++;
			}
		}
		return v;
	}

	public static long bcd2Long(byte[] data, int offset, int length) {
		// long value has 19 numbers; the leftmost number is 9
		if (length > 9) {
			/* numbers of int value should not be great than 8 */
			return -1;
		}

		int p = offset;
		int last = offset + length;
		if ((length & 0x01) != 0) {
			last--;
		}

		long v = 0;
		while (p < last) {
			v = v * 10000 + (long) bcd2Int(data[p], data[p + 1]);
			p += 2;
		}

		if ((length & 0x01) != 0) {
			v = v * 100 + (long) bcd2Int(data[p]);
			p++;
		}

		return v;
	}

	public static int bcd2Numstr(byte[] data, int offset, int length, byte[] numstr, int strlen) {
		int maxstrlen = (length << 1);
		maxstrlen = maxstrlen < strlen ? maxstrlen : strlen;
		maxstrlen = maxstrlen < numstr.length ? maxstrlen : numstr.length;

		int p = offset;
		int vlast = maxstrlen;
		if ((strlen & 0x01) != 0) {
			vlast--;
		}

		int i = 0;
		for (; i < vlast; i += 2) {
			numstr[i] = (byte) (((data[p] & 0xf0) >>> 4) + 0x30);
			numstr[i + 1] = (byte) ((data[p] & 0x0f) + 0x30);
			p++;
		}

		if ((strlen & 0x01) != 0) {
			numstr[i] = (byte) (((data[p] & 0xf0) >>> 4) + 0x30);
		}

		return maxstrlen;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] int2Byte(int intValue) {
		byte[] b = new byte[4];
		byte[] r = new byte[4];

		for (int i = 0; i < 4; ++i) {
			b[i] = (byte) (intValue >> 8 * (3 - i) & 255);
		}

		r[3] = b[0];
		r[2] = b[1];
		r[1] = b[2];
		r[0] = b[3];
		return r;
	}

	public static ProgressDialog getLoaderSpinner(Context ctx, String... title) {
		ProgressDialog nDialog = new ProgressDialog(ctx);
		nDialog.setMessage(title.length > 0 ? title[0] : "Cargando...");
		nDialog.setIndeterminate(false);
		nDialog.setCancelable(false);

		return nDialog;
	}

	public static View getViewByResource(@LayoutRes int Resource, LayoutInflater inflater) {
		return inflater.inflate(Resource, null);
	}

	public static void LoadingTask(Context ctx, LoaderTask taskhandle) {

		ProgressDialog nDialog = new ProgressDialog(ctx);
		nDialog.setMessage("Cargando...");
		nDialog.setIndeterminate(false);
		nDialog.setCancelable(false);
		nDialog.show();

		Thread tr = new Thread(() -> {
			try {
				Thread.sleep(0);
				taskhandle.Task();
				nDialog.dismiss();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		tr.start();

	}

	public static boolean patternMatches(String str, String pattern) {
		return Pattern.compile(pattern)
				.matcher(str)
				.matches();
	}

	public static boolean isValidEmail(String email) {
		return patternMatches(email, "^.+@.+(\\.[^\\.]+)+$");
	}

	public static int tryIntParse(String num, int ...defaultInt) {
		try {
			return Integer.parseInt(num);
		} catch (Exception e) {
			return defaultInt.length > 0 ? defaultInt[0] : 0;
		}
	}

	public static String maskText(String text, int maskLenght, char mask, boolean... start) {
		MASK_CHAR = mask;
		return maskText(text, maskLenght, start);
	}

	public static String maskText(String text, int maskLenght, boolean... start) {
		String replaceAllRegex = "[\\s\\S]";
		if (text.length() < maskLenght)
			return text;
		if (text.length() == maskLenght)
			return text.replaceAll(replaceAllRegex, Character.toString(MASK_CHAR));
		int unmaskedLenght = text.length() - maskLenght;
		boolean _start = start.length > 0 && start[0];
		String unmaskedText = _start ? text.substring(0, maskLenght) : text.substring(unmaskedLenght);
		StringBuilder maskedText = new StringBuilder();
		for (int i = 0; i <= unmaskedLenght; i++) {
			maskedText.append(MASK_CHAR);
		}
		MASK_CHAR = '*';
		return _start ? unmaskedText + maskedText : maskedText + unmaskedText;
	}


	public static Date dateToUTC(Long timestamp) {
		TimeZone timeZoneUTC = TimeZone.getDefault();
		int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
		return new Date(timestamp + offsetFromUTC);
	}

}
