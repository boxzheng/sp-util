package com.sit.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Map<String,Object> umap) {
        List<String> tlist = (List<String>)umap.get("roles");
        List<String> tmplist = new ArrayList<String>();
        for(String item:tlist)
        {
            tmplist.add("role_" + item);
        }
        return new JwtUser(
                (int)umap.get("id"),
                (String)umap.get("account"),
                (String)umap.get("password"),
                (String)umap.get("tel"),
                mapToGrantedAuthorities(tmplist)
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
