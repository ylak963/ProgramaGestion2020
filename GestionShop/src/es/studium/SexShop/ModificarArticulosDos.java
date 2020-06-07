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

public class ModificarArticulosDos extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblNombreArticulo = new Label("Nombre:");
	TextField txtNombreArticulo = new TextField(20);
	Label lblTamanioArticulo = new Label("Tamaño:");
	TextField txtTamanioArticulo = new TextField(20);
	Label lblDescripArticulo = new Label("Descripción:");
	TextField txtDescripArticulo = new TextField(20);
	Label lblPrecioArticulo = new Label("Precio:");
	TextField txtPrecioArticulo = new TextField(20);
	Label lblIdProveFK = new Label("Proveedor");
	TextField txtIdProveFK = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Connection con = null;
	Statement statement = null;
	ResultSet rs = null;
		
	String [] cadena;
	Dialog dlgMensaje = new Dialog(this,"Mensaje", true);
	Label mensaje = new Label("");
	
	int idArticuloModificar;
	Registros registros = new Registros();
	Login logUsuario = new Login();
	
	
	
	public ModificarArticulosDos(int idArticuloModificar)
	{
		this.idArticuloModificar = idArticuloModificar; 
		// Conectar BD
		Connection con = conectar();
		cadena= (consultarArticulo(con, idArticuloModificar)).split("-");
		// cadena[0] = idArticulo
		// cadena[1] = nombreArticulo
		// cadena[2] = tamanioArticulo
		// cadena[3] = descripcionArticulo
		// cadena[4] = precioArticulo
		// cadena[5] = idProveedorFK
		setTitle("Modificar Artículo");
		setLayout(new FlowLayout());
	
		add(lblNombreArticulo);
		txtNombreArticulo.setText(cadena[1]);
		add(txtNombreArticulo);
		
		add(lblTamanioArticulo);
		txtTamanioArticulo.setText(cadena[2]);
		add(txtTamanioArticulo);
		
		add(lblDescripArticulo);
		txtDescripArticulo.setText(cadena[3]);
		add(txtDescripArticulo);
		
		add(lblPrecioArticulo);
		txtPrecioArticulo.setText(cadena[4]);
		add(txtPrecioArticulo);
		
		add(lblIdProveFK);
		txtIdProveFK.setText(cadena[5]);
		add(txtIdProveFK);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		
		
		add(btnAceptar);
		add(btnLimpiar);
		addWindowListener(this);
		setSize(240,400); 
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(btnLimpiar.equals(arg0.getSource()))
		{
			txtNombreArticulo.selectAll();
			txtNombreArticulo.setText("");
			txtTamanioArticulo.selectAll();
			txtTamanioArticulo.setText("");
			txtDescripArticulo.selectAll();
			txtDescripArticulo.setText("");
			txtPrecioArticulo.selectAll();
			txtPrecioArticulo.setText("");
			txtIdProveFK.selectAll();
			txtIdProveFK.setText("");
		}
		else if(btnAceptar.equals(arg0.getSource()))// btnAceptar
		{
			String nombreArticulo=txtNombreArticulo.getText();
			String tamanioArticulo=txtTamanioArticulo.getText();
			String descripcionArticulo=txtDescripArticulo.getText();
			String precioArticulo=txtPrecioArticulo.getText();
			String idProveedorFK=txtIdProveFK.getText();	
			
			String usuario = logUsuario.txtUsuario.getText();
			
			Connection con = conectar();
			cadena= (consultarArticulo(con, idArticuloModificar)).split("-");
			// Hacer UPDATE
			String sentencia = "UPDATE articulos SET nombreArticulo ='"+nombreArticulo+
					"', tamanioArticulo = "+tamanioArticulo+
					", descripcionArticulo = '"+descripcionArticulo+
					"',precioArticulo = "+precioArticulo+
					",idProveedorFK = "+idProveedorFK+
					" WHERE idArticulo = "+idArticuloModificar;
			
			registros.registrarMovimiento(usuario,sentencia);
			
			// Mostrar cuadro de diálog
			if((modificarArticulo(con, sentencia))==0)
			{
				// Todo bien
				mensaje.setText("Modificación del artículo correcta");
				dlgMensaje.setTitle("Modificar Artículo");
				dlgMensaje.setSize(220,120);
				dlgMensaje.setLayout(new FlowLayout());
				dlgMensaje.addWindowListener(this);
				dlgMensaje.add(mensaje);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
			else
			{
				// Error
				mensaje.setText("Error en la Modificación del Artículo");
				dlgMensaje.setTitle("Modificar Artículo");
				dlgMensaje.setSize(180,120);
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

	@Override
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
		} catch (SQLException ex) 
		{
			System.out.println("ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();
		} catch (ClassNotFoundException cnfe) 
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		return con;
	}
	
	public String consultarArticulo(Connection c, int idArticulo)
	{
		String resultado = "";
		String usuario = logUsuario.txtUsuario.getText();
		try
		{
			String sentencia = "SELECT * FROM articulos where idArticulo="+idArticulo;
			//Crear una sentencia
			Statement  stm = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stm.executeQuery(sentencia);
			rs.next();
			
				resultado = rs.getInt("idArticulo") + "-" +
						rs.getString("nombreArticulo") + "-" +
						rs.getInt("tamanioArticulo") + "-" +
						rs.getString("descripcionArticulo")+ "-"+
						rs.getDouble("precioArticulo")+"-"+
						rs.getInt("idProveedorFK");
				
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


