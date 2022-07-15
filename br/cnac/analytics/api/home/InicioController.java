package br.cnac.analytics.api.home;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("inicio/")
public class InicioController {


    @GetMapping("/index")
    public String index(){
        return "home/inicio";
    }

}
