package com.sit.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.sit.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhenglin on 2018/10/15.
 */
@Component
public class AccessFilter extends ZuulFilter {

    protected final Log logger = LogFactory.getLog(this.getClass());
    @Override
    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        System.out.println(String.format("%s AccessUserNameFilter request to %s", request.getMethod(), request.getRequestURL().toString()));

        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(username.equals("anonymousUser"))
        {
            logger.info("############## AccessFilter anonymousUser" );
            return null;
        }
        try
        {
            InputStream in = ctx.getRequest().getInputStream();
            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            if(StringUtils.isBlank(body))
            {
                body = "{}";
            }
            logger.info("############## AccessFilter body=" +  body);


            StringBuilder sb = new StringBuilder();
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities)
            {
                System.out.println("########## Authority " + grantedAuthority.getAuthority());
                String tmp = grantedAuthority.getAuthority();
                String[] strs = StringUtil.split(tmp, "_");
                if(strs.length == 2) {
                    sb.append(strs[1]);
                    sb.append("_");
                }
            }


            JSONObject jsonObject = JSON.parseObject(body);
            jsonObject.put("account", username);
            jsonObject.put("role", sb.toString());
            String newBody = jsonObject.toString();
            logger.info("############## AccessFilter newBody=" + newBody);
            final byte[] reqBodyBytes = newBody.getBytes();
            ctx.setRequest(new HttpServletRequestWrapper(request) {
                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return new ServletInputStreamWrapper(reqBodyBytes);
                }

                @Override
                public int getContentLength() {
                    return reqBodyBytes.length;
                }

                @Override
                public long getContentLengthLong() {
                    return reqBodyBytes.length;
                }
            });

            /*
            request.getParameterMap();// 关键步骤，一定要get一下,下面这行代码才能取到值
            Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();

            if (requestQueryParams == null) {
                requestQueryParams = new HashMap<>();
            }

            ArrayList<String> arrayList2 = new ArrayList<>();
            arrayList2.add(username);
            requestQueryParams.put("account", arrayList2);

            ctx.setRequestQueryParams(requestQueryParams);*/

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public int filterOrder() {
        return 0;// 优先级为0，数字越大，优先级越低
    }

    @Override
    public String filterType() {
        return "pre";// 前置过滤器
    }

}
