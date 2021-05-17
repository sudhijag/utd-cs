#include "header.h"

/*
 Name: printrules
 Parameters: None
 Return: None
 Description: Displays the rules of CSCE 1030 Stratego
 */

void printrules(){
    cout << "W e l c o m e   t o   CSCE 1 0 3 0   S t r a t e g o" << endl;
    cout << "------------------------------------------------------------------------" << endl;
    cout << "This program will set up a 5x5 game board for a 1030 version of the game" << endl;
    cout << "of Stratego. One player will compete against the computer, each assigned" << endl;
    cout << "10 total pieces consisting of the following:" << endl;
    cout << "1 FLAG (F)" << endl;
    cout << "3 BOMB (B) " << endl;
    cout << "1 MARSHAL (1) or GENERAL (2) " << endl;
    cout << "3 COLONEL (3), MAJOR (4), CAPTAIN (5), LIEUTENANT (6), or SERGEANT (7) " << endl;
    cout << "1 MINER (8) " << endl;
    cout << "1 SPY (S) " << endl;
    cout << "GENERAL RULES: " << endl;
    cout << "-------------------------------------------------------------------------- " << endl;
    cout << " For the most part, the game will follow the standard Stratego rules, although" << endl;
    cout << "For the most part, the game will follow the standard Stratego rules, although" <<endl;
    cout << "there are some exceptions." <<endl;
    cout << "1. Both players (BLUE and RED) will have all of their 10 game pieces as-" <<endl;
    cout << " signed randomly with the only requirement being that the FLAG must be" <<endl;
    cout << "placed in the back row. RED will start the game first." <<endl;
    cout << "2. Higher ranked pieces can capture lower ranked pieces in the following" <<endl;
    cout << "order: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> S, meaning that 1 (the" <<endl;
    cout << " MARSHAL) can remove 2 (the GENERAL) and so forth. The MINER (8) piece" <<endl;
    cout << " may strike a BOMB and remove it to occupy the now unoccupied space. A" <<endl;
    cout << " SPY (S), although the lowest ranked piece, may remove the MARSHAL (1)" <<endl;
    cout << "or the GENERAL (2)." <<endl;
    cout << "When pieces have equal rank, both pieces are removed" << endl;
    cout << "3. The FLAG and BOMBs are not moveable while all of the other pieces may" <<endl;
    cout << " move one square at a time forward, backward, or sideward, but not di-" <<endl;
    cout << "agonally to an open square." << endl;
    cout << "4. A player must either move or strike on his/her turn." << endl;
    cout << "5. The game ends when a player strikes his/her opponent's flag." << endl;
    cout << "------------------------------------------------------------------------"<<endl << endl;
}
/*
 Name: generate
 Parameters: An board to contain the pieces, an board to contain the colors, and the boardsize
 Return: None
 Description: Sets all values on the board to be empty
 */
void generate(int length, GameBoard ** board){
    for(int i=0; i < length; i++){
        for(int j=0; j < length; j++){
            board[i][j].piece = NONE;
            board[i][j].color = BLACK;
        }
    }
    cout << "Empty board initialization complete." << endl;
}

/*
 Name: printboard
 Parameters: The length of the board, an board of type Pieces, an board of type Colors
 Return: void
 Description: Displays the game board to the userny
 */

/*void printboard(int length, GameBoard ** board, bool hide){
    char letters[5] = {'A', 'B', 'C', 'D', 'E'};
    int letterno = 0;
    cout << "Printing board..." << endl;
    cout << "   1 2 3 4 5 " << endl;
    cout << " +-----------+" << endl;
    
    for(int i = 0 ; i < length; i++){
        cout << letters[letterno];//Prints A-E
        cout << "| ";//Left wall
        for(int j = 0 ; j < length; j++){
            if(board[i][j].piece == NONE){
                //cout << " ";
            }
            
            if(hide){
                if(board[i][j].color==RED){
                    cout << static_cast<char>(88);
                }
                else{
                    cout << static_cast<char>(board[i][j].piece);
                }
            }
            else{
                cout << static_cast<char>(board[i][j].piece);
            }
            
            
            cout << " ";//Spaces between each piece for readability
        }
        cout << "|";
        cout << endl;
        letterno++;//Moves on to the next letter for the row marker
    }
    cout << " +-----------+" << endl;
    move(length, board);
}*/


