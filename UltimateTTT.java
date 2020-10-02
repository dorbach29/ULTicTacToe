import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;


public class UltimateTTT{




    public static void main(String[] args) {

        

        int GameStatus = 0;
        boolean TwoPlayerGame = true; 
        boolean IsPlayer1Turn = true;
        int Sector = -1; 
        int[][] board = new int[9][9];
        int choice = -1;
        
        Scanner dog = new Scanner(System.in);
        MonteCarlo AI = new MonteCarlo(board, new int[]{choice, choice});
        System.out.println("Two Player Game or One Player Game (Enter 2 or 1)");
        int tpg = dog.nextInt();

        if(tpg != 2){
            TwoPlayerGame = false; 
            System.out.println("Choose difficulty: Easy, Medium, Hard, MiniMax-Beating ");
            dog = new Scanner(System.in); 
            String difficulty = dog.nextLine();
            if(difficulty.substring(0, 1).equals("E")){
                AI.setDuration((1000) / 50);
                AI.setExpansionLimit(-1);
            }
            if(difficulty.substring(0, 2).equals("Me")){
                AI.setDuration((1000) / 15);
                AI.setExpansionLimit(-1);                
            }
            if(difficulty.substring(0, 1).equals("H")){
                AI.setDuration((1000));
                AI.setExpansionLimit(1);                
            }
            if(difficulty.substring(0, 2).equals("Mi")){
                AI.setDuration((1000) * 30);
                AI.setExpansionLimit(300);                
            }
        }

         



        //GAME LOOP
        while(GameStatus == 0){
            //Displayes the boards
            display(board);


            //Get Player Choice
            //Sector and Choice should now reflect where the player went
            if((IsPlayer1Turn || TwoPlayerGame)){
                //The Playes Last Choice becomes the Sector we have to play in 
                Sector = choice; 

                //If Sector is invalid get new Sector
                while(Sector < 0 || Sector > 8 || getWinner(getSectorArray(Sector, board)) != 0){
                        System.out.println("Please Enter What Sector you would want to play in \n(0, 1, 2)\n(3, 4, 5)\n(6, 7, 8)");
                    Sector = dog.nextInt(); 
                } 
               
                //Get the choice and check if it worked
                int tmpChoice = getPlayerChoice(Sector); 
                while(tmpChoice < 0 || tmpChoice > 8 || !isEmpty(tmpChoice, Sector, board)){
                    tmpChoice = getPlayerChoice(Sector);  
                }
                choice = tmpChoice;
                
            }



            //Get Computer Choice if needed
            //Sets the new root to match where the player went
            //Sets Sector and Choice to match where the AI Went
            if(!(TwoPlayerGame || IsPlayer1Turn)){
                int[] lastPlay;
                lastPlay = AI.getNextPlay(board, new int[]{Sector, choice});
                Sector = lastPlay[0];
                choice = lastPlay[1]; 
                System.out.println("Computer Choice: \n" + Sector + ", " + choice);
            }



            updateBoard(choice, Sector, IsPlayer1Turn, board);

            //Switches Turn
            IsPlayer1Turn = !IsPlayer1Turn ; 


            //Displays Score Board and checks for a winner
                System.out.println("\n \n  "); 
                System.out.println("MAIN TICTACTOE BOARD:"); 
            display(getScoreBoard(board)); 
                System.out.println("\n \n "); 
            GameStatus = getGameWinner(board);
        }


        //Prints Winner
        if(GameStatus == 1){
            System.out.println("It was a tie \n");
        }
        else if(GameStatus == 3){
            System.out.print("Player 1 Victory \n");
        }
        else{
            System.out.println("Player 2 Victory \n"); 
        }
        

        dog.close();
    }

    //Returns wether the game is won (For info check get winner)
    public static int getGameWinner(int[][] board){
        return getWinner(getScoreBoard(board));
    }


    //Takes a 3x3 TicTacToe array with 3 representing X and -3 representing O
    //Returns 3 or -3 for winners, for this method might be used on itself
    // 3 p1 won|-3 p2 won| 0 ongoing| 1 tie |
    public static int getWinner(int[][] arr){
        boolean isFull = true; 
        for(int i = 0; i < 3 ; i ++){
            int currentRowVal = 0;
            int currentColVal= 0;
            int diag1Val= 0;
            int diag2Val= 0;
            for(int j = 0; j < 3; j ++){
                currentRowVal += arr[i][j]; 
                currentColVal += arr[j][i];
                if(i ==0){
                    diag1Val += arr[j][j];
                    diag2Val += arr[j][2 -j];
                }
                if(arr[i][j] == 0)  isFull = false; 
            }
            if(i == 0){
                if(diag1Val * diag1Val == 81) return diag1Val / 3; 
                if(diag2Val * diag2Val == 81) return diag2Val / 3; 
            }

            if(currentRowVal * currentRowVal == 81)return currentRowVal / 3; 
            if(currentColVal * currentColVal == 81)return currentColVal / 3; 
        }

        if(isFull) return 1; 
        return 0; 
    }


