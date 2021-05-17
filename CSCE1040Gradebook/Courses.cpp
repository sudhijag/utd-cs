#include "Course.h"
#include "Courses.h"

#include <iostream>
using namespace std;

Courses::Courses(){
    numcourse= 20;
    course_list= new Course[numcourse];
}

Courses::~Courses(){
    cout << "Deleting dynamic course array..." << endl;
    delete [] course_list;
}

void Courses::printCourses(){
    for(int i=0; i< numcourse; i++){
        string name= course_list[i].getName();
        int ID= course_list[i].getID();
        string location= course_list[i].getLocation();
        string time= course_list[i].getTime();
        cout << name << "\t" << ID << "\t" << location << "\t" << time << endl;
    }
}

void Courses::addCourse(){
    Course* temp;//Creating a pointer to a new dynamic array
    temp= new Course[numcourse * 2];
    
    for(int i=0; i< numcourse; i++){
        temp[i] = course_list[i];//Copying all the elements from the old array to our new one
    }
    delete [] course_list;
    course_list=temp;
    
    int tempID;
    string tempname;
    string templocation;
    string tempmt;
    
    cout << "Enter the new courses's name: ";
    cin >> tempname;
    cout << "Enter the new courses's ID: ";
    cin >> tempID;
    cout << "Enter the new course's location: ";
    cin >> templocation;
    cout << "Enter the new course's meeting time: ";
    cin >> tempmt;
    
    course_list[numcourse].setName(tempname);
    cout << "Set name." << endl;
    course_list[numcourse].setID(tempID);
    cout << "Set ID." << endl;
    course_list[numcourse].setLocation(templocation);
    cout << "Set location." << endl;
    course_list[numcourse].setTime(tempmt);
    
    numcourse += 1;
}
