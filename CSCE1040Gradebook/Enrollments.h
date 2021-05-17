#ifndef Enrollments_h
#define Enrollments_h

class Enrollments{
    private:
        Enrollment* enrollment_list;
        int numenroll;
    public:
        Enrollments();
        ~Enrollments();
        
        void addEnrollment();
        void addGrades();
        void prtStudGrades();
        int calcStudAvg();
        int calcCourseAvg();
    
        int findEnd(int j);
    
        void write();
        void read();
};

#endif /* Enrollments_h */
