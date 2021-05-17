/*
    Name: Sudarshana Jagadeeshi
    Email: sudarshanajagadeeshi@my.unt.edu
    Description: Manages a flight control program for Mean Greens
    Citation: Jagadeeshi, Sudarshana. "Homework 4". Software.
*/

#include "Flights.h"

void Load(Planes &p, Members &m, Flights &f, istream& is);
void Save(Planes &p, Members &m, Flights &f, ostream& os);
void printmenu();

int main(){
    Planes p;
    Members m;
    Flights f;
    
    int choice;
    bool cont=true;
    
    char ofile[30];
    char ifile[30];
    
    ofstream os;
    ifstream is;
    
    int sizes[3];//Reading variables
    int intread;
    string strread;
    string strreads[3];
    
    cout << "Department: TAMS \t Course Number: CSCE 1040.001 \tProgram Name: Mean Greens Airlines \nName: Sudarshana Jagadeeshi \t Email Address: sudarshanajagadeeshi@my.unt.edu" << endl;
    
    cout << endl << "********Welcome to Mean Greens Airlines************** ";   
    do{
        cout << endl << "1)Manage PLANES";
        cout << endl << "2)Manage CREW MEMBERS";
        cout << endl << "3)Manage FLIGHTS";
        cout << endl << "4)SAVE to an external file";
        cout << endl << "5)LOAD from an existing file";
        cout << endl << "?) QUIT (Enter any other key)" << endl;
        
        cout << "CHOICE: ";
        cin >> choice;
        
        switch(choice){
            case 1:
            //If the user wants to manage planes
                cout << endl << "1) Add a plane";
                cout << endl << "2) Delete a plane";
                cout << endl << "3) Edit a plane";
                cout << endl << "4) Print data a plane";
                cout << endl << "5) List all planes";
                cout << endl << "6) Search if a plane exists";
                cout << endl << "?) QUIT (Enter any other key)" << endl;
                
                cout << "CHOICE: ";
                cin >> choice;
                
                switch(choice){
                    case 1:
                        p.Add();
                        break;
                    case 2:
                        p.Delete();
                        break;
                    case 3:
                        p.Edit();
                        break;
                    case 4:
                        p.Print();
                        break;
                    case 5:
                        p.List();
                        break;
                    case 6:
                        cout << "Enter the tail number of the plane you wish to search for: " << endl;
                        cin >> strread;
                        p.Search(strread);
                        break;
                    default:
                        exit(-1);
                        break;
                }
                break;
            case 2:
            //If the user wants to manage crew members
                cout << endl << "1) Add a crew member";
                cout << endl << "2) Delete a crew member";
                cout << endl << "3) Edit a crew member";
                cout << endl << "4) Print data for a crew member";
                cout << endl << "5) List all crew members";
                cout << endl << "6) Search if a crew member exists";
                cout << endl << "?) QUIT (Enter any other key)" << endl;
                
                cout << "CHOICE: ";
                cin >> choice;
                
                switch(choice){
                    case 1:
                        m.Add();
                        break;
                    case 2:
                        m.Delete();
                        break;
                    case 3:
                        m.Edit();
                        break;
                    case 4:
                        m.Print();
                        break;
                    case 5:
                        m.List();
                        break;
                    case 6:
                        cout << "Enter the crew member ID of the member you wish to search for: " << endl;
                        cin >> strread;
                        m.Search(strread);
                    default:
                        exit(-1);
                        break;
                }
                break;
            case 3:
            //If the user wants to manage flights
                cout << endl << "1) Add a flight";
                cout << endl << "2) Delete a flight";
                cout << endl << "3) Edit a flight";
                cout << endl << "4) Print data for a flight";
                cout << endl << "5) List all flights";
                cout << endl << "6) Search if a flight exists";
                cout << endl << "7) Remove completed and cancelled flights";
                cout << endl << "8) Update status based on time/date";
                cout << endl << "9) Print assignment schedule for a particular plane";
                cout << endl << "10) Print assignment schedule for a specific crew member";
                cout << endl << "11) Print flights based on status";
                cout << endl << "?) QUIT (Enter any other key)" << endl;
                
                cout << "CHOICE: ";
                cin >> choice;
                
                switch(choice){
                    case 1:
                      f.Add(p,m);
                        
                        break;
                    case 2:
                        f.Delete();
                        break;
                    case 3:
                        f.Edit();
                        break;
                    case 4:
                        cout << "Enter the plane ID: " << endl;
                        cin >> strreads[0];
                        cout << "Enter the pilot ID" << endl;
                        cin >> strreads[1];
                        
                        f.Print(strreads[0], strreads[1]);
                        break;
                    case 5:
                        f.List();
                        break;
                    case 6:
                        cout << "Enter the plane ID: " << endl;
                        cin >> strreads[0];
                        cout << "Enter the pilot ID" << endl;
                        cin >> strreads[1];
                        
                        f.Search(strreads[0], strreads[1]);
                        break;
                    case 7:
                        f.Clean();
                        break;
                    case 8:
                        f.Update();
                        break;
                    case 9:
                        cout << "Enter the plane ID" << endl;
                        cin >> strread;
                        f.ScheduleByPlane(strread);
                        break;
                    case 10:
                        cout << "Enter the member ID" << endl;
                        cin >> strread;
                        f.ScheduleByMember(strread);
                        break;
                    case 11:
                        f.ListByStatus();
                        break;
                    default:
                        exit(-1);
                        break;
                }
                break;
            case 4:
            //If the user wants to SAVE to an external file
                cout << "Enter the name of the file you would like to load to" << endl;
                cin >> ofile;
                os.open(ofile);
                
                if(os.fail()){
                    cout << "ERROR: Could not create output file." << endl;
                    break;
                }
                
                Save(p,m,f,os);
                
                
                cout << "Finished Writing" << endl;
		break;                
            case 5:
                //If the user wants to read into the program
                
                cout << "Enter the name of the file you would like to read in from" << endl;
                cin >> ifile;
                is.open(ifile);
                
                if(is.fail()){
                    cout << "ERROR: Could not locate input file." << endl;
                    break;
                }
                
                Load(p,m,f,is);
                
                cout << "Finished Loading" << endl;
                
                break;
            default:
                //If the user types in anything else
                cout << "Okay, quitting program" << endl;
                exit(-1);
                break;
        }
    }while(cont);
    
    cout << endl;
    return 0;
    
}

void Load(Planes &p, Members &m, Flights &f, istream& is){
    p.Load(is);
    m.Load(is);
    f.Load(is);
}

void Save(Planes &p, Members &m, Flights &f, ostream& os){
    p.Save(os);
    m.Save(os);
    f.Save(os);
}

void printmenu(){

}
