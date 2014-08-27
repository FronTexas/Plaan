package com.example.plaan;

import android.graphics.Typeface;
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

	public void setTypeface(TextView v, String typeface) {
		pacifico = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/pacifico.ttf");
		leagueGothic = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/leaguegothic.otf");
		titilium = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/titilium.otf");
		leagueGothicItalic = Typeface.createFromAsset(v.getContext()
				.getAssets(), "fonts/leaguegothic_italic.ttf");
		openSansExtraBold = Typeface.createFromAsset(
				v.getContext().getAssets(), "fonts/opensans_extra_bold.ttf");
		openSansSemiBold = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/opensans_semi_bold.ttf");
		openSansItalic = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/OpenSans-BoldItalic.ttf");

		switch (typeface) {
		case PACIFICO:
			v.setTypeface(pacifico);
			break;
		case LEAGUE_GOTHIC:
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
