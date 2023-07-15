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


@Controller
public class AdminController {
    private final DataSource dataSource;

    @Autowired
    public AdminController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/createAccAdmin")
    public String addAccAdmin(HttpSession session, @ModelAttribute("createAccAdmin") Admin admin) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO admin (admin_id, admin_email, admin_password) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, admin.getId());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());

            statement.executeUpdate();

            connection.close();
            return "redirect:/adminlogin";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
    
    @GetMapping("/createAccAdmin")
    public String addAdmin(HttpSession session, Admin admin, Model model) {
        return "createAccAdmin";
    }



    @PostMapping("/adminlogin")
public String adminlogin(HttpSession session, @ModelAttribute("adminlogin") Admin admin, Model model) {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT admin_id, admin_email, admin_password FROM admin WHERE admin_id=? AND admin_email=? AND admin_password=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, admin.getId());
        statement.setString(2, admin.getEmail());
        statement.setString(3, admin.getPassword());
        ResultSet resultSet = statement.executeQuery();

        String returnPage = "/adminlogin";

        if (resultSet.next()) {
            session.setAttribute("admin_id", admin.getId());
            session.setAttribute("admin_email", admin.getEmail());
            return "redirect:/loginHomeAdmin";
        }

        connection.close();
        return returnPage;
    } catch (Throwable t) {
        t.printStackTrace();
        return "/adminlogin";
    }
}

@GetMapping("/adminHomePage")
public String adminHomePage(HttpSession session, Model model) {
    String admin_id = (String) session.getAttribute("admin_id");

    if (admin_id != null) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT admin_id, admin_email, password FROM admin WHERE admin_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, admin_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String viewId = resultSet.getString("admin_id");
                String email = resultSet.getString("admin_email");
                String password = resultSet.getString("admin_password");

                Admin viewAccAdmin = new Admin(viewId, email, password);
                model.addAttribute("viewAccAdmin", viewAccAdmin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return "error";
}

@GetMapping("viewAccAdmin")
public String viewAccAdmin(Model model, HttpSession session) {
    String admin_id = (String) session.getAttribute("admin_id");

    if (admin_id != null) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT admin_id, admin_email, admin_password FROM admin WHERE admin_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, admin_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String viewEmail = resultSet.getString("admin_email");
                String password = resultSet.getString("admin_password");

                Admin viewAccAdmin = new Admin(admin_id, viewEmail, password);
                model.addAttribute("viewAccAdmin", viewAccAdmin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return "error";
}

@GetMapping("/loginHomeAdmin")
public String loginHomeAdmin(){
    return "loginHomeAdmin";
}




    @PostMapping("/viewAccAdmin")
    public String viewAccAdmin(HttpSession session, Model model) {
        String admin_id = (String) session.getAttribute("admin_id");

        if (admin_id != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT admin_id, admin_email, admin_password FROM admin WHERE admin_id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, admin_id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String admin_email = resultSet.getString("admin_email");
                    String admin_password = resultSet.getString("admin_password");

                    System.out.println("admin_id from db: " + admin_id);
                    Admin viewAccAdmin = new Admin(admin_id, admin_email, admin_password);
                    model.addAttribute("viewAccAdmin", viewAccAdmin);
                    System.out.println("Session viewAccAdmin: " + model.getAttribute("viewAccAdmin"));
                    return "viewAccAdmin";
                } else {
                    return "error";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "error";
    }

    @PostMapping("/updateAccAdmin")
    public String updateAccAdmin(HttpSession session, @ModelAttribute("updateAccAdmin") Admin admin, Model model) {

        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE admin SET admin_email=?, admin_password=? WHERE admin_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, admin.getEmail());
            statement.setString(2,admin.getPassword());
            statement.setString(3, (String) session.getAttribute("admin_id"));


            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                // Update successful
                return "redirect:/viewAccAdmin";
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

    @PostMapping("/deleteAccAdmin")
    public String deleteAccAdmin(HttpSession session, Model model) {
        String admin_id = (String) session.getAttribute("admin_id");

        if (admin_id != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM admin WHERE admin_id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, admin_id);

                statement.executeUpdate(); // Use executeUpdate() instead of executeQuery()

            // Redirect to a success page or perform any necessary actions
            return "redirect:/adminlogin";

            } catch (SQLException e) {
                e.printStackTrace();
                return "deleteError";
            }
        }

        return "deleteError";
    }
}
