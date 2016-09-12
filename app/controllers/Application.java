package controllers;

import play.*;
import play.data.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(index.render("ResourceLocator Miami"));
    }

    public Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

    public Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            return redirect(
                    routes.Application.index()
            );
        }
    }

    public Result logout() {
        session().clear();
        return redirect(
                routes.Application.login()
        );
    }

    public static class Login {
        public String username;
        public String password;

        public String validate() {
            if (userAuthenticate(username, password) == null) {
                return "Invalid username or password";
            }
            return null;
        }
    }

    private static String userAuthenticate(String username, String password) {
        /*if ((username.toLowerCase().equals("switchprovider")) && (password.equals("switchprovider2015"))) {
            return "OK";
        }*/

        if ((username.toLowerCase().equals("admin")) && (password.equals("1234"))) {
            return "OK";
        }

        /*if ((username.toLowerCase().equals("helpline")) && (password.equals("miami211"))) {
            return "OK";
        }*/

        if ((username.toLowerCase().equals("resourcelocator")) && (password.equals("ResourceLocator2015"))) {
            return "OK";
        }

        return null;
    }

}
