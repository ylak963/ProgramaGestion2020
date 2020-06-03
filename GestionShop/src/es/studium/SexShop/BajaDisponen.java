package es.studium.SexShop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BajaDisponen  extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblArticulo = new Label("Proveedor a borrar:");
	Choice choDisponen = new Choice();
	Button btnEliminar = new Button("Eliminar");
	Button btnLimpiar = new Button("Limpiar");
	Dialog seguro;
	Button btnSi;
	Button btnNo;
	
	Dialog dlgBajaProveedores = new Dialog (this,"Mensaje",true);
	Label mensaje = new Label("");

	Registros registros = new Registros();
	Login logUsuario = new Login();
	
	BajaDisponen()	
	{
		setTitle("Baja de disponen");
		setLayout(new FlowLayout());
		
		// Montar el Choice
		choDisponen.add("Seleccionar uno...");
		
		// Conectar a la base de datos
		Connection con = conectar();
		
		// Sacar los datos de la tabla proveedores
		// Rellenar el Choice
		String sqlSelect = "SELECT * FROM disponen";
		try 
		{
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			//Para sentencia SELECT
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				choDisponen.add(rs.getInt("idProveedor")+
						"-"+rs.getString("nombreProveedor"));
						/*+
						", "+rs.getString("telefonoProveedor")+
						","+rs.getString("generoProveedor"));*/
			}
			rs.close();
			stmt.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR: En la baja");
			ex.printStackTrace();
		}
		// Cerrar la conexión
		desconectar(con);
		
		btnEliminar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		add(choDisponen);
		add(btnEliminar);
		add(btnLimpiar);
		
		addWindowListener(this);
		setSize(290,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnLimpiar))
		{
			choDisponen.select(0);
		}
		else if(objetoPulsado.equals(btnEliminar))
		{
			seguro = new Dialog(this,"¿Seguro?", true);
			btnSi = new Button("Sí");
			btnNo = new Button("No");
			Label lblEtiqueta = new Label("¿Está seguro de eliminar?");
			seguro.setLayout(new FlowLayout());
			seguro.setSize(120,100);
			btnSi.addActionListener(this);
			btnNo.addActionListener(this);
			seguro.add(lblEtiqueta);
			seguro.add(btnSi);
			seguro.add(btnNo);
			seguro.addWindowListener(this);
			seguro.setResizable(false);
			seguro.setLocationRelativeTo(null);
			seguro.setVisible(true);
		}
		else if(objetoPulsado.equals(btnNo))
		{
			seguro.setVisible(false);
		}
		else if(objetoPulsado.equals(btnSi))
		{
			// Conectar a BD
			Connection con = conectar(); 
			// Borrar
			String[] Proveedor=choDisponen.getSelectedItem().split("-");
			int respuesta = borrar(con, Integer.parseInt(Proveedor[0]));
			// Mostramos resultado
			if(respuesta == 0)
			{
				mensaje.setText("Baja de proveedor correcta");
				dlgBajaProveedores.setTitle("Baja Proveedores");
				dlgBajaProveedores.setSize(190,120);
				dlgBajaProveedores.setLayout(new FlowLayout());
				dlgBajaProveedores.addWindowListener(this);
				dlgBajaProveedores.add(mensaje);
				dlgBajaProveedores.setLocationRelativeTo(null);
				dlgBajaProveedores.setVisible(true);
				System.out.println("Borrado del proveedor correcto");
			}
			else
			{
				mensaje.setText("Error en la baja de proveedores");
				dlgBajaProveedores.setTitle("Baja Proveedores");
				dlgBajaProveedores.setSize(190,120);
				dlgBajaProveedores.setLayout(new FlowLayout());
				dlgBajaProveedores.addWindowListener(this);
				dlgBajaProveedores.add(mensaje);
				dlgBajaProveedores.setLocationRelativeTo(null);
				dlgBajaProveedores.setVisible(true);
				System.out.println("Error en borrado del proveedor");
			}
			
			// Actualizar el Choice
			choDisponen.removeAll();
			choDisponen.add("Seleccionar uno...");
			String sqlSelect = "SELECT * FROM proveedores";
			try 
			{
				// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) 
				{
					choDisponen.add(rs.getInt("idProveedor")+
							"-"+rs.getString("nombreProveedor"));
							/*+
							", "+rs.getString("telefonoProveedor")+
							","+rs.getString("generoProveedor"));*/
				}
				rs.close();
				stmt.close();
			} 
			catch (SQLException ex) 
			{
				System.out.println("ERROR:al consultar");
				ex.printStackTrace();
			}
			// Desconectar
			desconectar(con);
			seguro.setVisible(false);
		}
	}
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e)
	{
		setVisible(false);
		/*
		if(this.isActive())
		{
			setVisible(false);
		}
		else
		{
			seguro.setVisible(false);
		}*/
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
	public int borrar(Connection con, int idProveedor)
	{
		int respuesta = 0;
		String sql = "DELETE FROM proveedores WHERE idProveedor = " + idProveedor;
		String usuario = logUsuario.txtUsuario.getText();
		System.out.println(sql);
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			
			//Registros de movimientos de usuarios
			registros.registrarMovimiento(usuario,sql);
			sta.executeUpdate(sql);
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer un Delete");
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
