package others;

import statistical.StatisticalManager;

import java.util.Scanner;

public class Menu {

    public static StatisticalManager[] statisticalManagerAcllmbds;
    public static StatisticalManager[] statisticalManagerAmazon;

    public static void start(int maxLinesToBeProcessedStatistical) {
        if (yesNoQuestion("Czy chcesz wykonac analize statystyczna?")) {
            boolean charts = yesNoQuestion("Czy chcesz generowac wykresy?");
            boolean files = yesNoQuestion("Czy chcesz generowac pliki txt?");
            int aclMax = numericQuestion("Jak duzo chcesz obliczyc dla zbioru acllmbd? [0 jesli dla tego zbioru nie chcesz liczyc]");
            int amazonMax = numericQuestion("Jak duzo chcesz obliczyc dla zbioru amazona? [0 jesli dla tego zbioru nie chcesz liczyc]");
            if (aclMax > 0)
                statisticalManagerAcllmbds = MainManagement.calculateStatisticalDataAcllmbd(aclMax, files, charts);
            if (amazonMax > 0)
                statisticalManagerAmazon = MainManagement.calculateStatisticalDataAcllmbd(amazonMax, files, charts);
        }

        System.out.println("Zakonczono.");
    }

    public static boolean yesNoQuestion(String question) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(question + " [T/N]");
            String s = scanner.nextLine();
            if (s.contains("t") || s.contains("T"))
                return true;
            else if (s.contains("n") || s.contains("N"))
                return false;
            else System.out.println("Nieprawidlowa wartosc - wpisz 't' lub 'n'.");
        }
    }

    public static int numericQuestion(String question) {
        System.out.println(question);
        Scanner sn = new Scanner(System.in);
        while (true) {
            String line = sn.nextLine();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.err.println("Niepoprawna wartosc - podaj liczbe:");
            }
        }
    }
}
