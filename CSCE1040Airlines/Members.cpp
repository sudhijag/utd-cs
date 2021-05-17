#include "Members.h"
#include <iostream>

using namespace std;

void Members::Add(){
    Member temp;
    
    string tempName;
    string tempID;
    string tempType;
    string tempStatus;
    
    cout << "Enter the new crew member's name (First Last) seperated by space: ";
    cin.ignore();
    getline(cin, tempName);
    
    cout << "Enter the new crew member's ID: ";
    //cin.ignore();
    cin >> tempID;
    
    do{
        cout << "Enter the new crew member's type (Pilot, Copilot, Crew) only: ";
        cin >> tempType;
    }while(tempType != "Pilot" && tempType != "Copilot" && tempType != "Crew");
    
    do{
        cout << "Enter the new crew member's status (Available, Not) only: ";
        cin >> tempStatus;
    }while(tempStatus != "Available" && tempStatus != "Not");

    //Check to see if that ID aint taken yet
    bool found = false;
    int index=0;
    
    for(int i=0; i < mlist.size(); i++){
        if(tempID == mlist[i].GetID()){
            found = true;
            index = i;
        }
    }
    
    if(found){
        cout << "That member has an crew ID that exists already, nothing added." << endl;
        return;
    }
    
    temp.SetName(tempName);
    temp.SetID(tempID);
    temp.SetType(tempType);
    temp.SetStatus(tempStatus);

    mlist.push_back(temp);
    
    cout << "Crew Member added to collection." << endl;
}

void Members::Delete(){
    string tempID = " ";
    cout << "Enter the ID of the crew member you wish to delete: " << endl;
    cin >> tempID;
    
    for(int i=0; i < mlist.size(); i++){
        if(tempID == mlist[i].GetID()){
            mlist.erase(mlist.begin()+ i);
            cout << "Crew Member deleted from member collection. " << endl;
            return;
        }
    }
    
    cout << "Crew Member not found, nothing deleted." << endl;
    return;
}

void Members::Edit(){
    string tempName;
    string tempID;
    string tempType;
    string tempStatus;
   
    string editID = " ";
    cout << "Enter the ID of the crew member you wish to edit: " << endl;
    cin >> editID;
    
    bool found = false;
    int index=0;
    
    for(int i=0; i < mlist.size(); i++){
        if(editID == mlist[i].GetID()){
            found = true;
            index = i;
        }
    }
    
    if(found==false){
        cout << "Crew Member not found, nothing deleted." << endl;
        return;
    }
    
    cout << "Enter the new crew member's name (First Last) seperated by space: ";
    cin.ignore();
    getline(cin, tempName);
    
    cout << "Enter the new crew member's ID: ";
    cin >> tempID;
    
    do{
        cout << "Enter the new crew member's type (Pilot, Copilot, Crew) only: ";
        cin >> tempType;
    }while(tempType != "Pilot" && tempType != "Copilot" && tempType != "Crew");
    
    do{
        cout << "Enter the new crew member's status (Available, Not) only: ";
        cin >> tempStatus;
    }while(tempStatus != "Available" && tempStatus != "Not");
    
    mlist[index].SetName(tempName);
    mlist[index].SetID(tempID);
    mlist[index].SetType(tempType);
    mlist[index].SetStatus(tempStatus);
}

void Members::List(){
    if(mlist.size() ==0){
        cout << "No crew members currently in the collection. Go add some!" << endl;
        return;
    }
    
    for(int i=0; i < mlist.size(); i++){
        cout << "PRINTING DATA FIELDS FOR CREW MEMBER NUMBER " << i << endl;
        cout << "Name: " << mlist[i].GetName() << endl;
        cout << "ID: " << mlist[i].GetID() << endl;
        cout << "Type: " << mlist[i].GetType() << endl;
        cout << "Status: " << mlist[i].GetStatus() << endl;
        cout << endl;
    }
}

void Members::Print(){
    string editID = " ";
    cout << "Enter the ID of the crew member you wish to edit: " << endl;
    cin >> editID;
    
    bool found = false;
    int index=0;
    
    for(int i=0; i < mlist.size(); i++){
        if(editID == mlist[i].GetID()){
            found = true;
            index = i;
        }
    }
    
    if(found==false){
        cout << "Crew Member not found, nothing printed." << endl;
        return;
    }
    
    cout << "Name: " << mlist[index].GetName() << endl;
    cout << "ID: " << mlist[index].GetID() << endl;
    cout << "Type: " << mlist[index].GetType() << endl;
    cout << "Status: " << mlist[index].GetStatus() << endl;
}

Member* Members::Search(string ID, string type){
    Member* temp;
    for(int i=0; i < mlist.size(); i++){
        if(ID == mlist[i].GetID()){
            if(mlist[i].GetType()==type){
                temp=&mlist[i];
                return temp;
            }
        }
    }
    cout << "We could not find that crew member" << endl;
    return 0;
}


Member* Members::Search(string ID){
    Member* temp;
    for(int i=0; i < mlist.size(); i++){
        if(ID == mlist[i].GetID()){
            temp=&mlist[i];
            cout << "YES, that member does exist" << endl;
            return temp;
        }
    }
    cout << "NO, that member does exist" << endl;
    return 0;
}

void Members::Save(ostream& os){
    os << "M " << mlist.size() << endl;

    for(int i=0; i < mlist.size(); i++){
        os << mlist[i].GetName() << endl;
        os << mlist[i].GetID() << endl;
        os << mlist[i].GetType() << endl;
        os << mlist[i].GetStatus() << endl;
    }
}

void Members::Load(istream& is){
    string strread;
    string strread2;
    int intread;
    int size=-1;
    
    is >> strread;
    is >> size;
    cout << "SIZE OF MEMBERS COLLECTION: " << size << endl;

    for(int i=0; i < size; i++){
        Member mtemp;
        is >> strread;
        is >> strread2;
        mtemp.SetName(strread + " " + strread2);
        is >> strread;
        mtemp.SetID(strread);
        is >> strread;
        mtemp.SetType(strread);
        is >> strread;
        mtemp.SetStatus(strread);
        
        mlist.push_back(mtemp);
    }
}
