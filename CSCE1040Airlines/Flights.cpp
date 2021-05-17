#include "Flights.h"

#include <iostream>
#include <ctime>

using namespace std;

Flights::Flights(){

}

void Flights::Add(Planes& p, Members& m){
    if(p.GetSize()== 0 || m.GetSize() < 5){
        cout << "ERROR: You need at least 5 crew members and 1 plane to add a flight." << endl;
        return;
    }
    
    Flight temp;
    
    string tempPlaneID;
    string tempPilotID;
    string tempCopilotID;
    string tempCrewID[3];
    int tempStarttime[6];
    int tempEndtime[6];
    string tempSac;
    string tempEac;
    int tempPass;
    string tempStatus;

    cout << "Enter the new flight's plane ID: ";
    cin >> tempPlaneID;
    cout << "Enter the new flight's pilot ID: ";
    cin >> tempPilotID;
    cout << "Enter the new flight's copilot ID: ";
    cin >> tempCopilotID;
    
    cout << "Enter the new flight's crew IDs: ";
    cin >> tempCrewID[0] >> tempCrewID[1] >> tempCrewID[2];
    
    cout << "Enter the new flight's start time(year month day hours minutes seconds) (6 numbers seperated by spaces)";
    cin >> tempStarttime[0] >> tempStarttime[1] >> tempStarttime[2] >> tempStarttime[3] >> tempStarttime[4] >> tempStarttime[5];
    
    cout << "Enter the new flight's ending time(year month day hours minutes seconds) (6 numbers seperated by spaces)";
    cin >> tempEndtime[0] >> tempEndtime[1] >> tempEndtime[2] >> tempEndtime[3] >> tempEndtime[4] >> tempEndtime[5];
    
    
    cout << "Enter the starting airport code: ";
    cin >> tempSac;
    cout << "Enter the ending airport code: ";
    cin >> tempEac;
    cout << "Enter the new flight's number of passenders: ";
    cin >> tempPass;
    
    do{
        cout << "Enter the new flight's status(Active, Cancelled, Completed) only: ";
        cin >> tempStatus;
    }while(tempStatus != "Active" && tempStatus!= "Cancelled" && tempStatus != "Completed");
    
    
    //Does the plane exist in the plane collection?
    if(p.Search(tempPlaneID) == 0){
        cout << "ERROR: User provided plane ID is not an existing plane." << endl;
    }
    else{
        cout << "Valid plane entered. " << endl;
    }
    
    //Does the plane have enough seats?
    Plane* foundplane=p.Search(tempPlaneID);
    if(tempPass > foundplane -> GetSeats()){
        cout << "ERROR: User provided passenger count too high to accomodate" << endl;
    }
    
    //Is the plane available?
    if(foundplane -> GetStatus() == "Not"){
        cout << "ERROR: The plane status indicates that it is not available";
    }
    
    for(int i=0; i < flist.size(); i++){
        if(flist[i].GetPlaneID() == tempPlaneID) {
            for(int j=0; j < 6; j++){
                if(tempEndtime[j] < flist[i].GetEndTime(j)){
                    break;//Flight has not ended: return to the main loop, examine next plane
                }
                else if(tempEndtime[j] > flist[i].GetEndTime(j)){
                    cout << "ERROR: Status indicates the flight has already finished"; flist[i].SetStatus("Completed");
                }
                else{
                    j++;
                }
            }
        }
    }
    
    
    //Is the pilot a pilot? Is he available?
    Member* pilot=m.Search(tempPilotID, "Pilot");
    
    if(pilot == 0){//was the pilot found in the member in member collection?
        cout << "ERROR: User provided pilot ID not a pilot." << endl;
    }
    
    if(pilot -> GetStatus() == "Not"){
        cout << "ERROR: Status indicates pilot is not available" << endl;
    }
    
    //Is the copilot a copilot? Is he available?
    Member* copilot=m.Search(tempCopilotID, "Copilot");
    
    if(copilot== 0){//was the pilot found in the member in member collection?
        cout << "ERROR: User provided copilot ID not a copilot." << endl;
    }
    if(copilot -> GetStatus() == "Not"){
        cout << "ERROR: The copilot status indicates he is not available" << endl;
    }
    
    //Do the crew members exist? Are they available
    //FIXME: Multiple hits from the search function
    Member* mhit;
    for(int i=0; i < 3; i++){
        mhit=m.Search(tempCrewID[i]);
        if(mhit == 0){
            cout << "ERROR: User provided crew ID not found" << endl;
        }
        else{
            if(mhit->GetStatus() == "Not"){
                cout << "ERROR: User provided crew ID " << i << "status field shows not available" << endl;
            }
        }
    }
    
    temp.SetPlaneID(tempPlaneID);
    temp.SetPilotID(tempPilotID);
    temp.SetCoPilotID(tempCopilotID);
    temp.SetCrewID(tempCrewID);
    temp.SetStartTime(tempStarttime);
    temp.SetEndTime(tempEndtime);
    temp.SetSac(tempSac);
    temp.SetEac(tempEac);
    temp.SetPass(tempPass);
    temp.SetStatus(tempStatus);
    
    flist.push_back(temp);
    

    cout << "Flight added to flight collection." << endl;
}

