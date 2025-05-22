package tech.ada.projetowebii.dto.mapper;

import org.junit.jupiter.api.Test;
import tech.ada.projetowebii.dto.SerieDTO;
import tech.ada.projetowebii.model.Serie;

import static org.junit.jupiter.api.Assertions.*;

class SerieMapperTest {

    @Test
    void deveConverterEntidadeParaDTO() {
        // Arrange
        Serie serie = new Serie(1L, "Breaking Bad", "Drama", 5, 2008);
        
        // Act
        SerieDTO dto = SerieMapper.toSerieDTO(serie);
        
        // Assert
        assertNotNull(dto);
        assertEquals("Breaking Bad", dto.getTitulo());
        assertEquals("Drama", dto.getGenero());
        assertEquals(5, dto.getTemporadas());
        assertEquals(2008, dto.getAnoLancamento());
        // Note que o ID não é transferido para o DTO
    }
}