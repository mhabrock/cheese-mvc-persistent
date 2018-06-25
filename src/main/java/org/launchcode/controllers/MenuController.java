package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
//all crated in p3
@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title","Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add New Menu");
        model.addAttribute(new Menu());
        return "menu/add";

    }
//saving
    @RequestMapping(value="add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu newMenu, Model model, Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add New Menu");
            return "menu/add";
        }
        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();

    }
    //user can view menu
    @RequestMapping(value ="view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable int menuId, Model model){
        Menu menu= menuDao.findOne(menuId);
        model.addAttribute(menu);

        return "menu/view";

    }
    //crated in p3 AddMenuItemForm and additem
    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(@PathVariable int menuId, Model model){
        Menu menu= menuDao.findOne(menuId);
        model.addAttribute("form", new AddMenuItemForm(menu, cheeseDao.findAll()));
        model.addAttribute("title", "Add to Menu: " + menu.getName());
        return "menu/add-item";

    }
    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm form, Model model, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("form",form);
            return "menu/add-item";}

        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
        Menu theMenu = menuDao.findOne(form.getMenuId());
        theMenu.addItem(theCheese);
        menuDao.save(theMenu);

        return "redirect:/menu/view/" + theMenu.getId();

    }
}