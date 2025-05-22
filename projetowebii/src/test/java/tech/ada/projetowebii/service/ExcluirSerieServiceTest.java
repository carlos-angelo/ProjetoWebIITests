package tech.ada.projetowebii.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.ada.projetowebii.model.Serie;
import tech.ada.projetowebii.repository.SeriesRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcluirSerieServiceTest {

    @Mock
    private SeriesRepository repository;

    @InjectMocks
    private ExcluirSerieService service;

    private Serie serieExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serieExistente = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);
    }

    @Test
    void deveExcluirSerie() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(serieExistente));
        doNothing().when(repository).delete(serieExistente);

        // Act
        service.excluir(1L);

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(serieExistente);
    }

    @Test
    void deveLancarExcecaoQuandoSerieNaoEncontrada() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            service.excluir(99L);
        });
        
        verify(repository, times(1)).findById(99L);
        verify(repository, never()).delete(any(Serie.class));
    }
}