void printboard(int length, GameBoard ** board, bool hide){
    char letters[5] = {'A', 'B', 'C', 'D', 'E'};
    int letterno = 0;
    cout << "Printing board..." << endl;
    cout << "   1 2 3 4 5 " << endl;
    cout << " +-----------+" << endl;
    
    for(int i = 0 ; i < length; i++){
        cout << letters[letterno];//Prints A-E
        cout << "| ";//Left wall
        for(int j = 0 ; j < length; j++){
            if(board[i][j].piece == NONE){
                //cout << " ";
            }
            
            if(hide){
                if(board[i][j].color==RED){
                    cout << static_cast<char>(88);
                }
                else{
                    cout << static_cast<char>(board[i][j].piece);
                }
            }
            else{
                cout << static_cast<char>(board[i][j].piece);
            }
            
            
            cout << " ";//Spaces between each piece for readability
        }
        cout << "|";
        cout << endl;
        letterno++;//Moves on to the next letter for the row marker
    }
    cout << " +-----------+" << endl;
}

/*Name: assign
 Parameters: The input board
 Return: none
 Description: Assigns pieces to each player*/
void assign(int length, GameBoard ** board){
    cout << "Proceeding to randomize..." << endl;
    srand(time(NULL));//seeds the random number generator
    int pos_x, pos_y;//The random numbers that are regenerated over and over the course of this function
    
    //FLAGS(F)
    pos_x = rand() % 2;
    pos_y = rand() % 5;
    
    board[0][pos_y].piece = FLAG;//Red Flag
    board[0][pos_y].color = RED;
    
    board[4][pos_y].piece = FLAG;//Blue Flag
    board[4][pos_y].color = BLUE;
    
    //BOMBS(B)
    for(int i = 1; i <= 3; i++){
        do{
            pos_x = rand() % 2;
            pos_y = rand() % 5;
        }while(board[pos_x][pos_y].piece != NONE);
        
        board[pos_x][pos_y].piece = BOMB;//Red Bomb
        board[pos_x][pos_y].color = RED;
    }
    
    for(int i = 1; i <= 3; i++){
        do{
            pos_x = rand() % 2 + 3;
            pos_y = rand() % 5;
        }while(board[pos_x][pos_y].piece != NONE);
        board[pos_x][pos_y].piece = BOMB;//Blue Bomb
        board[pos_x][pos_y].color = BLUE;
    }
    
    //GENERALS & MARSHALLS(1,2)
    do{
        pos_x = rand() % 2;
        pos_y= rand() % 5;
    }while(board[pos_x][pos_y].piece != NONE);
    
    if(rand() % 2== 0){
        board[pos_x][pos_y].piece = GENERAL;//Red General
        board[pos_x][pos_y].color = RED;
    }
    else{
        board[pos_x][pos_y].piece = MARSHALL;//Red Marshall
        board[pos_x][pos_y].color = RED;
    }
    
    do{
        pos_x = rand() % 2 + 3;
        pos_y= rand() % 5;
    }while(board[pos_x][pos_y].piece != NONE);
    
    if(rand() % 2== 0){
        board[pos_x][pos_y].piece = GENERAL;//Blue General
        board[pos_x][pos_y].color = BLUE;
    }
    else{
        board[pos_x][pos_y].piece = MARSHALL;//Blue Marshall
        board[pos_x][pos_y].color = BLUE;
    }
    
    //MINERS(8)
    do{
        pos_x = rand() % 2;
        pos_y= rand() % 5;
    }while(board[pos_x][pos_y].piece != NONE);
    board[pos_x][pos_y].piece = MINER;
    board[pos_x][pos_y].color = RED;
    
    do{
        pos_x = rand() % 2 + 3;
        pos_y= rand() % 5;
    }while(board[pos_x][pos_y].piece != NONE);
    
    board[pos_x][pos_y].piece= MINER;//Blue Miner
    board[pos_x][pos_y].color= BLUE;
    
    //SPIES(S)
    do{
        pos_x = rand() % 2;
        pos_y= rand() % 5;
    }while(board[pos_x][pos_y].piece != NONE);
    board[pos_x][pos_y].piece= SPY;//Red Spy
    board[pos_x][pos_y].color = RED;
    
    do{
        pos_x = rand() % 2 + 3;
        pos_y= rand() % 5;
    }while(board[pos_x][pos_y].piece != NONE);
    board[pos_x][pos_y].piece = SPY;//Blue Spy
    board[pos_x][pos_y].color = BLUE;
    
    //COLONELS, MAJORS, CAPTAINS, LIEUTENTANTS (3,4,5,6)
    for(int i = 1; i <= 3; i++){
        do{
            pos_x = rand() % 2;
            pos_y= rand() % 5;
        }while(board[pos_x][pos_y].piece != NONE);
        
        switch(rand() % 4){
            case 0:
                board[pos_x][pos_y].piece = COLONEL;//Red Colonel
                board[pos_x][pos_y].color = RED;
                break;
            case 1:
                board[pos_x][pos_y].piece = MAJOR;//Red Major
                board[pos_x][pos_y].color = RED;
                break;
            case 2:
                board[pos_x][pos_y].piece = CAPTAIN;//Red Captain
                board[pos_x][pos_y].color = RED;
                break;
            case 3:
                board[pos_x][pos_y].piece = LIEUTENTANT;//Red Lieutentant
                board[pos_x][pos_y].color = RED;
                break;
            case 4:
                board[pos_x][pos_y].piece = SERGEANT;//Red Sergeant
                board[pos_x][pos_y].color = RED;
                break;
            default:
                cout << "MAJOR ERROR!" << endl;
                break;
        }
    }
    
    for(int i = 1; i <= 3; i++){
        do{
            pos_x = rand() % 2 + 3;
            pos_y= rand() % 5;
        }while(board[pos_x][pos_y].piece != NONE);
        
        switch(rand() % 4){
            case 0:
                board[pos_x][pos_y].piece = COLONEL;//Blue Colonel
                board[pos_x][pos_y].color = BLUE;
                break;
            case 1:
                board[pos_x][pos_y].piece = MAJOR;//Blue Major
                board[pos_x][pos_y].color = BLUE;
                break;
            case 2:
                board[pos_x][pos_y].piece = CAPTAIN;//Blue Captain
                board[pos_x][pos_y].color = BLUE;
                break;
            case 3:
                board[pos_x][pos_y].piece = LIEUTENTANT;//Blue Lieutentant
                board[pos_x][pos_y].color = BLUE;
                break;
            case 4:
                board[pos_x][pos_y].piece = SERGEANT;//Blue Sergeant
                board[pos_x][pos_y].color = BLUE;
                break;
            default:
                cout << "MAJOR ERROR" << endl;
                break;
        }
    }
    cout << "Randomization complete. " << endl;
}

