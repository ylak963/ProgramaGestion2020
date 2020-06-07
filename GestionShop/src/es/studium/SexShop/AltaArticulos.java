package es.studium.SexShop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
//import java.awt.Choice;
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

public class AltaArticulos extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/empresanueva?useSSL=false&allowPublicKeyRetrieval=true";
	String login = "remotoEmpresa";
	String password = "1234";
	Connection conexion= null;
	Statement statement = null;
	ResultSet rs = null;
	
	//Declaración de los componentes que se incluirán en el frame
	Label lblNombreArticulo = new Label("Nombre:");
	TextField txtNombreArticulo = new TextField(20);
	Label lblTamanioArticulo = new Label("Tamaño:");
	TextField txtTamanioArticulo = new TextField(20);
	Label lblDescripArticulo = new Label("Descripción:");
	TextField txtDescripArticulo = new TextField(20);
	Label lblPrecioArticulo = new Label("Precio:");
	TextField txtPrecioArticulo = new TextField(20);
		
	Label lblProveedor = new Label("Proveedor");
	Choice choidProvFK = new Choice();
	String[] cadena;
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	Button btnAceptarYotro = new Button("Aceptar y otro");
	
	Dialog dlgAltaArticulos = new Dialog(this,"Mensaje",true);
	Label mensaje = new Label("");
	Registros registros = new Registros();
	Login logUsuario = new Login();
	Login loginn = new Login();
	
	
	//Constructor donde se añaden los componentes y los listener
	AltaArticulos()
	{
		
		setTitle("Alta de Artículos");
		setLayout(new FlowLayout());
		add(lblNombreArticulo);
		add(txtNombreArticulo);
		add(lblTamanioArticulo);
		add(txtTamanioArticulo);
		add(lblDescripArticulo);
		add(txtDescripArticulo);
		add(lblPrecioArticulo);
		add(txtPrecioArticulo);
		
		
		//Rellenar el Choice
		add(lblProveedor);
		choidProvFK.add("Seleccionar un proveedor");
		
		//Conectar a la base de datos
		Connection con = conectar();
		cadena = (consultarProveedoresChoice(con)).split("#");
		for(int i = 0; i < cadena.length; i++)
		{
			choidProvFK.add(cadena[i]);
		}
		
		add(choidProvFK);
		
					
		add(btnAceptar);
		add(btnAceptarYotro);
		add(btnLimpiar);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		btnAceptarYotro.addActionListener(this);
		addWindowListener(this);
		setSize(310,220);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}

	//Eventos 
	public void actionPerformed(ActionEvent e)
	{
		//Objeto llamado objetopulsado para que capture la fuente donde se origina el evento
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnAceptar))
		{			
			// Conectar BD
			Connection con = conectar();
			//Proceder el INSERT
			//String[] idProveedorFK = choidProvFK.getSelectedItem().split("-");
			
			int respuesta = insertar(con, "articulos", txtNombreArticulo.getText(),
					txtTamanioArticulo.getText(),txtDescripArticulo.getText(),
					txtPrecioArticulo.getText(), choidProvFK.getSelectedItem());
						
			// Mostramos resultado
			if(respuesta == 0)
			{
				mensaje.setText("Alta de artículos correcta");
				dlgAltaArticulos.setTitle("Alta Articulos");
				dlgAltaArticulos.setSize(180,120);
				dlgAltaArticulos.setLayout(new FlowLayout());
				dlgAltaArticulos.addWindowListener(this);
				dlgAltaArticulos.add(mensaje);
				dlgAltaArticulos.setLocationRelativeTo(null);
				dlgAltaArticulos.setVisible(true);
				System.out.println("ALTA de artículo correcta");
			}
			else
			{
				mensaje.setText("Error en Alta de artículos");
				dlgAltaArticulos.setTitle("Alta Articulos");
				dlgAltaArticulos.setSize(180,120);
				dlgAltaArticulos.setLayout(new FlowLayout());
				dlgAltaArticulos.addWindowListener(this);
				dlgAltaArticulos.add(mensaje);
				dlgAltaArticulos.setLocationRelativeTo(null);
				dlgAltaArticulos.setVisible(true);
				System.out.println("Error en Alta de artículo");
			}
			// Desconectar de la base
			desconectar(con);

		}
		else if(objetoPulsado.equals(btnAceptarYotro))
		{
			// Conectar BD
			Connection con = conectar();
			//Proceder el INSERT
			//String proveedorFK=choidProvFK.getSelectedItem();
			//String [] tabla = proveedorFK.split("-");
			int respuesta = insertar(con, "articulos", txtNombreArticulo.getText(),
					txtTamanioArticulo.getText(),txtDescripArticulo.getText(),
					txtPrecioArticulo.getText(), choidProvFK.getSelectedItem());
			//choProveedorFK.getSelectedItem
			// Mostramos resultado
			if(respuesta == 0)
			{
				System.out.println("ALTA de artículo correcta");
				txtNombreArticulo.selectAll();
				txtNombreArticulo.setText("");
				txtNombreArticulo.requestFocus();

				txtTamanioArticulo.selectAll();
				txtTamanioArticulo.setText("");


				txtDescripArticulo.selectAll();
				txtDescripArticulo.setText("");


				txtPrecioArticulo.selectAll();
				txtPrecioArticulo.setText("");

				choidProvFK.select(0);
			}
			else
			{
				System.out.println("Error en Alta de artículo");//
			}
			// Desconectar de la base
			desconectar(con);

		}
		else  if(objetoPulsado.equals(btnLimpiar))
		{
			txtNombreArticulo.selectAll();
			txtNombreArticulo.setText("");
			txtNombreArticulo.requestFocus();

			txtTamanioArticulo.selectAll();
			txtTamanioArticulo.setText("");


			txtDescripArticulo.selectAll();
			txtDescripArticulo.setText("");


			txtPrecioArticulo.selectAll();
			txtPrecioArticulo.setText("");
			
			choidProvFK.select(0);
			
		}
	}
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e)
	{
		//Solo cerramos esta ventana
		setVisible(false);
		//Cerramos ventana de login
		
	}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	//Fin de eventos
	
	//Función conectar
	public Connection conectar()
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/gestionshop?autoReconnect=true&useSSL=false";
		String usuario = "root";
		String password = "1234";
		Connection con = null;
		try 
		{
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			// Establecer la conexión con la BD empresa
			con = DriverManager.getConnection(url, usuario, password);
			if (con != null) 
			{
				System.out.println("Conectado a la base de datos");
			}
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();
		} catch (ClassNotFoundException cnfe) 
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		return con;
	}
	//Función insertar
	public int insertar(Connection con, String articulos, String nombreArticulo, 
			String tamanioArticulo, String descripcionArticulo, String precioArticulo,
			String choidProvFK) 
	{
		String usuario = logUsuario.txtUsuario.getText();
		int respuesta = 0;
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + articulos  
					+" VALUES (null, '" + nombreArticulo + "', "
					+ tamanioArticulo + ", "
					+ "'"+descripcionArticulo + "'"+ ", "	
					+ precioArticulo + ","  
					+ choidProvFK +");";

			System.out.println(cadenaSQL);
			//Registros de movimientos de usuarios
			registros.registrarMovimiento(usuario,cadenaSQL);
			logUsuario.login.setVisible(false);
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
	
	public String consultarProveedoresChoice(Connection c)
	{
		String resultado = "";
		String usuario = logUsuario.txtUsuario.getText();
		try
		{
			String sentencia = "SELECT * FROM proveedores";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idProveedor") /*+ "-" +
						rs.getString("nombreProveedor")*/+"#";
			}
			//Registros de movimientos de usuarios
			registros.registrarMovimiento(usuario,sentencia);
			logUsuario.login.setVisible(false);
									
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	//Procedimiento conectar
	public void desconectar(Connection con)
	{
		try
		{
			con.close();
		}
		catch(Exception e) {}
	}
}
