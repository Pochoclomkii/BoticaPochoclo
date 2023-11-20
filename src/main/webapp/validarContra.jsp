<%@page import="dao.UsuarioJpaController"%>
<%@page import="dto.Usuario"%>

<%
    UsuarioJpaController control = new UsuarioJpaController();
    
    String usuario = request.getParameter("logi");
    String clave = request.getParameter("pass");
    String clavecon = request.getParameter("passcon");
    String clavenew = request.getParameter("passnew");
    String clavenewcon = request.getParameter("passnewcon");
    String token = request.getParameter("token");
    String logihash = request.getParameter("logihash");
    Usuario usu = control.BuscarLogin(usuario);
    if(clave.equals(clavecon)){
    if(clavenew.equals(clavenewcon)){
    if (logihash.equals(token)) {
           usu.setPassUsua(clavenew);
            boolean ing = control.edit(usu);
            if (ing == false) {
            out.println("{\"resultado\":\"error\"}");
    } else {
    
        out.println("{\"resultado\":\"ok\"}");
    } 
        }
    
    }else{
    out.println("{\"resultado\":\"las contraseñas nuevas no son iguales\"}");
    }
    }else{
    out.println("{\"resultado\":\"las contraseñas no son iguales\"}");
    }
    
%>
