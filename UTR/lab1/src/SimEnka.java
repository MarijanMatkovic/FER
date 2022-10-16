import java.io.*;
import java.util.*;

public class SimEnka {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		//1. red ulazni nizovi
		String[] ulazniNiz = reader.readLine().split("\\|");
		//2. red skup stanja
		String[] stanja = reader.readLine().split(",");
		//3. red skup simbola abecede
		String[] abeceda = reader.readLine().split(",");
		//4. red skup prihvatljivih stanja
		String[] prihvatljiva = reader.readLine().split(",");
		//5. red pocetno stanje
		String pocetno = reader.readLine();
		//6. red i dalje funkcije prijelaza
		String funkcijaPrijelaza = new String();
		TreeMap<String, Map<String, String[]>> prijelazi = new TreeMap<>();
		//dodaj sve definirane prijelaze u TreeMap
		while((funkcijaPrijelaza=reader.readLine()) != null) {
			String[] prijelaz = funkcijaPrijelaza.split("->");						//podijeli funkciju na dva dijeli: [0]=trenutnoStanja,simbolAbecede; [1]=skupIducihStanja
			String trenutno = prijelaz[0].substring(0, prijelaz[0].indexOf(","));	//trenutno=trenutnoStanje
			String simbol = prijelaz[0].substring(prijelaz[0].indexOf(",") + 1);	//simbol=simbolAbecede
			String[] nova = prijelaz[1].split(",");									//nova[]={novoStanje1, novoStanje2,...novoStanjeN} 
			Map<String, String[]> promjena = new TreeMap<>();						
			if(prijelazi.containsKey(trenutno))
				promjena = prijelazi.get(trenutno);
			promjena.put(simbol, nova);												
			prijelazi.put(trenutno, promjena);										
		}
	
		String ispis = new String();
		for(String niz : ulazniNiz) {
			String linija = new String();
			String[] simboli = niz.split(",");
			TreeSet<String> proslaStanja = new TreeSet<>();
			TreeSet<String> prodenaStanja = new TreeSet<>();
			proslaStanja.add(pocetno);
			prodenaStanja.add(pocetno);
			checkEpsilonTransitions(prijelazi, prodenaStanja, pocetno);
			for(String stanje : prodenaStanja) {
				linija = linija.concat(stanje).concat(",");
				proslaStanja.add(stanje);
			}
			linija = linija.substring(0, linija.length() - 1);
			linija = linija.concat("|");
			prodenaStanja.clear();
			for(String simbol : simboli) {
				int f = 0;
				for(String stanje : proslaStanja) {
					if(prijelazi.containsKey(stanje) && prijelazi.get(stanje).get(simbol) != null) {
						for(String prodena : prijelazi.get(stanje).get(simbol)) {
							if(!prodena.equals("#")) {
								f++;
								prodenaStanja.add(prodena);
								checkEpsilonTransitions(prijelazi, prodenaStanja, prodena);
							}
						}
					}
				}
				proslaStanja.clear();
				for(String stanje : prodenaStanja) {
					linija = linija.concat(stanje).concat(",");
					proslaStanja.add(stanje);
				}
				if(f > 0) {
					linija = linija.substring(0, linija.length() - 1);
				}
				else
					linija = linija.concat("#");
				linija = linija.concat("|");
				prodenaStanja.clear();
			}
			linija = linija.substring(0, linija.length() - 1);
			ispis = ispis.concat(linija).concat(System.lineSeparator());
		}
		System.out.println(ispis);
	}
	
	public static void checkEpsilonTransitions(TreeMap<String, Map<String, String[]>> prijelazi, TreeSet<String> prodenaStanja, String stanje) {
		if(!prijelazi.containsKey(stanje) || prijelazi.get(stanje).get("$") == null)
			return;
		for(String epsilon : prijelazi.get(stanje).get("$")) {
			if(!prodenaStanja.contains(epsilon) && !epsilon.equals("#")) {
				prodenaStanja.add(epsilon);
				checkEpsilonTransitions(prijelazi, prodenaStanja, epsilon);
			}
		}
	}
}