package controller;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Race extends Thread {
    
    private static int nCarros = 14;
    private static final int nEquipes = 7;
    private static final int nVoltas = 3;
    private static final int maxCarros = 5;
    private int idCarro;
    private int equipe;
    Random rand = new Random();
    
    private static int[][] ranking = new int[14][2];
    
    private static Semaphore pista = new Semaphore(maxCarros);
    private static Semaphore[] equipes = new Semaphore[nEquipes];
    
    public Race(Semaphore[] s, int id) {
        equipes = s;
        this.idCarro = id;
        this.equipe = (id / 2);
    }
    
    private void toNaPista() {
    	//Quem estiver de carro, me atropela! (:
    	
    	int mTempo = 10000;
        System.out.println("O carro " + (idCarro+1) + " da equipe " + (equipe + 1) + " entrou na pista");

        for (int volta = 0; volta < nVoltas; volta++) {
            int tempoVolta = rand.nextInt(3,30);

            if (tempoVolta < mTempo) { 
                mTempo = tempoVolta;
                ranking[idCarro][0] = (idCarro+1); 
                ranking[idCarro][1] = mTempo;
            }
            
            System.out.println("O carro " + (idCarro+1) + " da equipe " + (equipe + 1) + " completou a volta " + (volta + 1) + " em " + tempoVolta + " segundos");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
    }
    
    private static void gridLargada() {
        int[][] melhoresTempos = new int[14][2];
        for (int i = 0; i < 14; i++) {
            melhoresTempos[i][0] = ranking[i][0];
            melhoresTempos[i][1] = ranking[i][1]; 
        }
        melhoresTempos = ordenarMatriz(melhoresTempos);

        System.out.println("\n---------------------------------- \n        Grid de Largada    \n----------------------------------\n");
        for (int i = 0; i < 14; i++) {
            System.out.println((i + 1) + ". Carro " + melhoresTempos[i][0] + " - Melhor tempo: " + melhoresTempos[i][1] + " segundos");
        }
    }
    
    private static int[][] ordenarMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length - 1; i++) {
            for (int j = i + 1; j < matriz.length; j++) {
                if (matriz[i][1] > matriz[j][1]) {
                    int[] temp = matriz[i];
                    matriz[i] = matriz[j];
                    matriz[j] = temp;
                }
            }
        }
        return matriz;
    }

    @Override
    public void run() {
        try {
            equipes[equipe].acquire();
            pista.acquire();
            toNaPista();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pista.release();
            equipes[equipe].release();
            System.out.println("O carro " + (idCarro+1) + " da equipe " + (equipe + 1) + " saiu da pista");
            --nCarros;
        }
        if (nCarros <= 0) {
        	gridLargada();
        }
    }
}
