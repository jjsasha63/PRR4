package com.red;

import java.beans.IntrospectionException;
import java.util.*;

public class Main {

    private static int iterations;

    private static int[][] init_board(ArrayList<Integer> queen){
        int[][] board = new int[8][8];
        for (int i =0;i<queen.size();i+=2) board[queen.get(i)-1][queen.get(i+1)-1] = 1;
        return board;
    }

    private static int[][] init_board_genome(ArrayList<Integer> queen){
        int[][] board = new int[8][8];
        for (int i =0;i<queen.size();i++) board[i][queen.get(i)-1] = 1;
        return board;
    }

    private static void print_board(int[][] board){
        for(int[] i:board){
            for (int j:i){
                if(j==1) System.out.print( " \uD83D\uDC51 ");
                else System.out.print(" \u2B24 ");
            }
            System.out.println();
        }
    }

    private static int count_strikes(ArrayList<Integer> queen,boolean mode){
        int[][] board = init_board(queen);
        int count_final = 0,count = 0;
        for(int i =0;i<queen.size();i+=2){
            if(queen.get(i)<queen.get(i+1)){
            for(int j =0;j<6-(queen.get(i+1)-queen.get(i)-2);j++)
                if (board[j][queen.get(i + 1) - queen.get(i) + j] == 1 && j != queen.get(i) - 1) count++;
            }else if(queen.get(i)==queen.get(i+1)){
                for(int j =0;j<8;j++)
                    if (board[j][j] == 1 && j != queen.get(i) - 1) count++;
            }
                else{
                for(int j=0;j<6-(queen.get(i)-queen.get(i+1)-2);j++){
                    if(board[queen.get(i) - queen.get(i+1) + j][j]==1&&j!=queen.get(i+1)-1)  count++;
                }}
            if(queen.get(i)+queen.get(i+1)>9){
                for(int j=7;j>=queen.get(i)+queen.get(i+1)-2-7;j--)
                    if(board[(queen.get(i)+queen.get(i+1)-2-7)+(7-j)][j]==1&&j!=queen.get(i+1)-1)  count++;
                } else if(queen.get(i)+queen.get(i+1)==9){
                for(int j =0;j<8;j++)
                    if (board[7-j][j] == 1 && j != queen.get(i) - 1) count++;
            }else{
                for(int j =0;j<queen.get(i+1)+queen.get(i)-2;j++)
                    if (board[j][queen.get(i+1)+queen.get(i)-2-j] == 1 && j != queen.get(i) - 1) count++;
                }
                for(int j=0;j<8;j++){
                    if(board[j][queen.get(i+1)-1]==1&&j!=queen.get(i)-1)  count++;
                    if(board[queen.get(i)-1][j]==1&&j!=queen.get(i+1)-1)  count++;
                }
                count_final+=count;
             if(mode) System.out.println("Queen with coordinates - (" + queen.get(i) + ";" + queen.get(i+1) + ") have " + count + " possible enemies to strike ");
             count = 0;
        }
        return count_final;
    }

    private static ArrayList<Integer> input_queens(){
        ArrayList<Integer> queen = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int n;
        System.out.println("How many queens you want to insert");
        n = scanner.nextInt();
        for(int i=0;i<n;i++){
            System.out.print("Insert X: ");
            queen.add(scanner.nextInt());
            System.out.print("Insert Y: ");
            queen.add(scanner.nextInt());
        }
        return queen;
    }

    private static int fitness(ArrayList<Integer> queen){
        ArrayList<Integer> temp_queen = new ArrayList<>();
        for(int i=1;i<=queen.size();i++){
            temp_queen.add(i);
            temp_queen.add(queen.get(i-1));
        }

        return 28-count_strikes(temp_queen,false);
    }

    private static int[] roulette(int[] fitness){
        int total=0;
        int[] result = new int[fitness.length];
        for(int i=0;i<fitness.length;i++) total+=fitness[i];
        Random random = new Random();
        for (int i = 0; i< fitness.length;i++){
            int rand = random.nextInt(total);
            int temp = 0;
            for(int j=0;j<fitness.length;j++){
                temp+= fitness[j];
                if(rand<=temp) {
                    result[i] = j;
                    break;
                }
            }
        }
        return result;
    }

    private static ArrayList<Integer>[] crossover(ArrayList<Integer>[] queen) {
        Random random = new Random();
        for (int j = 0; j < queen.length; j += 2) {
            int r1 = random.nextInt(queen[0].size()), r2 = random.nextInt(queen[0].size());
            if (r1 > r2) {
                int tmp = r2;
                r2 = r1;
                r1 = tmp;
            }
            for (int i = r1; i <= r2; i++) {
                int tmp = queen[j].get(i);
                queen[j].set(i,queen[j+1].get(i));
                queen[j+1].set(i,tmp);
            }
        }
        return queen;
    }

    private static ArrayList<Integer>[] mutation(ArrayList<Integer>[] queen){
        Random random = new Random();
            for (int i = 0; i < queen.length; i++) {
                for (int j = 0; j < queen[0].size(); j++) {
                    int rand = random.nextInt(100);
                    if (rand == 0) queen[i].set(j, random.nextInt(queen[i].size()) + 1);
                }
            }
        return queen;
    }

