/*
 * Algoritmo Playfair
 * Thiago David de Lima - Sistemas de Informação -PUC/Betim - Outubro de 2021
 */
package criptografias;

import java.util.Scanner;

public class Playfair {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String pw, alfabeto = "abcdefghiklmnopqrstuvwxyz", // sem o j pra dar 25
				frase, fEncriptada;
		char letraAux = 'x'; // letra com baixa frequencia no alfabeto. No inglês q ou x. no portugues poderia ser k, w ou y.
		
		char[][] matriz = new char[5][5]; // matriz que recebe o alfabeto		
		
		
		System.out.println("Escolha a palavra chave");
		pw = in.nextLine().toLowerCase();
		pw = pw.replace('j', 'i'); // trocar todos os jotas da pw por i para montar a tabela
		pw = pw.replaceAll("\\s", ""); // remove espaços em branco
				
		// if (pw.length() >= 25)			System.out.println("palavra chave deve ter menos que 25 caracteres");
		
		// inserir pw na matriz (remove a repeticao de letras na pw antes)	
		insertInMatrix(matriz, pwSemRepeticao(pw));
		// inserir alfabeto na matriz
		insertAlfabeto(matriz, alfabeto, pw);

		showMatrix(matriz);
		
		// matriz criada. pegar palavra pra criptografar.
		System.out.println("Informe a frase para criptografar");
		frase = in.nextLine().replaceAll("\\s", "").toLowerCase();// remover espaços. 
		frase = frase.replace('j', 'i'); // substituir j por i	
		
		fEncriptada = criptografar(frase, matriz, letraAux);
		System.out.println(fEncriptada); // mostra criptografada
		
		System.out.println(deCriptografar(fEncriptada, matriz, letraAux)); // mostra decriptografada
		// fechar leitura
		in.close();
	}

	
	private static String pwSemRepeticao(String pw) {
		// remove letras repetidas
		String novaPW = "";
		char letra;
		for (int i = 0; i < pw.length(); i++) {
			letra = pw.charAt(i);
			// verificar index off da letra antes de inserir
			if(novaPW.indexOf(letra) < 0) {
				novaPW += letra;
			}
		}
		
		return novaPW;
	}

	private static String criptografar(String frase, char[][] matriz, char letraAux) {
		
		String pos1, pos2, msgCripto = ""; // posicao da letra 1 e 2
		int lin1, col1, lin2, col2, dif;
		for (int i = 0; i < frase.length(); i++) {
			pos1 = indexOf(matriz, frase.charAt(i));			
			if((i+1)<frase.length() && frase.charAt(i) != frase.charAt(i+1)) // se letras diferentes
				pos2 = indexOf(matriz, frase.charAt(++i)); // caso diferente  pega a proxima e incrementa contador 2 vezes
			else // caso letras iguais
				pos2 = indexOf(matriz, letraAux);
			
			// calcular coluna e linha das letras
			lin1 = Character.getNumericValue(pos1.charAt(0)); // pegar coluna da letra 1
			col1 = Character.getNumericValue(pos1.charAt(pos1.length()-1));// linha letra 1
			lin2 = Character.getNumericValue(pos2.charAt(0)); // pegar coluna da letra 1
			col2 = Character.getNumericValue(pos2.charAt(pos1.length()-1));// linha letra 1
			
			if(lin1 == lin2) {
				// mesma linha
				msgCripto += matriz[lin1][(col1+1)%5]; // pega letra após letra1
				msgCripto += matriz[lin2][(col2+1)%5]; // pega letra após letra2
			}
			else if(col1 == col2){
				// mesma coluna
				msgCripto += matriz[(lin1+1)%5][col1]; // pega letra abaixo letra1
				msgCripto += matriz[(lin2+1)%5][col2]; // pega letra abaixo letra2
			}
				
			else {
				// diagonal
				// mesma linha coluna diferente - letra 1
				dif = col1 - col2; // diferença entre colunas da letra 1 pra 2	
				//se dif negativa, col1 menor q col2, então tem que somar dif a col1 e subtrair da coluna2
				// se dif positivo, faz o oposto.
				// na prática a col1 vai sempre fazer a operação oposta ao sinal da dif, então é só subitrair pra inverter o sinal da dif.
				
				msgCripto += matriz[lin1][(col1 - dif)]; // soma se dif negativo e subtrais se dif positivo
				// mesma linha coluna diferente - letra 1
				msgCripto += matriz[lin2][(col2 + dif)]; // o contrário. soma se dif positivo e subtrai se negativo 	
			}
		}
		
		return msgCripto;
	}
	
