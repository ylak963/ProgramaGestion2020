package es.studium.SexShop;
import java.awt.Button;

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

public class ModificarProveedoresDos extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblNombreProveedor = new Label("Proveedor:");
	TextField txtNombreProveedor = new TextField(20);
	Label lblTelefonoProveedor = new Label("Teléfono:");
	TextField txtTelefonoProveedor = new TextField(20);
	Label lblGeneroProveedor = new Label("Género:");
	TextField txtGeneroProveedor = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Connection con = null;


	String [] cadena;
	Dialog dlgMensaje = new Dialog(this,"Mensaje", true);
	Label mensaje = new Label("");

	int idProveedorModificar;
	Registros registros = new Registros();
	Login logUsuario = new Login();



	public ModificarProveedoresDos(int idProveedorModificar)
	{
		this.idProveedorModificar = idProveedorModificar; 
		// Conectar BD
		Connection con = conectar();
		cadena= (consultarProveedor(con, idProveedorModificar)).split("-");
		setTitle("Modificar Proveedor");
		setLayout(new FlowLayout());

		add(lblNombreProveedor);
		txtNombreProveedor.setText(cadena[1]);
		add(txtNombreProveedor);

		add(lblTelefonoProveedor);
		txtTelefonoProveedor.setText(cadena[2]);
		add(txtTelefonoProveedor);

		add(lblGeneroProveedor);
		txtGeneroProveedor.setText(cadena[3]);
		add(txtGeneroProveedor);

		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);

		add(btnAceptar);
		add(btnLimpiar);
		addWindowListener(this);
		setSize(240,300); 
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		if(btnLimpiar.equals(arg0.getSource()))
		{
			txtNombreProveedor.selectAll();
			txtNombreProveedor.setText("");
			txtTelefonoProveedor.selectAll();
			txtTelefonoProveedor.setText("");
			txtGeneroProveedor.selectAll();
			txtGeneroProveedor.setText("");
		
		}
		else if(btnAceptar.equals(arg0.getSource()))// btnAceptar
		{
			String nombreProveedor=txtNombreProveedor.getText();
			String telefonoProveedor=txtTelefonoProveedor.getText();
			String generoProveedor=txtGeneroProveedor.getText();
			
			String usuario = logUsuario.txtUsuario.getText();
			Connection con = conectar();
			cadena= (consultarProveedor(con, idProveedorModificar)).split("-");
			// Hacer UPDATE
			String sentencia = "UPDATE proveedores SET nombreProveedor ='"+nombreProveedor+
					"', telefonoProveedor = "+telefonoProveedor+
					", generoProveedor = '"+generoProveedor+
					"' WHERE idProveedor = "+idProveedorModificar;
			registros.registrarMovimiento(usuario,sentencia);

			// Mostrar cuadro de diálog
			if((modificarArticulo(con, sentencia))==0)
			{
				// Todo bien
				mensaje.setText("Modificación del proveedor correcta");
				dlgMensaje.setTitle("Modificar Proveedor");
				dlgMensaje.setSize(250,120);
				dlgMensaje.setLayout(new FlowLayout());
				dlgMensaje.addWindowListener(this);
				dlgMensaje.add(mensaje);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
			else
			{
				// Error
				mensaje.setText("Error en la Modificación del proveedor");
				dlgMensaje.setTitle("Modificar Proveedor");
				dlgMensaje.setSize(250,120);
				dlgMensaje.setLayout(new FlowLayout());
				dlgMensaje.addWindowListener(this);
				dlgMensaje.add(mensaje);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
			// Desconectar
			desconectar(con);
		}

	}
	public void windowActivated(WindowEvent arg0){}
	public void windowClosed(WindowEvent arg0){}
	public void windowClosing(WindowEvent arg0)
	{
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else
		{
			setVisible(false);
		}
	}

	public void windowDeactivated(WindowEvent arg0){}
	public void windowDeiconified(WindowEvent arg0){}
	public void windowIconified(WindowEvent arg0){}


	public void windowOpened(WindowEvent arg0){}
	public Connection conectar()
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/gestionshop?autoReconnect=true&useSSL=false";
		String user = "root";
		String password = "1234";
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

	public String consultarProveedor(Connection c, int idProveedor)
	{
		String resultado = "";
		String usuario = logUsuario.txtUsuario.getText();
		try
		{
			String sentencia = "SELECT * FROM proveedores where idProveedor="+idProveedor;
			//Crear una sentencia
			Statement  stm = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stm.executeQuery(sentencia);
			rs.next();

			resultado = rs.getInt("idProveedor") + "-" +
					rs.getString("nombreProveedor") + "-" +
					rs.getInt("telefonoproveedor") + "-" +
					rs.getString("generoProveedor");
			registros.registrarMovimiento(usuario,sentencia);

		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	public int modificarArticulo(Connection c, String sentencia)
	{
		Statement stm = null;
		int resultado = 1;
		try
		{
			//Crear una sentencia
			stm =c.createStatement();
			// Ejecutar la sentencia SQL
			if((stm.executeUpdate(sentencia))==1)
			{
				resultado = 0;
			}
			else
			{
				resultado = 1;
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
