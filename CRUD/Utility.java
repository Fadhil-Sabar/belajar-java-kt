package CRUD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {

    static long ambilEntryPerTahun(String penulis, String tahun) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while (data != null) {
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            penulis = penulis.replaceAll("\\s+", "");
            if (penulis.equalsIgnoreCase(dataScanner.next()) && tahun.equalsIgnoreCase(dataScanner.next())) {
                entry = dataScanner.nextInt();
            }
            data = bufferInput.readLine();
        }
        return entry;
    }

    protected static String ambilTahun() throws IOException {
        Scanner terminalInput = new Scanner(System.in);
        String tahun = terminalInput.nextLine();
        boolean tahunValid = false;

        while (!tahunValid) {
            try {
                Year.parse(tahun);
                tahunValid = true;
            } catch (Exception e) {
                System.out.println("Format tahun yang anda masukan salah, format=(YYYY)");
                System.out.print("silahkan masukan tahun terbit lagi: ");
                tahunValid = false;
                tahun = terminalInput.nextLine();
            }
        }
        return tahun;
    }

    static boolean cekBukuDatabase(String[] keywords, boolean isDisplay) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        boolean isExist = false;
        String data = bufferInput.readLine();
        int nomorData = 0;
        if (isDisplay) {

            System.out
                    .println("\n| No |\tTahun  |\tPenulis                 |\tPenerbit                |\tJudul Buku |");
            System.out.println(
                    "========================================================================================================");
        }
        while (data != null) {
            isExist = true;
            for (String keyword : keywords) {
                isExist = isExist && data.toLowerCase().contains(keyword);
            }

            if (isExist) {
                if (isDisplay) {

                    nomorData++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");
                    stringToken.nextToken();
                    System.out.printf("| %2d ", nomorData);
                    System.out.printf("|\t%4s   ", stringToken.nextToken());
                    System.out.printf("|\t%-20s    ", stringToken.nextToken());
                    System.out.printf("|\t%-20s    ", stringToken.nextToken());
                    System.out.printf("|\t%s    \n", stringToken.nextToken());
                } else {
                    break;
                }
            }
            data = bufferInput.readLine();
        }
        if (isExist) {
            System.out.println(
                    "========================================================================================================");
        }
        return isExist;
    }

    public static boolean getYesOrNo(String message) {
        System.out.print("\n" + message + " (y/n) : ");
        Scanner terminalInput = new Scanner(System.in);
        String pilihanUser = terminalInput.next();

        while (!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")) {
            System.err.println("Pilihan anda bukan (y/n)");
            System.out.print("\n" + message + " (y/n) : ");
            pilihanUser = terminalInput.next();
        }

        return pilihanUser.equalsIgnoreCase("y");

    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {
            System.out.println("Tidak bisa clear screen");
        }
    }
}
