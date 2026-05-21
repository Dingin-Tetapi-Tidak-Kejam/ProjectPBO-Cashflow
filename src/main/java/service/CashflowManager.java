package service;

import java.text.NumberFormat;
import java.util.Locale;
import cashflow.model.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CashflowManager {

    private String formatRupiah(double amount) {

        NumberFormat format =
                NumberFormat.getNumberInstance(new Locale("id", "ID"));

        return "Rp " + format.format(amount);
    }

    public String getFormattedBalance() {

        return formatRupiah(getBalance());
    }

    private List<Transaction> transactions;

    public CashflowManager() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {

        transactions.add(transaction);
    }

    public void removeTransaction(int id) {

        Iterator<Transaction> iterator = transactions.iterator();

        while (iterator.hasNext()) {

            Transaction t = iterator.next();

            if (t.getId() == id) {

                iterator.remove();
                System.out.println("Transaksi berhasil dihapus!");
                return;
            }
        }

        System.out.println("Transaksi tidak ditemukan!");
    }

    public double getBalance() {

        double balance = 0;

        for (Transaction t : transactions) {

            if (t.getTransactionType().equals("INCOME")) {

                balance += t.getAmount();

            } else {

                balance -= t.getAmount();
            }
        }

        return balance;
    }

    public void showAllTransactions() {

        if (transactions.isEmpty()) {

            System.out.println("Belum ada transaksi.");
            return;
        }

        System.out.println("\n===== DAFTAR TRANSAKSI =====");

        for (Transaction t : transactions) {

            System.out.println("----------------------------");
            System.out.println("ID          : " + t.getId());
            System.out.println("Type        : " + t.getTransactionType());
            System.out.println("Amount      : " + formatRupiah(t.getAmount()));
            System.out.println("Date        : " + t.getDate());
            System.out.println("Description : " + t.getDescription());
            System.out.println("Detail      : " + t.getDetail());
        }
    }
}