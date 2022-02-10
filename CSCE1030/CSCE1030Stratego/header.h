#include <iostream>
#include <ctime>
#include <stdio.h>
#include <cstdlib>
#include <cmath>

using namespace std;

const int BOARDSIZES=5;

//The possible values for each piece
enum Pieces{
    FLAG='F',
    BOMB='B',
    MARSHALL= '1',
    GENERAL= '2',
    COLONEL= '3',
    MAJOR= '4',
    CAPTAIN= '5',
    LIEUTENTANT= '6',
    SERGEANT= '7',
    MINER= '8',
    SPY= 'S',
    NONE = ' '
};

//The possible colors for each piece
enum Colors{
    BLUE = 'B',
    RED = 'R',
    BLACK = 'N'
};

//Our gameboard will be the same type of this struct
struct GameBoard{
    Colors color;
    Pieces piece;
    bool moveable;
};


void printid();
void printrules();
void generate(int length, GameBoard ** board);
void printboard(int length, GameBoard ** board, bool hide);
void assign(int length, GameBoard ** board);
void move(int length, GameBoard ** board);
void debug(GameBoard ** board);

