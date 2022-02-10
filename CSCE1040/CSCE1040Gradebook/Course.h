#ifndef Course_h
#define Course_h

#include <string>
using namespace std;

class Course{
    public:
        Course();
        void addCourse();
    
        int getID(){return ID;}
        string getName(){return name;}
        string getLocation(){return location;}
        string getTime(){return time;}
    
        void setName(string s){s=name;}
        void setID(int n){n=ID;}
        void setLocation(string s){s=location;}
        void setTime(string s){s=time;}
    private:
        int ID;
    
        string location;
        string name;
        string time;
};


#endif /* Course_h */
