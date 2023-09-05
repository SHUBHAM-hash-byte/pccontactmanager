package com.pccontactmanager.controller;


import com.pccontactmanager.dao.UserRepo;
import com.pccontactmanager.entity.User;
import com.pccontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class PcContactController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
  private UserRepo userRepo;
    @RequestMapping("/")
    public  String home(Model model)
    {
        model.addAttribute("title","Home - contact manager");
        return "home";
    }
    @RequestMapping("/about")
    public  String about(Model model)
    {
        model.addAttribute("title","About - contact manager");

        return "about";
    }

    @RequestMapping("/signup")
    public  String signup(Model model)
    {

        model.addAttribute("title","signup - contact manager");
        model.addAttribute("user",new User());
        return "signup";
    }

@RequestMapping(value = "/do_register", method = RequestMethod.POST)
public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bindingResult, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
    try {
        if (!agreement) {
            System.out.println("Please accept the terms and conditions");
            throw new Exception("Please accept the terms and conditions");
        }
        if(bindingResult.hasErrors())
        {
            System.out.println("Error"+bindingResult.toString());
            model.addAttribute("user",user);
            return "signup";
        }
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setImage("default.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        System.out.println(agreement);
        System.out.println(user);
        User save = this.userRepo.save(user);

        model.addAttribute("user", save);
        model.addAttribute("newUser", new User()); // Add a new user for resetting the form

        session.setAttribute("message", new Message("Successfully Registered ", "alert-error"));
        return "signup";
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("user", user);
        session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-error"));
        return "signup";
    }
}


}
