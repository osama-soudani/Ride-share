package com.rideshare.sos.service;

import com.rideshare.sos.common.exception.ConflictException;
import com.rideshare.sos.common.exception.ResourceNotFoundException;
import com.rideshare.sos.dto.request.MecanicienRequest;
import com.rideshare.sos.dto.response.MecanicienResponse;
import com.rideshare.sos.entity.Mecanicien;
import com.rideshare.sos.mapper.MecanicienMapper;
import com.rideshare.sos.repository.DemandeSOSRepository;
import com.rideshare.sos.repository.InterventionRepository;
import com.rideshare.sos.repository.MecanicienRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MecanicienService {

    private final MecanicienRepository repository;
    private final DemandeSOSRepository demandeRepository;
    private final InterventionRepository interventionRepository;

    public MecanicienService(MecanicienRepository repository,
                             DemandeSOSRepository demandeRepository,
                             InterventionRepository interventionRepository) {
        this.repository = repository;
        this.demandeRepository = demandeRepository;
        this.interventionRepository = interventionRepository;
    }

    public MecanicienResponse create(MecanicienRequest request) {
        return MecanicienMapper.toResponse(repository.save(MecanicienMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public List<MecanicienResponse> findAll() {
        return repository.findAll().stream().map(MecanicienMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MecanicienResponse findById(Long id) {
        return MecanicienMapper.toResponse(getEntity(id));
    }

    public MecanicienResponse update(Long id, MecanicienRequest request) {
        Mecanicien m = getEntity(id);
        MecanicienMapper.updateEntity(m, request);
        return MecanicienMapper.toResponse(repository.save(m));
    }

    public void delete(Long id) {
        Mecanicien m = getEntity(id);
        if (demandeRepository.existsByIdMecanicien(id) || interventionRepository.existsByIdMecanicien(id)) {
            throw new ConflictException("Mechanic " + id + " is referenced by SOS requests or interventions");
        }
        repository.delete(m);
    }

    Mecanicien getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mecanicien", id));
    }
}