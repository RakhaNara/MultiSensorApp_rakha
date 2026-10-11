# Multi-Sensor Android App (Jetpack Compose)

Aplikasi Android berbasis Kotlin dan Jetpack Compose yang memanfaatkan 2 sensor dari kategori berbeda untuk memberikan interaksi langsung pada User Interface (UI).

## 📱 Sensor yang Digunakan
1. **Light Sensor (Kategori: Environment)**
   - **Fungsi:** Membaca tingkat intensitas cahaya di sekitar perangkat (Lux).
   - **Efek UI:** Mengubah tema aplikasi secara realtime. Jika kondisi gelap (< 40 Lux), UI berubah menjadi Dark Mode, dan sebaliknya.

2. **Accelerometer Sensor (Kategori: Motion)**
   - **Fungsi:** Membaca koordinat sumbu X dan Y dari kemiringan perangkat.
   - **Efek UI:** Menggerakkan elemen bola di dalam lingkaran secara responsif mengikuti arah kemiringan HP.

## ⚙️ Pengaturan Lifecycle
Untuk menghemat penggunaan baterai dan memori perangkat:
- Sensor didaftarkan (`registerListener`) saat aplikasi dalam keadaan aktif (`onResume`).
- Sensor dilepas (`unregisterListener`) saat aplikasi masuk ke latar belakang (`onPause`).
