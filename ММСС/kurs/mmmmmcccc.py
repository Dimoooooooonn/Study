import numpy as np
import pandas as pd

# ========== Параметры ==========
file_input = "task_3217.xlsx"   # или "task.xlsm" после сохранения в xlsx
output_file = "results_task_3217.xlsx"

y0 = 0.1               # Эрл/абонент
a0 = 85.6e3            # бит/с (85.6 Kbit/s)
L = 200 * 8            # длина пакета в битах (200 байт)
T0 = 0.1               # сек (100 ms)
q = 98                 # %
p0 = 1 - q/100         # допустимая блокировка/порог по сервису (см. задание)
Topt = T0 / 2
dc = 10000             # шаг добавки пропускной способности (бит/с)

# ========== Чтение входных данных ==========
# Лист traffic_distribution должен содержать один столбец N_i (20 строк)
Ni = pd.read_excel(file_input, sheet_name="traffic_distribution", header=None).to_numpy().flatten()
n = Ni.size
if n != 20:
    print(f"ВНИМАНИЕ: ожидается 20 узлов, получено {n}")

# Матрица расстояний (20x20). Пустые/NaN -> отсутствие ребра -> INF
distances_raw = pd.read_excel(file_input, sheet_name="distances", header=None).to_numpy()
distances = distances_raw.astype(float)
distances = np.where(np.isnan(distances), np.inf, distances)
# В некоторых вариантах диагональ может быть 0 — оставим 0 на диагонали
np.fill_diagonal(distances, 0.0)

# ========== Интенсивности исходящего трафика y_i ==========
y_i = Ni * y0   # в Эрлангах

# ========== Формирование матрицы распределения k_ij и Y_ij ==========
S = Ni.sum()
# k_ij = N_j / S (включая j=i)
k = np.zeros((n, n), dtype=float)
for i in range(n):
    for j in range(n):
        k[i, j] = Ni[j] / S

# Матрица интенсивности трафика по направлениям (Эрланги)
Y = (y_i[:, None]) * k   # Y[i,j] = y_i * N_j / S  => симметрична по i,j если y_i = y0 * N_i

# ========== Флойда–Уоршелла с восстановлением next-node ==========
INF = float("inf")
dist = distances.copy()
# уже заменили NaN->inf, диагональ 0
next_node = np.full((n, n), -1, dtype=int)
for i in range(n):
    for j in range(n):
        if i != j and dist[i, j] != np.inf:
            next_node[i, j] = j

# основной цикл
for k_idx in range(n):
    for i in range(n):
        if dist[i, k_idx] == np.inf:
            continue
        for j in range(n):
            if dist[k_idx, j] == np.inf:
                continue
            if dist[i, k_idx] + dist[k_idx, j] < dist[i, j]:
                dist[i, j] = dist[i, k_idx] + dist[k_idx, j]
                next_node[i, j] = next_node[i, k_idx]

# R_paths для записи: хотим номера 1..n на диагонали (и номера next_node+1 иначе 0 если нет пути)
R_output = np.zeros((n, n), dtype=int)
for i in range(n):
    for j in range(n):
        if i == j:
            R_output[i, j] = i + 1
        else:
            R_output[i, j] = (next_node[i, j] + 1) if next_node[i, j] != -1 else 0

# ========== Вспомогательная функция: восстановление полного пути i->j ==========
def reconstruct_path(i, j, next_node):
    """Возвращает список вершин (0-based) от i до j включительно, или [] если пути нет."""
    if i == j:
        return [i]
    if next_node[i, j] == -1:
        return []
    path = [i]
    u = i
    visited = 0
    while u != j:
        u = next_node[u, j]
        if u == -1:
            return []
        path.append(u)
        visited += 1
        if visited > n + 5:  # защита от бесконечного цикла
            return []
    return path

