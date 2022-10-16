#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<signal.h>
#include<string.h>
#include<math.h>

#define BUFFER 100	//veličina polja za čitanje

/* funkcije za obradu signala, navedene ispod main-a */
void obradi_dogadjaj(int sig);
void obradi_sigterm(int sig);
void obradi_sigint(int sig);

int broj;
char status[] = "status.txt";
char obrada[] = "obrada.txt";

int main() {

	struct sigaction act;

	/* 1. maskiranje signala SIGUSR1 */
	act.sa_handler = obradi_dogadjaj; /* kojom se funkcijom signal obrađuje */
	sigemptyset(&act.sa_mask);
	sigaddset(&act.sa_mask, SIGTERM); /* blokirati i SIGTERM za vrijeme obrade */
	act.sa_flags = 0; /* naprednije mogućnosti preskočene */
	sigaction(SIGUSR1, &act, NULL); /* maskiranje signala preko sučelja OS-a */

	/* 2. maskiranje signala SIGTERM */
	act.sa_handler = obradi_sigterm;
	sigemptyset(&act.sa_mask);
	sigaction(SIGTERM, &act, NULL);

	/* 3. maskiranje signala SIGINT */
	act.sa_handler = obradi_sigint;
	sigaction(SIGINT, &act, NULL);

	char c[BUFFER];
	FILE *filePtr;

	/* pročitaj broj iz status.txt */
	filePtr = fopen(status, "r");
	fgets(c, BUFFER, filePtr);
	sscanf(c, "%d", &broj);
	fclose(filePtr);

	/* ako je broj jednak nuli čitaj brojeve iz obrada.txt dok ne dođes do EOF */
	if(broj == 0) {
		filePtr = fopen(obrada, "r");
		while(fgets(c, BUFFER, filePtr) != NULL) {
			sscanf(c, "%d", &broj);
			broj = sqrt(broj);
		}
		fclose(filePtr);
	}

	/* zapiši nulu u status.txt */
	filePtr = fopen(status, "w");
	fprintf(filePtr, "%d", 0);
	fclose(filePtr);

	filePtr = fopen(obrada, "a");
	int i = 1;
	printf("Program s PID=%ld krenuo s radom\n", (long) getpid());
	while(1) {							//beskonačna petlja
		broj++;
		int x = broj * broj;
		fprintf(filePtr, "%d\n", x);				//dodaj x u obrada.txt
		printf("Program: iteracija %d\n", i++);
		sleep(5);
	}
	fclose(filePtr);

	return 0;
}

/* Obrada signala SIGUSR1, ispisuje trenutni broj koji se koristi u obradi */
void obradi_dogadjaj(int sig) {
	printf("Primio signal SIGUSR1\n");
	printf("Broj signala je %d\n", broj);
}

/* Obrada signala SIGTERM, otvara status.txt, zapisuje zadnji broj i završava s radom */
void obradi_sigterm(int sig) {
	printf("Primio signal SIGTERM, upisujem broj u status.txt i prekidam rad\n");
	FILE *filePtr;
	filePtr = fopen(status, "w");
	fprintf(filePtr, "%d", broj);
	fclose(filePtr);
	exit(1);
}

/* Obrada signala SIGINT, prekida s radom */
void obradi_sigint(int sig) {
	printf("Primio signal SIGINT, prekidam rad\n");
	exit(1);
}
