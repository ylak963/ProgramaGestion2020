package es.studium.SexShop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AltaDisponen extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Label lblReunionFK = new Label("Referencia reunión:");

	Choice choReunion = new Choice();
	//TextField txtReunionFK = new TextField(20);
	
	Label lblArticuloFK = new Label("Referencia artículo:");
	TextField txtArticuloFK = new TextField(20); //Id de referencia al artículos. El empleado tendría una lista con los artículos y su id correspondiente
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Dialog dlgAltaProveedores = new Dialog (this,"Mensaje",true);
	Label mensaje = new Label("");
	
	String cadena [];
	Registros registros = new Registros();
	Login logUsuario = new Login();
	
	AltaDisponen()
	{
		setTitle("Alta de Disponen");
		setLayout(new FlowLayout());
		add(lblReunionFK);
		choReunion.add("Seleccionar una reunión...");
		
		//Conectamos a la bd
		Connection con = conectar();
		cadena = (consultarReunionesChoice(con)).split("#");
		for(int i=0; i<cadena.length; i++)
		{
			choReunion.add(cadena[i]);
		}
		add(choReunion);
		
		add(lblArticuloFK);
		add(txtArticuloFK);
		
		add(btnAceptar);
		add(btnLimpiar);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		addWindowListener(this);
		setSize(320,160);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	

	public void actionPerformed(ActionEvent e)
	{
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnLimpiar))
		{
			choReunion.select(0);
			
			txtArticuloFK.selectAll();
			txtArticuloFK.setText("");
			txtArticuloFK.requestFocus();
			
			
		}
		else if(objetoPulsado.equals(btnAceptar))
		{
			// Conectar BD
			Connection con = conectar();
			
			// Proceder con el INSERT
			int respuesta = insertar(con, "disponen", choReunion.getSelectedItem(),txtArticuloFK.getText());
			// Mostramos resultado
			if(respuesta == 0)
			{
				mensaje.setText("Alta de disponen correcta");
				dlgAltaProveedores.setTitle("Alta Disponen");
				dlgAltaProveedores.setSize(190,120);
				dlgAltaProveedores.setLayout(new FlowLayout());
				dlgAltaProveedores.addWindowListener(this);
				dlgAltaProveedores.add(mensaje);
				dlgAltaProveedores.setLocationRelativeTo(null);
				dlgAltaProveedores.setVisible(true);
				System.out.println("Alta de disponen correcta");
				
			}
			else
			{
				mensaje.setText("Error en Alta de Disponen");
				dlgAltaProveedores.setTitle("Alta Disponen");
				dlgAltaProveedores.setSize(190,120);
				dlgAltaProveedores.setLayout(new FlowLayout());
				dlgAltaProveedores.addWindowListener(this);
				dlgAltaProveedores.add(mensaje);
				dlgAltaProveedores.setLocationRelativeTo(null);
				dlgAltaProveedores.setVisible(true);
				
				System.out.println("Error en Alta de disponen");
			}
			// Desconectar de la base
			desconectar(con);
		}
	}

	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e)
	{		
		setVisible(false);
	}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}

	public Connection conectar()
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/gestionshop?autoReconnect=true&useSSL=false";
		String user = "root";
		String password = "1234";
		//Objeto para proporcionar un vinculo con entre la base de datos y una aplicacion en Java
		Connection con = null;
		try 
		{
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			
			// Establecer la conexión con la BD empresa
			con = DriverManager.getConnection(url, user, password);
			if (con != null) 
			{
				System.out.println("Conectado a la base de datos");
			}
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();
		} 
		catch (ClassNotFoundException cnfe) 
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		return con;
	}
	
	public int insertar(Connection con, String disponen, String idReunFK, String idArtiFK) 
	{
		int respuesta = 0;
		String usuario = logUsuario.txtUsuario.getText();
		try 
		{
			idReunFK = choReunion.getSelectedItem();
			idArtiFK = txtArticuloFK.getText();
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + disponen + " VALUES (idReunFK,idArtiFK);";
			
			System.out.println(cadenaSQL);
			
			//Registros de movimientos de usuarios
			registros.registrarMovimiento(usuario,cadenaSQL);
			
			sta.executeUpdate(cadenaSQL);
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer un Insert");
			ex.printStackTrace();
			respuesta = 1;
		}
		return respuesta;
	}
	public String consultarReunionesChoice(Connection c)
	{
		String resultado = "";
		String [] fechaEuropea;
		try
		{
			String sentencia = "SELECT * FROM reuniones";
			//Crear una sentencia
			Statement  stm = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stm.executeQuery(sentencia);
			while (rs.next())
			{
				fechaEuropea = (rs.getString("fechaReunion")).split("-");
				resultado = resultado + rs.getInt("idReunion") + "-" +
						fechaEuropea[2]+"/"+fechaEuropea[1]+"/"+fechaEuropea[0]+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	public void desconectar(Connection con)
	{
		try
		{
			con.close();
		}
		catch(Exception e) {}
	}

}
