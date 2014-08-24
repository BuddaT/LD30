package net.buddat.ludumdare.ld30;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class Utilities {

	public static final int ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_RIGHT = 2;

	public static void renderText(Font f, String str, int x, int y, int align, Color c,
			boolean wrap, int width) {
		String[] split = str.split(" ");
		String[] lines = new String[f.getWidth(str) / width + 1];

		int currentLine = 0;
		lines[currentLine] = "";
		for (String s : split) {
			if (f.getWidth(lines[currentLine] + s) < width) {
				lines[currentLine] += s + " ";
			} else {
				lines[++currentLine] = "";
				lines[currentLine] += s + " ";
			}
		}

		for (int i = 0; i <= currentLine; i++)
			renderText(f, lines[i], x, y + f.getLineHeight() * i, align, c);
	}

	public static void renderText(Font f, String str, int x, int y, int align, Color c) {
		Font currentFont = f;
		int textWidth = currentFont.getWidth(str);

		switch (align) {
		case ALIGN_CENTER:
			x -= textWidth / 2;
			break;
		case ALIGN_RIGHT:
			x -= textWidth;
			break;
		}

		f.drawString(x, y, str, c);
	}

}
