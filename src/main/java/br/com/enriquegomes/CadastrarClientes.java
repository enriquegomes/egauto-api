package br.com.enriquegomes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(name = "CadastrarClientes", urlPatterns = {"/clientes/cadastrar", "/clientes"})
public class CadastrarClientes extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String nomeCliente = req.getParameter("nome");
        
        System.out.println("Entrou no do Post");
        
        Clientes c = new Clientes();
        
        c.nome = nomeCliente;
        
        String cpfCliente = req.getParameter("cpf");
        
        c.cpf = cpfCliente;
        
        String emailCliente = req.getParameter("email");
        
        c.email = emailCliente;
        

        // Conexao BD
        Conexao conexao = new Conexao();
        
        Connection con = conexao.conectar();
        
        try{
            con.prepareStatement("INSERT INTO TB_CLIENTES(NOMECLIENT, CPF, EMAIL) VALUES('"+c.nome+"', '"+c.cpf+"','"+c.email+"')").execute();
            
        }catch(Exception ex){
            System.out.println("Erro no SQL"+ex.getMessage());            
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

      
    
}
