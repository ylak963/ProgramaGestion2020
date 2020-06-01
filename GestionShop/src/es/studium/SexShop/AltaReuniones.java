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

public class AltaReuniones extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblpuntosReunion = new Label("Puntos:");
	TextField txtpuntosReunion = new TextField(20);
	Label lblfechaReunion = new Label("Fecha:");
	TextField txtfechaReunion = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog dlgAltaReuniones = new Dialog (this,"Mensaje",true);
	Label mensaje = new Label("");
	
	Registros registros = new Registros();
	Login logUsuario = new Login();

	AltaReuniones()
	{
		setTitle("Alta de reuniones");
		setLayout(new FlowLayout());
		add(lblpuntosReunion);
		add(txtpuntosReunion);
		add(lblfechaReunion);
		add(txtfechaReunion);
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
			txtpuntosReunion.selectAll();
			txtpuntosReunion.setText("");
			txtpuntosReunion.requestFocus();
			
			txtfechaReunion.selectAll();
			txtfechaReunion.setText("");
			txtfechaReunion.requestFocus();
			
		}
		else if(objetoPulsado.equals(btnAceptar))
		{
			// Conectar BD
			Connection con = conectar();
			
			// Proceder con el INSERT
			
			//String fecha=txtFecha.getText();
			String puntReun = txtpuntosReunion.getText();
			String [] fechaReunionAmericana = txtfechaReunion.getText().split("/");
			int respuesta = insertar(con, "reuniones",puntReun,fechaReunionAmericana);
						
			//Guardar registros
			
			System.out.println(respuesta);
			// Mostramos resultado
			if(respuesta == 0)
			{
				mensaje.setText("Alta de reuniones correcta");
				dlgAltaReuniones.setTitle("Alta Reuniones");
				dlgAltaReuniones.setSize(180,120);
				dlgAltaReuniones.setLayout(new FlowLayout());
				dlgAltaReuniones.addWindowListener(this);
				dlgAltaReuniones.add(mensaje);
				dlgAltaReuniones.setLocationRelativeTo(null);
				dlgAltaReuniones.setVisible(true);
				
				System.out.println("Alta de reuniones correcta");
			}
			else
			{
				mensaje.setText("Error en Alta de Reuniones");
				dlgAltaReuniones.setTitle("Alta Reuniones");
				dlgAltaReuniones.setSize(180,120);
				dlgAltaReuniones.setLayout(new FlowLayout());
				dlgAltaReuniones.addWindowListener(this);
				dlgAltaReuniones.add(mensaje);
				dlgAltaReuniones.setLocationRelativeTo(null);
				dlgAltaReuniones.setVisible(true);
				System.out.println("Error en Alta de reuniones");
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
	
	public int insertar(Connection con, String reuniones, String puntReun, String[] fechaReunionAmericana) 
	{
		String usuario = logUsuario.txtUsuario.getText();
		int respuesta = 0;
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + reuniones + " VALUES (null,'"+puntReun +"','"
			+fechaReunionAmericana[2]+"-"+fechaReunionAmericana[1]+"-"+fechaReunionAmericana[0]+"');";
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
