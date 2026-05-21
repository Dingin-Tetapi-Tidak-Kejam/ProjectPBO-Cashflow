package util;

import model.ExpenseCategory;
import model.ExpenseSubCategory;
import model.IncomeSource;

import java.util.Scanner;

public class InputHelper {

    private static final Scanner input = new Scanner(System.in);

    public static int inputInt(String message) {

        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(input.nextLine());

                if (value < 0) {
                    System.out.println("Input tidak boleh negatif!");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat!");
            }
        }
    }

    public static double inputDouble(String message) {

        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(input.nextLine());

                if (value <= 0) {
                    System.out.println("Amount harus lebih dari 0!");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }

    public static String inputString(String message) {

        while (true) {
            System.out.print(message);
            String value = input.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("Input tidak boleh kosong!");
                continue;
            }

            return value;
        }
    }

    public static String inputEmail(String message) {

        while (true) {
            System.out.print(message);
            String email = input.nextLine().trim();

            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Format email tidak valid!");
                continue;
            }

            return email;
        }
    }

    public static String inputPassword(String message) {

        while (true) {
            System.out.print(message);
            String password = input.nextLine();

            if (password.length() < 6) {
                System.out.println("Password minimal 6 karakter!");
                continue;
            }

            return password;
        }
    }

    public static IncomeSource chooseIncomeSource() {

        IncomeSource[] sources = IncomeSource.values();

        System.out.println("\nPilih Source:");

        for (int i = 0; i < sources.length; i++) {

            System.out.println((i + 1) + ". " + sources[i]);
        }

        while (true) {

            int choice = inputInt("Masukkan pilihan: ");

            if (choice >= 1 && choice <= sources.length) {

                return sources[choice - 1];
            }

            System.out.println("Pilihan tidak valid!");
        }
    }

    public static ExpenseCategory chooseExpenseCategory() {

        ExpenseCategory[] categories = ExpenseCategory.values();

        System.out.println("\nPilih Category:");

        for (int i = 0; i < categories.length; i++) {

            System.out.println((i + 1) + ". " + categories[i]);
        }

        while (true) {

            int choice = inputInt("Masukkan pilihan: ");

            if (choice >= 1 && choice <= categories.length) {

                return categories[choice - 1];
            }

            System.out.println("Pilihan tidak valid!");
        }
    }

    public static ExpenseSubCategory chooseExpenseSubCategory() {

        ExpenseSubCategory[] subCategories = ExpenseSubCategory.values();

        System.out.println("\nPilih Sub Category:");

        for (int i = 0; i < subCategories.length; i++) {

            System.out.println((i + 1) + ". " + subCategories[i]);
        }

        while (true) {

            int choice = inputInt("Masukkan pilihan: ");

            if (choice >= 1 && choice <= subCategories.length) {

                return subCategories[choice - 1];
            }

            System.out.println("Pilihan tidak valid!");
        }
    }
}