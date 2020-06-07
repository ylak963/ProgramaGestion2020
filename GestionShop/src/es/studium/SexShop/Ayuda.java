package es.studium.SexShop;

import java.io.IOException;

public class Ayuda
{
	public Ayuda() {

		try {

			Runtime.getRuntime().exec("hh.exe Ayuda//Manual.chm");

		}catch(IOException e) {

			e.printStackTrace();
		}
	}
}