void Flights::Delete(){
    string planeID = " ";
    string pilotID= " ";
    
    cout << "Enter the plane ID of the flight you wish to delete." << endl;
    cin >> planeID;
    cout << "Enter the pilot ID of the flight you wish to delete" << endl;
    cin >> pilotID;

    for(int i=0; i < flist.size(); i++){
        if(planeID == flist[i].GetPlaneID()){
            if(pilotID==flist[i].GetPilotID()){
                cout << "We found that flight, erasing" << endl;
                flist.erase(flist.begin() +i);
                return;
            }
        }
    }
    
    cout << "Could not find flight, nothing deleted from flight collection. " << endl;
    return;
}

void Flights::Edit(){
    string tempPlaneID;
    string tempPilotID;
    string tempCopilotID;
    string tempCrewID[3];
    int tempStarttime[6];
    int tempEndtime[6];
    string tempSac;
    string tempEac;
    int tempPass;
    string tempStatus;
    
    string planeID = " ";
    string pilotID = " ";
    
    cout << "Enter the plane ID of the flight you wish to delete." << endl;
    cin >> planeID;
    cout << "Enter the pilot ID of the flight you wish to delete" << endl;
    cin >> pilotID;
    
    for(int i=0; i < flist.size(); i++){
        if(planeID == flist[i].GetPlaneID()){
            if(pilotID==flist[i].GetPilotID()){
                cout << "We found that flight, time to edit: " << endl;
                cout << "Enter the new flight's plane ID: ";
                cin >> tempPlaneID;
                cout << "Enter the new flight's pilot ID: ";
                cin >> tempPilotID;
                cout << "Enter the new flight's copilot ID: ";
                cin >> tempCopilotID;
                cout << "Enter the new flight's crew IDs: ";
                cin >> tempCrewID[0] >> tempCrewID[1] >> tempCrewID[2];
                
                cout << "Enter the new flight's start time(month day year hours minutes seconds)";
                cin >> tempStarttime[0] >> tempStarttime[1] >> tempStarttime[2] >> tempStarttime[3] >> tempStarttime[4] >> tempStarttime[5];
                cout << "Enter the new flight's ending time(month day year hours minutes seconds)";
                cin >> tempEndtime[0] >> tempEndtime[1] >> tempEndtime[2] >> tempEndtime[3] >> tempEndtime[4] >> tempEndtime[5];
                
                cout << "Enter the starting airport code: ";
                cin >> tempSac;
                cout << "Enter the ending airport code: ";
                cin >> tempEac;
                cout << "Enter the new flight's number of passenders: ";
                cin >> tempPass;
                
                do{
                    cout << "Enter the new flight's status(Active, Cancelled, Completed) only: ";
                    cin >> tempStatus;
                }while(tempStatus != "Active" && tempStatus!= "Cancelled" && tempStatus != "Completed");
                
                flist[i].SetPlaneID(tempPlaneID);
                flist[i].SetPilotID(tempPilotID);
                flist[i].SetCoPilotID(tempCopilotID);
                flist[i].SetCrewID(tempCrewID);
                flist[i].SetStartTime(tempStarttime);
                flist[i].SetEndTime(tempEndtime);
                flist[i].SetSac(tempSac);
                flist[i].SetEac(tempEac);
                flist[i].SetPass(tempPass);
                flist[i].SetStatus(tempStatus);
                
                return;
            }
        }
    }
    
    cout << "Could not find flight, nothing deleted from flight collection. " << endl;
    return;
    

}

