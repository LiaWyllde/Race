package view;

import java.util.concurrent.Semaphore;
import controller.Race;


public class Main {
		
	public static void main(String[] args) {
		Semaphore[] equipes = new Semaphore[7];
	    	
		for (int i = 0; i < 7; i++) {
			equipes[i] = new Semaphore(1);}
	      
		for (int j = 0; j < 14; j++) {
			Race carro = new Race(equipes,(j));
			carro.start();
			}
	 }
}
