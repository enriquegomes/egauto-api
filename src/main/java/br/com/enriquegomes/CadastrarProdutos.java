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
import javax.swing.JOptionPane;

/**
 *
 * @author enrique.gomes
 */
@WebServlet(name = "CadastrarProdutos", urlPatterns = {"/produtos/cadastrar", "/produtos", "/produtos/deletar/*", "/produtos/editar/*"})
public class CadastrarProdutos extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/egautopecas-api/produtos/cadastrar")) {

            System.out.println("Entrou no do Post");

            String nomeProduto = req.getParameter("nome");

            Produto p = new Produto();

            p.nome = nomeProduto;

            Integer quantidadeProduto = Integer.parseInt(req.getParameter("quantidade"));

            p.quantidade = quantidadeProduto;

            //Vamos conectar no banco de dados
            // Tipo da variavel  -  nome variavel -- nova instancia(copia em memoria)
            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            try {
                PreparedStatement comandoSQL = con.prepareStatement("INSERT INTO TB_PRODUTOS(NOMEPROD, QTD) VALUES(?,?)");
                comandoSQL.setString(1, nomeProduto);
                comandoSQL.setInt(2, quantidadeProduto);
                comandoSQL.execute();
            } catch (Exception e) {
                System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
            }

        } else {
            System.out.println("URL inválida");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        //Retorna URL utilizada
        if (req.getRequestURI().equals("/egautopecas-api/produtos")) {

            System.out.println("Chegou no GET");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            List listaProdutos = new ArrayList();

            try {
                ResultSet resultado = con.prepareStatement("SELECT * FROM TB_PRODUTOS ").executeQuery();
                //Enquanto resultado não for o ultimo
                while (!resultado.isLast()) {

                    resultado.next();

                    Produto p = new Produto();
                    p.id = resultado.getLong("COD_PROD");

                    p.nome = resultado.getString("NOMEPROD");

                    p.quantidade = resultado.getInt("QTD");

                    listaProdutos.add(p);
                }

            } catch (SQLException e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }

            resp.setCharacterEncoding("UTF-8");

            String resultadoASerPrintado = "[";

            for (int i = 0; i < listaProdutos.size(); i++) {
                Produto p = (Produto) listaProdutos.get(i);
                resultadoASerPrintado = resultadoASerPrintado + "{\"id\":" + p.id + ",\"nome\":\"" + p.nome + "\",\"quantidade\":" + p.quantidade + "},";

            }

            int indiceDaVirgulaExtra = resultadoASerPrintado.lastIndexOf(",");

            String resultadoSemAVirgulaExtra = resultadoASerPrintado.substring(0, indiceDaVirgulaExtra);

            resultadoSemAVirgulaExtra = resultadoSemAVirgulaExtra + "]";

            resp.getWriter().print(resultadoSemAVirgulaExtra);
        } else if (req.getRequestURI().startsWith("/egautopecas-api/produtos/editar/")) {
            
            System.out.println("Entrou no Editar");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();
            
            String idProdDaURL = req.getPathInfo();

            String idSemBarra = idProdDaURL.substring(1);

            Integer id = Integer.parseInt(idSemBarra);
            
            String infosEdit="";
            
            try {
                ResultSet valores = con.prepareStatement("SELECT * FROM TB_PRODUTOS WHERE COD_PROD = "+ id).executeQuery();
                
                valores.next();
                
                Produto p = new Produto();
                p.id = valores.getLong("COD_PROD");
                
                p.nome = valores.getString("NOMEPROD");
                
                p.quantidade = valores.getInt("QTD");
                   
                infosEdit= "{\"id\":" + p.id + ",\"nome\":\"" + p.nome + "\",\"quantidade\":" + p.quantidade + "}";
                
                
            } catch (Exception e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }
            
            resp.getWriter().print(infosEdit);
            
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().startsWith("/egautopecas-api/produtos/deletar/")) {
            System.out.println("Entrou no Delete");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            String idProdDaURL = req.getPathInfo();

            String idSemBarra = idProdDaURL.substring(1);

            Integer id = Integer.parseInt(idSemBarra);

            try {
                PreparedStatement comandosql = con.prepareStatement("DELETE FROM TB_PRODUTOS WHERE COD_PROD = ?");
                comandosql.setInt(1, id);
                comandosql.execute();
            } catch (Exception e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().startsWith("/egautopecas-api/produtos/editar/")) {
            
            System.out.println("Entrou no Editar");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();
            //Id
            String idProdDaURL = req.getPathInfo();

            String idSemBarra = idProdDaURL.substring(1);

            Integer id = Integer.parseInt(idSemBarra);
            
            System.out.println(idProdDaURL);
            // Atributos

            String nomeEditado = req.getParameter("editname");

            Produto p = new Produto();

            p.nome = nomeEditado;
            
            System.out.println(p.nome);
            
            Integer quantidadeEditada = Integer.parseInt(req.getParameter("editqtd"));

            p.quantidade = quantidadeEditada;
            
            
          
        }
            
    }
    
    

}