    //Gets the 3x3 array for the given sector
    public static int[][] getSectorArray(int Sector, int[][] board){
        int rowStart = (Sector / 3) * 3;
        int colStart = (Sector % 3) * 3;
        int[][] SectorArray = new int[3][3];
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                SectorArray[i][j] = board[rowStart + i][colStart + j];
            }
        }
        return SectorArray; 
    }


    //Returns the main ScoreBoard
    public static int[][] getScoreBoard(int[][] board){
        int[][] ScoreBoard = new int[3][3]; 
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                if(ScoreBoard[i][j] == 0 ){
                    int Sector = (i * 3) + j; 
                    ScoreBoard[i][j] =  getWinner(getSectorArray(Sector, board));
                }
            }
        }
        return ScoreBoard;
    }


    //Fills in gameboard at correct position
    //Requires row, collum ,sector and turn as well as the board object to be updated
    public static void updateBoard(int choice, int Sector, boolean IsPlayer1Turn , int[][] board){
        int row = choice / 3;
        int col = choice % 3; 
        int Symbol;
        if(IsPlayer1Turn){
            Symbol = 3; 
        }
        else{
             Symbol = -3;
        }
        int rowInSector = ((Sector / 3) * 3) + row;
        int colInSector = ((Sector % 3) * 3) + col;
        board[rowInSector][colInSector] = Symbol;

    }


    //Returns if the board is empty at given choice and sector
    public static boolean isEmpty(int choice, int Sector , int[][] board){
        int row = ((Sector / 3) * 3) + (choice / 3);
        int col =  ((Sector % 3) * 3) + (choice % 3); 
        return (board[row][col] == 0); 
    }



    public static void showPlays(List<int[]> legalPlays){
        ListIterator<int[]> iter = legalPlays.listIterator();
        while(iter.hasNext()){
            int[] arr = iter.next();
            System.out.print(" [" + arr[0] + " , " + arr[1] + "] "); 
        }

    }
    
    
    public static List<int[]> getLegalPlays(int prevSector, int[][] board){
        LinkedList<int[]> legalPlays = new LinkedList<int[]>(); 
        if(prevSector < 0 || prevSector > 8 || getWinner(getSectorArray(prevSector, board)) != 0){
            for(int Sector = 0; Sector < 9; Sector ++){
                if(getWinner(getSectorArray(Sector, board)) == 0){
                    for(int choice = 0; choice < 9; choice ++){
                        if(isEmpty(choice, Sector, board)){
                            legalPlays.add(new int[]{Sector, choice}); 
                        }
                    }
                }

            }
        }

        else{
            for(int choice = 0; choice < 9; choice ++){
                if(isEmpty(choice, prevSector, board)){
                    legalPlays.add(new int[]{prevSector, choice}); 
                }
            }
        }

        return legalPlays; 
    }


    public static int[] getRandomMove(List<int[]> legalPlays){
        return legalPlays.get((int)(Math.random() * legalPlays.size())); 
    }

    public static int[][] copyArray(int[][] toCopy){
        int[][] arr = new int[toCopy.length][];
        for(int i = 0; i < toCopy.length; i ++){
            int[] row = new int[toCopy[i].length];
            for(int j = 0; j < toCopy[i].length; j ++){
                row[j] = toCopy[i][j]; 
            }
            arr[i] = row; 
        }
        return arr; 
    }










    //FOR CONSOLE GAMEPLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY 
    //FOR CONSOLE GAMEPLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY 
    //FOR CONSOLE GAMEPLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY 
    //FOR CONSOLE GAMEPLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY    FOR CONSOLE GAME PLAY 


    //Display Board
    public static void display(int[][] board){
        for(int i = 0; i < board.length; i ++){
            for(int j =0; j < board[i].length; j ++){
                String symbol; 
                if(board[i][j] == 0) symbol = " ";
                else if(board[i][j] == 3) symbol = "x";
                else symbol = "o"; 
                System.out.print(" " + symbol + " ");
                if((j == 2 || j == 5 || j == 8) && board.length == 9) System.out.print("|"); 
            }
            System.out.println("");
            if((i == 2 || i == 5 || i == 8) && board.length == 9)System.out.println("----------------------------");
        }
    }        

    //Gets the number represeneting the choices index (number from 0 - 8)
    public static int getPlayerChoice(int Sector){
        String Section; 
            switch(Sector){
                case 0:
                    Section = "Top Left"; 
                break;
                case 1 :
                    Section = "Top Mid"; 
                break;
                case 2 :
                    Section = "Top Right";
                break;
                case 3 :
                    Section = "Mid Left";
                break;
                case 4 :
                    Section = "Middle";
                break;
                case 5 :
                    Section = "Mid Right";
                break;
                case 6 :
                    Section = "Bot Left";
                break;
                case 7 :
                    Section = "Bot Mid";
                break;
                case 8 :
                    Section = "Bot Right";
                break;
                default:
                    Section = "Middle"; 

            }

            Scanner dog = new Scanner(System.in);
            System.out.println("What Row? (1, 2, 3) " +  Section);
            int row = dog.nextInt() - 1;
            System.out.println("What Collum? (1, 2, 3)");
            int col = dog.nextInt() - 1;

            int choice = (row * 3) + col; 
            return choice; 
    }



 
}