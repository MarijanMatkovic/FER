#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<semaphore.h>
#include<sys/wait.h>
#include<sys/types.h>
#include<sys/shm.h>

/* Varijable */
sem_t *red_ulaz; // semafor koji osigurava ulazak N posjetitelja procesa
sem_t *red_izlaz; // semafor koji osigurava izlaz N posjetitelja procesa
sem_t *kapacitet; // binarni semafor koji osigurava da je vrtuljak pun/prazan
sem_t *cekaj; // binarni semafor koji osigurava da po jedan posjetitelj ulazi/izlazi iz vrtuljka

int *usli; // trenutni broj posjetitelja koji su se ukrcali u vrtuljak
int *izasli; // trenutni broj posjetitelja koji su se iskrcali iz vrtuljaka
volatile int N; // kapacitet vrtuljka

/* Pomocne funkcije */
void dolazak() {
	printf("Vožnja će početi, vrijeme je za ukrcavanje!\n");
	sleep(1);
}
void pokreni_vrtuljak() {
	printf("Vrtuljak je pun, započinjem vožnju!\n");
	sleep(1);
	printf("Vrtuljak se trenutno vrti!\n");
	sleep(2);
}
void zaustavi_vrtuljak() {
	printf("Vožnja je gotova, vrijeme za iskrcavanje!\n");
	sleep(1);
}
void sjedi() {
	printf("%d posjetitelja se ukrcalo u vrtuljak...\n", *usli);
	sleep(1);
}
void ustani() {
	printf("%d posjetitelja se iskrcalo iz vrtuljka...\n", *izasli);
	sleep(1);
}

/* Funkcije procesa */
void vrtuljak() {
	while(1) {
		dolazak();
		
		for(int i = 0; i < N; i++) sem_post(red_ulaz); // signaliziraj N procesa posjetitelja da se ukrcaju
		sem_wait(kapacitet); // čekaj da se svi posjetitelji ukrcaju
		
		pokreni_vrtuljak();
		zaustavi_vrtuljak();
		
		for(int i = 0; i < N; i++) sem_post(red_izlaz); // signaliziraj N procesa posjetitelja da se iskrcaju
		sem_wait(kapacitet); // čekaj da se svi posjetitelji iskrcaju
		printf("Vrtuljak je sada prazan!\n\n");
		sleep(2);
	}
}

void posjetitelj() {
	while(1) {
		sem_wait(red_ulaz); // čekaj da vrtuljak signalizira ukrcavanje
		
		sem_wait(cekaj);
		*usli = *usli + 1;
		sjedi();
		if (*usli == N){
			sem_post(kapacitet); // ako se vrtuljak popunio, signaliziraj da krene
			*usli = 0;
		}
		sem_post(cekaj);

		sem_wait(red_izlaz); // čekaj da se vožnja završi

		sem_wait(cekaj);
		*izasli = *izasli + 1;
		ustani();
		if (*izasli == N){
			sem_post(kapacitet); // ako se vrtuljak ispraznio, signaliziraj da se opet može puniti
			*izasli = 0;
		}
		sem_post(cekaj);
	}
}

int main(int argc, char *argv[]) {
	
	N = atoi(argv[1]);

	int ID;
	
	// zauzimanje zajedničke memorije
	ID = shmget(IPC_PRIVATE, sizeof(sem_t) * 4 + sizeof(int) * 2, 0600);
	if(ID == -1)
		exit(1);

	sem_t *ptr = (sem_t*)shmat(ID, NULL, 0);

	red_ulaz = ptr;
	red_izlaz = red_ulaz + 1;
	kapacitet = red_izlaz + 1;
	cekaj = kapacitet + 1;
	usli = (int *)(cekaj + 1);
	izasli = usli + 1;

	shmctl(ID, IPC_RMID, NULL);

	// kreiraj semafore
	sem_init(red_ulaz, 1, 0);
	sem_init(red_izlaz, 1, 0);
	sem_init(kapacitet, 1, 0);
	sem_init(cekaj, 1, 1);

	*usli = 0;
	*izasli = 0;
	
	// kreiraj procese i pokreni vrtuljak
	if(fork() == 0)
		vrtuljak();
	for(int i = 0; i < N; i++) {
		if(fork() == 0)
			posjetitelj();
	}
	
	for(int i = 0; i <= N; i++)
        wait(NULL);

	// uništi semafore
	sem_destroy(red_ulaz);
	sem_destroy(red_izlaz);
	sem_destroy(kapacitet);
	sem_destroy(cekaj);

	// oslobodi zajedničku memoriju
	shmdt(red_ulaz);
	shmdt(red_izlaz);
	shmdt(kapacitet);
	shmdt(cekaj);
	shmdt(usli);
	shmdt(izasli);

	return 0;
}