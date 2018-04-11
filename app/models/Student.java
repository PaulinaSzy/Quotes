package models;

import play.mvc.PathBindable;
import play.libs.F;
import java.util.*;
import play.data.format.*;
import play.data.validation.Constraints;
import javax.persistence.*;
import io.ebean.*;

@Entity
public class Student extends Model implements PathBindable<Student> {
    // persistence
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    // validation
    @Constraints.Required
    @Constraints.ValidateWith(value=NameValidator.class,message="Start with big letter")
    public String name;

    @Formats.DateTime(pattern="dd-MM-yyyy")
    public Date birthDate;

    @Constraints.Required
    public Long studId;
    
    public byte[] picture;
    
    public Student(String n, Long si) {
        name = n;
        studId = si;
    }
    
    public Student() {
        name = null;
        id = null;
    }
    
    // supporting methods
    public String toString() {
        return String.format("%s (%d) (id=%d)", name, studId, id);
    }
    

    // implementation of Pathbinding
    @Override
    public Student bind(String key, String value) {
        return findByStudId(Long.parseLong(value));
    }
    
    @Override
    public String unbind(String key) {
        return "" + this.studId;
    }
    
    @Override
    public String javascriptUnbind() {
        return "" + this.studId;
    }


    // implementation of data access
    public static List<Student> findAll() {
        return find.all();
    }

    public static Finder<Long, Student> find = new Finder<>(Student.class);
    
    public static Student findByStudId(Long studId) {
        return find.query().where().eq("studId", studId).findOne();
    }

    public static void remove(Student stud) {
        stud.delete();
    }
    

    // internal validator
    public static class NameValidator
      extends Constraints.Validator<String> {
          @Override
          public boolean isValid(String name) {
              String pattern="^[A-Z][a-z]*$";
              return name != null && name.matches(pattern);
          }
          
          @Override
          public F.Tuple<String, Object[]> getErrorMessageKey() {
              return new F.Tuple<String,Object[]>("error.invalid.name", new Object[]{});
          }
    }

/*
    other validators
    @Required
    @Min / @Max, np. @Min(3)
    @MinLenght / @MaxLenght
    @Pattern
    @ValidateWidth
    @Email
*/
}
    
    