void Flights::List(){
    if(flist.size() ==0){
        cout << "No flights currently in the collection. Go add some!" << endl;
        return;
    }
    
    for(int i=0; i < flist.size(); i++){
        cout << "PRINTING DATA FIELDS FOR FLIGHT NUMBER " << i << endl;
        
        cout << "Plane ID: " << flist[i].GetPlaneID() << endl;
        cout << "Pilot ID: " << flist[i].GetPilotID() << endl;
        cout << "Copilot ID: " << flist[i].GetCoPilotID() << endl;
        
        cout << "Crew ID's: ";
        flist[i].PrintCrewID();
        cout << endl;
        cout << "Start Time: ";
        flist[i].PrintStartTime();
        cout << endl;
        cout << "End Time: ";
        flist[i].PrintEndTime();
        cout << endl;
        
        cout << "Starting airport code: " << flist[i].GetSac() << endl;
        cout << "Ending airport code: " << flist[i].GetEac() << endl;
        cout << "Status: " << flist[i].GetStatus() << endl;
        
    }
}

void Flights::Print(string planeID, string pilotID){
    //PRINTS INFO FOR A SINGLE PLANE
    bool found = false;
    int index=0;
    
    for(int i=0; i < flist.size(); i++){
        if(planeID == flist[i].GetPlaneID()){
            if(pilotID == flist[i].GetPilotID()){
                found = true;
                index = i;
            }
        }
    }
    
    if(found==false){
        cout << "Flight not found, nothing printed." << endl;
        return;
    }
    
    cout << "Plane ID: " << flist[index].GetPlaneID() << endl;
    cout << "Pilot ID: " << flist[index].GetPilotID() << endl;
    cout << "Copilot ID: " << flist[index].GetCoPilotID() << endl;
    
    cout << "Crew IDs: ";
    flist[index].PrintCrewID();
    cout << endl;
    
    cout << "Start time: ";
    flist[index].PrintStartTime();
    cout << endl;
    
    cout << "End time: ";
    flist[index].PrintEndTime();
    cout << endl;
    
    cout << "Starting airport code: " << flist[index].GetSac() << endl;
    cout << "Ending airport code: " << flist[index].GetEac() << endl;
    cout << "Status: " << flist[index].GetStatus() << endl;
}

void Flights::ListByStatus(){
    if(flist.size() ==0){
        cout << "No flights currently in the collection. Go add some!" << endl;
        return;
    }
    
    for(int i=0; i < flist.size(); i++){
        cout << "ACTIVE FLIGHTS: " << endl;
        if(flist[i].GetStatus() == "Active"){
            Print(flist[i].GetPlaneID(), flist[i].GetPilotID());
        }
    }
    cout << endl;
    
    for(int i=0; i < flist.size(); i++){
        cout << "CANCELLED FLIGHTS: " << endl;
        if(flist[i].GetStatus() == "Cancelled"){
            Print(flist[i].GetPlaneID(), flist[i].GetPilotID());
        }
    }
    cout << endl;
    
    for(int i=0; i < flist.size(); i++){
        cout << "CANCELLED FLIGHTS: " << endl;
        if(flist[i].GetStatus() == "Cancelled"){
            Print(flist[i].GetPlaneID(), flist[i].GetPilotID());
        }
    }
    cout << endl;
}

Flight* Flights::Search(string planeID, string pilotID){
    Flight* temp;
    for(int i=0; i < flist.size(); i++){
        if(planeID == flist[i].GetPlaneID()){
            if(pilotID==flist[i].GetPilotID()){
                temp=&flist[i];
                cout << "YES, that flight exists" << endl;
                return temp;
            }
        }
    }
    cout << "NO, we could not find the plane you were looking for! " << endl;
    return 0;
}

