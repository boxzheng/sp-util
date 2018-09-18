package com.sit.auth;

import com.sit.client.DbClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private DbClient dbClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String,Object> userMap = dbClient.getUserInfo(username);

        if (userMap == null)
        {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        else
        {
            logger.info("##################  loadUserByUsername " + (String)userMap.get("account") + "  " +
                    (String)userMap.get("password") + " " + ((List<String>)userMap.get("roles")).get(0));
            return JwtUserFactory.create(userMap);
        }
    }
}
