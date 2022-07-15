package br.cnac.analytics.api.novidades;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("novidades/")
public class NovidadesController {

    @GetMapping("/saiba-mais")
    public String novidades() {

        return "utils/saiba-mais";
    }

}
