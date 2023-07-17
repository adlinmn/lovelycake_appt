package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class CustomerController {
    private final DataSource dataSource;

    @Autowired
    public CustomerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/createAccCust")
    public String addAccount(HttpSession session, @ModelAttribute("createAccCust") Customer customer) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO customer (custname, custaddress, custemail, custpassword) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPassword());

            statement.executeUpdate();

            connection.close();
            return "redirect:/userlogin";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
    
    @GetMapping("/createAccCust")
    public String addCustomer(HttpSession session,Customer customer,Model model){
        return "createAccCust";
    }


    @PostMapping("/userlogin")
    public String homePage(HttpSession session, @ModelAttribute("userlogin") Customer customer, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT custname, custemail, custpassword FROM customer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            String returnPage = "";

            while (resultSet.next()) {
                String custname = resultSet.getString("custname");
                String custemail = resultSet.getString("custemail");
                String custpassword = resultSet.getString("custpassword");

                if (custname.equals(customer.getName()) && custemail.equals(customer.getEmail()) && custpassword.equals(customer.getPassword())) {
                   session.setAttribute("custname", customer.getName());
                    session.setAttribute("custemail", customer.getEmail());
                    returnPage = "redirect:/homelogin";
                    break;
                } else {
                    returnPage = "redirect:/userlogin";
                }
            }

            connection.close();
            return returnPage;
        } catch (Throwable t) {
            t.printStackTrace();
            return "userlogin";
        }
    }

    @GetMapping("/homelogin")
    public String homelogin(){
        return "homelogin";
    }


    @GetMapping("/viewAccCust")
    public String viewAccCustomer(HttpSession session, Model model) {
    String custemail = (String) session.getAttribute("custemail");

    if (custemail != null) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT custname, custaddress, custemail, custpassword FROM customer WHERE custemail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, custemail);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String custname = resultSet.getString("custname");
                String custaddress = resultSet.getString("custaddress");
                String custpassword = resultSet.getString("custpassword");

                Customer viewAccCust = new Customer(custname, custaddress, custemail, custpassword);
                model.addAttribute("viewAccCust", viewAccCust);
                return "viewAccCust";
            } else {
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return "error";
}

    @PostMapping("/viewAccCust")
    public String viewAccCust(HttpSession session, Model model) {
        String custemail = (String) session.getAttribute("custemail");

        if (custemail != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT custname, custaddress, custemail, custpassword FROM customer WHERE custemail=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, custemail);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String custname = resultSet.getString("custname");
                    String custaddress = resultSet.getString("custaddress");
                    String custpassword = resultSet.getString("custpassword");

                    System.out.println("name from db: " + custname);
                    Customer viewAccCust = new Customer(custname, custaddress, custemail, custpassword);
                    model.addAttribute("viewAccCust", viewAccCust);
                    System.out.println("Session viewAccCust: " + model.getAttribute("viewAccCust"));
                    return "viewAccCust";
                } else {
                    return "error";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "error";
    }

    @PostMapping("/updateAcc")
public String updateAcc(HttpSession session, @ModelAttribute("updateAcc") Customer customer, Model model) {

    try (Connection connection = dataSource.getConnection()) {
        String sql = "UPDATE customer SET custname=?, custaddress=?, custpassword=? WHERE custemail=?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, customer.getName());
        statement.setString(2, customer.getAddress());
        statement.setString(3, customer.getPassword());
        statement.setString(4, (String) session.getAttribute("custemail"));

        int rowsUpdated = statement.executeUpdate();

        if (rowsUpdated > 0) {
            // Update successful
            return "redirect:/viewAccCust";
        } else {
            // Update failed
            model.addAttribute("error", "Failed to update account");
            return "error";
        }
    } catch (SQLException e) {
        e.printStackTrace();
        model.addAttribute("error", "Failed to update account");
        return "error";
    }
}


    
@PostMapping("/deleteAccCust")
public String deleteAccCust(HttpSession session, Model model) {
    String custemail = (String) session.getAttribute("custemail");

    if (custemail != null) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM customer WHERE custemail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, custemail);

            statement.executeUpdate(); // Use executeUpdate() instead of executeQuery()

            // Redirect to a success page or perform any necessary actions
            return "redirect:/userlogin";
        } catch (SQLException e) {
            e.printStackTrace();
            return "deleteError";
        }
    }

    return "deleteError";
}
}

