import java.io.*;
import java.util.*;

public class SemantickiAnalizator2 {

	public static void main(String[] args) throws IOException {

		ArrayList<String> ulaz = new ArrayList<>();
		ArrayList<ArrayList<String>> nizDefinicija = new ArrayList<>();
		nizDefinicija.add(0, new ArrayList<>());
		int nivo = 0;

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String linija = new String();

		while ((linija = reader.readLine()) != null)
			ulaz.add(linija);

		String proslaLinija = "";

		for (String red : ulaz) {
			if (proslaLinija.endsWith("za")) {
				nivo++;
				nizDefinicija.add(nivo, new ArrayList<>());
				boolean f = true;
				for (String identifikator : nizDefinicija.get(nivo)) {
					if (identifikator.substring(identifikator.length() - 1).equals(red.substring(red.length() - 1)))
						f = false;
				}
				if (f)
					nizDefinicija.get(nivo).add(red.substring(red.indexOf("IDN") + 3));
			} else if (proslaLinija.endsWith("<naredba_pridruzivanja>")) {
				boolean f = true;
				for (String identifikator : nizDefinicija.get(nivo)) {
					if (identifikator.substring(identifikator.length() - 1).equals(red.substring(red.length() - 1)))
						f = false;
				}
				if (f)
					nizDefinicija.get(nivo).add(red.substring(red.indexOf("IDN") + 3));
			} else if (proslaLinija.endsWith("az")) {
				nizDefinicija.remove(nivo);
				nivo--;
			}
			if (!proslaLinija.endsWith("<naredba_pridruzivanja>") && !proslaLinija.endsWith("za") && red.contains("IDN")
					&& !nizDefinicija.get(nivo).contains(red.substring(red.indexOf("IDN") + 3))) {
				boolean f = provjeriIdentifikatore(red, nizDefinicija, nivo);
				if (!f) {
					System.out.println("err" + red.substring(red.lastIndexOf("IDN ") + 3));
					System.exit(0);
				}
			}
			proslaLinija = red;
		}
	}
	
	public static boolean provjeriIdentifikatore(String red, ArrayList<ArrayList<String>> nizDefinicija, int nivo) {
		for (String identifikator : nizDefinicija.get(nivo)) {
			if (identifikator.substring(identifikator.length() - 1).equals(red.substring(red.length() - 1))) {
				System.out.println(
						red.substring(red.lastIndexOf("IDN ") + 4, red.lastIndexOf(' ')) + identifikator);
				return true;
			}
		}
		return nivo == 0 ? false : provjeriIdentifikatore(red, nizDefinicija, nivo - 1);
	}
}
