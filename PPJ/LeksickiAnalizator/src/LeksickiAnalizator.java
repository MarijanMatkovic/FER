import java.io.*;

public class LeksickiAnalizator {

	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int br = 0;				//brojac reda
		String procitaniRedString = new String();
		String ispis = new String();
		
		while((procitaniRedString = reader.readLine()) != null) {
			char[] procitaniRed = procitaniRedString.toCharArray();
			br++;
			String rijec = "";
			for (int i = 0; i < procitaniRed.length; i++) {
				if(rijec.equals("/") && (procitaniRed[i] == '/')) {
					procitaniRedString = "";
					procitaniRed = procitaniRedString.toCharArray();
					rijec = "";
				}
				else if(procitaniRed[i] != ' ') {
					if(rijec.equals("")) {
						rijec += procitaniRed[i];
					}
					else if((Character.isDigit(rijec.charAt(0)) && Character.isDigit(procitaniRed[i])) == true) {
						rijec += procitaniRed[i];
					}
					else if((Character.isAlphabetic(rijec.charAt(0)) && Character.isAlphabetic(procitaniRed[i])) == true) {
						rijec += procitaniRed[i];
					}
					else {
						ispis += ispisi(rijec, br);
						rijec = "" + procitaniRed[i];
					}
				}
				else if(procitaniRed[i] == ' ' && !rijec.equals("")) {
					ispis += ispisi(rijec, br);
					rijec = "";
				}
			}
			if(!rijec.equals("")) {
				ispis += ispisi(rijec, br);
				rijec = "";
			}
		}
		System.out.println(ispis);
	}
	
	public static String ispisi(String rijec, int br) {
		String ispis = new String();
		switch(rijec) {
			case "=": ispis += "OP_PRIDRUZI ";
				break;
			case "+": ispis += "OP_PLUS ";
				break;
			case "-": ispis += "OP_MINUS ";
				break;
			case "*": ispis += "OP_PUTA ";
				break;
			case "/": ispis += "OP_DIJELI ";
				break;
			case "(": ispis += "L_ZAGRADA ";
				break;
			case ")": ispis += "D_ZAGRADA ";
				break;
			case "za": ispis += "KR_ZA ";
				break;
			case "od": ispis += "KR_OD ";
				break;
			case "do": ispis += "KR_DO ";
				break;
			case "az": ispis += "KR_AZ ";
				break;
			default: if(Character.isDigit(rijec.charAt(0))) ispis += "BROJ ";
					 else ispis += "IDN ";
				break;
		}
		ispis += br + " ";
		ispis += rijec + System.lineSeparator();
		
		return ispis;
	}
	
}
