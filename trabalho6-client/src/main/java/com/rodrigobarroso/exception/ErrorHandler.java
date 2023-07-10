package com.rodrigobarroso.exception;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {
        // HttpStatus.Series.INFORMATIONAL(1),
        // HttpStatus.Series.SUCCESSFUL(2),
        // HttpStatus.Series.REDIRECTION(3),
        // HttpStatus.Series.CLIENT_ERROR(4),
        // HttpStatus.Series.SERVER_ERROR(5);
        return httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        // Esse método será executado apenas se o método acima tiver retornado true

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            ErrorResponse error = getErrorResponse(httpResponse);
            System.out.println(error);
            throw new RuntimeException(error.getMessage());
        }
        else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            // Uma informação errada fornecida pelo cliente provocou o erro.
            if (httpResponse.getStatusCode().value() == 404) {
                // Recuperando o objeto ErrorResponse que foi enviado pelo servidor à aplicação cliente
                ErrorResponse error = getErrorResponse(httpResponse);
                // Lançando a exceção que a aplicação cliente deverá capturar.
                // Estamos enviando para a aplicação cliente a mesma mensagem enviada pelo servidor.
                throw new EntidadeNaoEncontradaException(error.getMessage());
            }
            else if (httpResponse.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
                ErrorResponse error = getErrorResponse(httpResponse);
                String mensagem = "";
                for (String msg : error.getMap().values()) {
                    mensagem += msg + " ";
                }
                // Não utilizei aqui a exceção ConstraintViolationException pelo fato do
                // seu construtor esperar receber um Set de objetos do tipo ConstraintViolation
                throw new ViolacaoDeConstraintException(mensagem);
            }
            else if (httpResponse.getStatusCode().equals(HttpStatus.PRECONDITION_FAILED)) {
                // Recuperando o objeto ErrorResponse que foi enviado pelo servidor à aplicação cliente
                ErrorResponse error = getErrorResponse(httpResponse);
                // Lançando a exceção que a aplicação cliente deverá capturar.
                // Estamos enviando para a aplicação cliente a mesma mensagem enviada pelo servidor.
                throw new EstadoDeObjetoObsoletoException(error.getMessage());
            }
            throw new RuntimeException(
                    ">>>> Ocorreu um erro inesperado - statusCode = " +
                            httpResponse.getStatusCode().value());
        }
    }

    private ErrorResponse getErrorResponse(ClientHttpResponse httpResponse)
            throws IOException {
        // Abaixo estamos criando o objeto mapper que será utilizado para converter
        // o corpo da resposta http recebido em um objeto do tipo ErrorResponse.

        // Criando o objeto mapper assim: ObjectMapper mapper = new ObjectMapper();
        // Não irá funcionar para LocalDateTime (Java 8).

        // Para entender o código abaixo é preciso entender como como funciona o
        // padrão de projeto Builder.
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        // O método readValue() do objeto mapper irá mapear o JSON contido no corpo
        // da resposta http em um objeto do tipo ErrorResponse.
        ErrorResponse error = mapper.readValue(httpResponse.getBody(), ErrorResponse.class);
        return error;
    }
}