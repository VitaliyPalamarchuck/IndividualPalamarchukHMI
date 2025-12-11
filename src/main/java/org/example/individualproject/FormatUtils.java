package org.example.individualproject;

import java.text.DecimalFormat;

public class FormatUtils {

    /**
     * Форматує великі числа, скорочуючи їх до мільйонів, мільярдів і т.д.
     * @param number Число для форматування.
     * @return Відформатований рядок.
     */
    public static String formatLargeNumber(double number) {
        if (Math.abs(number) < 1_000_000) {
            return new DecimalFormat("#,##0.00").format(number);
        }

        String sign = number < 0 ? "-" : "";
        double absNumber = Math.abs(number);

        if (absNumber < 1_000_000_000) { // Менше мільярда
            return sign + new DecimalFormat("0.##").format(absNumber / 1_000_000) + " млн";
        } else if (absNumber < 1_000_000_000_000L) { // Менше трильйона
            return sign + new DecimalFormat("0.##").format(absNumber / 1_000_000_000) + " млрд";
        } else if (absNumber < 1_000_000_000_000_000L) { // Менше квадрильйона
            return sign + new DecimalFormat("0.##").format(absNumber / 1_000_000_000_000L) + " трлн";
        } else { // Квадрильйон і більше
            return sign + new DecimalFormat("0.##").format(absNumber / 1_000_000_000_000_000L) + " квдрлн";
        }
    }
}
