//#ifndef  FLIGHTS_H
//#define FLIGHTS_H

#include "Flight.h"
#include "Planes.h"
#include "Members.h"

class Flights{
    public:
        Flights();
    
        void Add(Planes& p, Members& m);
        
        void Edit();
        void Delete();
        void Update();
        void Clean();
    
        void Print(string planeID, string pilotID);
    
        void List();
        void ListByStatus();
    
        void ScheduleByPlane(string tempID);
        void ScheduleByMember(string tempID);
    
        Flight* Search(string planeID, string pilotID);
    
        void Load(istream& is);
        void Save(ostream& os);
    private:
        vector <Flight> flist;
};

//#endif FLIGHTS_H