void Flights::Update(){
    //Take in the current time
    int currTime[0];
     cout << "Enter the current time (month day year hours minutes seconds)";
    cin >> currTime[0] >> currTime[1] >> currTime[2] >> currTime[3] >> currTime[4] >> currTime[5];
    bool complete=false;
    
    //Compare to ending time
    for(int i=0; i < flist.size(); i++){
        for(int j=0; j < 6; j++){
            if(currTime[j] < flist[i].GetEndTime(j)){
                break;//Flight has not ended: return to the main loop, examine next plane
            }
            else if(currTime[j] > flist[i].GetEndTime(j)){
                flist[i].SetStatus("Completed");
            }
            else{
                j++;
            }
        }
    }
    cout << "Updated statuses of flights collection" << endl;
}

void Flights::Clean(){
    for(int i=0; i< flist.size(); i++){
        if(flist[i].GetStatus() == "Completed" || flist[i].GetStatus() == "Cancelled"){
            flist.erase(flist.begin()+i);//Remove that flight from our collection
        }
    }
    cout << "Cleaned flights" << endl;
}

void Flights::Load(istream &is){
    int intread;
    string strread;
    string strreads[3];
    
    int intreads[6];
    int size=-1;
    
    is >> strread;
    is >> size;
    cout << "SIZE OF FLIGHTS COLLECTION: " << size << endl;
    
    for(int i=0; i < size; i++){
        Flight ftemp;
        is >> strread;
        ftemp.SetPlaneID(strread);
        is >> strread;
        ftemp.SetPilotID(strread);
        is >> strread;
        ftemp.SetCoPilotID(strread);
        
        is >> strreads[0] >> strreads[1] >> strreads[2];
        ftemp.SetCrewID(strreads);
        is >> intreads[0] >> intreads[1] >> intreads[2] >> intreads[3] >> intreads[4] >> intreads[5];
        ftemp.SetStartTime(intreads);
        is >> intreads[0] >> intreads[1] >> intreads[2] >> intreads[3] >> intreads[4] >> intreads[5];
        ftemp.SetEndTime(intreads);
        
        is >> strread;
        ftemp.SetSac(strread);
        is >> strread;
        ftemp.SetEac(strread);
        is >> intread;
        ftemp.SetPass(intread);
        is >> strread;
        ftemp.SetStatus(strread);
        flist.push_back(ftemp);
    }
    
}

void Flights::Save(ostream &os){
    os << "F " << flist.size() << endl;

    for(int i=0; i < flist.size(); i++){
        os <<  flist[i].GetPlaneID() << endl;
        os <<  flist[i].GetPilotID() << endl;
        os <<  flist[i].GetCoPilotID() << endl;
        os << flist[i].GetCrewID(0) << " " << flist[i].GetCrewID(1) << " " << flist[i].GetCrewID(2) << " "<< endl;
        os << flist[i].GetStartTime(0) << " " << flist[i].GetStartTime(1) << " " << flist[i].GetStartTime(2) << " " << flist[i].GetStartTime(3) << " " << flist[i].GetStartTime(4) << " " << flist[i].GetStartTime(5) << endl;
        os << flist[i].GetEndTime(0) << " " << flist[i].GetEndTime(1) << " " << flist[i].GetEndTime(2) << " " << flist[i].GetEndTime(3) << " " << flist[i].GetEndTime(4) << " " << flist[i].GetEndTime(5) << endl;
        os << flist[i].GetSac() << endl;
        os << flist[i].GetEac() << endl;
        os << flist[i].GetPass() << endl;
        os << flist[i].GetStatus() << endl;
    }
}

void Flights::ScheduleByMember(string tempID){
    for(int i=0; i< flist.size(); i++){
        if(flist[i].GetCrewID(0)==tempID || flist[i].GetCrewID(1)==tempID || flist[i].GetCrewID(2)==tempID || flist[i].GetPilotID()==tempID || flist[i].GetCoPilotID()==tempID){
            cout << "Assignment Schedule: " << endl;
            cout << i << "." << endl;
            cout << "Start time: ";
            flist[i].PrintStartTime();
            cout << "Start time: ";
            flist[i].PrintEndTime();
        }
    }
}

void Flights::ScheduleByPlane(string tempID){
    for(int i=0; i< flist.size(); i++){
         if(flist[i].GetPlaneID()==tempID){
            cout << "Assignment Schedule" << endl;
            cout << i << "." << endl;
            cout << "Start time: ";
            flist[i].PrintStartTime();
            cout << "Start time: ";
            flist[i].PrintEndTime();
        }
    }
}
