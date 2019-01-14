import java.util.ArrayList;
import java.util.List;

//Contains static methods to implement a minimax algorithm for the 5x5 noughts and crosses.
public class AIMiniMax2
{
    //A static int variable to store the number of unevaluated nodes.
    static int nodesUnevaluated = 0;

    /*This method iterates through the nodes immediately available to the AI player and will retrieve the score
     *for each state and returns a PointsAndScores list of all nodes and their respective values.*/
    public static List<PointsAndScores> minimax(Board board)
    {
        //Reset the unevaluated nodes variable to 0.
        nodesUnevaluated = 0;

        //Create an array list of PointsAndScores objects.
        List<PointsAndScores> nodesAndValues = new ArrayList<>();

        //Retrieve a List of all points available to the AI player from this game state.
        List<Point> availablePoints = board.getAvailablePoints();

        //Set the current maximum value found at this depth. Used to perform alpha-beta pruning.
        int currMax = Integer.MIN_VALUE;

        //Declare a PointAndScore
        PointsAndScores nodeWithValue;

        /*For each available node (point), retrieve the nodes minimum score, create a node-score pair (PointsAndScores object)
         *and add the PointsAndScores object to the nodesAndValues List.*/
        for(Point point : availablePoints)
        {
            /*This block will construct the new board state by placing the current move, pass the new board state to
             *the min method, add the node-score pair to the list and then reverse the changes made to the board state.*/
            board.placeAMove(point, 1);
            /*The min method takes the current maximum value.
             *If during the method execution the current min score drops below the current max in this depth.
             *Then we can conclude that node will not be picked, so no further evaluation is required.*/
            int nodeScore = min(board, currMax);
            //If the current nodes score is higher than the current maximum, then make the current node the new highest.
            if(nodeScore > currMax){currMax = nodeScore;}
            //Reassign the value of the current node variable to represent the current point-score pair.
            nodeWithValue = new PointsAndScores(nodeScore, point);
            //Add this variable to the list and reverse the move.
            nodesAndValues.add(nodeWithValue);
            board.board[point.x][point.y] = 0;
        }

        //Return the PointsAndScores list.
        return nodesAndValues;
    }

    //Method for calculating the minimum value of a given board state. This is for when the AI Players turn is being
    //evaluated.
    private static int min(Board board, int currMaxScore)
    {
        //Will return a positive score of 1, if the board state represents a win for the AI Player.
        if(board.hasXWon()){return Integer.MAX_VALUE;}
        //Will return a negative score of -1, if the board state represents a win for the Human Player.
        if(board.hasOWon()){return Integer.MIN_VALUE;}

        //Retrieve a List of all points available to the Human player from this game state.
        List<Point> availablePoints = board.getAvailablePoints();
        //Will return 0 if the board is full and neither the AI or Human player has won.
        if(availablePoints.isEmpty()){return 0;}

        /*A score variable to store the score of the least valuable node (point) found in the list of points.
         *Therefore, it is set to the largest Integer value to guarantee it will be changed.*/
        int minScore = Integer.MAX_VALUE;

        //Create an int variable to store the maximum number of nodes to be evaluated at this depth.
        int nodesAvailable = availablePoints.size();
        //Create an int variable to store the number of nodes currently expanded at this depth.
        int nodesExpanded = 0;

        /*For each available node (point), retrieve the nodes maximum value and reassign the minScore if the nodeScore
         *is a new minimum score. This represents the minimisation stage which limits how well the Human can play.*/
        for(Point point : availablePoints)
        {
            /*This block will construct the new board state by placing the current move, pass the new board state to
             *the max method, reassign the minScore if necessary and then revert the changes made to the board state. */
            board.placeAMove(point, 2);
            /*The min method takes the current minimum value.
             *If during the method execution the current max score exceeds the current min in this depth.
             *Then we can conclude that node will not be picked, so no further evaluation is required.*/
            int nodeScore = max(board, minScore);
            //The nodesExpanded variable is incremented by 1.
            nodesExpanded++;
            /*Updates the minScore if necessary, reverts the move played and then will determine if further evaluation
             *is required. If the current minScore is less than the current maximum in the above depth, then we can
             *conclude that further evaluation is unnecessary and the score for this node can only get lower.*/
            if(nodeScore < minScore){minScore = nodeScore;}
            board.board[point.x][point.y] = 0;
            if(minScore < currMaxScore){break;}
        }

        //Create an int variable to hold the number of unexpanded nodes at this level.
        int nodesUnexpanded = nodesAvailable - nodesExpanded;
        /*Each node at this depth is equivalent to the number of nodes available at the depth below.
         *Therefore, we can multiple the number of unexpanded nodes by the number of nodes at the depth below to
         *calculate the number of unevaluated nodes.*/
        int nodesUnevaluated = nodesUnexpanded * nodesAvailable-1;

        //This result is then added to the static int variable that holds the number of unevaluated nodes overall.
        AIMiniMax2.nodesUnevaluated += nodesUnevaluated;

        //Return the minimum node score found from this board state.
        return minScore;
    }

