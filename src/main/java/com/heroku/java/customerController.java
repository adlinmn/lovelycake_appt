package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;

@Controller
public class CustomerController {
    private final DataSource dataSource;

    @Autowired
    public CustomerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    // @GetMapping("/userlogin")
    // public String userlogin() {
    //     return "userlogin";
    // }

    // @GetMapping("/createAccCust")
    // public String createAccCust() {
    //     return "createAccCust";
    // }

    //insert cust into database
    @PostMapping("/createAccCust")
    public String addAccount(HttpSession session, @ModelAttribute("createAccCust")Customer customer) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO customer (name, address, email, password) VALUES (?,?,?,?)";
      final var statement = connection.prepareStatement(sql);

      statement.setString(1, customer.getFullname());
      statement.setString(2, customer.getAddress());
      statement.setString(3, customer.getEmail());
      statement.setString(4, customer.getPassword());

      statement.executeUpdate();

      connection.close();
      return "redirect:/userlogin";

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/";
    }

  }
  @PostMapping("/userlogin") 
    public String homePage(HttpSession session, @ModelAttribute("userlogin") Customer customer, Model model) { 
        try (
            Connection connection = dataSource.getConnection()) { 
            final var statement = connection.createStatement(); 
            String sql ="SELECT name, email, password FROM customer"; 
            final var resultSet = statement.executeQuery(sql); 
 
            String returnPage = ""; 
 
            while (resultSet.next()) { 
                String name = resultSet.getString("name"); 
                String email = resultSet.getString("email"); 
                String password = resultSet.getString("password");  
 
                if (name.equals(customer.getName()) && email.equals(customer.getEmail()) && password.equals(customer.getPassword())) { 
                    session.setAttribute("name",customer.getName());
                    session.setAttribute("email",customer.getEmail());
                    returnPage = "redirect:/homePage"; 
                    break; 
                } else { 
                    returnPage = "/userlogin"; 
                } 
            } 
            connection.close();
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            return "/userlogin"; 
        } 
 
    }
        @PostMapping("/viewAccCust")
        public String viewAccCust(HttpSession session, Customer customer, Model model) {
        String email = (String) session.getAttribute("email");

        if (email != null) { 
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.prepareStatement("SELECT name, address, email, password FROM customer WHERE email= ? ");
            statement.setString(1, email);
            final var resultSet = statement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");

                System.out.println("name from db: " + name);
                Customer viewAccCust = new Customer(name, address, email, password);
                model.addAttribute("viewAccCust", viewAccCust);
                System.out.println("Session viewAccCust : " + model.getAttribute("viewAccCust"));
                // Return the view name for displaying customer details
            }   
                return "viewAccCust";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        // Customer not found or username is null, handle accordingly (e.g., redirect to an error page)
        return "error";
        }

        //Update Profile Customer
        @PostMapping("/updateAcc") 
        public String updateAcc(HttpSession session, @ModelAttribute("updateAcc") Customer customer, Model model) { 
            String password = customer.getPassword();
            String name = customer.getName();
            String email = customer.getEmail();
            String address =  customer.getAddress();
            try (
            Connection connection = dataSource.getConnection()) { 
            String sql = "UPDATE customer SET name=? ,address=?, email=?, password=? WHERE email=?";
            final var statement = connection.prepareStatement(sql);
            // String name = customer.getName();
            // String address = customer.getAddress();
            // String email = customer.getEmail();
            // String password = customer.getPassword();

            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, email);
            statement.setString(4, password);

            statement.executeUpdate();
                
            String returnPage = "viewAccCust"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "/userlogin"; 
        } 
 
    }
    //delete controller
    @PostMapping("/viewAccCust")
    public String viewAccCust(HttpSession session, Model model) {
    String email = (String) session.getAttribute("email");

    if (email != null) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.prepareStatement("DELETE FROM customer WHERE email=?");
            statement.setString(1, email);

            // Execute the delete statement
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Profile deleted successfully
                // You can redirect to a success page or perform any other desired actions
                return "userlogin";
            } else {
                // Profile not found or deletion failed
                // You can redirect to an error page or perform any other desired actions
                // return "deleteError";
                System.out.println("delete fail");
            }
        } catch (SQLException e) {
            // Handle any potential exceptions (e.g., log the error, display an error page)
            e.printStackTrace();
            return "deleteError";
        }
    }

    // Username is null, handle accordingly (e.g., redirect to an error page)
    return "deleteError";
}

}
