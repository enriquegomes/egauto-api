package br.com.enriquegomes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    
    String usuario, senha, url;
            
    public Connection conectar(){
        
        Connection con = null;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException erro){
            System.out.println("Erro ao carregar o driver");
        }
        
        usuario = "root";
        senha = "1234";
        url = "jdbc:mysql://localhost:3306/egauto?useSSL=false&serverTimezone=UTC";
        
        try {
            con = DriverManager.getConnection(url, usuario, senha);
        } catch(SQLException sqlException){
            System.out.println("Erro ao conectar no banco de dados, o erro foi" + sqlException.getMessage());
        }
        
        return con;
    }
    
}
