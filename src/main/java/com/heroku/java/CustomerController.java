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
            String sql = "INSERT INTO customer (name, address, email, password) VALUES (?,?,?,?)";
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
            String sql = "SELECT name, email, password FROM customer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            String returnPage = "";

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                if (name.equals(customer.getName()) && email.equals(customer.getEmail()) && password.equals(customer.getPassword())) {
                    session.setAttribute("name", customer.getName());
                    session.setAttribute("email", customer.getEmail());
                    returnPage = "redirect:/homePage";
                    break;
                } else {
                    returnPage = "/userlogin";
                }
            }

            connection.close();
            return returnPage;
        } catch (Throwable t) {
            t.printStackTrace();
            return "/userlogin";
        }
    }

    @PostMapping("/viewAccCust")
    public String viewAccCust(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");

        if (email != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT name, address, email, password FROM customer WHERE email=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String password = resultSet.getString("password");

                    System.out.println("name from db: " + name);
                    Customer viewAccCust = new Customer(name, address, email, password);
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
        String password = customer.getPassword();
        String name = customer.getName();
        String email = customer.getEmail();
        String address = customer.getAddress();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE customer SET name=?, address=?, email=?, password=? WHERE email=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, email);

            statement.executeUpdate();

            return "viewAccCust";
        } catch (Throwable t) {
            t.printStackTrace();
            return "/userlogin";
        }
    }

    @PostMapping("/deleteAccCust")
    public String deleteAccCust(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");

        if (email != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM customer WHERE email=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    session.invalidate();
                    return "redirect:/userlogin";
                } else {
                    System.out.println("Delete failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "deleteError";
            }
        }

        return "deleteError";
    }
}
