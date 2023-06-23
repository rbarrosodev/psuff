package com.rodrigobarroso.servico.controle;

import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;

import com.rodrigobarroso.anotacao.Autowired;
import com.rodrigobarroso.dao.controle.FabricaDeDAOs;
import net.sf.cglib.proxy.Enhancer;

public class FabricaDeServico {
    public static <T> T getServico(Class<T> tipo) {

        Reflections reflections = new Reflections("servico.impl");

        Set<Class<? extends T>> classes = reflections.getSubTypesOf(tipo);

        if (classes.size() > 1)
            throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());

        Class<?> classe = classes.iterator().next();

        T servico = tipo.cast(Enhancer.create(classe, new InterceptadorDeServico()));

        Field[] campos = classe.getDeclaredFields();
        for (Field campo : campos) {
            if (campo.isAnnotationPresent(Autowired.class)) {
                campo.setAccessible(true);
                try {
                    campo.set(servico, FabricaDeDAOs.getDAO(campo.getType()));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return servico;
    }
}
