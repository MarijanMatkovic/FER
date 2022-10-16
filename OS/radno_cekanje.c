#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<unistd.h>
#include<pthread.h>
#include<signal.h>
#include<sys/wait.h>
#include<sys/types.h>
#include<sys/ipc.h>
#include<sys/shm.h>

void obradi_sigint(int sig); //funkcija za obradu signala SIGINT

int Id;                      //identifikacijski broj segmenta
int *ZajednickaZaProcese;    //varijabla za komunikaciju ulazne i radne dretve koje su u zasebnim procesima
int ZajednickaZaDretve;      //varijabla za komunikaciju radne i izlazne dretve koje su u istom procesu
int br;                      //brojač za broj iteracija programa

/* funkcija za uvećavanje dobivenog nasumičnog broja */
void *uvecaj(void *a) {
    printf("Pokrenuta RADNA DRETVA\n");
    for(int i = 0; i < br; i++) {
        while(*ZajednickaZaProcese == 0 || ZajednickaZaDretve != 0) sleep(1);
        int num = *ZajednickaZaProcese + 1;
        *ZajednickaZaProcese = 0;
        ZajednickaZaDretve = num;
        sleep(1);
        printf("RADNA DRETVA: pročitan broj %d i povećan na %d\n", num - 1, num);
    }
    sleep(2);
    printf("Završila RADNA DRETVA\n");
}

int main(int argc, char *argv[]) {
    
    struct sigaction act;

    br = atoi(argv[1]);

    Id = shmget(IPC_PRIVATE, sizeof(int), 0600);        //zauzimanje zajedničke memorije
    if(Id == -1)
        exit(1);                                        //greška - nema zajedničke memorije

    ZajednickaZaProcese = (int *) shmat(Id, NULL, 0);
    *ZajednickaZaProcese = 0;

    ZajednickaZaDretve = 0;

    srand(time(NULL));

    /* pokretanje paralelnih procesa */
    if(fork() == 0) {

        printf("Pokrenut IZLAZNI PROCES\n");

        pthread_t thr_id[1];
        /* pokretanje radne dretve */
        if(pthread_create(&thr_id[0], NULL, uvecaj, NULL) != 0) {
            printf("Greska pri stvaranju dretve\n");
            exit(1);
        }

        for (int i = 0; i < br; i++) {
            while(ZajednickaZaDretve == 0) sleep(1);   //izlazna dretva radno čeka dok radna ne upiše u zajedničku varijablu

            /* upis  broja u datoteku */
            FILE *filePtr;
            filePtr = fopen("ispis.txt", "a");
            fprintf(filePtr, "%d\n", ZajednickaZaDretve);
            fclose(filePtr);
            sleep(1);
            printf("IZLAZNI PROCES: broj upisan u datoteku %d\n", ZajednickaZaDretve);
            ZajednickaZaDretve = 0;
        }

        sleep(2);
        printf("Završio IZLAZNI PRCOES\n");

        pthread_join(thr_id[0], NULL);                  //sačekaj kraj radne dretve
    }
    else {

        /* maskiranje signala SIGINT */
	    act.sa_handler = obradi_sigint;
	    sigaction(SIGINT, &act, NULL);

        printf("Pokrenuta ULAZNA DRETVA\n");
        for(int i = 0; i < br; i++) {
            sleep(rand() % 5 + 1);                      //čekaj 1 do 5 sekundi za generiranje broja
            *ZajednickaZaProcese = rand() % 100 + 1;
            int n = *ZajednickaZaProcese;
            while(*ZajednickaZaProcese != 0) sleep(1);
            printf("ULAZNA DRETVA: broj: %d\n", n);
        }
        sleep(2);
        printf("Završila ULAZNA DRETVA\n");
    }
    (void) wait(NULL);

    /* oslobađanje zajedničke memorije */
    (void) shmdt((char *) ZajednickaZaProcese);
    (void) shmctl(Id, IPC_RMID, NULL);

    return 0;
}

/* Obrada signala SIGINT, prekida s radom i oslobađa memoriju */
void obradi_sigint(int sig) {
    (void) wait(NULL);
    (void) shmdt((char *) ZajednickaZaProcese);
    (void) shmctl(Id, IPC_RMID, NULL);
	printf("Primio signal SIGINT, oslobađam memorije i prekidam rad\n");
	exit(1);
}