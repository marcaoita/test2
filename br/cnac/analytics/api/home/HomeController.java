package br.cnac.analytics.api.home;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("SameReturnValue")
@Controller
public class HomeController {

    @RequestMapping("/")
    public String inicialPage(Model model) {

        model.addAttribute("nome", nomeUsuario());

        return "home/index";
    }

    private String nomeUsuario() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping("/semAcesso")
    public String semAcesso() {

        return "utils/sem-acesso";
    }

}
