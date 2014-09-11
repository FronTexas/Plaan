package com.example.plaan;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class TypefacePlaan {
	public static final String TITILIUM = "titilium";
	public static final String PACIFICO = "pacifico";
	public static final String LEAGUE_GOTHIC = "leagueGothic";
	public static final String LEAGUE_GOTHIC_ITALIC = "leagueGothicItalic";
	public static final String OPEN_SANS_BOLD = "openSansBold";
	public static final String OPEN_SANS_SEMIBOLD = "openSansSemiBold";
	public static final String OPEN_SANS_ITALIC = "openSansItalic";

	private Typeface pacifico, leagueGothic, titilium, leagueGothicItalic,
			openSansExtraBold, openSansSemiBold, openSansItalic;

	public TypefacePlaan(Context context) {
		pacifico = Typeface.createFromAsset(context.getAssets(),
				"fonts/pacifico.ttf");
		leagueGothic = Typeface.createFromAsset(context.getAssets(),
				"fonts/leaguegothic.otf");
		titilium = Typeface.createFromAsset(context.getAssets(),
				"fonts/titilium.otf");
		leagueGothicItalic = Typeface.createFromAsset(context.getAssets(),
				"fonts/leaguegothic_italic.ttf");
		openSansExtraBold = Typeface.createFromAsset(context.getAssets(),
				"fonts/opensans_extra_bold.ttf");
		openSansSemiBold = Typeface.createFromAsset(context.getAssets(),
				"fonts/opensans_semi_bold.ttf");
		openSansItalic = Typeface.createFromAsset(context.getAssets(),
				"fonts/OpenSans-BoldItalic.ttf");
	}

	public void setTypeface(TextView v, String typeface) {
		switch (typeface) {
		case PACIFICO:
			v.setTypeface(pacifico);
			break;
		case LEAGUE_GOTHIC:
			Log.d("Inflating Error", "IE -- v = " + v);
			Log.d("Inflating Error", "IE -- leagueGothic = " + leagueGothic);
			v.setTypeface(leagueGothic);
			break;
		case LEAGUE_GOTHIC_ITALIC:
			v.setTypeface(leagueGothicItalic);
			break;
		case "titilium":
			v.setTypeface(titilium);
			break;
		case OPEN_SANS_BOLD:
			v.setTypeface(openSansExtraBold);
			break;
		case OPEN_SANS_SEMIBOLD:
			v.setTypeface(openSansSemiBold);
		case OPEN_SANS_ITALIC:
			v.setTypeface(openSansItalic);
		}
	}
}
