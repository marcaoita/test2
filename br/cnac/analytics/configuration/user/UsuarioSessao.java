package br.cnac.analytics.configuration.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSessao {

    /**
     * @return Retorna o nome da função de aplicativo em que o usuário está alocado.
     */
    public static String getAutoridade() {

        List<GrantedAuthority> autoridade = new ArrayList<>(new ArrayList<>(SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()));

        return autoridade.get(0).getAuthority();
    }

    /**
     * @return Retorna o nome do usuário na sessão.
     */
    public static String nomeUsuario() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
