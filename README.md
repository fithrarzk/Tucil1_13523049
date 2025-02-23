# Tucil1_13523049

| NIM | Nama |
| :---: | :---: |
| 13523049 | Muhammad Fithra Rizki |

## Deskripsi Tugas
Tugas ini berisi algoritma penyelesaian suatu permainan mengasah otak bernama IQ Puzzler Pro. Algoritma yang dibuat harus dibuat dengan pendekatan Brute Force tanpa menyertakan heuristik.

## Struktur Program
```bash
.
│   README.md
│
├───bin                                   
│
├───doc  
│   ├─── Laporan
│                      
├───src                                                       
│   ├─── Main.java 
│   ├─── PuzzleGUI.java
│   ├─── PuzzleInput.java
│   ├─── PuzzleSolver.javax                     
│  
└───test                            # Testing cases
    ├── input             
    ├─── tc1.txt
    ├─── tc2.txt
    ├─── tc3.txt
    ├── output 
    ├─── solusi_tc1.txt 
    ├─── solusi_tc1.png 
```

## How to Run
1. Clone repository ini dengan `git clone https://github.com/fithrarzk/Tucil1_13523049.git`
2. Pada terminal, lakukan compile dengan mengetik `javac -d bin src/*.java`
3. Lakukan run dengan mengetik `java -cp bin Main` pada terminal

## How to Use
1. Simpan file yang ingin diuji pada folder test/input/
2. Setelah program di run, pengguna akan diminta untuk mengetik nama file yang ingin dicari solusinya
3. Tekan tombol "Solve" untuk mencari hasil
4. Hasil akan keluar dalam bentuk gambar jika masalah memiliki solusi, jika tidak akan muncul pesan tidak ada hasil
5. Tekan tombol untuk menyimpan hasil sebagai teks atau gambar jika ingin menyimpan hasil dari permasalahan yang ada
