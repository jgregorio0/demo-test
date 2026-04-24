package dev.jgregorio.demo.data.infrastructure.api.center;

import dev.jgregorio.demo.data.application.in.center.CenterUseCase;
import dev.jgregorio.demo.data.domain.center.*;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchResponse;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/centers")
@RequiredArgsConstructor
public class CenterController {

    private final CenterUseCase useCase;
    private final CenterApiMapper mapper;
    private final CenterCreationApiMapper creationMapper;
    private final CenterReadApiMapper readApiMapper;
    private final CenterUpdateApiMapper updateApiMapper;
    private final CenterDeleteApiMapper deleteApiMapper;
    private final CenterSearchApiMapper searchMapper;

    @PostMapping
    public ResponseEntity<CenterResponse> create(
            @RequestBody @Valid final CenterCreationRequest creationRequest) {
        CenterCreation creation = creationMapper.fromRequest(creationRequest);
        Center created = useCase.create(creation);
        CenterResponse response = mapper.toResponse(created);
        return ResponseEntity.created(response.locationUri()).body(response);
    }

    @GetMapping
    public ResponseEntity<CenterResponse> read(@Valid CenterReadRequest readRequest) {
        CenterRead centerRead = readApiMapper.fromRequest(readRequest);
        Center center = useCase.read(centerRead);
        return ResponseEntity.ok(mapper.toResponse(center));
    }

    @PutMapping
    public ResponseEntity<CenterResponse> update(
            @RequestBody @Valid final CenterUpdateRequest updateRequest) {
        CenterUpdate toUpdate = updateApiMapper.fromRequest(updateRequest);
        Center updated = useCase.update(toUpdate);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid CenterDeleteRequest deleteRequest) {
        CenterDelete centerId = deleteApiMapper.fromRequest(deleteRequest);
        useCase.delete(centerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CenterSearchResponse>> search(
            final CenterSearchRequest searchRequest, @PageableDefault final Pageable pageable) {
        CenterSearch criteria = searchMapper.fromRequest(searchRequest);
        Page<Center> centerPage = useCase.search(criteria, pageable);
        Page<CenterSearchResponse> responsePage = centerPage.map(searchMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }
}
