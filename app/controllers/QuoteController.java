package controllers;

import models.Quote;
import views.html.*;

import play.*;
import play.data.*;
import play.mvc.*;
import static play.mvc.Http.MultipartFormData;

import java.util.*;
import java.io.*;
import com.google.common.io.Files;

import javax.inject.Inject;

public class QuoteController extends Controller {
    @Inject FormFactory formFactory;

    public static class Login {

        public String email;
        public String password;

    }

    public Result list() {
        List<Quote> quotes = Quote.findAll();
        return ok(quotesList.render(quotes));
    }
//
//    public Result login() {
//        return ok(
//                login.render(form(Login.class))
//        );
//    }
//
//    public static Result authenticate() {
//        Form<Login> loginForm = form(Login.class).bindFromRequest();
//        return ok();
//    }

    public Result info() {
        return ok(info.render());
    }

    public Result about() {
        return ok(about.render());
    }

    public Result presentation() {
        return ok(presentation.render());
    }

    public Result addRating(Long id){
        final Quote quote = Quote.findById(id);
        if(quote == null) {
            return badRequest(String.format("Quote with %d does not exists.", id));
        }
        quote.rate ++;
        quote.update();
        return redirect(routes.QuoteController.list());
    }

    public Result subRating(Long id){
        final Quote quote = Quote.findById(id);
        if(quote == null) {
            return badRequest(String.format("Quote with %d does not exists.", id));
        }
        quote.rate --;
        quote.update();
        return redirect(routes.QuoteController.list());
    }

    public Result newQuote() {
        return ok(editQuote.render(formFactory.form(Quote.class).bindFromRequest()));
    }

    public Result details(Quote quote) {
        Form<Quote> filledForm = formFactory.form(Quote.class).fill(quote);
        return ok(editQuote.render(filledForm));
    }

    public Result delete(Long id) {
        final Quote quote = Quote.findById(id);
        if(quote == null) {
            return badRequest(String.format("Quote with %d does not exists.", id));
        }
        quote.delete();
        return redirect(routes.QuoteController.list());
    }

    public Result save() {
        Form<Quote> quoteForm = formFactory.form(Quote.class).bindFromRequest();
        if(quoteForm.hasErrors()){
            flash("error", "Please correct the form below");
            return badRequest(editQuote.render(quoteForm));
        }

        Quote quote = quoteForm.get();

        if(quote.id == null) {
            quote.save();
        } else {
            quote.update();
        }

        flash("success", String.format("Successfully added quote: %s", quote));
        return redirect(routes.QuoteController.list());
    }

}