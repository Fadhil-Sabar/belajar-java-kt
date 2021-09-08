package CRUD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operasi {
    public static void updateData() throws IOException {
        // ambil db ORIGINAL
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // buat db sementeara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // tampil database
        System.out.println("List buku");
        tampilkanData();

        // ambil user Input
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukkan data yang ingin di update : ");
        int updateNum = terminalInput.nextInt();

        // tampilkan data yang akna di delete
        String data = bufferInput.readLine();
        int entryCounts = 0;

        while (data != null) {
            entryCounts++;

            StringTokenizer st = new StringTokenizer(data, ",");

            // tampilkan entryCounts kalo sama update
            if (updateNum == entryCounts) {
                System.out.println("Data yang ingin anda update adalah ");
                System.out.println("-----------------------------------");
                System.out.println("Referensi       : " + st.nextToken());
                System.out.println("Tahun           : " + st.nextToken());
                System.out.println("Penulis         : " + st.nextToken());
                System.out.println("Penerbit        : " + st.nextToken());
                System.out.println("Judul           : " + st.nextToken());

                // update data

                // ambil Input
                String[] fieldData = { "tahun", "penulis", "penerbit", "judul" };
                String[] tempData = new String[4];

                // refresh token
                st = new StringTokenizer(data, ",");
                String originalData = st.nextToken();

                for (int i = 0; i < fieldData.length; i++) {
                    boolean isUpdate = Utility.getYesOrNo("Apakah anda ingin merubah " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate) {
                        if (fieldData[i].equalsIgnoreCase("tahun")) {
                            System.out.println("Masukkan tahun terbit (YYYY) : ");
                            tempData[i] = Utility.ambilTahun();

                        } else {
                            // user Input
                            terminalInput = new Scanner(System.in);
                            System.out.print("\nMasukkan " + fieldData[i] + " baru");
                            tempData[i] = terminalInput.nextLine();

                        }

                    } else {
                        tempData[i] = originalData;
                    }

                }
                st = new StringTokenizer(data, ",");
                st.nextToken();
                // tampilkian data baru
                System.out.println("Data baru anda adalah ");
                System.out.println("-----------------------------------");
                System.out.println("Tahun           : " + st.nextToken() + "\t-->\t" + tempData[0]);
                System.out.println("Penulis         : " + st.nextToken() + "\t-->\t" + tempData[1]);
                System.out.println("Penerbit        : " + st.nextToken() + "\t-->\t" + tempData[2]);
                System.out.println("Judul           : " + st.nextToken() + "\t-->\t" + tempData[3]);

                boolean isUpdate = Utility.getYesOrNo("Apakah anda ingin mengupdate data tersebut? ");
                if (isUpdate) {
                    // cek data baru di database
                    boolean isExist = Utility.cekBukuDatabase(tempData, false);
                    if (isExist) {
                        System.err.println(
                                "Data buku sudah ada di db, proses update dibatalkan\nSilahkan delete data yang bersangkutan");

                    } else {
                        // format data baru dalam database
                        String tahun = tempData[0];
                        String penulis = tempData[1];
                        String penerbit = tempData[2];
                        String judul = tempData[3];

                        // bikin primaryKey
                        long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;
                        String penulisTanpaSpasi = penulis.replaceAll("\\s+", "");

                        String primaryKey = penulisTanpaSpasi + "_" + tahun + "_" + nomorEntry;

                        // tulis database
                        bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                    }

                } else {
                    // copy
                    bufferOutput.write(data);
                }
                System.out.println(Arrays.toString(tempData));

            } else {
                // copy
                bufferOutput.write(data);

            }
            bufferOutput.newLine();
            data = bufferInput.readLine();
        }
        bufferOutput.flush();
        bufferOutput.close();
        fileOutput.close();
        bufferInput.close();
        fileInput.close();
        System.gc();

        database.delete();

        // RENAME FILE SEMENTARA KE DATABASE
        tempDB.renameTo(database);
    }

    public static void deleteData() throws IOException {
        // kita ambil db ori
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // buat db smeentara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // tampil data
        System.out.println("List buku");
        tampilkanData();

        // kita ambil userinput untuk delete
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukkan nomor buku yang akan di hapus : ");
        long deleteNum = terminalInput.nextInt();

        // looping membaca tiap data baris dan skip data yang akan di delete
        boolean isFound = false;
        int entryCounts = 0;
        String data = bufferInput.readLine();
        while (data != null) {
            entryCounts++;
            StringTokenizer st = new StringTokenizer(data, ",");
            boolean isDelete = false;
            // tampilkan data yang ingin dihapus
            if (deleteNum == entryCounts) {
                System.out.println("\nData yang ingin anda hapus adalah :");
                System.out.println("=======================================");
                System.out.println("Referensi           : " + st.nextToken());
                System.out.println("Tahun               : " + st.nextToken());
                System.out.println("Penulis             : " + st.nextToken());
                System.out.println("Penerbit            : " + st.nextToken());
                System.out.println("Judul               : " + st.nextToken());

                isDelete = Utility.getYesOrNo("Apakah anda yakin akan menghapus?");
                isFound = true;
            }

            if (isDelete) {
                System.out.println("Data berhasil di hapus");
            } else {
                bufferOutput.write(data);
                bufferOutput.newLine();

            }

            data = bufferInput.readLine();
        }
        if (!isFound) {
            System.err.println("Buku tidak ditemukan");
        }
        // MENULIS DATA KE FILE
        bufferOutput.flush();
        bufferOutput.close();
        fileOutput.close();
        bufferInput.close();
        fileInput.close();

        System.gc();

        // DELETE ORIGINAL FILE
        database.delete();
        // RENAME FILE SEMENTARA KE DATABASE
        tempDB.renameTo(database);
    }

    public static void tampilkanData() throws IOException {

        FileReader fileInput;
        BufferedReader bufferInput;
        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e) {
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahDataBuku();
            return;
        }

        System.out.println("\n| No |\tTahun  |\tPenulis                 |\tPenerbit                |\tJudul Buku |");
        System.out.println(
                "========================================================================================================");
        String data = bufferInput.readLine();
        int nomorData = 0;

        while (data != null) {
            nomorData++;
            StringTokenizer stringToken = new StringTokenizer(data, ",");
            stringToken.nextToken();
            System.out.printf("| %2d ", nomorData);
            System.out.printf("|\t%4s   ", stringToken.nextToken());
            System.out.printf("|\t%-20s    ", stringToken.nextToken());
            System.out.printf("|\t%-20s    ", stringToken.nextToken());
            System.out.printf("|\t%s    \n", stringToken.nextToken());

            data = bufferInput.readLine();
        }
        System.out.println(
                "========================================================================================================");
        System.out.println("Akhir dari database");
    }

    public static void tambahDataBuku() throws IOException {
        FileWriter fileOutput = new FileWriter("database.txt", true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        Scanner terminalInput = new Scanner(System.in);
        String penulis, judul, penerbit, tahun;

        System.out.print("Masukkan nama penulis : ");
        penulis = terminalInput.nextLine();
        System.out.print("Masukkan judul buku : ");
        judul = terminalInput.nextLine();
        System.out.print("Masukkan nama penerbit : ");
        penerbit = terminalInput.nextLine();
        System.out.print("Masukkan tahun terbit(YYYY) : ");
        tahun = terminalInput.nextLine();

        String[] keywords = { tahun + "," + penulis + "," + penerbit + "," + judul };
        System.out.println(Arrays.toString(keywords));

        boolean isExist = Utility.cekBukuDatabase(keywords, false);

        if (!isExist) {
            long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;
            String penulisTanpaSpasi = penulis.replaceAll("\\s+", "");

            String primaryKey = penulisTanpaSpasi + "_" + tahun + "_" + nomorEntry;
            System.out.println("\nData yang akan anda masukan adalah");
            System.out.println("----------------------------------------");
            System.out.println("primary key  : " + primaryKey);
            System.out.println("tahun terbit : " + tahun);
            System.out.println("penulis      : " + penulis);
            System.out.println("judul        : " + judul);
            System.out.println("penerbit     : " + penerbit);

            boolean isTambah = Utility.getYesOrNo("Apakah anda ingin menambah data tersebut? : ");
            if (isTambah) {
                bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                bufferOutput.newLine();
                bufferOutput.flush();
            }
        } else {
            System.out.println("Buku yang akan anda masukkan sudah ada di Database dengan data berikut :");
            Utility.cekBukuDatabase(keywords, true);
        }

        bufferOutput.close();
    }

    public static void cariData() throws IOException {
        try {

            File file = new File("database.txt");
        } catch (Exception e) {
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahDataBuku();
            return;
        }
        Scanner terminalInput = new Scanner(System.in);
        System.out.println("Masukkan kata kunci untuk mencari buku ");
        String cariString = terminalInput.nextLine();

        String[] keywords = cariString.split("\\s+");

        Utility.cekBukuDatabase(keywords, true);

    }

}
