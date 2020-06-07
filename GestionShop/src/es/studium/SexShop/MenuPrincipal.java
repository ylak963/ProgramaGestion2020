package es.studium.SexShop;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Frame menuPrincipal = new Frame("Menu Principal");
	MenuBar barraMenu = new MenuBar();
	
	Menu menuArticulos = new Menu("Artículos");
	Menu menuProveedores = new Menu("Proveedores");
	Menu menuReuniones = new Menu("Reuniones");
	Menu menuDisponen = new Menu("Disponen       ");
	Menu menuAyuda = new Menu ("Ayuda");
	
	MenuItem mniAltaArticulos = new MenuItem("Alta");
	MenuItem mniBajaArticulos = new MenuItem("Baja");
	MenuItem mniConsultaArticulos = new MenuItem("Consulta");
	MenuItem mniModificarArticulos = new MenuItem("Modificación");
	
	MenuItem mniAltaProveedores = new MenuItem("Alta");
	MenuItem mniBajaProveedores = new MenuItem("Baja");
	MenuItem mniConsultaProveedores = new MenuItem("Consulta");
	MenuItem mniModificarProveedores = new MenuItem("Modificación");
	
	MenuItem mniAltaReuniones = new MenuItem("Alta");
	MenuItem mniBajaReuniones = new MenuItem("Baja");
	MenuItem mniConsultaReuniones = new MenuItem("Consulta");
	MenuItem mniModificarReuniones = new MenuItem("Modificación");
	
	MenuItem mniAltaDisponen = new MenuItem("Alta");
	MenuItem mniBajaDisponen = new MenuItem("Baja");
	MenuItem mniConsultaDisponen = new MenuItem("Consulta");
	MenuItem mniModificarDisponen = new MenuItem("Modificación");
	
	MenuItem mniAyuda = new MenuItem("Ayuda");
	Login login = new Login();
	
	public MenuPrincipal()
	{
		
		setTitle("Gestión SexShop");
		setLayout(new FlowLayout());
		menuArticulos.add(mniAltaArticulos);
		menuArticulos.add(mniBajaArticulos);
		menuArticulos.add(mniConsultaArticulos);
		menuArticulos.add(mniModificarArticulos);
		
		menuProveedores.add(mniAltaProveedores);
		menuProveedores.add(mniBajaProveedores);
		menuProveedores.add(mniConsultaProveedores);
		menuProveedores.add(mniModificarProveedores);
		
		menuReuniones.add(mniAltaReuniones);
		menuReuniones.add(mniBajaReuniones);
		menuReuniones.add(mniConsultaReuniones);
		menuReuniones.add(mniModificarReuniones);
		
		menuDisponen.add(mniAltaDisponen);
		menuDisponen.add(mniBajaDisponen);
		menuDisponen.add(mniConsultaDisponen);
		menuDisponen.add(mniModificarDisponen);
		menuAyuda.add(mniAyuda);
		
				
		barraMenu.add(menuArticulos);
		barraMenu.add(menuProveedores);
		barraMenu.add(menuReuniones);
		barraMenu.add(menuDisponen);
		barraMenu.add(menuAyuda);
		
		
		addWindowListener(this);
		mniAltaArticulos.addActionListener(this);
		mniBajaArticulos.addActionListener(this);
		mniConsultaArticulos.addActionListener(this);
		mniModificarArticulos.addActionListener(this);
		
		mniAltaProveedores.addActionListener(this);
		mniBajaProveedores.addActionListener(this);
		mniConsultaProveedores.addActionListener(this);
		mniModificarProveedores.addActionListener(this);
		
		mniAltaReuniones.addActionListener(this);
		mniBajaReuniones.addActionListener(this);
		mniConsultaReuniones.addActionListener(this);
		mniModificarReuniones.addActionListener(this);
		
		mniAltaDisponen.addActionListener(this);
		mniBajaDisponen.addActionListener(this);
		mniConsultaDisponen.addActionListener(this);
		mniModificarDisponen.addActionListener(this);
		
		
		mniAyuda.addActionListener(this);
		
		setMenuBar(barraMenu);
		setSize(400,150);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String[] args)
	{
		new MenuPrincipal();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object objetoPulsado = e.getSource();
		//Eventos de Artículos.
		if(objetoPulsado.equals(mniAltaArticulos)) 
		{
			//Creamos un objeto nuevo de esta clase
			new AltaArticulos();
		}
		else if(objetoPulsado.equals(mniBajaArticulos)) 
		{
			new BajaArticulos();
		}
		else if(objetoPulsado.equals(mniConsultaArticulos)) 
		{
			new ConsultaArticulos();
		}
		else if(objetoPulsado.equals(mniModificarArticulos)) 
		{
			new ModificarArticulos();
		}
		//Eventos de Proveedores.
		else if(objetoPulsado.equals(mniAltaProveedores)) 
		{
			new AltaProveedores();
		}
		else if(objetoPulsado.equals(mniBajaProveedores)) 
		{
			new BajaProveedores();
		}
		else if(objetoPulsado.equals(mniConsultaProveedores)) 
		{
			new ConsultaProveedores();
		}
		else if(objetoPulsado.equals(mniModificarProveedores)) 
		{
			new ModificarProveedores();
		}
		//Eventos de Reuniones.
		else if(objetoPulsado.equals(mniAltaReuniones)) 
		{
			new AltaReuniones();
		}
		else if(objetoPulsado.equals(mniBajaReuniones)) 
		{
			new BajaReuniones();
		}
		else if(objetoPulsado.equals(mniConsultaReuniones)) 
		{
			new ConsultaReuniones();
		}
		else if(objetoPulsado.equals(mniModificarReuniones)) 
		{
			new ModificarReuniones();
		}
		//Eventos de Disponen.
		else if(objetoPulsado.equals(mniAltaDisponen)) 
		{
			new AltaDisponen();
		}
		else if(objetoPulsado.equals(mniBajaDisponen)) 
		{
			new BajaDisponen();
		}
		else if(objetoPulsado.equals(mniConsultaDisponen)) 
		{
			new ConsultaDisponen();
		}
		else if(objetoPulsado.equals(mniModificarDisponen)) 
		{
			new ModificarDisponen();
		}
		else if(objetoPulsado.equals(mniAyuda))
		{
			new Ayuda();
		}
		
	}
	//Procedicimiento para que el usuario solo pueda ver las altas
	public void soloAlta()
	{
		mniBajaArticulos.setEnabled(false);
		mniConsultaArticulos.setEnabled(false);
		mniModificarArticulos.setEnabled(false);
		mniBajaProveedores.setEnabled(false);
		mniConsultaProveedores.setEnabled(false);
		mniModificarProveedores.setEnabled(false);
		mniBajaReuniones.setEnabled(false);
		mniConsultaReuniones.setEnabled(false);
		mniModificarReuniones.setEnabled(false);
		mniBajaDisponen.setEnabled(false);
		mniConsultaDisponen.setEnabled(false);
		mniModificarDisponen.setEnabled(false);
		
	}
	
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e)
	{
		//Que vuelva a login
		/*
		if(this.hasFocus())
		{
			setVisible(false);
		}
		else
		{
			new Login();
		}*/
		System.exit(0); 
		
	}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}

	
}
