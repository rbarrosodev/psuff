package com.rodrigobarroso.anotacao;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Autowired {
	// Essa anotação Autowired tem retenção de Runtime e target Field (o que é anotado é um campo em tempo de execução)
	// e é utilizada nos campos tipo ClasseDAO para que eles possam ser inicializados de maneira correta.
	// (ver arquivo AeroportoAppServiceImpl).

	Class<?>[] rollbackFor() default {};
}
