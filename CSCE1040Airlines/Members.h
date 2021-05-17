//#ifndef  MEMBERS_H
//#define MEMBERS_H

#include "Member.h"
#include <vector>

using namespace std;

class Members{
    public:
        void Add();
        void Edit();
        void Delete();
        void List();
        void Print();
    
        Member* Search(string ID, string type);
        Member* Search(string ID);
    
        void Save(ostream& os);
        void Load(istream& is);
    
        int GetSize(){return mlist.size();};
    
    private:
        vector <Member> mlist;
};

//#endif //MEMBERS_H
