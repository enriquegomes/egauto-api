package br.com.enriquegomes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Eduardo Gomes
 */
@WebServlet(name = "ServletProdutos", urlPatterns = {"/pedidos",})
public class ServletProdutos extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().equals("/egautopecas-api/pedidos")) {

            System.out.println("Entrou no POST");

            Produto p = new Produto();

            p.id = Long.parseLong(req.getParameter("idprod"));

            Clientes c = new Clientes();

            c.id = Long.parseLong(req.getParameter("idcliente"));

            Pedidos ped = new Pedidos();

            ped.cliente = c;

            ped.produto = p;

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            try {
                PreparedStatement comandosql = con.prepareStatement("INSERT INTO TB_PEDIDOS(COD_CLIENT, COD_PROD) VALUES(?, ?) ");
                comandosql.setLong(1, c.id);
                comandosql.setLong(2, p.id);
                comandosql.execute();
            } catch (Exception e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
                resp.sendError(500);
            }

            try {
                PreparedStatement comandosql = con.prepareStatement("UPDATE TB_PRODUTOS SET QTD = QTD - 1 WHERE COD_PROD=?");
                comandosql.setLong(1, p.id);
                comandosql.execute();

            } catch (Exception e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getRequestURI().equals("/egautopecas-api/pedidos")){
            
            System.out.println("Entrou no GET");
            
            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();
            
            List listaPedidos = new ArrayList();
            
            try{
                ResultSet result = con.prepareStatement("SELECT * FROM TB_PEDIDOS JOIN TB_PRODUTOS ON(TB_PEDIDOS.COD_PROD = TB_PRODUTOS.COD_PROD) JOIN TB_CLIENTES ON(TB_PEDIDOS.COD_CLIENT = TB_CLIENTES.COD_CLIENT)").executeQuery();
                
                while(!result.isLast()){
                   Pedidos p = new Pedidos();
                   
                   Produto prod = new Produto();
                   
                   Clientes c = new Clientes();
                   
                   result.next();
                   
                   prod.nome = result.getString("NOMEPROD");
                   
                   c.nome = result.getString("NOMECLIENT");
                    
                   p.cliente = c;
                   
                   p.produto = prod;
                   
                   listaPedidos.add(p);
                }
                
            }catch (SQLException e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }
                
            
        }
    }



}
