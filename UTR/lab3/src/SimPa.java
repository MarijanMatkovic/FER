import java.io.*;
import java.util.*;

public class SimPa {

	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		//1. red ulazni nizovi
		String[] ulazniNiz = reader.readLine().split("\\|");
		//2. red skup stanja
		String[] stanja = reader.readLine().split(",");
		//3. red skup simbola abecede
		String[] abeceda = reader.readLine().split(",");
		//4. red skup znakova stoga
		String[] stog = reader.readLine().split(",");
		//5. red skup prihvatljivih stanja
		String[] prihvatljiva = reader.readLine().split(",");
		//6. red pocetno stanje
		String pocetnoStanje = reader.readLine();
		//7. red pocetni znak stoga
		String pocetniZnak = reader.readLine();
		//8. red i dalje funkcije prijelaza
		String funkcijaPrijelaza = new String();
		TreeMap<String, Map<String, String>> prijelazi = new TreeMap<>();
		//dodaj sve definirane prijelaze u TreeMap
		while((funkcijaPrijelaza = reader.readLine()) != null) {
			String[] prijelaz = funkcijaPrijelaza.split("->");						//podijeli funkciju na dva dijeli: [0]=trenutnoStanja,ulazniZnak,znakStoga; [1]=novoStanje,nizZnakovaStoga
			String[] prvi = prijelaz[0].split(",");
			String trenutnoStanje = prvi[0];
			String simbolAbecede = prvi[1];
			String znakStoga = prvi[2];
			String novoStanje = prijelaz[1].split(",")[0];
			String noviStog = prijelaz[1].split(",")[1];
			Map<String, String> promjena = new TreeMap<>();						
			if(prijelazi.containsKey(trenutnoStanje + "," + znakStoga))
				promjena = prijelazi.get(trenutnoStanje + "," + znakStoga);
			promjena.put(simbolAbecede, novoStanje + "," + noviStog);												
			prijelazi.put(trenutnoStanje + "," + znakStoga, promjena);										
		}
		String ispis = new String();
		for(String niz : ulazniNiz) {
			String linija = new String();
			String[] simboli = niz.split(",");
			linija = linija.concat(pocetnoStanje).concat("#").concat(pocetniZnak).concat("|");
			String trenutnoStanje = pocetnoStanje;
			String trenutniZnak = pocetniZnak;
			String stogNiz = pocetniZnak;
			for(int i = 0; i < simboli.length; i++) {
				if(prijelazi.containsKey(trenutnoStanje + "," + trenutniZnak) && (prijelazi.get(trenutnoStanje + "," + trenutniZnak).get(simboli[i]) != null)) {
					String[] novo = prijelazi.get(trenutnoStanje + "," + trenutniZnak).get(simboli[i]).split(",");
					if(novo[1].equals("$"))
						stogNiz = stogNiz.substring(1);
					else
						stogNiz = novo[1] + stogNiz.substring(1);
					linija = linija.concat(novo[0]).concat("#").concat(stogNiz).concat("|");
					trenutnoStanje = novo[0];
					trenutniZnak = stogNiz.substring(0, 1);
				}
				else if(prijelazi.containsKey(trenutnoStanje + "," + trenutniZnak) && (prijelazi.get(trenutnoStanje + "," + trenutniZnak).get("$") != null)) {
					String[] novo = prijelazi.get(trenutnoStanje + "," + trenutniZnak).get("$").split(",");
					if(novo[1].equals("$")) {
						if(stogNiz.length() > 1)
							stogNiz = stogNiz.substring(1);
						else
							stogNiz = "$";
					}
					else
						stogNiz = novo[1] + stogNiz.substring(1);
					linija = linija.concat(novo[0]).concat("#").concat(stogNiz).concat("|");
					trenutnoStanje = novo[0];
					trenutniZnak = stogNiz.substring(0, 1);
					i--;
				}
				else {
					linija = linija.concat("fail|0");
					break;
				}
			}
			TreeSet<String> prihvatljivaSet = new TreeSet<>();
			for(String prihvaljivo : prihvatljiva)
				prihvatljivaSet.add(prihvaljivo);
			while(!prihvatljivaSet.contains(trenutnoStanje) && prijelazi.containsKey(trenutnoStanje + "," + trenutniZnak) && prijelazi.get(trenutnoStanje + "," + trenutniZnak).get("$") != null) {
				String[] novo = prijelazi.get(trenutnoStanje + "," + trenutniZnak).get("$").split(",");
				if(novo[1].equals("$")) {
					if(stogNiz.length() > 1)
						stogNiz = stogNiz.substring(1);
					else
						stogNiz = "$";
				}
				else
					stogNiz = novo[1] + stogNiz.substring(1);
				linija =linija.concat(novo[0]).concat("#").concat(stogNiz).concat("|");
				trenutnoStanje = novo[0];
				trenutniZnak = novo[1].substring(0, 1);
			}
			if(!linija.endsWith("0")) {
				if(prihvatljivaSet.contains(trenutnoStanje))
					linija = linija.concat("1");
				else
					linija = linija.concat("0");
			}
			ispis = ispis.concat(linija).concat(System.lineSeparator());
		}
		System.out.println(ispis);
	}
	
}