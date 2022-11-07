import java.io.*;
import java.util.*;

public class SintaksniAnalizator {

	public static ArrayList<String> niz = new ArrayList<>();
	public static String izlaz = "";
	public static String ulaz;
	public static int indeks = 0;

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String linija = new String();

		while ((linija = reader.readLine()) != null)
			niz.add(linija);

		ulaz = niz.get(indeks);
		indeks++;

		program(0);

		System.out.println(izlaz);
	}

	public static void program(int razmak) {
		izlaz = izlaz.concat("<program>").concat(System.lineSeparator());
		razmak++;
		if (ulaz.startsWith("IDN") || ulaz.startsWith("KR_ZA") || ulaz == null)
			lista_naredbi(razmak);
		else
			provjeriGresku();
	}

	public static void lista_naredbi(int razmak) {
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<lista_naredbi>").concat(System.lineSeparator());
		razmak++;
		if (ulaz == null || ulaz.startsWith("KR_AZ")) {
			for (int i = 0; i < razmak; i++)
				izlaz += " ";
			izlaz = izlaz.concat("$").concat(System.lineSeparator());
			return;
		} else if (ulaz.startsWith("IDN") || ulaz.startsWith("KR_ZA")) {
			naredba(razmak);
			lista_naredbi(razmak);
		} else
			provjeriGresku();
	}

	public static void naredba(int razmak) {
		if (ulaz == null) {
			ispisiZnakPraznine(razmak);
			return;
		}
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<naredba>").concat(System.lineSeparator());
		razmak++;
		if (ulaz.startsWith("IDN"))
			naredba_pridruzivanja(razmak);
		else if (ulaz.startsWith("KR_ZA"))
			za_petlja(razmak);
		else
			provjeriGresku();
	}

	public static void naredba_pridruzivanja(int razmak) {
		boolean f = false;
		if (ulaz == null) {
			ispisiZnakPraznine(razmak);
			return;
		}
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<naredba_pridruzivanja>").concat(System.lineSeparator());
		razmak++;
		if (ulaz.startsWith("IDN")) {
			provjeriKrajUlaza(razmak);
			if (ulaz == null) {
				ispisiZnakPraznine(razmak);
				return;
			} else if (ulaz.startsWith("OP_PRIDRUZI")) {
				provjeriKrajUlaza(razmak);
				f = E(razmak);
			}
		}
		if (f == false)
			provjeriGresku();
	}

	public static void za_petlja(int razmak) {
		if (ulaz == null) {
			ispisiZnakPraznine(razmak);
			return;
		}
		boolean f = false;
		boolean e = false;
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<za_petlja>").concat(System.lineSeparator());
		razmak++;
		if (ulaz.startsWith("KR_ZA")) {
			provjeriKrajUlaza(razmak);
			if (ulaz == null) {
				ispisiZnakPraznine(razmak);
				return;
			} else if (ulaz.startsWith("IDN")) {
				provjeriKrajUlaza(razmak);
				if (ulaz == null) {
					ispisiZnakPraznine(razmak);
					return;
				} else if (ulaz.startsWith("KR_OD")) {
					provjeriKrajUlaza(razmak);
					f = E(razmak);
					if (ulaz == null) {
						ispisiZnakPraznine(razmak);
						return;
					} else if (ulaz.startsWith("KR_DO")) {
						provjeriKrajUlaza(razmak);
						e = E(razmak);
						lista_naredbi(razmak);
						if (ulaz.startsWith("KR_AZ"))
							provjeriKrajUlaza(razmak);
					}
				}
			}
		}
		if ((e && f) == false)
			provjeriGresku();
		return;
	}

	public static boolean E(int razmak) {
		if (ulaz == null) {
			ispisiZnakPraznine(razmak);
			return false;
		}
		boolean f = false;
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<E>").concat(System.lineSeparator());
		razmak++;
		if (ulaz.startsWith("IDN") || ulaz.startsWith("BROJ") || ulaz.startsWith("OP_PLUS")
				|| ulaz.startsWith("OP_MINUS") || ulaz.startsWith("L_ZAGRADA")) {
			f = T(razmak);
			E_lista(razmak);
		} else
			provjeriGresku();
		return f;
	}

	public static void E_lista(int razmak) {
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<E_lista>").concat(System.lineSeparator());
		razmak++;
		if (ulaz == null || ulaz.startsWith("IDN") || ulaz.startsWith("KR_ZA") || ulaz.startsWith("KR_DO")
				|| ulaz.startsWith("KR_AZ") || ulaz.startsWith("D_ZAGRADA")) {
			for (int i = 0; i < razmak; i++)
				izlaz += " ";
			izlaz = izlaz.concat("$").concat(System.lineSeparator());
			return;
		} else if (ulaz.startsWith("OP_PLUS") || ulaz.startsWith("OP_MINUS")) {
			provjeriKrajUlaza(razmak);
			E(razmak);
		} else
			provjeriGresku();
	}

	public static boolean T(int razmak) {
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<T>").concat(System.lineSeparator());
		razmak++;
		if (ulaz == null) {
			ispisiZnakPraznine(razmak);
			return false;
		}
		boolean f = false;
		if (ulaz.startsWith("IDN") || ulaz.startsWith("BROJ") || ulaz.startsWith("OP_PLUS")
				|| ulaz.startsWith("OP_MINUS") || ulaz.startsWith("L_ZAGRADA")) {
			f = P(razmak);
			T_lista(razmak);
		} else
			provjeriGresku();
		return f;
	}

	public static void T_lista(int razmak) {
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<T_lista>").concat(System.lineSeparator());
		razmak++;
		if (ulaz == null || ulaz.startsWith("IDN") || ulaz.startsWith("KR_ZA") || ulaz.startsWith("KR_DO")
				|| ulaz.startsWith("KR_AZ") || ulaz.startsWith("OP_MINUS") || ulaz.startsWith("D_ZAGRADA")) {
			for (int i = 0; i < razmak; i++)
				izlaz += " ";
			izlaz = izlaz.concat("$").concat(System.lineSeparator());
			return;
		} else if (ulaz.startsWith("OP_PUTA") || ulaz.startsWith("OP_DIJELI")) {
			provjeriKrajUlaza(razmak);
			T(razmak);
		} else {
			for (int i = 0; i < razmak; i++)
				izlaz += " ";
			izlaz = izlaz.concat("$").concat(System.lineSeparator());
			return;
		}
	}

	public static boolean P(int razmak) {
		if (ulaz == null) {
			ispisiZnakPraznine(razmak);
			return false;
		}
		boolean f = false;
		int e = 0;
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("<P>").concat(System.lineSeparator());
		razmak++;
		if (ulaz.startsWith("OP_PLUS") || ulaz.startsWith("OP_MINUS")) {
			provjeriKrajUlaza(razmak);
			f = P(razmak);
		} else if (ulaz.startsWith("L_ZAGRADA")) {
			e++;
			for (int i = 0; i < razmak; i++)
				izlaz += " ";
			izlaz = izlaz.concat(ulaz).concat(System.lineSeparator());
			ulaz = niz.get(indeks);
			indeks++;
			f = E(razmak);
			if (ulaz == null) {
				ispisiZnakPraznine(razmak);
				return false;
			} else if (ulaz.startsWith("D_ZAGRADA")) {
				e--;
				provjeriKrajUlaza(razmak);
				return true;
			}
		} else if (ulaz.startsWith("IDN") || ulaz.startsWith("BROJ")) {
			provjeriKrajUlaza(razmak);
			return true;
		} else
			provjeriGresku();
		if (e != 0)
			provjeriGresku();
		return f;
	}

	public static void ispisiZnakPraznine(int razmak) {
		izlaz = izlaz.concat(System.lineSeparator());
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat("$");
	}

	public static void provjeriGresku() {
		if (indeks == niz.size() && ulaz == null) {
			System.out.println("err kraj");
			System.exit(0);
		} else {
			System.out.println("err " + ulaz);
			System.exit(0);
		}
	}

	public static void provjeriKrajUlaza(int razmak) {
		for (int i = 0; i < razmak; i++)
			izlaz += " ";
		izlaz = izlaz.concat(ulaz).concat(System.lineSeparator());
		if (indeks < niz.size()) {
			ulaz = niz.get(indeks);
			indeks++;
		} else
			ulaz = null;
	}
}
