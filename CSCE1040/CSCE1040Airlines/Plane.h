//#ifndef PLANE_H
//#define PLANE_H

#include <iostream>
#include <cstdlib>
#include <iomanip>
#include <fstream>
#include <vector>
#include <string>
#include <ctime>

using namespace std;

class Plane{
    private:
        string make;
        string model;
        string tail;
        int seats;
        int range;
        string status;
    public:
        Plane();
    
        //Accessor
        string GetMake() const{return make;}
        string GetModel() const{return model;}
        string GetTail() const{return tail;}
        int GetSeats() const{return seats;}
        int GetRange() const{return range;}
        string GetStatus() const{return status;}
    
        //Mutator
        void SetMake(string s){make = s;}
        void SetModel(string s){model = s;}
        void SetTail(string s){tail=s;}
        void SetSeats(int n){seats=n;}
        void SetRange(int n){range=n;}
        void SetStatus(string s){status=s;}
};

//#endif //PLANE_H
