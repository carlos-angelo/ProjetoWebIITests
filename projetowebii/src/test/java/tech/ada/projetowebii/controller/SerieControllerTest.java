package tech.ada.projetowebii.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.projetowebii.dto.AtualizarSerieDTO;
import tech.ada.projetowebii.dto.CriarSerieRequestDTO;
import tech.ada.projetowebii.dto.SerieDTO;
import tech.ada.projetowebii.exception.SerieNaoEncontradaException;
import tech.ada.projetowebii.model.Serie;
import tech.ada.projetowebii.service.AtualizarSerieService;
import tech.ada.projetowebii.service.BuscarSeriesService;
import tech.ada.projetowebii.service.CriarSerieService;
import tech.ada.projetowebii.service.ExcluirSerieService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SerieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CriarSerieService criarSerieService;

    @Mock
    private AtualizarSerieService atualizarSerieService;

    @Mock
    private BuscarSeriesService buscarSeriesService;

    @Mock
    private ExcluirSerieService excluirSerieService;

    @InjectMocks
    private SerieController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new tech.ada.projetowebii.config.ControllerAdviceRest())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarSerie() throws Exception {
        // Arrange
        CriarSerieRequestDTO requestDTO = new CriarSerieRequestDTO("Breaking Bad", "Drama", 5, 2008);
        Serie serieCriada = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);

        when(criarSerieService.criarSerie(any(Serie.class))).thenReturn(serieCriada);

        // Act & Assert
        mockMvc.perform(post("/series")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Breaking Bad")))
                .andExpect(jsonPath("$.genero", is("Drama")))
                .andExpect(jsonPath("$.temporadas", is(5)))
                .andExpect(jsonPath("$.anoLancamento", is(2008)));

        verify(criarSerieService, times(1)).criarSerie(any(Serie.class));
    }

    @Test
    void deveAtualizarSerie() throws Exception {
        // Arrange
        AtualizarSerieDTO requestDTO = new AtualizarSerieDTO("Better Call Saul", "Drama/Comédia", 6, 2015);
        Serie serieAtualizada = new Serie(1L, "Better Call Saul", "Drama/Comédia", 6, 2015);

        when(atualizarSerieService.atualizar(eq(1L), any(AtualizarSerieDTO.class))).thenReturn(serieAtualizada);

        // Act & Assert
        mockMvc.perform(put("/series/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Better Call Saul")))
                .andExpect(jsonPath("$.genero", is("Drama/Comédia")))
                .andExpect(jsonPath("$.temporadas", is(6)))
                .andExpect(jsonPath("$.anoLancamento", is(2015)));

        verify(atualizarSerieService, times(1)).atualizar(eq(1L), any(AtualizarSerieDTO.class));
    }

    @Test
    void deveExcluirSerie() throws Exception {
        // Arrange
        doNothing().when(excluirSerieService).excluir(1L);

        // Act & Assert
        mockMvc.perform(delete("/series/1"))
                .andExpect(status().isNoContent());

        verify(excluirSerieService, times(1)).excluir(1L);
    }

    @Test
    void deveBuscarTodasAsSeries() throws Exception {
        // Arrange
        List<SerieDTO> series = Arrays.asList(
            new SerieDTO("Breaking Bad", "Drama", 5, 2008),
            new SerieDTO("Game of Thrones", "Fantasia", 8, 2011)
        );

        when(buscarSeriesService.buscarTodasAsSeries()).thenReturn(series);

        // Act & Assert
        mockMvc.perform(get("/series"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo", is("Breaking Bad")))
                .andExpect(jsonPath("$[0].genero", is("Drama")))
                .andExpect(jsonPath("$[0].temporadas", is(5)))
                .andExpect(jsonPath("$[0].anoLancamento", is(2008)))
                .andExpect(jsonPath("$[1].titulo", is("Game of Thrones")))
                .andExpect(jsonPath("$[1].genero", is("Fantasia")))
                .andExpect(jsonPath("$[1].temporadas", is(8)))
                .andExpect(jsonPath("$[1].anoLancamento", is(2011)));

        verify(buscarSeriesService, times(1)).buscarTodasAsSeries();
    }

    @Test
    void deveBuscarSeriePorId() throws Exception {
        // Arrange
        Serie serie = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);

        when(buscarSeriesService.buscarSeriePorId(1L)).thenReturn(serie);

        // Act & Assert
        mockMvc.perform(get("/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Breaking Bad")))
                .andExpect(jsonPath("$.genero", is("Drama")))
                .andExpect(jsonPath("$.temporadas", is(5)))
                .andExpect(jsonPath("$.anoLancamento", is(2008)));

        verify(buscarSeriesService, times(1)).buscarSeriePorId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSerieNaoEncontrada() throws Exception {
        // Arrange
        when(buscarSeriesService.buscarSeriePorId(99L)).thenThrow(new SerieNaoEncontradaException("Série com ID 99 não encontrada"));

        // Act & Assert
        mockMvc.perform(get("/series/99"))
                .andExpect(status().isNotFound());

        verify(buscarSeriesService, times(1)).buscarSeriePorId(99L);
    }
}