    private static ArrayList<Integer>[] mutation_new(ArrayList<Integer>[] queen){
        Random random = new Random();
        double mutation_rate = 0.744;
        for (int i = 0; i < queen.length; i++) {
            double mutation = random.nextDouble();
            if (mutation < mutation_rate) Collections.swap(queen[i], random.nextInt(queen[i].size()), random.nextInt(queen[i].size()));
        }
        return queen;
    }



//    private static ArrayList<Integer>[] init(int gen){
//        ArrayList<Integer>[] queen = new ArrayList[gen];
//        for(int j=0;j<gen;j++){
//            queen[j] = new ArrayList<>();
//            for (int i=1; i<9; i++)
//                queen[j].add(i);
//            Collections.shuffle(queen[j]);
//        }
//        return queen;
//    }



    private static ArrayList<Integer>[] init(int gen){
        Random random = new Random();
        ArrayList<Integer>[] queen = new ArrayList[gen];
        for(int j=0;j<gen;j++){
            queen[j] = new ArrayList<>();
            for (int i=0; i<8; i++) queen[j].add(random.nextInt(8)+1);

        }
        return queen;
    }

    private static ArrayList<Integer> complete(int gen){
        while(true) {
            ArrayList<Integer>[] queen = genome_simple(gen,init(gen));
            iterations++;
            for (int i = 0; i < gen; i++) if (Math.abs(fitness(queen[i]) - 28) == 0) return queen[i];
        }
    }

    public static ArrayList<Integer>[]  genome_simple(int gen,ArrayList<Integer>[] queen){
            ArrayList<Integer>[] new_queen = new ArrayList[gen];
            int[] fitness = new int[gen];
            for (int j = 0; j < gen; j++) fitness[j] = fitness(queen[j]);
            int[] fitnesed = roulette(fitness);
            for (int i = 0; i < gen; i++) {
                new_queen[i] = new ArrayList<>(queen[fitnesed[i]]);
            }
            for (int i = 0; i < gen; i++) queen[i].clear();
            queen = mutation(crossover(new_queen));
            return queen;
    }

    public static ArrayList<Integer>  genome_solution(int gen,ArrayList<Integer>[] queen){
        iterations++;
        while (true) {
            ArrayList<Integer>[] new_queen = new ArrayList[gen];
            int[] fitness = new int[gen];
            for (int j = 0; j < gen; j++) fitness[j] = fitness(queen[j]);
            int[] fitnesed = roulette(fitness);
            for (int i = 0; i < gen; i++) {
                new_queen[i] = new ArrayList<>(queen[fitnesed[i]]);
            }
            for (int i = 0; i < gen; i++) queen[i].clear();
            queen = mutation(crossover(new_queen));
            for (int i = 0; i < gen; i++)
                if (Math.abs(fitness(queen[i]) - 28) == 0){
                    System.out.println(iterations);
                    return queen[i];
                }

            return genome_solution(gen, queen);
        }
    }

    private static ArrayList<Integer> rand_solution(){
        ArrayList<Integer> queen = new ArrayList<>();
        boolean next;
        int count=0;

        Random random = new Random();
        for(int i=0;i<16;i+=2){
            next = false;
            queen.add(0);
            queen.add(0);
            while(!next){
                count++;
                boolean tmp = true;
                int x = random.nextInt(8) + 1,y = random.nextInt(8) + 1;
                for(int j = 0;j<queen.size();j+=2) if(queen.get(j)==x||queen.get(j+1)==y) tmp = false;
                if(tmp) {
                    queen.set(i, x);
                    queen.set(i + 1, y);
                    if (count_strikes(queen, false) == 0) next = true;
                    count=0;
                }
                if(count==64){
                    queen.clear();
                    next = true;
                    i=-2;
                    count=0;
                }
            }
        }
        return queen;
    }

    public static void main(String[] args) {
        iterations = 0;
//        ArrayList<Integer> queen = new ArrayList<>(){
//            {
//                add(5);
//                add(3);
//                add(7);
//                add(6);
//                add(6);
//                add(1);
//                add(6);
//                add(7);
//                add(3);
//                add(5);
//                add(2);
//                add(4);
//            }
//        };


        //check time
//        long one , two;
//        long start = System.nanoTime();
//        ArrayList<Integer> genome_queen = genome_solution(50);
//        long end = System.nanoTime();
//        one = end - start;
//        start = System.nanoTime();
//        ArrayList<Integer> rand_queen = rand_solution();
//        end = System.nanoTime();
//        two = end - start;
//        System.out.println("Genome time - " + one + " nanosec\nRandom time - "+ two + " nanosec");


     //   ArrayList<Integer> queen = new ArrayList<>()
     //   queen = input_queens();
//        ArrayList<Integer> rand_queen = rand_solution();
//        int[][] board = init_board(rand_queen);
//        print_board(board);
     //  count_strikes(queen,true);

//        try{
//            ArrayList<Integer> genome_queen = genome_solution(60,init(60));
//            int[][] board = init_board_genome(genome_queen);
//            print_board(board);
//        }
//        catch(StackOverflowError e){
//            System.err.println("It's more than I can handle - " + iterations);
//        }

        ArrayList<Integer> genome_queen = complete(60);
        int[][] board = init_board_genome(genome_queen);
        print_board(board);
        System.out.println("Iterations - " + iterations);
    }
}
