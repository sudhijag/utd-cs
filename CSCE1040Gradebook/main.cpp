#include "Student.h"
#include "Course.h"
#include "Enrollment.h"
#include "Students.h"
#include "Courses.h"
#include "Enrollments.h"

void printmenu();
void write();
void read();

int main() {
    Students s;
    Courses c;
    Enrollments e;
    
    bool cont = false;//controls do-while loop
    char contchoice;//y or n
    
    int choice=0;
    
    do{
        printmenu();
        cout << endl << "Select an option: ";
        cin >> choice;
    
        switch(choice){
            case 1:
                cout << "OK, adding a new course." << endl;
                c.addCourse();
                break;
            case 2:
                cout << "OK, adding a new student" << endl;
                s.addStudent();
                break;
            case 3:
                cout << "OK, adding a new enrollment" << endl;
                e.addEnrollment();
                break;
            case 4:
                cout << "OK, adding grades." << endl;
                e.addGrades();
                break;
                
            case 5:
                e.prtStudGrades();
                break;
            case 6:
                
                break;
                
            case 7:
                e.calcCourseAvg();
                break;
                
            case 8:
                c.printCourses();
                break;
                
            case 9:
                s.printStudents();
                break;
                
            case 10:
                e.calcCourseAvg();
                break;
            case 11:
                e.write();
                break;
            case 12:
                e.read();
                break;
            default:
                cout << "Not a valid menu choice. Ending program." << endl;
                exit(EXIT_FAILURE);
         }
        cout << endl << "Action performed. Continue?(y/n) " << endl;
        cin >>contchoice;
        
        if(contchoice == 'n' || contchoice == 'N'){
            cout << endl << "Exiting program" << endl;
            cont=false;
        }
        else {cont=true;}
    }while(cont==true);

    return 0;
}

void printmenu(){
    cout << "Welcome to the gradebook menu." << endl;
    cout << "1) Add a new course " << endl;
    cout << "2) Add a new student " << endl;
    cout << "3) Add a student to a course (add enrollment)" << endl;
    cout << "4) Add grades for a student in a course" << endl;
    cout << "5) Print a list of all grades for a student in a course" << endl;
    cout << "6) Print a list of all students in a course" << endl;
    cout << "7) Compute the average for a student in a course" << endl;
    cout << "8) Print a list of all courses" << endl;
    cout << "9) Print a list of all students" << endl;
    cout << "10) Compute the average for a course" << endl;
    cout << "11) Store Grade book (to a disk file)" << endl;
    cout << "12) Load Grade book (from a disk file)" << endl;
    cout << "??) Type any other number or character (i.e. q or 13) to quit/exit" << endl;
}

void read(){

}

void write(){
}
