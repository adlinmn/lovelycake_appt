package com.heroku.java.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.bean.Menu;
import com.heroku.java.order.dao.MenuDAO;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

@Controller
public class MenuController {
    private final MenuDAO menuDAO;

    @Autowired
    public MenuController(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @PostMapping("/createMenu")
    public String addMenu(HttpSession session, @ModelAttribute("menu") Menu menu) {
        menuDAO.addMenu(menu);
        return "redirect:/viewMenuAdmin";
    }

    @GetMapping("/createMenu")
    public String addMenu(HttpSession session, Menu menu, Model model) {
        return "createMenu";
    }

    @GetMapping("/viewMenuAdmin")
    public String viewMenuAdmin(HttpSession session, Model model) {
        ArrayList<Menu> menus = menuDAO.getAllMenus();
        model.addAttribute("menus", menus);
        return "viewMenuAdmin";
    }

    @GetMapping("/updateMenu")
    public String showUpdateForm(@RequestParam("menuId") String menuId, Model model) {
        Menu menu = menuDAO.getMenuById(menuId);
        model.addAttribute("menu", menu);
        return "updateMenu";
    }

    @PostMapping("/updateMenu")
    public String updateMenu(@ModelAttribute("menu") Menu menu) {
        menuDAO.updateMenu(menu);
        return "redirect:/viewMenuAdmin";
    }

    @PostMapping("/deleteMenu")
    public String deleteMenu(@RequestParam("menuId") String menuId) {
        menuDAO.deleteMenu(menuId);
        return "redirect:/viewMenuAdmin";
    }

    @GetMapping("/menu")
    public String displayPastryMenu(Model model) {
        ArrayList<Menu> menus = menuDAO.getAllMenus();
        model.addAttribute("menus", menus);
        return "menu";
    }


}
