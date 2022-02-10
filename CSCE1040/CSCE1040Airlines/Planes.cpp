#include "Planes.h"

void Planes::Add(){
    Plane temp;
    
    string tempMake;
    string tempModel;
    string tempTail;
    int tempSeats;
    int tempRange;
    string tempStatus;
    
    cout << endl;
    cout << "Enter the new plane's make: ";
    cin >> tempMake;
    cout << "Enter the new plane's model: ";
    cin >> tempModel;
    cout << "Enter the new plane's tail: ";
    cin >> tempTail;
    cout << "Enter the new plane's seats: ";
    cin >> tempSeats;
    cout << "Enter the new plane's range: ";
    cin >> tempRange;
    
    do{
        cout << "Enter the new plane's status(Available or Not) only: ";
        cin >> tempStatus;
    }while(tempStatus != "Available" && tempStatus != "Not");

    bool found = false;
    int index=0;
    
    for(int i=0; i < plist.size(); i++){
        if(tempTail == plist[i].GetTail()){
            found = true;
            index = i;
        }
    }
    
    if(found){
        cout << "That plane has an tail ID that exists already, try again!" << endl;
        return;
    }
    
    temp.SetMake(tempMake);
    temp.SetModel(tempModel);
    temp.SetTail(tempTail);
    temp.SetSeats(tempSeats);
    temp.SetRange(tempRange);
    temp.SetStatus(tempStatus);

    plist.push_back(temp);
    
    cout << "Plane added to plane collection." << endl;
    
    return;
}

void Planes::Delete(){
    string temptail;
    cout << "Enter the tail number of the plane you wish to delete: ";
    cin >> temptail;
    cout << endl;
    
    bool found = false;
    int index=0;
    
    for(int i=0; i < plist.size(); i++){
        if(temptail == plist[i].GetTail()){
            found = true;
            index = i;
        }
    }
    
    if(found==false){
        cout << "Plane not found, nothing deleted." << endl;
        return;
    }
    
    plist.erase(plist.begin()+ index);
    cout << "Plane deleted from plane collection. " << endl;
}

void Planes::Edit(){
    Plane temp;
    
    string tempMake;
    string tempModel;
    string tempTail;
    int tempSeats;
    int tempRange;
    string tempStatus;
    
    string temptail;
    cout << "Enter the tail number of the plane you wish to edit." << endl;
    cin >> temptail;
    
    bool found = false;
    int index=0;
    
    for(int i=0; i < plist.size(); i++){
        if(temptail == plist[i].GetTail()){
            found = true;
            index = i;
        }
    }
    
    if(found==false){
        cout << "Plane not found, nothing edited." << endl;
        return;
    }
    
    cout << "Enter the new make: ";
    cin >> tempMake;
    cout << "Enter the new model: ";
    cin >> tempModel;
    cout << "Enter the new tail: ";
    cin >> tempTail;
    cout << "Enter the new seats: ";
    cin >> tempSeats;
    cout << "Enter the new range: ";
    cin >> tempRange;
    cout << "Enter the new status: ";
    cin >> tempStatus;
    
    temp.SetMake(tempMake);
    temp.SetModel(tempModel);
    temp.SetTail(tempTail);
    temp.SetSeats(tempSeats);
    temp.SetRange(tempRange);
    temp.SetStatus(tempStatus);

    
    //Replace the old plane with the temp one
}

void Planes::List(){
    if(plist.size() ==0){
        cout << "No planes currently in the collection. Go add some!" << endl;
        return;
    }
    
    for(int i=0; i < plist.size(); i++){
        cout << "PRINTING DATA FIELDS FOR PLANE NUMBER " << i << endl;
        cout << "Make: " << plist[i].GetMake() << endl;
        cout << "Model: " << plist[i].GetModel() << endl;
        cout << "Tail Number: " << plist[i].GetTail() << endl;
        cout << "Number of Seats: "<< plist[i].GetSeats() << endl;
        cout << "Range in miles: " << plist[i].GetRange() << endl;
        cout << "Status: " << plist[i].GetStatus() << endl;
    }
}

void Planes::Print(){
    string temptail;
    
    cout << "Enter the tail number of the plane you wish to print." << endl;
    cin >> temptail;
    
    bool found = false;
    int index=0;
    
    for(int i=0; i < plist.size(); i++){
        if(temptail == plist[i].GetTail()){
            found = true;
            index = i;
        }
    }
    
    if(found==false){
        cout << "Plane not found, nothing printed." << endl;
        return;
    }
    
    cout << "Make: " << plist[index].GetMake() << endl;
    cout << "Model: " << plist[index].GetModel() << endl;
    cout << "Tail Number: " << plist[index].GetTail() << endl;
    cout << "Number of Seats: "<< plist[index].GetSeats() << endl;
    cout << "Range in miles: " << plist[index].GetRange() << endl;
    cout << "Status: " << plist[index].GetStatus() << endl;
}

Plane* Planes::Search(string ID){
    Plane* temp;
    int index=0;
    
    for(int i=0; i < plist.size(); i++){
        if(ID == plist[i].GetTail()){
            index = i;
            temp= &plist[i];
            cout << "YES, that plane does exist" << endl;
            return temp;
        }
    }
    
    cout << "NO, that plane was not found in the collection" << endl;
    return 0;
}

void Planes::Save(ostream& os){
    os << "P " << plist.size() << endl;
    
    for(int i=0; i < plist.size(); i++){
        os <<  plist[i].GetMake() << endl;//Remove the labels
        os <<  plist[i].GetModel() << endl;
        os <<  plist[i].GetTail() << endl;
        os <<  plist[i].GetSeats() << endl;
        os <<  plist[i].GetRange() << endl;
        os <<  plist[i].GetStatus() << endl;
    }
}

void Planes::Load(istream& is) {
    string strread;
    int intread;
    int size=-1;
    
    is >> strread;
    is >> size;
    
    cout << "SIZE OF PLANES COLLECTION: " << size <<  endl;
    
    for(int i=0; i < size; i++){
        Plane temp;
        is >> strread;
        temp.SetMake(strread);
        is >> strread;
        temp.SetModel(strread);
        is >> strread;
        temp.SetTail(strread);
        is >> intread;
        temp.SetSeats(intread);
        is >> intread;
        temp.SetRange(intread);
        is >> strread;
        temp.SetStatus(strread);
        plist.push_back(temp);
    }
    
}
