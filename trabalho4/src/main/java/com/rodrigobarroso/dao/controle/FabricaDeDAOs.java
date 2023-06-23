package com.rodrigobarroso.dao.controle;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FabricaDeDAOs {
	private static ResourceBundle prop;

	static {
		try {
			prop = ResourceBundle.getBundle("dao");
		} catch (MissingResourceException e) {
			System.out.println("Aquivo dao.properties não encontrado.");
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDAO(Class<T> tipo) {
		T dao = null;
		String nomeDaClasse = null;

		try {
			nomeDaClasse = prop.getString(tipo.getSimpleName());
			dao = (T) Class.forName(nomeDaClasse).newInstance();
		} catch (InstantiationException e) {
			System.out.println("Não foi possível criar um objeto do tipo " + nomeDaClasse);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("Não foi possível criar um objeto do tipo " + nomeDaClasse);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("Classe " + nomeDaClasse + " não encontrada");
			throw new RuntimeException(e);
		} catch (MissingResourceException e) {
			System.out.println("Chave " + tipo + " não encontrada em dao.properties");
			throw new RuntimeException(e);
		}

		return dao;
	}
}
