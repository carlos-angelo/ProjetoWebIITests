package tech.ada.projetowebii.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.ada.projetowebii.dto.AtualizarSerieDTO;
import tech.ada.projetowebii.model.Serie;
import tech.ada.projetowebii.repository.SeriesRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizarSerieServiceTest {

    @Mock
    private SeriesRepository repository;

    @InjectMocks
    private AtualizarSerieService service;

    private Serie serieExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serieExistente = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);
    }

    @Test
    void deveAtualizarTodosOsCampos() {
        // Arrange
        AtualizarSerieDTO dto = new AtualizarSerieDTO("Better Call Saul", "Drama/Comédia", 6, 2015);
        
        when(repository.findById(1L)).thenReturn(Optional.of(serieExistente));
        when(repository.save(any(Serie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Serie resultado = service.atualizar(1L, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Better Call Saul", resultado.getTitulo());
        assertEquals("Drama/Comédia", resultado.getGenero());
        assertEquals(6, resultado.getTemporadas());
        assertEquals(2015, resultado.getAnoLancamento());
        
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Serie.class));
    }

    @Test
    void deveAtualizarApenasOsCamposNaoNulos() {
        // Arrange
        AtualizarSerieDTO dto = new AtualizarSerieDTO(null, "Drama/Suspense", null, 2009);
        
        when(repository.findById(1L)).thenReturn(Optional.of(serieExistente));
        when(repository.save(any(Serie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Serie resultado = service.atualizar(1L, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Breaking Bad", resultado.getTitulo()); // Não alterado
        assertEquals("Drama/Suspense", resultado.getGenero()); // Alterado
        assertEquals(5, resultado.getTemporadas()); // Não alterado
        assertEquals(2009, resultado.getAnoLancamento()); // Alterado
        
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Serie.class));
    }

    @Test
    void deveLancarExcecaoQuandoSerieNaoEncontrada() {
        // Arrange
        AtualizarSerieDTO dto = new AtualizarSerieDTO("Better Call Saul", "Drama/Comédia", 6, 2015);
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            service.atualizar(99L, dto);
        });
        
        verify(repository, times(1)).findById(99L);
        verify(repository, never()).save(any(Serie.class));
    }
}