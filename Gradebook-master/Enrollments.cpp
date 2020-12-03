#include "Enrollment.h"
#include "Enrollments.h"

#include <iostream>
#include <fstream>

using namespace std;

Enrollments::Enrollments(){
    numenroll= 5;
    enrollment_list= new Enrollment[numenroll];
}

Enrollments::~Enrollments(){
    cout << "Deleting dynamic enrollments array..." << endl;
    delete [] enrollment_list;
}

void Enrollments::addEnrollment(){
    //MENU ITEM 3
    int tempID, tempStudID, tempCourseID;

    Enrollment *temp;
    temp = new Enrollment[numenroll];
    for (int i = 0;i < numenroll;i++){
        temp[i] = enrollment_list[i];
    }
    
    delete [] enrollment_list;
    temp=enrollment_list;
    
    cout << "Enter new enrollment id: ";
    cin >> tempID;
    cout << "Enter student ID: ";
    cin >> tempStudID;
    cout << "Enter course ID:" ;
    cin >> tempCourseID;

    enrollment_list[numenroll].setID(tempID);
    enrollment_list[numenroll].setCourseID(tempCourseID);
    enrollment_list[numenroll].setStudID(tempStudID);
    numenroll += 1;
}

void Enrollments::addGrades(){
    //MENU ITEM 4
    int tempID;
    int blank[10]={-1};
    
    cout << "Enter enrollment id: ";
    cin >> tempID;
    
    int i=0;
    int fails=0;
    
    for(int i=0; i<numenroll; i++){
        if(tempID == enrollment_list[i].getID()){
            cout << "Found him @ i= " << i << endl;
            break;
        }
        else{
            fails++;
        }
    }
    
    if(fails == numenroll){
        cout <<"That's not a valid enrollment ID! " << endl;
        exit(EXIT_FAILURE);
    }
    
    //TODO: print existing grades?
    cout << "Enter grades for this student, seperated by returns. Enter no more than 10 grades. To stop entering grades, enter a -1." << endl;
    bool go=true;
    while(go){
        cin >> blank[i];
        if(blank[i]==-1){go=false;}
        i++;
    }
    
    cout << "The grades in the gradebook right now: " << endl;
    for(int j=0; j < 10; j++){
        cout << blank[j] << " ";//Echoes the entered grades.
    }
    
    cout << "i= " << i << endl;

    for(int j=0; j<10; j++){
        enrollment_list[i].setGrades(j, blank[j]);//Copies the values from blank to enrollment_list
    }
    
    for(int j=0; j< 10; j++){
        cout << endl << enrollment_list[i].getGrades(i) << " ";//Echoes the entered grades
    }

}

void Enrollments::prtStudGrades(){
    //MENU ITEM #5
    int tempID=0;
    int hits[20];
    
    int fails=0;
    int i=0;
    
    for(i=0; i<numenroll; i++){
        if(tempID == enrollment_list[i].getID()){
            cout << "Found him @ i= " << i << endl;
            break;
        }
        else{
            fails++;
        }
    }
    
    if(fails == numenroll){
        cout <<"That's not a valid enrollment ID! " << endl;
        exit(EXIT_FAILURE);
    }
    
    for(int j=0; j< findEnd(i); j++){
        //cout <<
    }
}

int Enrollments::calcStudAvg(){
    int tempID;
    int i=0;
    
    cout << "Enter enrollment ID: ";
    cin >> tempID;
    
    for(i; i<numenroll; i++){
        if(tempID == enrollment_list[i].getID()){
            cout << "Found him @ i= " << i << endl;
            break;
        }
        else{
            cout <<"That's not a valid enrollment ID! " << endl;
        }
    }
    
    int average=0;
    
    enrollment_list[i].findEnd();
    
    for(int j=0; j< i; j++){
        average += enrollment_list[i].getGrades(i);
    }
    
    average /= i;
    enrollment_list[i].setAvg(average);
    
    return average;
}

int Enrollments::calcCourseAvg(){
    int tempCourseID;
    int i=0;
    int average=0;
    
    int hits[20];
    
    cout << "Enter course ID: " << endl;
    cin >> tempCourseID;
    
    for(i; i<numenroll; i++){
        if(tempCourseID == enrollment_list[i].getCourseID()){
            cout << "Found him @ i= " << i << endl;
            
            break;
        }
        else{
            cout <<"That's not a valid enrollment ID! " << endl;
        }
    }
    
    return average;
    //Compute the average of the
}

int Enrollments::findEnd(int j){
    int fails=0;
    for(int i=0; i< numenroll; i++){
        if(enrollment_list[j].getGrades(i)==-1){
            return j;
        }
        else{
            fails++;
        }
    }
    if(fails==numenroll){
        return numenroll;
    }
    return j;
}

void Enrollments::write(){
    ofstream off;
    char filename[30];
    
    cout << "Write to which file? : ";
    cin >> filename;
    off.open(filename);
    
    if(off.fail()){
        cout << "Failed to write.";
        exit(EXIT_FAILURE);
    }
    
    for(int i=0; i< numenroll; i++){
        off << enrollment_list[i].getGrades(i); //Format to write to file
    }
}

void Enrollments::read(){
    ifstream inf;
    char filename[30];
    
    cout << "Read from which file? : " ;
    cin >> filename;
    inf.open(filename);
    if(inf.fail()){
        cout << "Gradebook file failed to open.";
        exit(EXIT_FAILURE);
    }
}
    
    