# ========== Метод суперпозиции: считаем нагрузку на линиях Y_line (направленная) ==========
Y_line = np.zeros((n, n), dtype=float)   # направленная нагрузка (Эрланги)
for i in range(n):
    for j in range(n):
        if i == j:
            continue
        y_ij = Y[i, j]
        if y_ij == 0:
            continue
        path = reconstruct_path(i, j, next_node)
        if len(path) == 0:
            # нет маршрута i->j
            continue
        # прибавляем y_ij ко всем смежным рёбрам пути
        for idx in range(len(path) - 1):
            p = path[idx]
            q = path[idx + 1]
            Y_line[p, q] += y_ij

# также полезна суммарная (неориентированная) нагрузка на физическую линию:
Y_line_undirected = Y_line + Y_line.T

# ========== Матрица потоков V: находи минимальное число потоков v, дающих блокировку <= p0 ==========
# Используем функцию Эрланга B (B(v, y)) для расчёта вероятности блокировки.
def erlangB(v, y):
    """Вычисление формулы Эрланга B (в устойчивой форме) для нагрузки y (Эрланги) и v каналов."""
    if y <= 0:
        return 0.0
    # вычислим B через рекуррентную формулу устойчиво
    invB = 1.0
    for k_ in range(1, int(v) + 1):
        invB = 1.0 + invB * (k_ / y)
    return 1.0 / invB

# Перебираем v от 1 до max_v (макс каналов, разумно ограничить, например, 500)
max_v = 500
V = np.zeros((n, n), dtype=float)  # в этом поле храним число потоков (целое число, но храним float)
for p in range(n):
    for q in range(n):
        y_val = Y_line[p, q]
        if y_val <= 0:
            V[p, q] = 0.0
            continue
        found = False
        for v in range(1, max_v + 1):
            b = erlangB(v, y_val)
            if b <= p0:
                V[p, q] = v
                found = True
                break
        if not found:
            V[p, q] = max_v  # не достигли требуемой блокировки в пределах max_v

# ========== Интенсивность трафика ПД A (бит/с) ==========
A = a0 * V  # a0 в бит/с, V — число потоков

# ========== Пропускная способность B (M/M/1) ==========
# Согласно формуле задания: b_ij = a_ij + L / T0
B = A + (L / T0)

# ========== Задержки на линиях DEL и суммарные задержки по маршрутам DL ==========
# Для модели M/M/1 задержка на линии = L / (b - a)  (где b > a)
DEL = np.full((n, n), np.inf, dtype=float)
for p in range(n):
    for q in range(n):
        if B[p, q] > A[p, q]:
            DEL[p, q] = L / (B[p, q] - A[p, q])
        else:
            DEL[p, q] = np.inf

# Рассчитаем суммарную задержку DL по маршруту i->j как сумма DEL по рёбрам пути
DL = np.full((n, n), np.inf, dtype=float)
for i in range(n):
    for j in range(n):
        path = reconstruct_path(i, j, next_node)
        if len(path) == 0:
            DL[i, j] = np.inf
            continue
        t_sum = 0.0
        feasible = True
        for idx in range(len(path) - 1):
            p = path[idx]
            q = path[idx + 1]
            if not np.isfinite(DEL[p, q]):
                feasible = False
                break
            t_sum += DEL[p, q]
        DL[i, j] = t_sum if feasible else np.inf

# ========== Простая оптимизация добавкой dc: итеративно ищем улучшение ==========
# (Проще: используем предложенный в задании алгоритм — попробуем добавить dc к каждой линии и принять лучшую итерацию)
Bo = B.copy()            # текущие пропускные способности (бит/с)
DEL_current = DEL.copy()
improvement = True
iteration = 0
max_iter = 2000  # защита от бесконечного цикла

