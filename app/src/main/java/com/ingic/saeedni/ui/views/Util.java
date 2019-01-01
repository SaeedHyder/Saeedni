package com.ingic.saeedni.ui.views;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.ingic.saeedni.R;

@SuppressLint("StringFormatInvalid")

public class Util {
	private static long lastClickTime;

	public static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();
	
	public static void setTypeface(AttributeSet attrs, TextView textView ) {
		Context context = textView.getContext();
		
		TypedArray values = context.obtainStyledAttributes( attrs,
				R.styleable.AnyTextView );
		String typefaceName = values
				.getString( R.styleable.AnyTextView_typeface );
		
		if ( typefaceCache.containsKey( typefaceName ) ) {
			textView.setTypeface( typefaceCache.get( typefaceName ) );
		} else {
			Typeface typeface;
			try {
				typeface = Typeface.createFromAsset( textView.getContext()
						.getAssets(),
						context.getString( R.string.assets_fonts_folder )
								+ typefaceName );
			} catch ( Exception e ) {
				Log.v( context.getString( R.string.app ), String.format(
						context.getString( R.string.typeface_not_found ),
						typefaceName ) );
				return;
			}
			
			typefaceCache.put( typefaceName, typeface );
			textView.setTypeface( typeface );
		}
		values.recycle();
	}
	
	public static void setTypefaceUpdated( AttributeSet attrs, TextView textView ) {
		Context context = textView.getContext();
		
		TypedArray values = context.obtainStyledAttributes( attrs,
				R.styleable.AnyTextView );
		String typefaceName = values
				.getString( R.styleable.AnyTextView_typeface );
		
		if ( typefaceCache.containsKey( typefaceName ) ) {
			textView.setTypeface( typefaceCache.get( typefaceName ) );
		} else {
			Typeface typeface;
			try {
				typeface = Typeface.createFromAsset( textView.getContext()
						.getAssets(),
						context.getString( R.string.assets_fonts_folder )
								+ typefaceName );
			} catch ( Exception e ) {
				Log.v( context.getString( R.string.app ), String.format(
						context.getString( R.string.typeface_not_found ),
						typefaceName ) );
				return;
			}
			
			typefaceCache.put( typefaceName, typeface );
			textView.setTypeface( typeface );
		}
		
		values.recycle();
	}
	public static boolean doubleClickCheck() {
		if (SystemClock.elapsedRealtime() - lastClickTime < 200) {
			return false;
		}
		lastClickTime = SystemClock.elapsedRealtime();
		return true;
	}

}