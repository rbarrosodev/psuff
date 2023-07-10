package com.rodrigobarroso.anotacao;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface RecuperaObjeto {
    // Essa anotação é utilizada em alguns métodos (métodos que retornem um objeto) do AeroportoDAO
    // para que seja interceptado pelo InterceptadorDeDAO, e execute o método 'busca' do DAOGenerico.
}