def compute_DL_from_DEL(next_node, DEL_matrix):
    DL_tmp = np.full((n, n), np.inf)
    for i in range(n):
        for j in range(n):
            path = reconstruct_path(i, j, next_node)
            if len(path) == 0:
                DL_tmp[i, j] = np.inf
                continue
            s = 0.0
            feasible = True
            for idx in range(len(path) - 1):
                p = path[idx]; q = path[idx+1]
                if not np.isfinite(DEL_matrix[p, q]):
                    feasible = False
                    break
                s += DEL_matrix[p, q]
            DL_tmp[i, j] = s if feasible else np.inf
    return DL_tmp

# Начальный DL
DEL_current = np.full((n, n), np.inf)
for p in range(n):
    for q in range(n):
        if Bo[p, q] > A[p, q]:
            DEL_current[p, q] = L / (Bo[p, q] - A[p, q])
        else:
            DEL_current[p, q] = np.inf
DL_current = compute_DL_from_DEL(next_node, DEL_current)

# Целевая функция: суммарное превышение Topt (можно взять квадраты или абсолютное превышение)
def objective(DL_matrix, Topt):
    finite_mask = np.isfinite(DL_matrix)
    exceed = np.maximum(DL_matrix - Topt, 0.0)
    exceed[~finite_mask] = 1e6  # большие штрафы за недостижимость
    return exceed.sum()

O_prev = objective(DL_current, Topt)

while improvement and iteration < max_iter:
    iteration += 1
    best_O = O_prev
    best_pair = None
    # Перебираем все ориентированные линии, пробуем добавить dc и считаем O
    for p in range(n):
        for q in range(n):
            if Bo[p, q] <= 0:
                continue
            Bo_try = Bo.copy()
            Bo_try[p, q] += dc
            # обновляем DEL_try для всех линий
            DEL_try = np.full((n, n), np.inf)
            for u in range(n):
                for v in range(n):
                    if Bo_try[u, v] > A[u, v]:
                        DEL_try[u, v] = L / (Bo_try[u, v] - A[u, v])
                    else:
                        DEL_try[u, v] = np.inf
            DL_try = compute_DL_from_DEL(next_node, DEL_try)
            O_try = objective(DL_try, Topt)
            if O_try < best_O:
                best_O = O_try
                best_pair = (p, q, DEL_try, Bo_try, DL_try)
    if best_pair is None:
        improvement = False
    else:
        # принять улучшение
        p_best, q_best, DEL_current, Bo, DL_current = best_pair
        O_prev = best_O
        # продолжим цикл
# После оптимизации Bo содержит оптимизированные пропускные способности

# ========== Сохранение всех результатов в Excel ==========
with pd.ExcelWriter(output_file) as writer:
    pd.DataFrame(y_i).to_excel(writer, sheet_name="yi", index=False, header=False)
    pd.DataFrame(k).to_excel(writer, sheet_name="k_ij", index=False, header=False)
    pd.DataFrame(Y).to_excel(writer, sheet_name="Y_matrix", index=False, header=False)
    pd.DataFrame(R_output).to_excel(writer, sheet_name="R_paths", index=False, header=False)
    pd.DataFrame(Y_line).to_excel(writer, sheet_name="Y_line_directed", index=False, header=False)
    pd.DataFrame(Y_line_undirected).to_excel(writer, sheet_name="Y_line_undirected", index=False, header=False)
    pd.DataFrame(V).to_excel(writer, sheet_name="V_flows", index=False, header=False)
    pd.DataFrame(A).to_excel(writer, sheet_name="A_traffic", index=False, header=False)
    pd.DataFrame(B).to_excel(writer, sheet_name="B_capacity_initial", index=False, header=False)
    pd.DataFrame(Bo).to_excel(writer, sheet_name="B_capacity_optimized", index=False, header=False)
    pd.DataFrame(DEL_current).to_excel(writer, sheet_name="DEL_line_delays", index=False, header=False)
    pd.DataFrame(DL_current).to_excel(writer, sheet_name="DL_path_delays", index=False, header=False)

print(f"Готово — результаты сохранены в '{output_file}'")
