package com.rideshare.sos.service;

import com.rideshare.sos.common.exception.ConflictException;
import com.rideshare.sos.common.exception.ResourceNotFoundException;
import com.rideshare.sos.dto.request.InterventionRequest;
import com.rideshare.sos.dto.response.InterventionResponse;
import com.rideshare.sos.entity.Intervention;
import com.rideshare.sos.mapper.InterventionMapper;
import com.rideshare.sos.repository.DemandeSOSRepository;
import com.rideshare.sos.repository.InterventionRepository;
import com.rideshare.sos.repository.MecanicienRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InterventionService {

    private final InterventionRepository repository;
    private final DemandeSOSRepository demandeRepository;
    private final MecanicienRepository mecanicienRepository;

    public InterventionService(InterventionRepository repository,
                               DemandeSOSRepository demandeRepository,
                               MecanicienRepository mecanicienRepository) {
        this.repository = repository;
        this.demandeRepository = demandeRepository;
        this.mecanicienRepository = mecanicienRepository;
    }

    public InterventionResponse create(InterventionRequest request) {
        checkReferences(request);
        if (repository.existsByIdDemande(request.idDemande())) {
            throw new ConflictException("SOS request " + request.idDemande() + " already has an intervention");
        }
        return InterventionMapper.toResponse(repository.save(InterventionMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> findAll() {
        return repository.findAll().stream().map(InterventionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public InterventionResponse findById(Long id) {
        return InterventionMapper.toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public InterventionResponse findByDemande(Long idDemande) {
        return repository.findByIdDemande(idDemande)
                .map(InterventionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention for request", idDemande));
    }

    public InterventionResponse update(Long id, InterventionRequest request) {
        Intervention i = getEntity(id);
        checkReferences(request);
        if (!i.getIdDemande().equals(request.idDemande()) && repository.existsByIdDemande(request.idDemande())) {
            throw new ConflictException("SOS request " + request.idDemande() + " already has an intervention");
        }
        InterventionMapper.updateEntity(i, request);
        return InterventionMapper.toResponse(repository.save(i));
    }

    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private void checkReferences(InterventionRequest request) {
        if (!demandeRepository.existsById(request.idDemande())) {
            throw new ResourceNotFoundException("DemandeSOS", request.idDemande());
        }
        if (!mecanicienRepository.existsById(request.idMecanicien())) {
            throw new ResourceNotFoundException("Mecanicien", request.idMecanicien());
        }
    }

    private Intervention getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));
    }
}