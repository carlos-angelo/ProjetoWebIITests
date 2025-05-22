package tech.ada.projetowebii.dto.mapper;

import org.junit.jupiter.api.Test;
import tech.ada.projetowebii.dto.CriarSerieRequestDTO;
import tech.ada.projetowebii.model.Serie;

import static org.junit.jupiter.api.Assertions.*;

class CriarSerieRequestMapperTest {

    @Test
    void deveConverterDTOParaEntidade() {
        // Arrange
        CriarSerieRequestDTO dto = new CriarSerieRequestDTO("Breaking Bad", "Drama", 5, 2008);
        
        // Act
        Serie serie = CriarSerieRequestMapper.toEntity(dto);
        
        // Assert
        assertNotNull(serie);
        assertNull(serie.getId()); // ID deve ser nulo pois é uma nova entidade
        assertEquals("Breaking Bad", serie.getTitulo());
        assertEquals("Drama", serie.getGenero());
        assertEquals(5, serie.getTemporadas());
        assertEquals(2008, serie.getAnoLancamento());
    }
}