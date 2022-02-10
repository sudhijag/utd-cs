#include "Enrollment.h"
#include <iostream>

using namespace std;

Enrollment::Enrollment(){
    int courseID=0;
    int studID=0;
    int ID=0;
    
    int grades[10]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    int avg=0;
    char letteravg='B';
}

Enrollment::~Enrollment(){
    
}

int Enrollment::findEnd(){
    int i;
    for(i=0; i < 10; i++){
        if(getGrades(i)==-1){
            cout << "This is the end." << endl;
            break;
        }
        else{
            i=10;
        }
    }
    return i;
}
