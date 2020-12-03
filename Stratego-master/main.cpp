#include "header.h"

int main(){
    int length = 5;

    printrules();
    
    GameBoard ** board = new GameBoard * [length];//Declares an array of pointers to pointers
    
    for(int i=0; i < length; i++){
        board[i] = new GameBoard [length];//Makes an array for each of the pointers in the array
    }

    generate(length, board);
    assign(length, board);
    //debug(board);
    printboard(length, board, true);
    move(length, board);
    
    return 0;
}