    //Method for calculating the maximum value of a given board state. This is for when the Human Players turn is being
    //evaluated.
    private static int max(Board board, int currMinScore)
    {
        //Will return a positive score of Integer.MAX_VALUE, if the board state represents a win for the AI Player.
        if(board.hasXWon()){return Integer.MAX_VALUE;}
        //Will return a negative score of Integer.MIN_VALUE, if the board state represents a win for the Human Player.
        if(board.hasOWon()){return Integer.MIN_VALUE;}

        //Retrieve a List of all points available to the Human player from this game state.
        List<Point> availablePoints = board.getAvailablePoints();
        //Will return 0 if the board is full and neither the AI or Human player has won.
        if(availablePoints.isEmpty()){return 0;}

        /*A score variable to store the score of the most valuable node (point) found in the list of points.
         *Therefore, it is set to the minimum Integer value to guarantee it will be changed.*/
        int maxScore = Integer.MIN_VALUE;

        //Create an int variable to store the maximum number of nodes to be evaluated at this depth.
        int nodesAvailable = availablePoints.size();
        //Create an int variable to store the number of nodes currently evaluated at this depth.
        int nodesEvaluated = 0;

        /*For each available node (point), retrieve the nodes maximum value, reassign the minScore if the nodeScore
         *is a new minimum score. This represents the maximisation stage which maximises how the AI can play.*/
        for(Point point : availablePoints)
        {
            /*This block will construct the new board state by placing the current move, pass the new board state to
             *the min method, reassign the maxScore if necessary and then revert the changes made to the board state.*/
            board.placeAMove(point, 1);
            //Calls the eval method to evaluate the current game state.
            int nodeScore = eval(board.board);
            //Increment the nodesEvaluated variable by 1, revert
            nodesEvaluated++;
            /*Updates the maxScore if necessary, reverts the move played and then will determine if further evaluation
             *is required. If the current maxScore is more than the current minimum in the above depth, then we can
             *conclude that further evaluation is unnecessary and the score for this node can only get higher.*/
            if(nodeScore > maxScore){maxScore = nodeScore;}
            board.board[point.x][point.y] = 0;
            if(nodeScore > currMinScore){break;}
        }

        //Create an int variable to hold the number of unevaluated nodes at this level.
        int nodesUnevaluated = nodesAvailable - nodesEvaluated;

        /*The nodes unevaluated at this depth are of a 1:1 ratio (1 unexpanded node = 1 unevaluated node).
         *This is then added to the static int variable that holds the number of unevaluated nodes overall.*/
        AIMiniMax2.nodesUnevaluated += nodesUnevaluated;

        //Return the maximum node score found from this board state.
        return maxScore;
    }

