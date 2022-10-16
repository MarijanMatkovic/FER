#include<iostream>
#include<climits>
#include<algorithm>
#include<vector>
using namespace std;

int pohlepni(int **G, int n) {
    int duljinaPuta = 0;
    int minDuljina = INT_MAX;
    int poc, krajnji;
    bool *f = new bool[n];
    for(int i = 0; i < n; i++)
        f[i] = false;
    for(int i = 0; i < n; i++)
        for(int j = 0; j < n; j++)
            if(G[i][j] < minDuljina && i != j) {
                minDuljina = G[i][j];
                poc = i;
                krajnji = j;
            }
    f[poc] = true;
    f[krajnji] = true;
    cout << endl << "Uzimamo duljinu brida |" << poc + 1 << krajnji + 1 << "| i ona je " << minDuljina;
    duljinaPuta += minDuljina;
    for(int i = 0; i < n - 2; i++) {
        int minDuljinaPoc = INT_MAX;
        int minDuljinaKraj = INT_MAX;
        int p, q;
        for(int l = 0; l < n; l++) {
            if(f[l])
                continue;
            if(G[krajnji][l] < minDuljinaKraj) {
                p = l;
                minDuljinaKraj = G[krajnji][l];
            }
            if(G[poc][l] < minDuljinaPoc) {
                q = l;
                minDuljinaPoc = G[poc][l];
            }
        }
        if(minDuljinaKraj < minDuljinaPoc) {
            f[p] = true;
            cout << endl << "Uzimamo duljinu brida |" << krajnji + 1 << p + 1 << "| i ona je " << minDuljinaKraj;
            krajnji = p;
            duljinaPuta += minDuljinaKraj;
        }
        else {
            f[q] = true;
            cout << endl << "Uzimamo duljinu brida |" << poc + 1 << q + 1 << "| i ona je " << minDuljinaPoc;
            poc = q;
            duljinaPuta += minDuljinaPoc;
        }
    }
    cout << endl << "Uzimamo duljinu brida |" << poc + 1 <<  krajnji + 1 << "| i ona je " << G[poc][krajnji] << endl;
    duljinaPuta += G[poc][krajnji];
    delete[] f;
    return duljinaPuta;
}

int iscrpni(int **G, int n) {
    int minPut = INT_MAX;
    vector<int> niz;
    for(int j = 1; j < n; j++)
        niz.push_back(j);
    int najtezi = INT_MIN;
    do {
        int duljina = 0;
        int l = 0;
        int najteziLok = INT_MIN;
        for(int k = 0; k < niz.size(); k++) {
            duljina += G[l][niz[k]];
            if(G[l][niz[k]] > najteziLok)
                najteziLok = G[l][niz[k]];
            l = niz[k];
        }
        duljina += G[l][0];
        if(duljina < minPut && najteziLok > najtezi)
            najtezi = najteziLok;
        minPut = min(minPut, duljina);
    } while(next_permutation(niz.begin(), niz.end()));
    cout << endl << "Brid najvece tezine je " << najtezi << endl;
    return minPut;
}

int main() {
    int n, a, b;
    cout << "Unesite redom, odvojene razmakom, parametre n, a i b ";
    cin >> n >> a >> b;
    int **G = new int*[n];
    for(int i = 0; i < n; i++)
        G[i] = new int[n];
    for(int k = 0; k < n; k++) {
        G[k][k] = 0;
        for(int l = k + 1; l < n; l++) {
            G[k][l] = (a * (k + 1) + b * (l + 1)) * (a * (k + 1) + b * (l + 1)) + 1;
            G[l][k] = G[k][l];
        }
    }
    cout << endl;
    for(int k = 0; k < n; k++) {
        for(int l = 0; l < n; l++) {
            cout << G[k][l] << " ";
        }
        cout << endl;
    }
    int poh = pohlepni(G, n);
    int isc = iscrpni(G, n);
    cout << endl << "Pohlepni algoritam nalazi ciklus duljine " << poh;
    cout << endl << "Iscrpni algoritam nalazi ciklus duljine " << isc;
    if(poh > isc) cout << endl << "Pohlepni algoritam na ovom grafu ne daje optimalno rjesenje";
    else cout << endl << "Pohlepni algoritam na ovom grafu daje optimalno rjesenje";
    for (int i = 0; i < n; i++) {
        delete[] G[i];
    }
    delete[] G;
    return 0;
}