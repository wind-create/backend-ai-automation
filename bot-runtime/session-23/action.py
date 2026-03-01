import time
import pyautogui
import pyperclip
import os

pyautogui.FAILSAFE = True  # geser mouse ke pojok kiri atas = STOP

DATA_FILE = "data_from_action.txt"
DECISION_FILE = "decision_from_otak.txt"

print("=== BOT ACTION START ===")
print("Arahkan mouse ke SEL USER ID (kolom A), mulai dalam 3 detik...")
time.sleep(5)

# ===== COPY USER ID =====
pyautogui.click()  # klik posisi mouse saat ini (HARUS di sel A)
time.sleep(0.2)
pyautogui.hotkey("ctrl", "c")
time.sleep(0.2)
user_id = pyperclip.paste().strip()

print("User ID ter-copy:", repr(user_id))

# ===== PINDAH KE NOMINAL (KANAN) =====
pyautogui.press("right")
time.sleep(0.2)
pyautogui.hotkey("ctrl", "c")
time.sleep(0.2)
nominal = pyperclip.paste().strip()

print("Nominal ter-copy:", repr(nominal))

# ===== VALIDASI DASAR =====
if not user_id or not nominal:
    print("ERROR: User ID atau Nominal kosong. STOP.")
    exit(1)

# ===== KIRIM DATA KE BOT OTAK =====
with open(DATA_FILE, "w", encoding="utf-8") as f:
    f.write(f"USER={user_id}\n")
    f.write(f"NOMINAL={nominal}\n")

print("Data dikirim ke Bot Otak.")

# ===== TUNGGU KEPUTUSAN =====
print("Menunggu keputusan dari Bot Otak...")
while not os.path.exists(DECISION_FILE):
    time.sleep(0.5)

with open(DECISION_FILE, "r", encoding="utf-8") as f:
    decision = f.read().strip()

print("Keputusan diterima:", decision)

# ===== TULIS KE EXCEL (KOLOM C) =====
pyautogui.press("right")  # ke kolom Status
time.sleep(0.2)
pyautogui.typewrite(decision)

# ===== BERSIH-BERSIH =====
os.remove(DECISION_FILE)

print("=== BOT ACTION SELESAI ===")
