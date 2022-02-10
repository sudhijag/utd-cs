//#ifndef FLIGHTS_H
//#define FLIGHTS_H

#include <string>
#include <iostream>

using namespace std;

class Flight{
    private:
        string planeID;
        string pilotID;
        string copilotID;
        string crewID[3];
        int starttime[6];
        int endtime[6];
        string sac;//starting airport code
        string eac;//ending airport code
        int pass;
        string status;
    public:
        Flight();
        
        //Accessors
        string GetPlaneID() const{return planeID;}
        string GetPilotID() const{return pilotID;}
        string GetCoPilotID() const{return copilotID;}
        string GetCrewID(int n) const{return crewID[n];}
        string GetSac() const{return sac;}
        string GetEac() const{return eac;}
        int GetPass() const{return pass;}
        string GetStatus() const{return status;}
        int GetStartTime(int n) const{return starttime[n];}
        int GetEndTime(int n) const{return endtime[n];}
    
        //Prints
        void PrintStartTime() const{for(int i=0; i< 6; i++){cout << starttime[i] << " ";}}
        void PrintEndTime() const{for(int i=0; i< 6; i++){cout << endtime[i]<< " ";}}
        void PrintCrewID() const{for(int i=0; i< 3; i++){cout << crewID[i] << " ";}}
    
        //Mutators
        void SetPlaneID(string s){planeID=s;}
        void SetPilotID(string s){pilotID=s;}
        void SetCoPilotID(string s){copilotID=s;}
        void SetCrewID(string arr[]){for(int i=0; i< 3; i++){crewID[i]=arr[i];}}
        void SetStartTime(int arr[]){for(int i=0; i< 6; i++){starttime[i]=arr[i];}}
        void SetEndTime(int arr[]){for(int i=0; i< 6; i++){endtime[i]=arr[i];}}
        void SetSac(string s){sac=s;}
        void SetEac(string s){eac=s;}
        void SetPass(int n){pass=n;}
        void SetStatus(string s){status=s;}
};

//#endif //FLIGHTS_H
