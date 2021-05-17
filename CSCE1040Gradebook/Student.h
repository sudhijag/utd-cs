//#ifndef Student_h
//#define Student_h

#include <iostream>
#include <iomanip>
#include <fstream>

using namespace std;

class Student{
    public:
        Student();
    
        int getID(){return ID;}
        string getName(){return name;}
        string getClassif(){return classif;}
        float getAvg(){return avg;}
    
        void setID(int n){ID=n;}
        void setName(string s){name=s;}
        void setClassification(string s){classif=s;}
        void setavg(int n){avg=n;}
    private:
        int ID;
        string name;
        string classif;
        float avg;
};

//#endif /* Student_h */
