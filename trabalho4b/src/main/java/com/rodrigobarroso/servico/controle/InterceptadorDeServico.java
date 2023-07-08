package com.rodrigobarroso.servico.controle;

import com.rodrigobarroso.anotacao.Transactional;
import com.rodrigobarroso.util.JPAUtil;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class InterceptadorDeServico implements MethodInterceptor {
    public Object intercept(Object objeto, Method metodo, Object[] args, MethodProxy metodoOriginal) throws Throwable {
        try {
            if (metodo.isAnnotationPresent(Transactional.class)) {
                JPAUtil.beginTransaction();
            }

            System.out.println(
                    "Método interceptado: " + metodo.getName() + " da classe " + metodo.getDeclaringClass().getName()
            );

            Object obj = metodoOriginal.invokeSuper(objeto, args);

            if (metodo.isAnnotationPresent(Transactional.class)) {
                JPAUtil.commitTransaction();
            }

            return obj;
        }
        catch (RuntimeException e) {
            if (metodo.isAnnotationPresent(Transactional.class)) {
                JPAUtil.rollbackTransaction();
            }
            throw e;
        }
        catch (Exception e) {
            if (metodo.isAnnotationPresent(Transactional.class)) {
                Class<?>[] classes = metodo.getAnnotation(Transactional.class).rollbackFor();
                boolean achou = false;
                for (Class<?> classe : classes) {
                    if (classe.isInstance(e)) {
                        achou = true;
                        break;
                    }
                }
                if (achou) {
                    JPAUtil.rollbackTransaction();
                } else {
                    JPAUtil.commitTransaction();
                }
            }
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }
}
