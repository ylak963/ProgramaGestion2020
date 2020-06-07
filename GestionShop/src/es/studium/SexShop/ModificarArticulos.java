package es.studium.SexShop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ModificarArticulos extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Choice choArticulo = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Connection con = null;
	String [] cadena ;
	int idArticuloModificar = 0;

	ModificarArticulos()
	{
		setTitle("Modificar Articulo");
		setLayout(new FlowLayout());
		// Rellenar el Choice
		choArticulo.add("Seleccionar un artículo...");
		// Conectar BD
		Connection con = conectar();
		// Sacar los datos de la tabla articulos
		// Rellenar el Choice
		cadena=(consultarArticulosChoice(con)).split("#"); //
		for(int i=0; i<cadena.length; i++)
		{
			choArticulo.add(cadena[i]);
		}
		
		// Cerrar la conexión
		//desconectar(con);		
		add(choArticulo);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		add(btnAceptar);
		add(btnLimpiar);
		addWindowListener(this);
		setSize(400,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{		
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnLimpiar))
		{
			choArticulo.select(0);
		}
		else if(objetoPulsado.equals(btnAceptar))
		{
			if(choArticulo.getSelectedItem().equals("Seleccionar un articulo..."))
			{
				// No ocurriría nada
			}
			else
			{
				// Coger el elemento seleccionado
				String [] tabla = choArticulo.getSelectedItem().split("-");
				// El idArticulo que quiero editar está en tabla[0]
				idArticuloModificar = Integer.parseInt(tabla[0]);
				new ModificarArticulosDos(idArticuloModificar); 
			}


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

	
	public String consultarArticulosChoice(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM articulos";
			//Crear una sentencia
			Statement stmt = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stmt.executeQuery(sentencia);
			while (rs.next())
			{
				choArticulo.add/*resultado = */(rs.getInt("idArticulo")+
						"-"+rs.getString("nombreArticulo"));
				/*+
								", "+rs.getString("tamanioArticulo")+
								","+rs.getString("descripcionArticulo")+
								","+rs.getString("precioArticulo")+
								","+rs.getString("idProveedorFK"));*/
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
