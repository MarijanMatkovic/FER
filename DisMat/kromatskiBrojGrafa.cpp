#include<iostream>
#include<fstream>
#include<string>
#include<sstream>
#include<cmath>
using namespace std;

bool isSafe(int v, bool **graph, int color[], int c, int n) {
    for(int i = 0; i < n; i++)
        if (graph[v][i] && c == color[i])
            return false;
    return true;
}

bool graphColoringUtil(bool **graph, int m, int color[], int v, int n) {
    if (v == n)
        return true;
    for(int c = 1; c <= m; c++) {
        if (isSafe(v, graph, color, c, n)) {
            color[v] = c;
            if (graphColoringUtil(graph, m, color, v + 1, n) == true)
                return true;
            color[v] = 0;
        }
    }
    return false;
}

bool graphColoring(bool **graph, int m, int n) {
    int *color = new int[n];
    for(int i = 0; i < n; i++)
        color[i] = 0;
    if (graphColoringUtil(graph, m, color, 0, n) == false) {
        return false;
    }
    delete[] color;
    return true;
}

int main() {
    int n, s;
    string linija;
    cout << "Unesite ime datoteke: ";
    getline(cin, linija);
    ifstream graf(linija);
    graf >> n;
    graf >> s;

    int *S = new int[s];
    for(int i = 0; i < s; i++)
        graf >> S[i];

    bool **graph = new bool*[n];
    for(int i = 0; i < n; i++)
        graph[i] = new bool[n];
    
    for(int k = 0; k < n; k++) {
        graph[k][k] = false;
        for(int l = k + 1; l < n; l++) {
            graph[k][l] = false;
            for(int i = 0; i < s; i++) {
                if(abs(k - l) == S[i]) {
                    graph[k][l] = true;
                    break;
                }
            }
            graph[l][k] = graph[k][l];
        }
    }

    for(int i = 2; i < n; i++) {
        if(graphColoring(graph, i, n)) {
            cout << "Kromatski broj zadanog grafa je " << i;
            break;
        }
    }

    for (int i = 0; i < n; i++) {
        delete[] graph[i];
    }
    delete[] graph;
    delete[] S;
    return 0;
}