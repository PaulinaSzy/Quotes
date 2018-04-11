package models;

import play.mvc.PathBindable;
import play.libs.F;
import java.util.*;
import play.data.format.*;
import play.data.validation.Constraints;
import javax.persistence.*;
import io.ebean.*;

@Entity
public class Quote extends Model implements PathBindable<Quote> {
    // persistence
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    // validation
    @Constraints.Required
    @Constraints.MaxLength(100)
    public String content;

    public String author;

    @Constraints.Min(0)
    @Constraints.Max(5)
    public int rate;
    
    public Quote(Long idNum, String con, String aut) {
        id = idNum;
        content = con;
        author = aut;
    }
    
    public Quote() {
        content = null;
        id = null;
        author = null;
    }

    public void addRating(){
        rate++;
    }

    public void subRating(){
        rate--;
    }
    
    // supporting methods
    public String toString() {
        return String.format("%d (%s) (rate=%d)", id, content, rate);
    }
    

    // implementation of Pathbinding
    @Override
    public Quote bind(String key, String value) {
        return findById(Long.parseLong(value));
    }
    
    @Override
    public String unbind(String key) {
        return "" + this.id;
    }
    
    @Override
    public String javascriptUnbind() {
        return "" + this.id;
    }


    // implementation of data access
    public static Finder<Integer, Quote> find = new Finder<>(Quote.class);

    public static List<Quote> findAll() {
        return find.all();
    }


    public static Quote findById(Long id) {
        return find.query().where().eq("id", id).findOne();
    }

    public static void remove(Quote quote) {
        quote.delete();
    }
    

    // internal validator
    public static class ContentValidator
      extends Constraints.Validator<String> {
          @Override
          public boolean isValid(String content) {

              return content != null && content.length()<100;
          }
          
          @Override
          public F.Tuple<String, Object[]> getErrorMessageKey() {
              return new F.Tuple<String,Object[]>("error.invalid.content", new Object[]{});
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
    
    
