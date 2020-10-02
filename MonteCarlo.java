
import java.util.LinkedList;
import java.util.ListIterator;


public class MonteCarlo {
    SNode root;
    long Duration =  1;
    int ExpansionLimit = -1; 


    public MonteCarlo(int[][] board, int[] lastPlay){
         root = new SNode(board, false, lastPlay, null);
         expand(root); 
    }


    public void setDuration(long Duration){
        this.Duration = Duration; 
    }
    public void setExpansionLimit(int ExpansionLimit){
        this.ExpansionLimit = ExpansionLimit; 
    }
    
    public int[] getNextPlay(int[][] board, int[] lastPlay){


        //move the root Pointer to match lastPlay (if there is no children it expands its)
        if(root.getChildren().size() == 0) expand(root);
        for(int i = 0; i < root.getChildren().size(); i ++){
            if(root.getChildren().get(i).getLastPlay()[0] == lastPlay[0] && root.getChildren().get(i).getLastPlay()[1] == lastPlay[1]){
                root = root.getChildren().get(i); 
            }
        }

        
        long startTime = System.currentTimeMillis();
        //UCT - EXPANSION  - SIMULATION  - UPDATING DATA
        while(System.currentTimeMillis() - startTime < Duration){
            SNode currNode = runUCT(root); 
            if(currNode.getPlays() > ExpansionLimit)  expand(currNode); 
            if(currNode.getChildren().size() > 0){
                currNode = currNode.getRandomChild(); 
            }
            int rolloutValue = rollout(currNode); 
            updateData(rolloutValue, currNode); 
        }

        //Moving the root pointer again

        SNode nodeToPlay = getBestNode(root);
        System.out.println(nodeToPlay.getPlays());
        root = nodeToPlay; 


        return nodeToPlay.getLastPlay();
    }


    //Increments up the tree untill the Node is Won or is a leaf node
    public SNode getBestNode(SNode root){
        LinkedList<SNode> children = root.getChildren();
        ListIterator<SNode> iter = children.listIterator(); 
        double maxValue = (double)Integer.MIN_VALUE;
        SNode bestNode = children.get(0); 
        while(iter.hasNext()){
            SNode currNode = iter.next();
            double value = (double)currNode.getWins()  /(double) currNode.getPlays(); 
            if(value > maxValue){
                bestNode = currNode;
                maxValue = value; 
            }
        }
        return bestNode; 
    }

    public SNode runUCT(SNode currNode){
        while(currNode.getChildren().size() > 0 && !currNode.isWon()){
            currNode = getNextNodeUCT(currNode);
        }
        return currNode;
    }


    //Determines which SNode to move to out of the children of the node provided 
    public SNode getNextNodeUCT(SNode node){
        LinkedList<SNode> children = node.getChildren();
        ListIterator<SNode> iter = children.listIterator(); 
        double maxValue = (double)Integer.MIN_VALUE;
        SNode bestNode = children.get(0); 
        int TotalPlays = 0; 
        while(iter.hasNext()){
            SNode currNode = iter.next();
            TotalPlays += currNode.getPlays(); 
        }
        while(iter.hasPrevious()){
            SNode currNode = iter.previous(); 
            double currValue = getUCTValue(currNode, TotalPlays);
            if(currValue > maxValue){
                bestNode = currNode;
                maxValue = currValue; 
            }
        }
        return bestNode; 
    }

    //Gets the UCTValye of a noce given its total plays
    public double getUCTValue(SNode node, int totalPlays){
        double plays =  (double) node.getPlays();
        double wins = (double) node.getWins();
        double TotalPlays = (double) totalPlays; 
        if(plays == 0) plays = 0.1;
        if(TotalPlays == 0 ) TotalPlays = 1; 
        double winRatio = wins / plays;
        double doubt = Math.sqrt((2 * Math.log(TotalPlays))/plays); 
        return winRatio + (1.5 * doubt); 
    }

    //If State has No Children and Is Not won
    //find all next possible children intialize them and add them to its list. 
    //Whenver a Node is initalized it Children are initalized as empty
    public void expand(SNode currNode){
        if(!currNode.isWon() && currNode.getChildren().size() == 0){
            //Getting info about currNode
            boolean playerToMove = !currNode.getPlayerMovedLast();
            int[] lastPlay = currNode.getLastPlay(); 
            int previousSector = lastPlay[1]; 
            int[][] currentBoard = currNode.getCurrentBoard();

            //Initializing and adding children
            LinkedList<int[]> legalPlays = (LinkedList<int[]>) UltimateTTT.getLegalPlays(previousSector, currentBoard);
            ListIterator<int[]> iter = legalPlays.listIterator();
            while(iter.hasNext()){
                int[] nextPlay = iter.next();
                int Sector = nextPlay[0];
                int choice = nextPlay[1];
                int[][] boardCopy = UltimateTTT.copyArray(currentBoard);
                UltimateTTT.updateBoard(choice, Sector, playerToMove, boardCopy);

                //Initializing child
                SNode newChild = new SNode(boardCopy, playerToMove, nextPlay, currNode);
                currNode.addChild(newChild);
            }
        }
    }

    //rolls out random plays untill the end of the game 
    //Returns the winner at the end of the game
    public int rollout(SNode currNode){
        int[][] currBoard = UltimateTTT.copyArray( currNode.getCurrentBoard()); 
        boolean currPlayer = !currNode.getPlayerMovedLast(); 
        int[] lastPlay = currNode.getLastPlay();

        //Untill the game is not in progress get a random play and move forward
        while(UltimateTTT.getGameWinner(currBoard) == 0){
            lastPlay =  UltimateTTT.getRandomMove(UltimateTTT.getLegalPlays(lastPlay[1], currBoard)); 
            UltimateTTT.updateBoard(lastPlay[1], lastPlay[0], currPlayer, currBoard);
            currPlayer = !currPlayer;
        }
        //Return the result
        return UltimateTTT.getGameWinner(currBoard); 
    }

    public void updateData(int rolloutValue, SNode currNode){
        while(currNode.getParent() != null){
            currNode.addPlay();
            if(rolloutValue == 3 && currNode.getPlayerMovedLast()){
                currNode.addWin();
            }
            else if(rolloutValue == -3 && !currNode.getPlayerMovedLast()){
                currNode.addWin(); 
            }
            currNode = currNode.getParent(); 
        }
    }


}