    /*Method for evaluating a given game state.
     *Heuristic Used:
     * - if a row/column/diagonal is uncontested:
     *      + 1 x in that row/column/diagonal is worth 1 to the AI
     *      + 2 x's in that row/column/diagonal is worth 10 to the AI
     *      + 3 x's in that row/column/diagonal is worth 100 to the AI
     *      + 4 x's in that row/column/diagonal is worth 1000 to the AI
     *      + 5 x's in that row/column/diagonal is worth Integer.MAX_VALUE to the AI
     *      + 1 o in that row/column/diagonal is worth -1 to the AI
     *      + 2 o's in that row/column/diagonal is worth -10 to the AI
     *      + 3 o's in that row/column/diagonal is worth -100 to the AI
     *      + 4 o's in that row/column/diagonal is worth -1000 to the AI
     *      + 5 o's in that row/column/diagonal is worth Integer.MIN_VALUE to the AI
     */
    private static int eval(int[][] board)
    {
        //Declare the row and column count variables for both Human and AI points.
        int xRowCount;
        int oRowCount;
        int xColumnCount;
        int oColumnCount;

        //Instantiate the diagonal count variables to 0 for both Human and AI points.
        int xFirstDiagCount = 0;
        int oFirstDiagCount = 0;
        int xSecondDiagCount = 0;
        int oSecondDiagCount = 0;

        //Instantiate the nodeScore variable to store the score for this node.
        int nodeScore = 0;

        //Iterate from 0 to 4.
        for(int i = 0; i < 5; i++)
        {
            //Reset the row and column counts as a new row and column is being evaluated on each iteration of i.
            xRowCount = 0;
            oRowCount = 0;
            xColumnCount = 0;
            oColumnCount = 0;

            //Iterate from 0 to 4.
            for(int j = 0; j < 5; j++)
            {
                //Checks a row using i and j e.g. {(i=0, j=0), ..., (i=0, j=4)}
                if(board[i][j] == 1){xRowCount++;}
                else if(board[i][j] == 2){oRowCount++;}
                //Checks a column using j and i e.g. {(j=0, i=0), ..., (j=4, i=0)}
                if(board[j][i] == 1){xColumnCount++;}
                else if(board[j][i] == 2){oColumnCount++;}
                //When i and j are equal, they represent a point in the diagonal line from (0, 0) to (4, 4)
                if(i == j)
                {
                    if(board[i][j] == 1){xFirstDiagCount++;}
                    else if(board[i][j] == 2){oFirstDiagCount++;}
                }
                //When i and j sum to 4, they represent a point in the diagonal line from (0, 4) to (4, 0)
                if(i + j == 4)
                {
                    if(board[i][j] == 1){xSecondDiagCount++;}
                    else if(board[i][j] == 2){oSecondDiagCount++;}
                }
            }

            //If xRowCount is more than 0 and oRowCount is 0, then there are x's in an uncontested row.
            if(xRowCount > 0 && oRowCount == 0)
            {
                //Determine score based on number of uncontested x's in this row.
                if(xRowCount == 1){nodeScore += 1;}
                else if(xRowCount == 2){nodeScore += 10;}
                else if(xRowCount == 3){nodeScore += 100;}
                else if(xRowCount == 4){nodeScore += 1000;}
                else{return Integer.MAX_VALUE;}
            }
            //If oRowCount is more than 0 and xRowCount is 0, then there are o's in an uncontested row.
            else if(oRowCount > 0 && xRowCount == 0)
            {
                //Determine score based on number of uncontested o's in this row.
                if(oRowCount == 1){nodeScore -= 1;}
                else if(oRowCount == 2){nodeScore -= 10;}
                else if(oRowCount == 3){nodeScore -= 100;}
                else if(oRowCount == 4){nodeScore -= 1000;}
                else{return Integer.MIN_VALUE;}
            }

            //If xColumnCount is more than 0 and oColumnCount is 0, then there are x's in an uncontested column.
            if(xColumnCount > 0 && oColumnCount == 0)
            {
                //Determine score based on number of uncontested x's in this column.
                if(xColumnCount == 1){nodeScore += 1;}
                else if(xColumnCount == 2){nodeScore += 10;}
                else if(xColumnCount == 3){nodeScore += 100;}
                else if(xColumnCount == 4){nodeScore += 1000;}
                else{return Integer.MAX_VALUE;}
            }
            //If oColumnCount is more than 0 and xColumnCount is 0, then there are o's in an uncontested column.
            else if(oColumnCount > 0 && xColumnCount == 0)
            {
                //Determine score based on number of uncontested o's in this column.
                if(oColumnCount == 1){nodeScore -= 1;}
                else if(oColumnCount == 2){nodeScore -= 10;}
                else if(oColumnCount == 3){nodeScore -= 100;}
                else if(oColumnCount == 4){nodeScore -= 1000;}
                else{return Integer.MIN_VALUE;}
            }
        }

        //If xFirstDiagCount is more than 0 and oFirstDiagCount is 0, then there are x's in an uncontested diagonal.
        if(xFirstDiagCount > 0 && oFirstDiagCount == 0)
        {
            //Determine score based on number of uncontested x's in this diagonal.
            if(xFirstDiagCount == 1){nodeScore += 1;}
            else if(xFirstDiagCount == 2){nodeScore += 10;}
            else if(xFirstDiagCount == 3){nodeScore += 100;}
            else if(xFirstDiagCount == 4){nodeScore += 1000;}
            else{return Integer.MAX_VALUE;}
        }
        //If oFirstDiagCount is more than 0 and xFirstDiagCount is 0, then there are o's in an uncontested diagonal.
        else if(oFirstDiagCount > 0 && xFirstDiagCount == 0)
        {
            //Determine score based on number of uncontested o's in this diagonal.
            if(oFirstDiagCount == 1){nodeScore -= 1;}
            else if(oFirstDiagCount == 2){nodeScore -= 10;}
            else if(oFirstDiagCount == 3){nodeScore -= 100;}
            else if(oFirstDiagCount == 4){nodeScore -= 1000;}
            else{return Integer.MIN_VALUE;}
        }

        //If xSecondDiagCount is more than 0 and oSecondDiagCount is 0, then there are x's in an uncontested diagonal.
        if(xSecondDiagCount > 0 && oSecondDiagCount == 0)
        {
            //Determine score based on number of uncontested x's in this diagonal.
            if(xSecondDiagCount == 1){nodeScore += 1;}
            else if(xSecondDiagCount == 2){nodeScore += 10;}
            else if(xSecondDiagCount == 3){nodeScore += 100;}
            else if(xSecondDiagCount == 4){nodeScore += 1000;}
            else{return Integer.MAX_VALUE;}
        }
        //If oSecondDiagCount is more than 0 and xSecondDiagCount is 0, then there are o's in an uncontested diagonal.
        else if(oSecondDiagCount > 0 && xSecondDiagCount == 0)
        {
            //Determine score based on number of uncontested o's in this diagonal.
            if(oSecondDiagCount == 1){nodeScore -= 1;}
            else if(oSecondDiagCount == 2){nodeScore -= 10;}
            else if(oSecondDiagCount == 3){nodeScore -= 100;}
            else if(oSecondDiagCount == 4){nodeScore -= 1000;}
            else{return Integer.MIN_VALUE;}
        }

        //Return the final node score.
        return nodeScore;
    }
}
