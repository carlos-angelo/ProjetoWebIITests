package tech.ada.projetowebii.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.ada.projetowebii.exception.SerieNaoEncontradaException;

@ControllerAdvice
public class ControllerAdviceRest {

    @ExceptionHandler({SerieNaoEncontradaException.class})
    public ResponseEntity<Void> handlerSerieNaoEncontradaException(SerieNaoEncontradaException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.notFound().build();
    }


}
