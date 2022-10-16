import java.io.*;

public class Parser {

	public static String niz;
	public static String izlaz = "";
	public static char ulaz;
	public static int indeks = 0;
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		niz = reader.readLine().concat("$");
		ulaz = niz.charAt(indeks);
		indeks++;
		
		S();
		
		System.out.println(izlaz);
		if(ulaz == '$')
			System.out.println("DA");
		else
			System.out.println("NE");

	}
	
	public static void S() {
		izlaz = izlaz.concat("S");
		if(ulaz != 'a' && ulaz != 'b') {
			System.out.println(izlaz);
			System.out.println("NE");
			System.exit(0);
			
		}
		if(ulaz == 'a') {
			ulaz = niz.charAt(indeks);
			indeks++;
			A();
			B();
		}
		else if(ulaz == 'b') {
			ulaz = niz.charAt(indeks);
			indeks++;
			B();
			A();
		}
	}
	
	public static void A() {
		izlaz = izlaz.concat("A");
		if(ulaz != 'a' && ulaz != 'b') {
			System.out.println(izlaz);
			System.out.println("NE");
			System.exit(0);
		}
		if(ulaz == 'a') {
			ulaz = niz.charAt(indeks);
			indeks++;
			return;
		}
		else if(ulaz == 'b') {
			ulaz = niz.charAt(indeks);
			indeks++;
			C();
		}
	}

	public static void B() {
		izlaz = izlaz.concat("B");
		if(ulaz == 'c') {
			ulaz = niz.charAt(indeks);
			indeks++;
			if(ulaz != 'c') {
				System.out.println(izlaz);
				System.out.println("NE");
				System.exit(0);
			}
			ulaz = niz.charAt(indeks);
			indeks++;
			S();
			if(ulaz != 'b') {
				System.out.println(izlaz);
				System.out.println("NE");
				System.exit(0);
			}
			ulaz = niz.charAt(indeks);
			indeks++;
			if(ulaz != 'c') {
				System.out.println(izlaz);
				System.out.println("NE");
				System.exit(0);
			}
			ulaz = niz.charAt(indeks);
			indeks++;
		}
	}
	
	public static void C() {
		izlaz = izlaz.concat("C");
		A();
		A();
	}
}