private static String deCriptografar(String cifra, char[][] matriz, char letraAux) {
		
		String pos1, pos2, msgCripto = ""; // posicao da letra 1 e 2
		int lin1, col1, lin2, col2, dif;
		for (int i = 0; i < cifra.length(); i++) {
			pos1 = indexOf(matriz, cifra.charAt(i));			
			if((i+1)<cifra.length() && cifra.charAt(i) != cifra.charAt(i+1)) // se letras iguais
				pos2 = indexOf(matriz, cifra.charAt(++i)); // caso diferente  pega a proxima e incrementa contador 2 vezes
			else
				pos2 = indexOf(matriz, letraAux);
			
			// calcular coluna e linha das letras
			lin1 = Character.getNumericValue(pos1.charAt(0)); // pegar coluna da letra 1
			col1 = Character.getNumericValue(pos1.charAt(pos1.length()-1));// linha letra 1
			lin2 = Character.getNumericValue(pos2.charAt(0)); // pegar coluna da letra 1
			col2 = Character.getNumericValue(pos2.charAt(pos1.length()-1));// linha letra 1
			
			if(lin1 == lin2) {
				// mesma linha
				msgCripto += matriz[lin1][(col1+4)%5];
				msgCripto += matriz[lin2][(col2+4)%5];
			}
			else if(col1 == col2){
				// mesma coluna
				msgCripto += matriz[(lin1+4)%5][col1];
				msgCripto += matriz[(lin2+4)%5][col2];
			}
				
			else { // a lógica pra decritografar é a mesma / não muda, caso a letra esteja na diagonal
				// mesma linha coluna diferente - letra 1
				dif = col1 - col2; // diferença entre colunas da letra 1 pra 2	
				//se dif negativa, quer dizer que tem que somar pois col1 menor q col2
				// e vice versa
				msgCripto += matriz[lin1][(col1 - dif)];
				// mesma linha coluna diferente - letra 1
				msgCripto += matriz[lin2][(col2 + dif)]; // só inverte o sinal do cálculo da coluna				
			}
		}
		
		return msgCripto;
	}

	private static void showMatrix(char[][] matriz) {

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				if(matriz[i][j] == 'i')
					System.out.printf("%c/j  ", matriz[i][j]);
				else
					System.out.printf("%-4c ", matriz[i][j]);
			}
			System.out.println();
		}
		
	}

	private static boolean insertAlfabeto(char[][] matriz, String alfabeto, String pw) {
		char letra='|';
		// para quando passar por toda a matriz (25 espaços)
		for (int i = 0, p = 0; i < matriz.length; i++) { // p é posicao na string alfabeto
			for (int j = 0; j < matriz[i].length; j++) {
				if ((int) matriz[i][j] == 0) { // procuarar posição vazia
					
					// verificar se existe em pw antes de inserir e se p>0
					do {
						if(p < alfabeto.length()) // evitar estouro{
						{
							letra = alfabeto.charAt(p++); // pegar letra do alfabeto
							//if(letra == 'j')letra = 'i';
						}
						else
							return false; // algo deu errado se nao conseguir pegar uma letra.
					}while ( pw.indexOf(letra) >= 0); // repete se estiver na palavra chave 
										
					matriz[i][j] = letra; // insere a letra

				}
			}
		}
		return true;
	}

	// retorna verdadeiro caso consiga inserir a palavra letra a letra no vetor
	private static boolean insertInMatrix(char[][] matriz, String pw) {
		
		for (int i = 0, p = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				if (p < pw.length())
					matriz[i][j] = pw.charAt(p++);
				else
					return true;
			}
		}
		return false;
	}

	
	// retorna a posicao na matriz em que a letra ocorre no formato string: "lin,col" ou "" se não encontrar
	public static String indexOf(char matriz[][], char letra) {

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				if (letra == matriz[i][j])
					return i + "," + j;
			}
		}
		return "";
	}

	
}
