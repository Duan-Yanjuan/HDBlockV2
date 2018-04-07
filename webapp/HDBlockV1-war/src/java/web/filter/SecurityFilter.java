/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author David
 */

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter  implements Filter {
    
    
     FilterConfig filterConfig;
    
    private static final String CONTEXT_ROOT = "/HDBlockV1-war";
    
   

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }
    
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();
        String userType = "";
        Boolean staffIsLogin = false;
        Boolean userIsLogin = false;
  
        System.out.println("********** DO FILTER ");
        
        if(httpSession.getAttribute("userIsLogin") == null)
        {
                 httpSession.setAttribute("userIsLogin", false);
        }

         userIsLogin = (Boolean)httpSession.getAttribute("userIsLogin");     
        
   
        
             //checl customer exclude login
            if(!excludeLoginCheckUser(requestServletPath))
              {
                  if(userIsLogin == true)
                  {
                   //   Staff currentStaffEntity = (Staff)httpSession.getAttribute("currentStaffEntity");
                
                      if(checkUserAccessRight(requestServletPath))
                      {
                          chain.doFilter(request, response);
                      }
                      else
                      {
                          httpServletResponse.sendRedirect(CONTEXT_ROOT + "/error.xhtml");
                      }
                  }
                  else
                  {
                      //System.out.println("Navigating to staff Login." + (Boolean)httpSession.getAttribute("staffIsLogin") + "");
                      httpServletResponse.sendRedirect(CONTEXT_ROOT + "/Login.xhtml");
                  }
              }
              else
              {
                  chain.doFilter(request, response);
              }
        
          
      
    }



    public void destroy()
    {

    }
  


   public Boolean excludeLoginCheckUser(String path){
       
        if(path.equals("/Login.xhtml") ||  path.equals("/error.xhtml") || path.equals("/ICAStaffLogin.xhtml") ||
                path.equals("/ICAStaffHome.xhtml")  || path.equals("/ICAStaffIdentityApproval.xhtml") ||  path.equals("/HDBStaffHome.xhtml") || path.equals("/HDBStaffLogin.xhtml")||
                path.equals("HDBStaffHouseApproval.xhtml") || path.equals("/HDBStaffPolicy.xhtml") ||
                path.startsWith("/images") ||   path.startsWith("/javax.faces.resource"))
        {
            return true;
        }
        else
        {
            return false;
        }
   }


  
    private Boolean checkUserAccessRight(String path)
    {
        
        if(path.equals("/home.xhtml") || path.equals("/tenancyAgreement.xhtml") || path.equals("/profile.xhtml") || path.equals("/explore.xhtml") || path.equals("/house.xhtml"))
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
}
