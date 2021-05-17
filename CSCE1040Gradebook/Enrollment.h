#ifndef Enrollment_h
#define Enrollment_h

class Enrollment{
    public:
        void setID(int n){ID=n;}
        void setCourseID(int n){courseID=n;}
        void setStudID(int n){studID=n;}
        void setAvg(int n){studID=n;}
        void setGrades(int index, int value){grades[index]=value;}
    
        int getID(){return ID;}
        int getStudID(){return studID;}
        int getCourseID(){return courseID;}
        int getGrades(int i){return grades[i];}
    
        int findEnd();
    
        Enrollment();
        ~Enrollment();
    private:
        int ID;
        int courseID;
        int studID;
        int grades[10];
        int avg;
        char letteravg;
};


#endif /* Enrollment_h */
