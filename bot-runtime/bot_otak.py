import time
import os

DATA_FILE = "data_from_action.txt"
DECISION_FILE = "decision_from_otak.txt"

print("=== BOT OTAK START ===")
print("Menunggu data dari Bot Action...")

# ===== TUNGGU DATA =====
while not os.path.exists(DATA_FILE):
    time.sleep(0.5)

# ===== BACA DATA =====
data = {}

with open(DATA_FILE, "r", encoding="utf-8") as f:
    for line in f:
        line = line.strip()
        if not line:
            continue
        if "=" not in line:
            continue
        key, value = line.split("=", 1)
        data[key] = value.strip()

print("Data diterima:", data)

user = data.get("USER", "")
nominal_raw = data.get("NOMINAL", "").strip()

# ===== VALIDASI NOMINAL =====
if not nominal_raw:
    decision = "ERROR_NOMINAL_EMPTY"
    print("ERROR: Nominal kosong")
else:
    try:
        nominal = int(nominal_raw.replace(".", "").replace(",", ""))
        if nominal <= 1_000_000:
            decision = "APPROVE"
        else:
            decision = "HOLD"
    except ValueError:
        decision = "ERROR_NOMINAL_INVALID"
        print("ERROR: Nominal tidak valid:", nominal_raw)

# ===== KIRIM KEPUTUSAN =====
with open(DECISION_FILE, "w", encoding="utf-8") as f:
    f.write(decision)

print("Keputusan dikirim:", decision)

# ===== BERSIH-BERSIH =====
os.remove(DATA_FILE)

print("=== BOT OTAK SELESAI ===")
