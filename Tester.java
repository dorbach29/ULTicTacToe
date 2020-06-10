import java.util.Scanner;
import java.util.Map;
import java.util.Hashtable; 


public class Tester{



    /* 
    Every "mini" tic tac toe game is called a Sector and represented by nums 0 - 8
    When we get a Row/Col combo from user we format it as one choice (r * 3) + c;
    This should also give 

    A Sector can be used as the starting point for the 3x3 "Sub arr" in our larger 9x9 arr; 
    (Sec / 3) * 3 Gives us the Verticle starting index for our 3x3 "Sub arr";
    (S % 3) * 3 gives us the horizontal index. 

    */
    public static void main(String[] args) {



        //0 - Ongoing, 9 P1Won,  1- tie;
        int GameStatus = 0;
        int Sector = -1;
        boolean TwoPlayerGame = true; 
        boolean IsPlayer1Turn = true;

        int[][] board = new int[9][9];
        int[][] FinalBoard = new int[3][3]; 
        
        
        
        
        

        /*
        MAIN GAME LOOP  Structure 
                display Board
                get player choice / computer Choice

                Update FinalBoard
                    Only Updates FinalBoard if the Sector is not already won. 
                    Check each Sector in the board 

                

                CheckGame
                    Checks FinalBoard

        */

        //GAME LOOP
        while(GameStatus == 0){
            display(board);
            boolean ValidMove = false; 

            while((IsPlayer1Turn || TwoPlayerGame) && !ValidMove){

                while(Sector < 0 || Sector > 8 || getWinner(0, getSectorArray(Sector, board)) != 0){
                    Scanner dog = new Scanner(System.in);
                    System.out.println("Please Enter What Sector you would want to play in \n(0, 1, 2)\n(3, 4, 5)\n(6, 7, 8)");
                    Sector = dog.nextInt(); 
                } 

                int choice = getPlayerChoice(Sector); 
                if(isEmpty(choice, Sector, board)){
                    int row = choice / 3;
                    int col = choice % 3; 
                    updateBoard(row, col, Sector, IsPlayer1Turn, board);
                    Sector = choice; //This works because we already format choice as one of 9 numbers
                    ValidMove = true; 
                }
                else{
                    System.out.println("Invalid submit another choice");
                    ValidMove = false; 
                }
            }
            if(!IsPlayer1Turn && !TwoPlayerGame){
                //Get Computer Move
            }

            IsPlayer1Turn = !IsPlayer1Turn ; 
            updateFinalBoard(FinalBoard, board);
            System.out.println("\n \n  "); 
            System.out.println("MAIN TICTACTOE BOARD:"); 
            display(FinalBoard); 
            System.out.println("\n \n "); 
            GameStatus = getWinner(GameStatus, FinalBoard);
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
        


    }



    //Takes a 3x3 TicTacToe array with 3 representing X and -3 representing O
    //Winning moves are  3 or -3 because these may be put into finalboard which uses this method
    // 3 p1 won|-3 p2 won| 0 ongoing| 1 tie |
    public static int getWinner(int GameStatus, int[][] FinalBoard ){
        boolean isFull = true; 
        for(int i = 0; i < 3 ; i ++){
            int currentRowVal = 0;
            int currentColVal= 0;
            int diag1Val= 0;
            int diag2Val= 0;
            for(int j = 0; j < 3; j ++){
                currentRowVal += FinalBoard[i][j]; 
                currentColVal += FinalBoard[j][i];
                if(i ==0){
                    diag1Val += FinalBoard[j][j];
                    diag2Val += FinalBoard[j][2 -j];
                }
                if(FinalBoard[i][j] == 0)  isFull = false; 
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
    //Used to update the FinalBoard
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


    //Updates the FinalBoard array that is inputed at every sector
    //FinalBoard is the main tic tac toe board on which the game depends on
    //USES: getSectorArray and getWinner 
    //the winner of each SectorArray gets placed on the final board

    public static void updateFinalBoard(int[][] FinalBoard, int[][] board){
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                if(FinalBoard[i][j] == 0 ){
                    int Sector = (i * 3) + j; 
                    FinalBoard[i][j] =  getWinner(0, getSectorArray(Sector, board));
                }
            }
        }
    }


    //Fills in gameboard at correct position
    //Requires row, collum ,sector and turn as well as the board object to be updated
    public static void updateBoard(int row, int col, int Sector, boolean IsPlayer1Turn , int[][] board){
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
                else if(board[i][j] == 3) symbol = "o";
                else symbol = "x"; 
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