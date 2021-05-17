//#ifndef PLANES_H
//#define PLANES_H

#include "Plane.h"

class Planes{
    public:
        void Add();
        void Edit();
        void Delete();
    
        Plane* Search(string ID);
            
        void List();
        void Print();
    
        void Load(istream& is);
        void Save(ostream& os);
    
        int GetSize(){return plist.size();}
    private:
        vector <Plane> plist;

};

//#endif //PLANES_H
