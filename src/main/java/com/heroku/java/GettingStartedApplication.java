package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }
    
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    //@GetMapping("adminHomePage")
    //public String adminHomePage() {
        //return "adminHomePage";
    //}

    @GetMapping("/indexAdmin")
    public String indexAdmin() {
        return "indexAdmin";
    }


    //@GetMapping("createAccAdmin")
    //public String createAccAdmin() {
       // return "createAccAdmin";
    //}

    @GetMapping("updateAccAdmin")
    public String updateAccAdmin() {
        return "updateAccAdmin";
    }

    //@GetMapping("viewAccAdmin")
   // public String viewAccAdmin() {
//return "viewAccAdmin";
    //}

    @GetMapping("createNewAcc")
    public String createNewAcc() {
        return "createNewAcc";
    }

    @GetMapping("/userlogin")
    public String userlogin() {
        return "userlogin";
    }


    @GetMapping("/adminlogin")
    public String adminlogin() {
        return "adminlogin";
    }

    @GetMapping("LoginOption")
    public String LoginOption() {
        return "LoginOption";
    }

    @GetMapping("updateAcc")
    public String updateAcc() {
        return "updateAcc";
    }

    @GetMapping("createAppointment")
    public String createAppointment() {
       return "createAppointment";
    }

    @GetMapping("adminQR")
    public String adminQR() {
        return "adminQR";
    }

    @GetMapping("AdminViewOrder")
    public String AdminViewOrder() {
        return "AdminViewOrder";
    }


    @GetMapping("createQR")
    public String createQR() {
        return "createQR";
    }

    @GetMapping("custPayment")
    public String custPayment() {
        return "custPayment";
    }

    @GetMapping("custViewOrder")
    public String custViewOrder() {
        return "custViewOrder";
    }

    @GetMapping("makePayment")
    public String makePayment() {
        return "makePayment";
    }

    @GetMapping("deleteMenu")
    public String deleteMenu() {
        return "deleteMenu";
    }




    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    
    
}
  }
