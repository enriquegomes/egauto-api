package br.com.enriquegomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

@WebServlet(name = "CadastrarClientes", urlPatterns = {"/clientes/cadastrar", "/clientes", "/clientes/deletar/*", "/clientes/editar/*"})
public class CadastrarClientes extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/egautopecas-api/clientes/cadastrar")) {

            System.out.println("Entrou no POST");

            String nomeCliente = req.getParameter("nome");

            Clientes c = new Clientes();

            c.nome = nomeCliente;

            String cpfCliente = req.getParameter("cpf");

            c.cpf = cpfCliente;

            System.out.println(c.cpf);

            String emailCliente = req.getParameter("email");

            c.email = emailCliente;

            // Conexao BD
            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            try {
                PreparedStatement comandoSQL = con.prepareStatement("INSERT INTO TB_CLIENTES(NOMECLIENT, CPF, EMAIL) VALUES(?,?,?)");
                comandoSQL.setString(1, nomeCliente);
                comandoSQL.setString(2, cpfCliente);
                comandoSQL.setString(3, emailCliente);
                comandoSQL.execute();

            } catch (Exception ex) {
                System.out.println("Erro no SQL" + ex.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        if (req.getRequestURI().equals("/egautopecas-api/clientes")) {
            System.out.println("Chegou no GET");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            List listaClientes = new ArrayList();

            try {
                ResultSet select = con.prepareStatement("SELECT * FROM TB_CLIENTES").executeQuery();

                while (!select.isLast()) {
                    select.next();

                    Clientes c = new Clientes();

                    c.id = select.getLong("COD_CLIENT");

                    c.nome = select.getString("NOMECLIENT");

                    c.cpf = select.getString("CPF");

                    c.email = select.getString("EMAIL");

                    listaClientes.add(c);

                }

            } catch (SQLException e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }

            resp.setCharacterEncoding("UTF-8");

            String resultadoASerPrintado = "[";

            for (int i = 0; i < listaClientes.size(); i++) {

                Clientes c = (Clientes) listaClientes.get(i);

                resultadoASerPrintado = resultadoASerPrintado + "{\"id\":" + c.id + ",\"nome\":\"" + c.nome + "\",\"cpf\":\"" + c.cpf + "\",\"email\":\"" + c.email + "\"},";

            }

            int indiceDaVirgulaExtra = resultadoASerPrintado.lastIndexOf(",");

            String resultadoSemAVirgulaExtra = resultadoASerPrintado.substring(0, indiceDaVirgulaExtra);

            resultadoSemAVirgulaExtra = resultadoSemAVirgulaExtra + "]";

            resp.getWriter().print(resultadoSemAVirgulaExtra);

        } else if (req.getRequestURI().startsWith("/egautopecas-api/clientes/editar/")) {

            System.out.println("Entrou no Editar");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            String idProdDaURL = req.getPathInfo();

            String idSemBarra = idProdDaURL.substring(1);

            Integer id = Integer.parseInt(idSemBarra);

            String infosEdit = "";

            try {
                ResultSet valores = con.prepareStatement("SELECT * FROM TB_CLIENTES WHERE COD_CLIENT = " + id).executeQuery();

                valores.next();

                Clientes c = new Clientes();
                c.id = valores.getLong("COD_CLIENT");

                c.nome = valores.getString("NOMECLIENT");

                c.cpf = valores.getString("cpf");

                c.email = valores.getString("email");

                infosEdit = "{\"id\":" + c.id + ",\"nome\":\"" + c.nome + "\",\"cpf\":\"" + c.cpf + "\",\"email\":\"" + c.email + "\"}";

                System.out.println(infosEdit);

            } catch (Exception e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }

            resp.getWriter().print(infosEdit);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().startsWith("/egautopecas-api/clientes/deletar/")) {
            System.out.println("Entrou no Delete");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            String idProdDaURL = req.getPathInfo();

            String idSemBarra = idProdDaURL.substring(1);

            Integer id = Integer.parseInt(idSemBarra);

            try {
                PreparedStatement comandosql = con.prepareStatement("DELETE FROM TB_CLIENTES WHERE COD_CLIENT = ?");
                comandosql.setInt(1, id);
                comandosql.execute();
            } catch (Exception e) {
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().startsWith("/egautopecas-api/clientes/editar/")) {

            System.out.println("Entrou no Editar");

            Conexao conexao = new Conexao();

            Connection con = conexao.conectar();

            String idProdDaURL = req.getPathInfo();

            String idSemBarra = idProdDaURL.substring(1);

            Long id = Long.parseLong(idSemBarra);

            BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
            //nome=faroll&email=eee&cpf=999
            //nome=Iam&quantidade=432
            String dados = br.readLine();

            String nome = dados.substring(5, dados.indexOf("&"));

            int indiceEComercial = dados.indexOf("&");
            
            dados = dados.substring(indiceEComercial + 1);
            
            String cpf = dados.substring(4,dados.indexOf("&"));
            
            int ultimoEComercial = dados.indexOf("&");
            
            dados = dados.substring(ultimoEComercial + 1);
            
            String email = dados.substring(6, dados.indexOf("&"));

            Clientes c = new Clientes();

            c.nome = nome;
            
            c.cpf = cpf;

            c.email = email;
            
            c.id = id;

            try{
                PreparedStatement comandosql = con.prepareStatement("UPDATE TB_CLIENTES SET NOMECLIENT = ?, CPF = ?, EMAIL = ? WHERE COD_CLIENT = ?");
                comandosql.setString(1, nome);
                comandosql.setString(2, cpf);
                comandosql.setString(3, email);
                comandosql.setLong(4, id);
                comandosql.execute();
                     
            }catch(Exception e){
                System.out.println("Erro ao executar o SQL: " + e.getMessage());
            }
        }

    }

}
