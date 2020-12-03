#include "Student.h"
#include "Students.h"

Students::Students(){
    numstud= 20;
    student_list= new Student[numstud];
}

void Students::printStudents(){
    cout << "NAME" << "\t \t" << "ID" << "\t \t" << "Classification" << endl;
    for(int i=0; i< numstud; i++){
        string name= student_list[i].getName();
        int ID= student_list[i].getID();
        string classif= student_list[i].getClassif();
        cout << name << "\t" << ID << "\t" << classif << endl;
    }
}

void Students::addStudent(){
    Student* temp;//Creating a pointer to a new dynamic array
    temp= new Student[numstud * 2];
    
    for(int i=0; i< numstud; i++){
        temp[i] = student_list[i];//Copying all the elements from the old array to our new one
    }
    delete [] student_list;//Deleting the info in the old array
    student_list=temp;//Redirecting the pointer to the old array to the new oen
    
    int tempID;
    string tempname;
    string tempclassif;
    float tempavg;
    
    cout << "Enter the new student's name: ";
    getline(cin, tempname);
    cout << "Enter the new student's ID: ";
    cin >> tempID;
    cout << "Enter the new student's classification: ";
    cin >> tempclassif;
    cout << "Enter the new student's average: ";
    cin >> tempavg;

    student_list[numstud].setName(tempname);
    cout << "Set name." << endl;
    student_list[numstud].setID(tempID);
    cout << "Set ID." << endl;
    student_list[numstud].setClassification(tempclassif);
    cout << "Set classification." << endl;
    
    numstud += 1;
}

Students::~Students(){
    cout << "Destroying dynamic student array..." << endl;
    delete [] student_list;
}
