import java.io.*;
import java.util.*;


public class MinDka {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		//1. red skup stanja
		String[] stanja = reader.readLine().split(",");
		//2. red skup simbola abecede
		String[] abeceda = reader.readLine().split(",");
		//3. red skup prihvatljivih stanja
		String[] prihvatljivaNiz = reader.readLine().split(",");
		TreeSet<String> prihvatljivaSet = new TreeSet<>();
		for(String prihvaljivo : prihvatljivaNiz)
			prihvatljivaSet.add(prihvaljivo);
		//4. red pocetno stanje
		String pocetno = reader.readLine();
		//5. red i dalje funkcije prijelaza
		String funkcijaPrijelaza = new String();
		TreeMap<String, String> prijelazi = new TreeMap<>();
		//dodaj sve definirane prijelaze u TreeMap
		while((funkcijaPrijelaza=reader.readLine()) != null) {
			String[] prijelaz = funkcijaPrijelaza.split("->");
			prijelazi.put(prijelaz[0], prijelaz[1]);
		}
		
		TreeSet<String> dohvatljivaStanja = new TreeSet<>();
		TreeSet<String> novaStanja = new TreeSet<>();
		dohvatljivaStanja.add(pocetno);
		novaStanja.add(pocetno);
		do {
			TreeSet<String> tmp = new TreeSet<>();
			for(String stanje : novaStanja) {
				for(String simbol : abeceda) {
					String prijelaz = stanje + "," + simbol;
					if(prijelazi.containsKey(prijelaz))
						tmp.add(prijelazi.get(prijelaz));
				}
			}
			novaStanja = tmp;
			novaStanja.removeAll(dohvatljivaStanja);
			dohvatljivaStanja.addAll(novaStanja);
		} while(!novaStanja.isEmpty());
		
		TreeSet<String> prihvatljivaStara = new TreeSet<>();
		for(String prihvatljivo : prihvatljivaSet) {
			int f = 0;
			for(String stanje : dohvatljivaStanja) {
				if(stanje.equals(prihvatljivo))
					f++;
			}
			if(f == 0)
				prihvatljivaStara.add(prihvatljivo);
		}
		prihvatljivaSet.removeAll(prihvatljivaStara);
		
		TreeSet<String> prijelaziStari = new TreeSet<>();
		for(Map.Entry<String, String> entry : prijelazi.entrySet()) {
			String[] key = entry.getKey().split(",");
			int f = 0;
			for(String stanje : dohvatljivaStanja) {
				if(stanje.equals(key[0]))
					f++;
			}
			if(f == 0)
				prijelaziStari.add(entry.getKey());
		}
		for(String key : prijelaziStari) {
			if(prijelazi.containsKey(key))
				prijelazi.remove(key);
		}
		
		TreeSet<String> neprihvatljiva = new TreeSet<>();
		TreeSet<String> prihvatljiva = new TreeSet<>();
		prihvatljiva.addAll(prihvatljivaSet);
		neprihvatljiva.addAll(dohvatljivaStanja);
		neprihvatljiva.removeAll(prihvatljiva);
		ArrayList<TreeSet<String>> grupe = new ArrayList<>();
		grupe.add(prihvatljiva);
		grupe.add(neprihvatljiva);
		ArrayList<TreeSet<String>> noveGrupe = new ArrayList<>();
		do {
			noveGrupe.clear();
			for(TreeSet<String> grupa : grupe) {
				if(!grupa.isEmpty()) {
					TreeSet<String> novaGrupa = new TreeSet<>();
					String prvo = grupa.first();
					for(String stanje : grupa) {
						int changeHappened = 0;
						for(String simbol : abeceda) {
							for(TreeSet<String> grupaOpet : grupe) {
								if(grupaOpet.contains(prijelazi.get(prvo+","+simbol)))
									if(!grupaOpet.contains(prijelazi.get(stanje+","+simbol)))
										changeHappened++;
							}
						}
						if(changeHappened > 0)
							novaGrupa.add(stanje);
					}
					if(!novaGrupa.isEmpty())
						noveGrupe.add(novaGrupa);
					grupa.removeAll(novaGrupa);
				}
			}
			if(!noveGrupe.isEmpty())
				grupe.addAll(noveGrupe);
		} while(!noveGrupe.isEmpty());
		
		dohvatljivaStanja.clear();
		for(TreeSet<String> grupa : grupe)
			if(!grupa.isEmpty())
				dohvatljivaStanja.add(grupa.first());
		
		prihvatljivaStara.clear();
		for(String prihvatljivo : prihvatljivaSet) {
			int f = 0;
			for(String stanje : dohvatljivaStanja) {
				if(stanje.equals(prihvatljivo))
					f++;
			}
			if(f == 0)
				prihvatljivaStara.add(prihvatljivo);
		}
		prihvatljivaSet.removeAll(prihvatljivaStara);
		
		prijelaziStari.clear();
		for(Map.Entry<String, String> entry : prijelazi.entrySet()) {
			String[] key = entry.getKey().split(",");
			int f = 0;
			for(String stanje : dohvatljivaStanja) {
				if(stanje.equals(key[0]))
					f++;
			}
			if(f == 0)
				prijelaziStari.add(entry.getKey());
		}
		for(String key : prijelaziStari) {
			if(prijelazi.containsKey(key))
				prijelazi.remove(key);
		}

		for(Map.Entry<String, String> entry : prijelazi.entrySet()) {
			for(TreeSet<String> grupa : grupe) {
				if(!grupa.isEmpty() && grupa.contains(entry.getValue())) {
					if(!grupa.first().equals(entry.getValue()))
						prijelazi.replace(entry.getKey(), grupa.first());
				}
			}
		}
		
		for(TreeSet<String> grupa : grupe) {
			if(!grupa.isEmpty() && grupa.contains(pocetno))
				if(!grupa.first().equals(pocetno))
					pocetno = grupa.first();
		}
		
		String ispis = new String();
		
		for(String stanje : dohvatljivaStanja)
			ispis = ispis.concat(stanje).concat(",");
		ispis = ispis.substring(0, ispis.length() - 1);
		ispis = ispis.concat(System.lineSeparator());
		
		for(String simbol : abeceda)
			ispis = ispis.concat(simbol).concat(",");
		ispis = ispis.substring(0, ispis.length() - 1);
		ispis = ispis.concat(System.lineSeparator());
		
		int f = 0;
		for(String prihvatljivo : prihvatljivaSet) {
			ispis = ispis.concat(prihvatljivo).concat(",");
			f++;
		}
		if(f > 0)
			ispis = ispis.substring(0, ispis.length() - 1);
		ispis = ispis.concat(System.lineSeparator());
		
		ispis = ispis.concat(pocetno).concat(System.lineSeparator());
		
		for(Map.Entry<String, String> entry : prijelazi.entrySet()) {
			ispis = ispis.concat(entry.getKey()).concat("->").concat(entry.getValue()).concat(System.lineSeparator());
		}
		
		ispis = ispis.substring(0, ispis.length() - 1);			//mozda ne triba 
		
		System.out.println(ispis);
	}

}
