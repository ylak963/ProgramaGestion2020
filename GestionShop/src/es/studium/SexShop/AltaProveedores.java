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
import java.sql.SQLException;
import java.sql.Statement;

public class AltaProveedores extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblnombreProveedor = new Label("Proveedor:");
	TextField txtnombreProveedor = new TextField(20);
	Label lblTelefonoProveedor = new Label("Teléfono:");
	TextField txtTelefonoProveedor = new TextField(20);
	Label lblGeneroProveedor = new Label("Género:");
	TextField txtGeneroProveedor = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Dialog dlgAltaProveedores = new Dialog (this,"Mensaje",true);
	Label mensaje = new Label("");
	
	Registros registros = new Registros();
	Login logUsuario = new Login();
	
	AltaProveedores()
	{
		setTitle("Alta de proveedores");
		setLayout(new FlowLayout());
		add(lblnombreProveedor);
		add(txtnombreProveedor);
		add(lblTelefonoProveedor);
		add(txtTelefonoProveedor);
		add(lblGeneroProveedor);
		add(txtGeneroProveedor);
		add(btnAceptar);
		add(btnLimpiar);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		addWindowListener(this);
		setSize(280,150);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnLimpiar))
		{
			txtnombreProveedor.selectAll();
			txtnombreProveedor.setText("");
			txtnombreProveedor.requestFocus();
			
			txtTelefonoProveedor.selectAll();
			txtTelefonoProveedor.setText("");
			txtTelefonoProveedor.requestFocus();
			
			txtGeneroProveedor.selectAll();
			txtGeneroProveedor.setText("");
			txtGeneroProveedor.requestFocus();
		}
		else if(objetoPulsado.equals(btnAceptar))
		{
			// Conectar BD
			Connection con = conectar();
			
			// Proceder con el INSERT
			//String nomProv = txtnombreProveedor.getText();
			String telProv = txtTelefonoProveedor.getText();
			String genProv = txtGeneroProveedor.getText();
			int respuesta = insertar(con, "proveedores", txtnombreProveedor.getText(),telProv,genProv);
			// Mostramos resultado
			if(respuesta == 0)
			{
				mensaje.setText("Alta de proveedores correcta");
				dlgAltaProveedores.setTitle("Alta Proveedores");
				dlgAltaProveedores.setSize(190,120);
				dlgAltaProveedores.setLayout(new FlowLayout());
				dlgAltaProveedores.addWindowListener(this);
				dlgAltaProveedores.add(mensaje);
				dlgAltaProveedores.setLocationRelativeTo(null);
				dlgAltaProveedores.setVisible(true);
				System.out.println("Alta de proveedor correcta");
				
			}
			else
			{
				mensaje.setText("Error en Alta de Proveedores");
				dlgAltaProveedores.setTitle("Alta Proveedores");
				dlgAltaProveedores.setSize(190,120);
				dlgAltaProveedores.setLayout(new FlowLayout());
				dlgAltaProveedores.addWindowListener(this);
				dlgAltaProveedores.add(mensaje);
				dlgAltaProveedores.setLocationRelativeTo(null);
				dlgAltaProveedores.setVisible(true);
				
				System.out.println("Error en Alta de proveedores");
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
	
	public int insertar(Connection con, String proveedores, String nomProv, String telProv, String genProv) 
	{
		int respuesta = 0;
		String usuario = logUsuario.txtUsuario.getText();
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + proveedores + " VALUES (null,'" + nomProv + "','"+telProv
					+"','"+ genProv+"');";
			
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
	
	public void desconectar(Connection con)
	{
		try
		{
			con.close();
		}
		catch(Exception e) {}
	}

}
