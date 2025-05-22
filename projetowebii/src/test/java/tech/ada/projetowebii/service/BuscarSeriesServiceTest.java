package tech.ada.projetowebii.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.ada.projetowebii.dto.SerieDTO;
import tech.ada.projetowebii.exception.SerieNaoEncontradaException;
import tech.ada.projetowebii.model.Serie;
import tech.ada.projetowebii.repository.SeriesRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarSeriesServiceTest {

    @Mock
    private SeriesRepository repository;

    @InjectMocks
    private BuscarSeriesService service;

    private Serie serie1;
    private Serie serie2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        serie1 = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);
        serie2 = new Serie(2L, "Game of Thrones", "Fantasia", 8, 2011);
    }

    @Test
    void deveBuscarTodasAsSeries() {
        // Arrange
        List<Serie> series = Arrays.asList(serie1, serie2);
        when(repository.findAll()).thenReturn(series);

        // Act
        List<SerieDTO> resultado = service.buscarTodasAsSeries();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        assertEquals("Breaking Bad", resultado.get(0).getTitulo());
        assertEquals("Drama", resultado.get(0).getGenero());
        assertEquals(5, resultado.get(0).getTemporadas());
        assertEquals(2008, resultado.get(0).getAnoLancamento());
        
        assertEquals("Game of Thrones", resultado.get(1).getTitulo());
        assertEquals("Fantasia", resultado.get(1).getGenero());
        assertEquals(8, resultado.get(1).getTemporadas());
        assertEquals(2011, resultado.get(1).getAnoLancamento());
        
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveBuscarSeriePorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(serie1));

        // Act
        Serie resultado = service.buscarSeriePorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Breaking Bad", resultado.getTitulo());
        assertEquals("Drama", resultado.getGenero());
        assertEquals(5, resultado.getTemporadas());
        assertEquals(2008, resultado.getAnoLancamento());
        
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSerieNaoEncontrada() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SerieNaoEncontradaException.class, () -> {
            service.buscarSeriePorId(99L);
        });
        
        verify(repository, times(1)).findById(99L);
    }
}