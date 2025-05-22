package tech.ada.projetowebii.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.ada.projetowebii.model.Serie;
import tech.ada.projetowebii.repository.SeriesRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarSerieServiceTest {

    @Mock
    private SeriesRepository repository;

    @InjectMocks
    private CriarSerieService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarSerie() {
        // Arrange
        Serie serie = new Serie(null, "Breaking Bad", "Drama", 5, 2008);
        Serie serieSalva = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);
        
        when(repository.save(serie)).thenReturn(serieSalva);

        // Act
        Serie resultado = service.criarSerie(serie);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Breaking Bad", resultado.getTitulo());
        assertEquals("Drama", resultado.getGenero());
        assertEquals(5, resultado.getTemporadas());
        assertEquals(2008, resultado.getAnoLancamento());
        
        verify(repository, times(1)).save(serie);
    }
}