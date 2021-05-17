//#ifndef MEMBERS_H
//#define MEMBERS_H

#include <string>

using namespace std;

class Member{
    private:
        string name;
        string ID;
        string type;
        string status;
    public:
        Member();
    
        //Accessor
        string GetName() const{return name;}
        string GetID() const{return ID;}
        string GetType() const{return type;}
        string GetStatus() const{return status;}
    
        //Mutator
        void SetName(string s){name=s;}
        void SetID(string s){ID=s;}
        void SetType(string s){type=s;}
        void SetStatus(string s){status =s;}
};





//#endif //MEMBERS_H
