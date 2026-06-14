# 💰 CashFlow — Aplikasi Manajemen Keuangan Pribadi

> Proyek Akhir Mata Praktikum Pemrograman Berorientasi Objek  
> Kelompok **Dingin Tetapi Tidak Kejam**

---

## 👥 Anggota Kelompok

| 1  | Nasywa Zavira Lubis | 241401006 | 
| 2  | Dewi Fortuna Halim | 241401066 | 
| 3  | Pocut Qanitah Putroe Azizul | 241401105 | 
| 4  | Valmai Imtiyaz | 241401135 | 

---

## 📌 Deskripsi Aplikasi

CashFlow adalah aplikasi manajemen keuangan pribadi berbasis desktop yang memungkinkan pengguna mencatat, memantau, dan menganalisis transaksi keuangan mereka secara terstruktur.

Pengguna dapat mencatat pemasukan (income) dan pengeluaran (expense) dengan kategori berjenjang, melihat ringkasan saldo secara real-time, serta mencari riwayat transaksi berdasarkan kata kunci. Seluruh data terlindungi oleh sistem autentikasi berbasis session sehingga setiap pengguna hanya bisa mengakses data miliknya sendiri.

---

## 🛠️ Teknologi yang Digunakan

### Backend
| Teknologi | Versi | Fungsi |
|-----------|-------|--------|
| Java | 21 | Bahasa pemrograman utama |
| Spring Boot | 3.4.5 | Framework backend |
| Spring Data JPA | — | Abstraksi database (ORM) |
| Spring Security | — | Keamanan dan enkripsi password |
| BCryptPasswordEncoder | — | Hash password sebelum disimpan |
| H2 Database | — | Database file-based (`cashflowdb.mv.db`) |
| Maven | — | Build tool dan dependency manager |

### Frontend
| Teknologi | Versi | Fungsi |
|-----------|-------|--------|
| Java | 21 | Bahasa pemrograman utama |
| JavaFX | 21.0.2 | Framework GUI desktop |
| FXML | — | Markup layout tampilan |
| Java HttpClient | — | Komunikasi HTTP ke backend |
| Maven | — | Build tool |

---

## ✨ Fitur Utama

- **Registrasi & Login** — Daftar akun baru, login dengan username atau email, password dienkripsi BCrypt
- **Dashboard** — Menampilkan total pemasukan, pengeluaran, dan saldo bersih secara real-time
- **Tambah Pemasukan** — Input income dengan kategori: Gaji, Bonus, Uang Saku, Freelance, Investasi, Lainnya
- **Tambah Pengeluaran** — Input expense dengan kategori berjenjang (misal: Makanan → Makan Siang)
- **Riwayat Transaksi** — Daftar semua transaksi diurutkan dari terbaru, lengkap dengan tanggal dan detail
- **Pencarian** — Cari transaksi berdasarkan kata kunci di deskripsi maupun detail
- **Hapus Transaksi** — Hapus transaksi tertentu; saldo otomatis diperbarui
- **Keamanan per User** — Setiap pengguna hanya bisa melihat dan mengelola data miliknya sendiri


