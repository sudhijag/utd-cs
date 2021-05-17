#ifndef Students_h
#define Students_h

class Students{
    public:
        void addStudent();
        void printStudents();
        Students();
        ~Students();
        int numstud;
    private:
        Student* student_list;
};

#endif /* Students_h */
