import csv
import socket
import time
from pathlib import Path

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import psutil


def main():
    host = socket.gethostname()
    folder = Path.cwd()
    start = time.monotonic()
    points = []
    psutil.cpu_percent()
    with (folder / "lab3_metrics.csv").open("w", newline="") as stream:
        writer = csv.writer(stream)
        writer.writerow(["seconds", "cpu_percent", "ram_percent"])
        for target in range(5, 301, 5):
            time.sleep(max(0, start + target - time.monotonic()))
            elapsed = round(time.monotonic() - start, 2)
            cpu = psutil.cpu_percent()
            ram = psutil.virtual_memory().percent
            points.append((elapsed, cpu, ram))
            writer.writerow(points[-1])
            stream.flush()
            print(f"{elapsed:6.1f} с   CPU {cpu:5.1f}%   RAM {ram:5.1f}%", flush=True)

    seconds, cpu, ram = zip(*points)
    fig, ax = plt.subplots(figsize=(10, 5))
    ax.plot(seconds, cpu, label="CPU", color="tab:blue")
    ax.plot(seconds, ram, label="RAM", color="tab:orange")
    ax.set(xlabel="Время, с", ylabel="Использование, %",
           title=f"Нагрузка на узле {host} за 5 минут", ylim=(0, 100))
    ax.grid(alpha=0.25)
    ax.legend()
    fig.tight_layout()
    fig.savefig(folder / "lab3_graph.png", dpi=160)
    plt.close(fig)
    print(f"Результаты в {folder}: lab3_metrics.csv и lab3_graph.png")


if __name__ == "__main__":
    main()
