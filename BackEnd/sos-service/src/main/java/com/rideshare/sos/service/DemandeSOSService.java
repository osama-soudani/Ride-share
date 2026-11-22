package com.rideshare.sos.service;

import com.rideshare.sos.common.exception.ConflictException;
import com.rideshare.sos.common.exception.ResourceNotFoundException;
import com.rideshare.sos.dto.request.DemandeSOSRequest;
import com.rideshare.sos.dto.request.DemandeSOSUpdateRequest;
import com.rideshare.sos.dto.response.DemandeSOSResponse;
import com.rideshare.sos.entity.DemandeSOS;
import com.rideshare.sos.entity.enums.StatutDemandeSOS;
import com.rideshare.sos.mapper.DemandeSOSMapper;
import com.rideshare.sos.repository.DemandeSOSRepository;
import com.rideshare.sos.repository.InterventionRepository;
import com.rideshare.sos.repository.MecanicienRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class DemandeSOSService {

    private final DemandeSOSRepository repository;
    private final MecanicienRepository mecanicienRepository;
    private final InterventionRepository interventionRepository;

    public DemandeSOSService(DemandeSOSRepository repository,
                             MecanicienRepository mecanicienRepository,
                             InterventionRepository interventionRepository) {
        this.repository = repository;
        this.mecanicienRepository = mecanicienRepository;
        this.interventionRepository = interventionRepository;
    }

    public DemandeSOSResponse create(DemandeSOSRequest request) {
        return DemandeSOSMapper.toResponse(repository.save(DemandeSOSMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public List<DemandeSOSResponse> findAll() {
        return repository.findAll().stream().map(DemandeSOSMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DemandeSOSResponse findById(Long id) {
        return DemandeSOSMapper.toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<DemandeSOSResponse> findByChauffeur(String idChauffeur) {
        return repository.findByIdChauffeur(idChauffeur).stream().map(DemandeSOSMapper::toResponse).toList();
    }

    public DemandeSOSResponse update(Long id, DemandeSOSUpdateRequest request) {
        DemandeSOS d = getEntity(id);
        if (request.idMecanicien() != null && !mecanicienRepository.existsById(request.idMecanicien())) {
            throw new ResourceNotFoundException("Mecanicien", request.idMecanicien());
        }
        boolean wasAssigned = d.getIdMecanicien() != null;
        DemandeSOSMapper.updateEntity(d, request);

        // Traceability dates are maintained by the system, not by the client.
        if (d.getIdMecanicien() != null && !wasAssigned) {
            d.setDateAssignation(Instant.now());
        }
        if (d.getIdMecanicien() == null) {
            d.setDateAssignation(null);
        }
        if (d.getStatut() == StatutDemandeSOS.TERMINEE || d.getStatut() == StatutDemandeSOS.ANNULEE) {
            if (d.getDateCloture() == null) {
                d.setDateCloture(Instant.now());
            }
        } else {
            d.setDateCloture(null);
        }
        return DemandeSOSMapper.toResponse(repository.save(d));
    }

    public void delete(Long id) {
        DemandeSOS d = getEntity(id);
        if (interventionRepository.existsByIdDemande(id)) {
            throw new ConflictException("SOS request " + id + " already has an intervention");
        }
        repository.delete(d);
    }

    DemandeSOS getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DemandeSOS", id));
    }
}