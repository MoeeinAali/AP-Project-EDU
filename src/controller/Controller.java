package controller;

import model.Course;
import model.Student;
import model.Teacher;
import view.LoginMenu;
import view.MainMenu;
import view.StudentMenu;
import view.TeacherMenu;


public class Controller {
    private Student loggedInStudent;
    private Teacher loggedInTeacher;
    private final LoginMenu loginMenu;
    private final MainMenu mainMenu;
    private final TeacherMenu teacherMenu;
    private final StudentMenu studentMenu;


    //    Constructor
    public Controller() {
        loginMenu = new LoginMenu(this);
        mainMenu = new MainMenu(this);
        teacherMenu = new TeacherMenu(this);
        studentMenu = new StudentMenu(this);
    }

    //  Student or Teacher ?
    public boolean isLoggedInUserStudent() {
        if (loggedInStudent == null) {
            return false;
        } else {
            return true;
        }
    }

    //  Starts Controller from here
    public void run() {
//        works with loginMenu
        if (loginMenu.run().equals("exit")) {
            return;
        } else {
            while (true) {
//                fields of loginMenu
                switch (mainMenu.run()) {
//                  field1 : teacher menu
                    case "teacher menu":
                        teacherMenu.run();
                        break;
//                  field2: student menu
                    case "student menu":
                        studentMenu.run();
                        break;
//                  field3: logout
                    case "logout":
                        if (loginMenu.run().equals("exit")) {
                            return;
                        }
                        break;
                }
            }
        }

    }

    public String register(String username, String password, String role) {
        if (Teacher.getTeacherByUsername(username) != null || Student.getStudentByUsername(username) != null) {
            return "register failed: username already exists";
        }
        if (password.length() < 5 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*[0-9].*")) {
            return "register failed: password is weak";
        }
        if (role.equals("teacher")) {
            Teacher.addTeacher(username, password);
        } else if (role.equals("student")) {
            Student.addStudent(username, password);
        } else {
            return "register failed: invalid role!";
        }
        return "register successful";
    }

    public String login(String username, String password) {
        if ((loggedInTeacher = Teacher.getTeacherByUsername(username)) != null) {
            if (loggedInTeacher.isPasswordCorrect(password) != true) {
                loggedInTeacher = null;
                return "login failed: incorrect password!";
            }
            return "login successful";
        } else if ((loggedInStudent = Student.getStudentByUsername(username)) != null) {
            if (loggedInStudent.isPasswordCorrect(password) != true) {
                loggedInStudent = null;
                return "login failed: incorrect password!";
            }
            return "login successful";
        } else {
            return "login failed: user not found!";
        }
    }

    public String logout() {
        loggedInStudent = null;
        loggedInTeacher = null;
        return "logout successful";
    }

    public String addCourse(String name, int capacity) {
        loggedInTeacher.addCourse(name, capacity);
        return "course added successfully";
    }

    public String showMyCourses() {
        StringBuilder result = new StringBuilder();
        for (Course i : this.loggedInStudent.getCourses()) {
            result.append(i.toString());
            result.append("\n");
        }
        return result.toString();
    }

    public String showTeacherCourses() {
        StringBuilder result = new StringBuilder();
        for (Course i : loggedInTeacher.getCourses()) {
            result.append(i.toString());
            System.out.println("test");
//            result.append(" " + i.getSize() + "/" + i.getCapacity() + "\n");
        }
        return result.toString();
    }

    public String showAllCourses() {
        StringBuilder result = new StringBuilder();
        for (Teacher i : Teacher.getTeachers()) {
            for (Course j : i.getCourses()) {
                result.append(j.toString());
                result.append("\n");
            }
        }
        return result.toString();
    }

    public String takeCourse(String name) {
        Course course = Course.getCourseByName(name);
        if (course == null) {
            return "take course failed. Course not found!";
        }
        if (course.isFull()) {
            return "take course failed: full!";
        }
        if (loggedInStudent.getCourses().contains(course)) {
            return "take course failed: you've already taken this course!";
        }
        loggedInStudent.takeCourse(course);
        return "Course taken Successfully!";
    }


}

