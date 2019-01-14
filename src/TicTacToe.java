//Commenting is not done deliberately

import java.util.*;

public class TicTacToe {

    public static void main(String[] args) {
        
        Board b = new Board();
        Point p = new Point(0, 0);
        Random rand = new Random();
        
        b.displayBoard();

        System.out.println("Who makes first move? (1)Computer (2)User: ");
        int choice = b.scan.nextInt();
        if(choice == 1){
            p.x = rand.nextInt(5);
            p.y = rand.nextInt(5);
            b.placeAMove(p, 1);  
            b.displayBoard();
        }
        
        while (!b.isGameOver()) {
            System.out.println("Your move: line (1, 2, 3, 4, or 5) colunm (1, 2, 3, 4, or 5)");
            Point userMove = new Point(b.scan.nextInt() - 1, b.scan.nextInt() - 1);

            while (b.getState(userMove) != 0) {
                System.out.println("Invalid move. Make your move again: ");
                userMove.x = b.scan.nextInt() - 1;
                userMove.y = b.scan.nextInt() - 1;
            }

            b.placeAMove(userMove, 2);
            b.displayBoard();

            if (b.isGameOver()) {
                break;
            }

            /*Create a list of PointAndScores objects to store the points and scores of moves immediately
             *available to the AI player.*/
            List<PointsAndScores> nodesAndValues = AIMiniMax2.minimax(b);
            //Create a variable to store a Point representing the best move available to the AI player.
            Point bestMove = null;
            //Create an int variable to store the current best point's score.
            int bestScore = Integer.MIN_VALUE;
            //Print an empty line.
            System.out.println();

            //Iterate through the list of PointAndScores objects to find the point with the highest score i.e best move.
            for(PointsAndScores node : nodesAndValues)
            {
                //For Demonstration purposes, display the score of each available point.
                System.out.println(node.point + " ," + node.score);
                //If a higher score is found, update the current bestMove and bestScore.
                if(node.score > bestScore)
                {
                    bestScore = node.score;
                    bestMove = node.point;
                }
            }

            /*As the game reaches an end (full board) the equation for determining the unevaluated
             *nodes will use a negative value.
             *Therefore this would show no nodes were left unevaluated and this will be shown.*/
            if(!(AIMiniMax2.nodesUnevaluated <= 0))System.out.println("Nodes Unevaluated Due To Alpha-Beta Pruning: " + AIMiniMax2.nodesUnevaluated);
            else{System.out.println("Nodes Unevaluated Due To Alpha-Beta Pruning: 0");}

            //Prints out the move that the AI player made. This is for demonstration purposes only.
            System.out.println("Computer Chose The Point: " + bestMove);

            //Place the move that the AI player chose and then display the new board state.
            b.placeAMove(bestMove, 1);
            b.displayBoard();
        }

        if (b.hasXWon()) {
            System.out.println("Unfortunately, you lost!");
        } else if (b.hasOWon()) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a draw!");
        }
    }
    
}