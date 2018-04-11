package controllers;

import models.Student;
import views.html.*;

import play.*;
import play.data.*;
import play.mvc.*;
import static play.mvc.Http.MultipartFormData;

import java.util.*;
import java.io.*;
import com.google.common.io.Files;

import javax.inject.Inject;

public class Students extends Controller {
    @Inject FormFactory formFactory;

    public Result list() {
        List<Student> students = Student.findAll();
        return ok(list.render(students));
    }
    
    public Result newStudent() {
        return ok(details.render(formFactory.form(Student.class).bindFromRequest()));
    }

    public Result details(Student student) {
        Form<Student> filledForm = formFactory.form(Student.class).fill(student);
        return ok(details.render(filledForm));
    }

    public Result delete(Long studId) {
        final Student student = Student.findByStudId(studId);
        if(student == null) {
            return badRequest(String.format("Student with %d does not exists.", studId));
        }
        student.delete();
        return redirect(routes.Students.list());
    }
    
    public Result save() {
        Form<Student> studentForm = formFactory.form(Student.class).bindFromRequest();
        if(studentForm.hasErrors()){
            flash("error", "Please correct the form below");
            return badRequest(details.render(studentForm));
        }

        Student student = studentForm.get();
     
        MultipartFormData body = request().body().asMultipartFormData();
        MultipartFormData.FilePart part = body.getFile("picture");
        
        if(part != null) {
            File picture = (File)part.getFile();
            try {
                student.picture = Files.toByteArray(picture);
            } catch(IOException e) {
                return internalServerError("Error reading file upload");
            }
        }

        if(student.id == null) {
            student.save();
        } else {
            student.update();
        }

        flash("success", String.format("Successfully added student: %s", student));
        return redirect(routes.Students.list());
    }
    
    public Result picture(Long studId) {
        final Student student = Student.findByStudId(studId);
        if(student == null) return notFound();
        return ok(student.picture);
    }
}