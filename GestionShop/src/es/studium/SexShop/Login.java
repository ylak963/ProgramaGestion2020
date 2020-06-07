package es.studium.SexShop;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Login extends WindowAdapter implements ActionListener 
{
	//Declaración de los componentes y ventanas
	
	Frame login = new Frame("Login GestionShop");
	Dialog errorLogin = new Dialog(login, "ERROR", true);
	
	Label lblUsuario = new Label("Usuario       ");
	Label lblPass = new Label("Contraseña");
	
	TextField txtUsuario = new TextField("admin", 20);
	TextField txtPass = new TextField("admin", 20);
	
	Button btnAcceder = new Button("Acceder");
	Button btnLimpiar = new Button("Cancelar");
	Button btnVolver = new Button("Volver");
	
	// Conexión a la base de datos
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/gestionshop?useSSL=false&allowPublicKeyRetrieval=true";
	String usuario = "root";
	String password = "1234";
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	
	public Login()
	{		
		//Constructor del Layout Login
		login.setLayout(new FlowLayout());
		login.setSize(290, 125);
		login.setLocationRelativeTo(null);
		login.setResizable(false);
		login.addWindowListener(this);
		login.setVisible(true);
		
		login.add(lblUsuario);
		login.add(txtUsuario);
		login.add(lblPass);
		
		txtPass.setEchoChar('*');
		login.add(txtPass);
		login.add(btnAcceder);
		login.add(btnLimpiar);
		login.setVisible(true);
		btnAcceder.addActionListener((ActionListener) this);
		btnLimpiar.addActionListener((ActionListener) this);
	
		// Layout de error
		errorLogin.setLayout(new FlowLayout());
		errorLogin.add(btnVolver);
		errorLogin.setSize(200, 100);
		errorLogin.setLocationRelativeTo(null);
		errorLogin.addWindowListener(this);

	}

	public static void main(String[] args)
	{
		new Login();
	}

	public void windowClosing(WindowEvent e)
	{
		if (errorLogin.isActive())
		{
			errorLogin.setVisible(false);
		} 
		else
		{
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent evento)
	{

		//String tipoUsuario =0;
		if (evento.getSource().equals(btnLimpiar))
		{
			//Que realizará el botón limpiar
			txtUsuario.selectAll();
			txtUsuario.setText("");
			txtPass.selectAll();
			txtUsuario.setText("");
			txtUsuario.requestFocus();
		}

		else if (evento.getSource().equals(btnAcceder)/*|tipoUsuario==0*/)
		{
			// Funcionalidad del botón acceder
			String cadenaEncriptada = getSHA256(txtPass.getText());
			//int tipoUsuario = 0;
			sentencia = "SELECT nombreUsuario,claveUsuario,tipoUsuario FROM usuarios WHERE ";
			sentencia += "nombreUsuario = '"+txtUsuario.getText()+"'";
			sentencia += " AND claveUsuario = '"+cadenaEncriptada+"'";
			

			System.out.println(sentencia);//Quitar
						
			try
			{
				//Cargar los controladores para el acceso a la BD
				Class.forName(driver);

				//Establecer la conexión con la BD Empresa
				connection = DriverManager.getConnection(url, usuario, password);

				//Crear una sentencia
				statement = connection.createStatement();

				//Crear un objeto ResultSet para guardar lo obtenido

				//y ejecutar la sentencia SQL

				rs = statement.executeQuery(sentencia);

				if(rs.next())

				{
					if((txtUsuario.getText().equals("admin")) && (txtPass.getText().equals("admin")))
					{
						new MenuPrincipal();
						
						login.setVisible(false);
					}
					else
					{
						new MenuPrincipal().soloAlta();
						login.setVisible(false);
						
					}
					
				}
			
				else
				{
					//Se abrira la ventana indicando que hay un error y dispondra de un boton volver
					errorLogin.setLayout(new FlowLayout());
					errorLogin.setSize(160, 90);
					errorLogin.setResizable(false);
					errorLogin.addWindowListener(this);
					errorLogin.add(new Label("Credenciales Incorrectas"));
					Button btnVolver = new Button("Volver");
					btnVolver.addActionListener(this);
					errorLogin.add(btnVolver);
					errorLogin.setLocationRelativeTo(null);
					errorLogin.setVisible(true);
									
				}
			}

			catch (ClassNotFoundException cnfe)
			{
				System.out.println("Error 1-"+cnfe.getMessage());
			}

			catch (SQLException sqle)
			{
				System.out.println("Error 2-"+sqle.getMessage());
			}

			/*finally
			{
				try
				{
					if(connection!=null)
					{
						connection.close();
					}
				}
				catch (SQLException e)
				{
					System.out.println("Error 3-"+e.getMessage());
				}
			}*/
			
		}
		else
		{
			// Tareas del Volver
			errorLogin.setVisible(false);
		}
	}
	//Funcion para la encriptación de la clave
	public static String getSHA256(String data)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			md.update(data.getBytes());
			byte byteData[] = md.digest();
			for (int i = 0; i < byteData.length; i++) 
			{
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100,16).substring(1));
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return sb.toString();
	}
}
