#include <iostream>
#include <vector>
#include <climits>
#include <cstdlib>
#include <ctime>
#include <locale.h>
#include <algorithm>
#include <queue>
#include <chrono>  // Для замера времени
#include <iomanip> // Для настройки точности вывода

using namespace std;
using namespace chrono;  // Для удобства работы с временем

// Функция для генерации матрицы смежности
vector<vector<int>> generateGraph(int n) {
    vector<vector<int>> graph(n, vector<int>(n, INT_MAX));

    // Инициализация диагонали
    for (int i = 0; i < n; ++i) {
        graph[i][i] = 0;
    }

    srand(time(0));
    for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
            int edge_exists = rand() % 4;  // С вероятностью 25% будет ребро или нет
            if (edge_exists == 1) {
                int weight = rand() % 100 + 1;  // Вес рёбер от 1 до 100
                graph[i][j] = weight;
                graph[j][i] = weight;
            }
        }
    }

    return graph;
}

// Функция для вывода матрицы смежности
void printGraph(const vector<vector<int>>& graph) {
    cout << "Матрица смежности графа:\n";
    for (size_t i = 0; i < graph.size(); ++i) {
        for (size_t j = 0; j < graph[i].size(); ++j) {
            if (graph[i][j] == INT_MAX) {
                cout << "INF\t";
            }
            else {
                cout << graph[i][j] << "\t";
            }
        }
        cout << endl;
    }
}

// Алгоритм Флойда-Уоршелла
void floydWarshall(vector<vector<int>>& graph) {
    int n = graph.size();

    for (int k = 0; k < n; ++k) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (graph[i][k] != INT_MAX && graph[k][j] != INT_MAX &&
                    graph[i][j] > graph[i][k] + graph[k][j]) {
                    graph[i][j] = graph[i][k] + graph[k][j];
                }
            }
        }
    }
}

// Рекурсивная функция для прямого перебора путей
void dfs(int current, int end, int currentDistance, vector<bool>& visited, const vector<vector<int>>& graph, int& minDistance) {
    if (current == end) {
        minDistance = min(minDistance, currentDistance);
        return;
    }
    visited[current] = true;

    for (int next = 0; next < graph.size(); ++next) {
        if (!visited[next] && graph[current][next] != INT_MAX) {
            dfs(next, end, currentDistance + graph[current][next], visited, graph, minDistance);
        }
    }

    visited[current] = false;
}

// Функция для поиска кратчайшего пути с помощью прямого перебора
int bruteForceShortestPath(const vector<vector<int>>& graph, int start, int end) {
    int n = graph.size();
    vector<bool> visited(n, false);
    int minDistance = INT_MAX;

    // Начинаем рекурсию из стартовой вершины
    dfs(start, end, 0, visited, graph, minDistance);

    return minDistance;
}

// Алгоритм Дейкстры для поиска кратчайших путей
void dijkstra(const vector<vector<int>>& graph, int start, vector<int>& dist) {
    int n = graph.size();
    dist.assign(n, INT_MAX);
    dist[start] = 0;

    // Приоритетная очередь (минимальная куча)
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    pq.push({ 0, start });

    while (!pq.empty()) {
        int u = pq.top().second;
        int d = pq.top().first;
        pq.pop();

        if (d > dist[u]) continue;

        // Проходим по всем соседям
        for (int v = 0; v < n; ++v) {
            if (graph[u][v] != INT_MAX) {
                int newDist = dist[u] + graph[u][v];
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    pq.push({ dist[v], v });
                }
            }
        }
    }
}

int main() {
    setlocale(LC_ALL, "");  // Для корректного отображения русских символов в консоли
    int n;
    cout << "Введите количество вершин графа (n): ";
    cin >> n;

    long long totalFloydTime = 0, totalBruteForceTime = 0, totalDijkstraTime = 0;

    for (int i = 0; i < 100000; i++) {
        vector<vector<int>> graph = generateGraph(n);
        //printGraph(graph);
        // Замер времени для алгоритма Флойда-Уоршелла
        auto start = high_resolution_clock::now();
        floydWarshall(graph);
        auto end = high_resolution_clock::now();
        auto duration = duration_cast<nanoseconds>(end - start); // замер в наносекундах
        totalFloydTime += duration.count();
        //printGraph(graph);

        // Замер времени для прямого перебора
        start = high_resolution_clock::now();
        int bruteForceResult = bruteForceShortestPath(graph, 0, n - 1);
        end = high_resolution_clock::now();
        duration = duration_cast<nanoseconds>(end - start); // замер в наносекундах
        totalBruteForceTime += duration.count();

        // Замер времени для алгоритма Дейкстры
        start = high_resolution_clock::now();
        vector<int> dist;
        dijkstra(graph, 0, dist);
        end = high_resolution_clock::now();
        duration = duration_cast<nanoseconds>(end - start); // замер в наносекундах
        totalDijkstraTime += duration.count();

    }

    // Выводим среднее время выполнения для каждого алгоритма
    cout << "\nСреднее время выполнения алгоритма Флойда-Уоршелла: "
        << fixed << setprecision(9) << (totalFloydTime * 1e-9) / 100000 << " секунд\n";

    cout << "Среднее время выполнения прямого перебора: "
        << fixed << setprecision(9) << (totalBruteForceTime * 1e-9) / 100000 << " секунд\n";

    cout << "Среднее время выполнения алгоритма Дейкстры: "
        << fixed << setprecision(9) << (totalDijkstraTime * 1e-9) / 100000 << " секунд\n";

    return 0;
}