/*Name: move
 Parameters: The input board
 Return: none
 Description: The user moves pieces as long as they are legal*/
void move(int length, GameBoard ** board){
    char user_x, user_y;
    int old_x, old_y;
    bool valid, gameEnd;
    do{//Exits when game ends
        do{//Exits when valid starting coordinate is entered
            valid=false;
            cout << "Enter the CURRENT coordinates of your piece (e.g. D2 or QQ to quit): ";
            cin >> user_x >> user_y;
            old_x = user_x - 'A';
            old_y = user_y - '0' - 1;
            //cout << "The current x coordinate is: " << old_x << endl;
            //cout << "The current y coordinate is: " << old_y << endl;
            //cout << "Color at that square: " << static_cast<char>(board[old_x][old_y].color) << endl;
            /*if(board[user_x][user_y].color == BLACK || user_x +'A' > 'E'  || user_x + 'A' < 'A' || user_y > '5' || user_y < '1'){
             cout << "This is an invalid starting coordinate!" << endl;
             }*/
            if(old_y == ' '){//The ascii value if the user types QQ
                cout << "Quitting the game. Thanks for playing!" << endl;
                exit(EXIT_FAILURE);
            }
            else if(board[old_x][old_y].color == BLACK || board[old_x][old_y].color == RED || old_x < 0  || old_x > 5 || old_y < 0  || old_y > 5 || board[old_x][old_y].piece == FLAG || board[old_x][old_y].piece==BOMB){
                cout << "This is an invalid starting coordinate!" << endl;
            }
            else{
                cout << "This is valid" << endl << endl;
                valid = true;
            }
        }while(!valid);
        
        bool adjacent = false;
        int new_x, new_y;
        do{//Exits when valid ending coordinate is entered
            valid = false;
            cout << "Enter the ENDING coordinates of your piece: ";
            cin >> user_x >> user_y;
            new_x= user_x - 'A';
            new_y= user_y - '0' - 1;
            //cout << "The current x coordinate is: " << new_x << endl;
            //cout << "The current y coordinate is: " << new_y << endl;
            //cout << "Color at that square: " << static_cast<char>(board[new_x][new_y].color) << endl;
            
            //Checks to see if new square is horizontally or vertically adjacent
            //The third part of the below condition ensures that diagonal movement is impossible
            if(abs(new_x - old_x) <= 1 && abs(new_y - old_y) <= 1 && abs(abs(new_y - old_y) + abs(new_x - old_x) < 2)){
                cout << "The squares are adjacent!" << endl;
                adjacent = true;
            }
            
            //If the square isn't adjacent, we go to the second condition and the user tries again
            if(!adjacent || new_x < 0  || new_x > 5 || new_y < 0  || new_y > 5 || board[new_x][new_y].color == board[old_x][old_y].color){
                cout << "This is an invalid ending coordinate!" << endl;
            }
            else{
                cout << "This is valid" << endl;
                valid = true;
            }
            
            //cout << "new value: " << static_cast<char>(board[new_x][new_y].piece) << " old value: " << static_cast<char>(board[new_x][new_y].piece) << endl;
            /*if(new_x != old_x + 1 || new_y != old_y + 1){ //|| new_x > 4 || new_x < 0 || new_y > 4 || new_y < 0){
             cout << "This is an invalid ending coordinate, c'mon man!" << endl;
             }
             else if(new_x == 'Q' && new_y == 'Q'){
             cout << "Exiting game..." << endl;
             break;
             }
             else{
             valid = true;
             adjacent = true;
             }*/
        }while(!valid);
        
        //cout << "old square " << board[old_x][old_y].piece << endl;
        //cout << "new square " << board[new_x][new_y].piece << endl;
        //cout << "old square " << static_cast<char>(board[old_x][old_y].piece) << endl;
        //cout << "new square " << static_cast<char>(board[new_x][new_y].piece) << endl;
        
        if(board[new_x][new_y].piece == FLAG){
            cout << "You have captured the flag!!" << endl;
            gameEnd = true;
            printboard(5, board, false);
        }
        else if(board[new_x][new_y].piece == NONE){
            cout << "There is nothing here!" << endl;
            board[new_x][new_y].piece = board[old_x][old_y].piece;
            board[old_x][old_y].piece = NONE;
            
            board[new_x][new_y].color= board[old_x][old_y].color;
            board[old_x][old_y].color= BLACK;
        }
        else if(board[new_x][new_y].piece ==BOMB){
            cout << "This is a bomb" << endl;
            if(board[old_x][old_y].piece == MINER){
                cout << "Your miner defused the enemy bomb!" << endl;
                board[new_x][new_y].piece = board[old_x][old_y].piece;//Set captured piece to old piece value
                board[new_x][new_y].color = board[old_x][old_y].color;
                
                board[old_x][old_y].piece = NONE;
                board[old_x][old_y].color = BLACK;
            }
            board[old_x][old_y].piece= NONE;
            board[old_x][old_y].color= BLACK;
        }
        else if(board[old_x][old_y].piece == board[new_x][new_y].piece){//Wipe out both pieces
            cout << "Your opponent also had a: " << static_cast<char>(board[new_x][new_y].piece) << endl;
            board[old_x][old_y].piece = NONE;
            board[old_x][old_y].color= BLACK;
            
            board[new_x][new_y].piece = NONE;
            board[new_x][new_y].color = BLACK;
        }
        else if(board[old_x][old_y].piece < board[new_x][new_y].piece){//Lesser values win
            cout << "You have the stronger piece!" << endl;
            board[new_x][new_y].piece=board[old_x][old_y].piece;//Set captured piece to old piece value
            board[new_x][new_y].color=board[old_x][old_y].color;
            
            board[old_x][old_y].piece = NONE;
            board[old_x][old_y].color= BLACK;
        }
        else if(board[old_x][old_y].piece > board[new_x][new_y].piece){//Greater values lose
            cout << "They have the stronger piece!" << endl;
            if(board[old_x][old_y].piece==SPY && (board[new_x][new_y].piece == MARSHALL || board[new_x][new_y].piece == GENERAL)){
                cout << "Your spy took out the enemy " << board[new_x][new_y].piece << endl;
                board[new_x][new_y].piece = board[old_x][old_y].piece;//Set captured piece to old piece value
                board[new_x][new_y].color = board[old_x][old_y].color;
                
                board[old_x][old_y].piece = NONE;
                board[old_x][old_y].color = BLACK;
            }
            board[old_x][old_y].piece = NONE;
            board[old_x][old_y].color= BLACK;
        }
        else{
            cout << "I dunno what happened here lol :3" << endl;
        }
        
        //debug(board);
        printboard(5, board, true);
    }while(!gameEnd);
}

/*Name: debug
 Parameters: The input board
 Return: none
 Description: Helps me make sure the program is functioning correctly */
void debug(GameBoard ** board){
    for(int i = 0; i< 5; i++){
        for(int j=0; j < 5; j++){
            cout << "Piece at " << i << j << " : " << static_cast<char>(board[i][j].piece) << endl;
            cout << "Color at " << i << j << " : " << static_cast<char>(board[i][j].color) << endl;
        }
    }
}

