#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<pthread.h>
#include<time.h>

/* Varijable */
#define N 5                 // količina nakon koje će se puštati programeri drugog tipa

pthread_mutex_t m;          // kluč za zaključavanje
pthread_cond_t red[2];      // uvjetne varijable

int br;                     // ukupni broj programera
int unutra[2] = {0, 0};     // broj programera u restoranu
int ceka[2] = {0, 0};       // čekaju red na ulazu
int posluzeno[2] = {0, 0};  // koliko ih je pojelo

/* Pomocne funkcije */
void obavi(int tip) {
    if(tip == 0)
        printf("Linux programer jede večeru\n");
    else
        printf("Microsoft programer jede večeru\n");
    sleep(3);
}

void udi(int tip) {
    pthread_mutex_lock(&m);         // zaključaj dio programa
    ceka[tip]++;
    while(unutra[1 - tip] > 0 || (ceka[1 - tip] > 0 && posluzeno[tip] >= N))
        pthread_cond_wait(&red[tip], &m);       // postavi dretvu u stanje čekanja
    unutra[tip]++;
    ceka[tip]--;
    posluzeno[1 - tip] = 0;
    posluzeno[tip]++;
    pthread_mutex_unlock(&m);       // otklučaj
}

void izadi(int tip) {
    pthread_mutex_lock(&m);         // zaključaj dio programa
    unutra[tip]--;
    if(unutra[tip] == 0)
        pthread_cond_broadcast(&red[1 - tip]);      // omogući svim dretvama nastavak izvođenja
    pthread_mutex_unlock(&m);       // otključaj
}

void *programer(void *p) {
    int tip = *((int *) p);
    udi(tip);
    obavi(tip);
    izadi(tip);
}

int main() {

    // inicijalizacija monitora
    pthread_mutex_init(&m, NULL);
	pthread_cond_init(&red[0], NULL);
    pthread_cond_init(&red[1], NULL);

    printf("Upisite ukupni broj programera: ");
    scanf("%d", &br);

    srand((unsigned int) time(NULL));

    // postavi redoslijed ulaska programera
    pthread_t t[br];
    int redoslijed[br];

    printf("Redoslijed na ulazu (0 linux, 1 microsoft): ");
    for(int i = 0; i < br; i++) {
        redoslijed[i] = rand() % 2;
        printf("%d ", redoslijed[i]);
    }
    printf("\n");

    for(int i = 0; i < br; i++) {
        if(pthread_create(&t[i], NULL, programer, &redoslijed[i]) != 0) {
            printf("\nGreska pri stvaranju dretve!");
            exit(1);
        }
        sleep(1);
    }

    for(int i = 0; i < br; i++)
        pthread_join(t[i], NULL);

    // uništi monitore
    pthread_mutex_destroy(&m);
	pthread_cond_destroy(&red[0]);
	pthread_cond_destroy(&red[1]);

    return 0;
}