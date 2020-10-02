
import java.util.LinkedList;

public class SNode {
    //The Board at this Node
    private int[][] currBoard;
    //The last player who moved in to here TRUE if Player moved here, FALSE if computer moved here
    private boolean lastPlayer;
    //Sector, Choice that lead to this poition
    private int[] lastPlay;


    //Number of times this State has been visited
    private int playCount;
    //Number of wins on this node
    private int winCount; 
    


    //All next posible States
    private LinkedList<SNode> children; 
    //Parent Node
    private SNode parent; 



    //Initializes an SNode
    public SNode(int[][] currBoard, boolean lastPlayer, int[] lastPlay, SNode parent){
            this.currBoard = UltimateTTT.copyArray(currBoard);
            this.lastPlayer = lastPlayer;
            this.lastPlay = lastPlay;
            this.parent = parent; 
            playCount = 0;
            winCount = 0;
            children = new LinkedList<SNode>(); 
    }


    //ACCESSOR METHODS
    public LinkedList<SNode> getChildren(){
        return children;
    }

    public int getPlays(){
        return playCount; 
    }

    public int getWins(){
        return winCount; 
    }

    public boolean isWon(){
       return !(UltimateTTT.getGameWinner(currBoard) == 0);
    }

    public boolean getPlayerMovedLast(){
        return lastPlayer; 
    }

    public int[] getLastPlay(){
        return lastPlay; 
    }

    public int[][] getCurrentBoard(){
        return currBoard; 
    }

    public SNode getParent(){
        return parent; 
    }

    public SNode getRandomChild(){
        return children.get((int) (Math.random() * children.size()));
    }

    //Setters

    public void addChild(SNode child){
        children.add(child); 
    }

    public void addPlay(){
        playCount ++;
    }

    public void addWin(){
        winCount ++; 
    }



}