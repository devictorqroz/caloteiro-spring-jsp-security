package com.caloteiros.caloteiro.domain.services;

import com.caloteiros.caloteiro.application.dto.CaloteiroPageDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.application.mapper.CaloteiroMapper;
import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import com.caloteiros.caloteiro.domain.exceptions.CaloteiroException;
import com.caloteiros.caloteiro.domain.repositories.CaloteiroRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaloteiroService {

    private final CaloteiroRepository caloteiroRepository;
    private final CaloteiroMapper caloteiroMapper;

    public CaloteiroService(CaloteiroRepository caloteiroRepository, CaloteiroMapper caloteiroMapper) {
        this.caloteiroRepository = caloteiroRepository;
        this.caloteiroMapper = caloteiroMapper;
    }

    public CaloteiroPageDTO list(@PositiveOrZero int pageNumber, @Positive @Max(100) int pageSize) {
        Page<Caloteiro> page = caloteiroRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<CaloteiroDTO> caloteiros = page.get().map(caloteiroMapper::toCaloteiroDTO).toList();

        return new CaloteiroPageDTO(
            caloteiros,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasPrevious(),
            page.hasNext(),
            page.isFirst(),
            page.isLast()
        );
    }

    public CaloteiroDTO findById(Long id) {
        return caloteiroRepository.findById(id)
                .map(caloteiroMapper::toCaloteiroDTO)
                .orElseThrow(() -> new CaloteiroException("Caloteiro não encontrado com o ID: " + id));
    }

    public void create(CreateCaloteiroDTO createCaloteiroDTO) {
        Caloteiro caloteiro = caloteiroMapper.fromCreateDTOToEntity(createCaloteiroDTO);
        caloteiroRepository.save(caloteiro);
    }

    public void deleteById(Long id) {
        caloteiroRepository.deleteById(id);
    }

    public void update(Long id, UpdateCaloteiroDTO updateCaloteiro) {
        Caloteiro caloteiro = caloteiroRepository.findById(id)
                .orElseThrow(() -> new CaloteiroException("Caloteiro não encontrado com o ID: " + id));

        caloteiro = caloteiroMapper.fromUpdateDTOToEntity(updateCaloteiro, caloteiro);
        caloteiroRepository.save(caloteiro);
    }
}
