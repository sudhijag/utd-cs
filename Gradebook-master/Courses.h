#ifndef Courses_h
#define Courses_h


class Courses{
    private:
        Course* course_list;
        int numcourse;
    public:
        void addCourse();
        Courses();
        ~Courses();
        void printCourses();
};

#endif /* Courses_h */
