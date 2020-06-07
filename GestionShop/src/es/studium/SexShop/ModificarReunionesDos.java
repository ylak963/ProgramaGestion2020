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

public class ModificarReunionesDos extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblPuntosReunion = new Label("Puntos:");
	TextField txtPuntosReunion = new TextField(20);
	Label lblFechaReunion = new Label("Fecha:");
	TextField txtFechaReunion = new TextField(20);
		
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Connection con = null;
	String [] cadena;
	Dialog dlgMensaje = new Dialog(this,"Mensaje", true);
	Label mensaje = new Label("");

	int idReunionModificar;
	Registros registros = new Registros();
	Login logUsuario = new Login();

	public ModificarReunionesDos(int idReunionModificar)
	{
		this.idReunionModificar = idReunionModificar; 
		// Conectar BD
		Connection con = conectar();
		cadena= (consultarReunion(con, idReunionModificar)).split("-");
		setTitle("Modificar Reunión");
		setLayout(new FlowLayout());

		add(lblPuntosReunion);
		txtPuntosReunion.setText(cadena[1]);
		add(txtPuntosReunion);

		add(lblFechaReunion);
		txtFechaReunion.setText(cadena[2]);
		add(txtFechaReunion);
	

		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);

		add(btnAceptar);
		add(btnLimpiar);
		addWindowListener(this);
		setSize(240,230); 
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		if(btnLimpiar.equals(arg0.getSource()))
		{
			txtPuntosReunion.selectAll();
			txtPuntosReunion.setText("");
			txtFechaReunion.selectAll();
			txtFechaReunion.setText("");
		}
		else if(btnAceptar.equals(arg0.getSource()))// btnAceptar
		{
			String puntosReunion=txtPuntosReunion.getText();
			String[] fechaAmericana = txtFechaReunion.getText().split("/");
				
			String usuario = logUsuario.txtUsuario.getText();
			Connection con = conectar();
			cadena= (consultarReunion(con, idReunionModificar)).split("-");
			// Hacer UPDATE
			String sentencia = "UPDATE reuniones SET puntosReunion ="+puntosReunion+
					", fechaReunion='"+fechaAmericana[2]+"-"+fechaAmericana[1]+"-"+fechaAmericana[0]+
					"' WHERE idReunion = "+idReunionModificar;
			
			registros.registrarMovimiento(usuario,sentencia);

			// Mostrar cuadro de diálog
			if((modificarArticulo(con, sentencia))==0)
			{
				// Todo bien
				mensaje.setText("Modificación de la reunión correcta");
				dlgMensaje.setTitle("Modificar Reunión");
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
				mensaje.setText("Error en la Modificación de la reunión");
				dlgMensaje.setTitle("Modificar Reunión");
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

	public String consultarReunion(Connection c, int idReunion)
	{
		String resultado = "";
		String [] fechaAmericana;
		String usuario = logUsuario.txtUsuario.getText();
		try
		{
			String sentencia = "SELECT * FROM reuniones where idReunion="+idReunion;
			//Crear una sentencia
			Statement  stm = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stm.executeQuery(sentencia);
			rs.next();
			fechaAmericana=(rs.getString("fechaReunion")).split("-");

			resultado = rs.getInt("idReunion") + "-" +
					rs.getInt("puntosReunion") + "-" +
					fechaAmericana[2]+"/"+fechaAmericana[1]+"/"+fechaAmericana[0]/*+"#"*/;
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
